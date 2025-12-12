package Market_Model;

public class CardItem {
    private Product product;
    private int quantity;

    public CardItem(Product product, int quantity) {
        if (quantity == 0) {
            throw new IllegalArgumentException("Товардың количестовасы 1 болу керек");
        }
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void addQuantity(int amount) {
        quantity = quantity + amount;
    }

    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }

    public void getQuantity(int i) {
    }
}