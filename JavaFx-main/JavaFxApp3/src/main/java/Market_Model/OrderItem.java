package Market_Model;

public class OrderItem {
    private Product product;
    private int quantity;
    private double priceAtPurchase;

    // ОДИН конструктор
    public OrderItem(Product product, int quantity, double priceAtPurchase) {
        this.product = product;
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public double getTotalPrice() {
        return priceAtPurchase * quantity;
    }
}
