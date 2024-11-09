package store;

import java.util.List;
import store.controller.StoreController;
import store.controller.StoreControllerFactory;
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
        List<String> rawPromotionInformations = inputView.readFile(FileLocation.PROMOTIONS.getLocation());
        storeController.savePromotionInformation(rawPromotionInformations);

//    private void inputPurchaseList() {
//        while (true) {
//            try {
//                String rawPurchaseOrder = inputView.purchaseInput();
//                List<PurchaseResponse> purchaseResponses = storeController.inputPurchasingProducts(rawPurchaseOrder);
//                List<PurchaseResponse> purchaseResponsesWithoutSuccessResponse = purchaseResponses.stream()
//                        .filter(response -> response.getResponseMessage() != ResponseMessage.SUCCESS)
//                        .toList();
//                // 구매 응답이 전부 Success 일 경우
//                if(purchaseResponsesWithoutSuccessResponse.isEmpty()) {
//                    return;
//                }
//                // 구매 응답 중 사용자의 확인이 필요한 경우
//                purchaseResponsesWithoutSuccessResponse.forEach(
//                        purchaseResponse -> {
//                            outputView.printPurchaseResponse(purchaseResponse);
//                            String yesOrNo = inputView.simpleInput();
//                            ValidationUtil.validateYesOrNo(yesOrNo, new WrongInputException(INPUT_Y_OR_N_EXCEPTION));
//                            if(yesOrNo.equals("Y")) {
//                                if(purchaseResponse.getResponseMessage().equals(ResponseMessage.TAKE_EXTRA_BENEFIT)) {
//                                    TakeExtraBenefitResponse takeExtraBenefitResponse = (TakeExtraBenefitResponse) purchaseResponse;
//                                    long benefitQuantity = takeExtraBenefitResponse.getBenefitQuantity();
//                                    purchaseResponse.getProductOrderDto().
//                                }
//                            }
//                        }
//                );
//            } catch (IllegalArgumentException e) {
//                System.out.println(e.getMessage());
//            }
//        }
//
//    }

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
