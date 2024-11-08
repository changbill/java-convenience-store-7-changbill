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

        List<String> rawProductInformations = inputView.readFile(FileLocation.PRODUCTS.getLocation());
        storeController.saveProductInformation(rawProductInformations);

        List<String> productsToString = storeController.getProductsToString();
        outputView.printIntroductionMessage(productsToString);

//        String purchaseContext = inputView.purchaseInput();
//        storeController.inputPurchasingProducts(purchaseContext);
    }
}
