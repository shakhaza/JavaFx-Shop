package Market_Model;

public class Product_Category {
    private int id;
    private String name;

    public Product_Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }
}