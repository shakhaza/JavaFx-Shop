package Market_Model;

public class Smartphone extends Product {
    private double screenSize;
    private int ramGB;
    private int storageGB;
    private int batteryCapacity;
    private String camera;
    private String model;

    public Smartphone(int id, String name, double price, String description,
                      String imageUrl, String category, double screenSize,
                      int ramGB, int storageGB, int batteryCapacity,
                      String camera, String model) {
        super(id, name, price, description, imageUrl, category);
        this.screenSize = screenSize;
        this.ramGB = ramGB;
        this.storageGB = storageGB;
        this.batteryCapacity = batteryCapacity;
        this.camera = camera;
        this.model = model;
    }

    public double getScreenSize() { return screenSize; }
    public int getRamGB() { return ramGB; }
    public int getStorageGB() { return storageGB; }
    public int getBatteryCapacity() { return batteryCapacity; }
    public String getCamera() { return camera; }
    public String getModel() { return model; }
}