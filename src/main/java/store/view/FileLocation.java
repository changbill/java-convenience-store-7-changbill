package store.view;

public enum FileLocation {
    PRODUCTS("src/main/resources/products.md"),
    PROMOTIONS("src/main/resources/promotions.md");

    private String location;

    FileLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }
}
