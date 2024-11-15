package store;

import static store.exception.InputExceptionMessage.INPUT_Y_OR_N_EXCEPTION;
import static store.util.ParseConstant.NO;
import static store.util.ParseConstant.YES;
import static store.util.ResponseMessage.*;

import java.util.ArrayList;
import java.util.List;
import store.controller.StoreController;
import store.controller.StoreControllerFactory;
import store.exception.InputException;
import store.model.dto.ProductOrderDto;
import store.model.dto.ReceiptDto;
import store.model.dto.orderCalculationResponse.OrderCalculationResponse;
import store.model.dto.orderCalculationResponse.SomeDontBenefitResponse;
import store.model.dto.orderCalculationResponse.TakeExtraBenefitResponse;
import store.view.InputView;
import store.view.OutputView;

public class ConvenienceStore {

    private final InputView inputView;
    private final OutputView outputView;
    private final StoreController storeController;

    public ConvenienceStore() {
        inputView = new InputView();
        outputView = new OutputView();
        storeController = StoreControllerFactory.getStoreController();
    }

    public void run(String promotionsFileLocation, String productsFileLocation) {
            setUpPromotionsFile(promotionsFileLocation);
            setUpProductsFile(productsFileLocation);
        while(true) {
            introduceProducts();
            List<ReceiptDto> receiptDtos = inputPurchaseList();
            printReceiptCoverWithTryCatch(receiptDtos);
            if(!isRunAgain()) {
                break;
            }
        }
    }

    public void setUpProductsFile(String location) {
        List<String> rawProductInformations = inputView.readFile(location);
        storeController.saveProductInformation(rawProductInformations);
    }

    public void setUpPromotionsFile(String location) {
        List<String> rawPromotionInformations = inputView.readFile(location);
        storeController.savePromotionInformation(rawPromotionInformations);
    }

    private boolean isRunAgain() {
        String yesOrNo = inputView.getDoYouNeedSomethingElse();
        if (yesOrNo.equals(YES.getValue())) {
            return true;
        } else if (yesOrNo.equals(NO.getValue())) {
            return false;
        }
        throw new InputException(INPUT_Y_OR_N_EXCEPTION);
    }

    private void printReceiptCoverWithTryCatch(List<ReceiptDto> receiptDtos) {
        while(true) {
            try{
                printReceipt(receiptDtos);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void printReceipt(List<ReceiptDto> receiptDtos) {
        String isMembership = inputView.questionMembershipDiscount();
        if (isMembership.equals(YES.getValue())) {
            outputView.printReceipt(receiptDtos, true);
            return;
        } else if (isMembership.equals(NO.getValue())) {
            outputView.printReceipt(receiptDtos, false);
            return;
        }

        throw new InputException(INPUT_Y_OR_N_EXCEPTION);
    }

    private List<ReceiptDto> inputPurchaseList() {
        List<OrderCalculationResponse> orderCalculationResponses;
        while (true) {
            try {
                String rawPurchaseOrder = inputView.purchaseInput();
                orderCalculationResponses = storeController.getCalculationResponse(rawPurchaseOrder);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        List<ProductOrderDto> reInputProductOrders = new ArrayList<>();
        List<ProductOrderDto> successOrders = orderCalculationResponses.stream()
                .filter(response -> response.getResponseMessage() == SUCCESS)
                .map(OrderCalculationResponse::getProductOrderDto)
                .toList();

        List<OrderCalculationResponse> orderCalculationResponsesWithoutSuccessResponse = orderCalculationResponses.stream()
                .filter(response -> response.getResponseMessage() != SUCCESS)
                .toList();

        // 구매 응답 중 일부 Success 가 아닌 경우
        if (!orderCalculationResponsesWithoutSuccessResponse.isEmpty()) {
            // 구매 응답 중 사용자의 확인이 필요한 경우
            orderCalculationResponsesWithoutSuccessResponse
                    .forEach(purchaseResponse ->
                            reInputProductOrders.add(getUpdatedProductOrder(purchaseResponse)));
        }

        reInputProductOrders.addAll(successOrders);
        return storeController.inputOrders(reInputProductOrders);
    }

    private ProductOrderDto getUpdatedProductOrder(OrderCalculationResponse purchaseResponse) {
        while (true) {
            try {
                String yesOrNo = inputView.printPurchaseResponse(purchaseResponse);
                if (purchaseResponse.getResponseMessage().equals(TAKE_EXTRA_BENEFIT)) {
                    TakeExtraBenefitResponse takeExtraBenefitResponse = (TakeExtraBenefitResponse) purchaseResponse;
                    long benefitQuantity = takeExtraBenefitResponse.getBenefitQuantity();
                    String productName = purchaseResponse.getProductOrderDto().productName();
                    long quantity = purchaseResponse.getProductOrderDto().quantity();
                    if (yesOrNo.equals(YES.getValue())) {
                        return ProductOrderDto.of(productName, quantity + benefitQuantity);
                    } else if (yesOrNo.equals(NO.getValue())) {
                        return ProductOrderDto.of(productName, quantity);
                    }
                } else if (purchaseResponse.getResponseMessage().equals(SOME_DONT_BENEFIT)) {
                    SomeDontBenefitResponse someDontBenefitResponse = (SomeDontBenefitResponse) purchaseResponse;
                    long notBenefitQuantity = someDontBenefitResponse.getNotBenefitQuantity();
                    String productName = purchaseResponse.getProductOrderDto().productName();
                    long quantity = purchaseResponse.getProductOrderDto().quantity();
                    if (yesOrNo.equals(YES.getValue())) {
                        return ProductOrderDto.of(productName, quantity);
                    } else if (yesOrNo.equals(NO.getValue())) {
                        return ProductOrderDto.of(productName, quantity - notBenefitQuantity);
                    }
                }

                throw new InputException(INPUT_Y_OR_N_EXCEPTION);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void introduceProducts() {
        List<String> productsToString = storeController.getProductsToString();
        outputView.printIntroductionMessage(productsToString);
    }
}
