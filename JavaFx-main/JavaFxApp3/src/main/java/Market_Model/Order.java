package Market_Model;

import java.time.LocalDateTime;
import java.util.List;


public class Order {

    private int id;
    private List<OrderItem> items;
    private double totalPrice;
    private LocalDateTime createAt;


    public Order(int id, List<OrderItem> items, double totalPrice, LocalDateTime createAt) {
        this.id = id;
        this.items = items;
        this.totalPrice = totalPrice;
        this.createAt = createAt;
    }

    public int getId() {
        return id;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    // 1 количество все товар
    public int getTotalQuantity(){
        int counter = 0;
        for (OrderItem item : items){
            counter = counter + item.getQuantity();
        }
        return counter;
    }

    //2 количество ноутбук
    public int getLaptopQuantity(){
        int counter = 0;
        for (OrderItem item : items){
            if (item.getProduct() instanceof Laptop){
                counter = counter + item.getQuantity();
            }
        }
        return counter;
    }

    // 3 количество смартфоны
    public int getSmartphoneQuantity(){
        int counter = 0;
        for (OrderItem item : items){
            if (item.getProduct() instanceof Smartphone){
                counter = counter + item.getQuantity();
            }
        }
        return counter;
    }


}
