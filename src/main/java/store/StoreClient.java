package store;

import static store.exception.InputExceptionMessage.INPUT_Y_OR_N_EXCEPTION;
import static store.util.ResponseMessage.*;

import java.util.ArrayList;
import java.util.List;
import store.controller.StoreController;
import store.controller.StoreControllerFactory;
import store.exception.WrongInputException;
import store.model.dto.ProductOrderDto;
import store.model.dto.ReceiptDto;
import store.model.dto.orderCalculationResponse.OrderCalculationResponse;
import store.model.dto.orderCalculationResponse.SomeDontBenefitResponse;
import store.model.dto.orderCalculationResponse.TakeExtraBenefitResponse;
import store.view.FileLocation;
import store.view.InputView;
import store.view.OutputView;

public class StoreClient {

    private final InputView inputView;
    private final OutputView outputView;
    private final StoreController storeController;

    public StoreClient() {
        inputView = new InputView();
        outputView = new OutputView();
        storeController = StoreControllerFactory.getStoreController();
    }

    public void run() {
        while(true) {
            setUpPromotionsFile();
            setUpProductsFile();
            introduceProducts();
            List<ReceiptDto> receiptDtos = inputPurchaseList();
            printReceiptCoverWithTryCatch(receiptDtos);
            if(!isRunAgain()) {
                break;
            }
        }

    }

    private boolean isRunAgain() {
        String yesOrNo = inputView.getDoYouNeedSomethingElse();
        if (yesOrNo.equals("Y")) {
            return true;
        } else if (yesOrNo.equals("N")) {
            return false;
        }
        throw new WrongInputException(INPUT_Y_OR_N_EXCEPTION);
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
        if (isMembership.equals("Y")) {
            outputView.printReceipt(receiptDtos, true);
            return;
        } else if (isMembership.equals("N")) {
            outputView.printReceipt(receiptDtos, false);
            return;
        }

        throw new WrongInputException(INPUT_Y_OR_N_EXCEPTION);
    }

    private List<ReceiptDto> inputPurchaseList() {
        List<OrderCalculationResponse> orderCalculationResponses;
        while (true) {
            try {
                String rawPurchaseOrder = inputView.purchaseInput();
                orderCalculationResponses = storeController.getCalculationResponse(
                        rawPurchaseOrder);
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
                    if (yesOrNo.equals("Y")) {
                        return ProductOrderDto.of(productName, quantity + benefitQuantity);
                    } else if (yesOrNo.equals("N")) {
                        return ProductOrderDto.of(productName, quantity);
                    }
                } else if (purchaseResponse.getResponseMessage().equals(SOME_DONT_BENEFIT)) {
                    SomeDontBenefitResponse someDontBenefitResponse = (SomeDontBenefitResponse) purchaseResponse;
                    long notBenefitQuantity = someDontBenefitResponse.getNotBenefitQuantity();
                    String productName = purchaseResponse.getProductOrderDto().productName();
                    long quantity = purchaseResponse.getProductOrderDto().quantity();
                    if (yesOrNo.equals("Y")) {
                        return ProductOrderDto.of(productName, quantity);
                    } else if (yesOrNo.equals("N")) {
                        return ProductOrderDto.of(productName, quantity - notBenefitQuantity);
                    }
                }

                throw new WrongInputException(INPUT_Y_OR_N_EXCEPTION);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void introduceProducts() {
        List<String> productsToString = storeController.getProductsToString();
        outputView.printIntroductionMessage(productsToString);
    }

    private void setUpProductsFile() {
        List<String> rawProductInformations = inputView.readFile(FileLocation.PRODUCTS.getLocation());
        storeController.saveProductInformation(rawProductInformations);
    }

    private void setUpPromotionsFile() {
        List<String> rawPromotionInformations = inputView.readFile(FileLocation.PROMOTIONS.getLocation());
        storeController.savePromotionInformation(rawPromotionInformations);
    }
}
