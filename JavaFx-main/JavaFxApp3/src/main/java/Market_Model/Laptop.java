package Market_Model;

public class Laptop extends Product {
    private double screenSize;
    private String processor;
    private int cores;
    private int ramGB;
    private int storageGB;
    private String graphicsCard;
    private int refreshRate;
    private double weight;
    private int batteryCapacity;
    private String model;

    public Laptop(int id, String name, double price, String description,
                  String imageUrl, String category, double screenSize,
                  String processor, int cores, int ramGB, int storageGB,
                  String graphicsCard, int refreshRate, double weight,
                  int batteryCapacity, String model) {
        super(id, name, price, description, imageUrl, category);
        this.screenSize = screenSize;
        this.processor = processor;
        this.cores = cores;
        this.ramGB = ramGB;
        this.storageGB = storageGB;
        this.graphicsCard = graphicsCard;
        this.refreshRate = refreshRate;
        this.weight = weight;
        this.batteryCapacity = batteryCapacity;
        this.model = model;
    }

    public double getScreenSize() { return screenSize; }
    public String getProcessor() { return processor; }
    public int getCores() { return cores; }
    public int getRamGB() { return ramGB; }
    public int getStorageGB() { return storageGB; }
    public String getGraphicsCard() { return graphicsCard; }
    public int getRefreshRate() { return refreshRate; }
    public double getWeight() { return weight; }
    public int getBatteryCapacity() { return batteryCapacity; }
    public String getModel() { return model; }
}