package Market_Model;

public abstract class Product implements HasBasicInfo {
    private int id;
    private String name;
    private double price;
    private String description;
    private String imageUrl;
    private String category;

    public Product(int id, String name, double price, String description,
                   String imageUrl, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @Override
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String getImageURL() { return imageUrl; }
    public void setImageURL(String imageUrl) { this.imageUrl = imageUrl; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}