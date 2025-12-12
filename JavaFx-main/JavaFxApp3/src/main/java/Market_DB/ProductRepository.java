package Market_DB;

import Market_Model.Laptop;
import Market_Model.Product;
import Market_Model.Smartphone;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    public List<Product> findByCategoryName(String categoryName) {
        String sql = "SELECT * FROM product WHERE category = ? ORDER BY id";
        List<Product> list = new ArrayList<>();

        try (Connection conn = DataBase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, categoryName);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Product load error: " + e.getMessage(), e);
        }
        return list;
    }

    public Product findById(int id) {
        String sql = "SELECT * FROM product WHERE id = ?";
        try (Connection conn = DataBase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Product by id error: " + e.getMessage(), e);
        }
        return null;
    }

    private Product map(ResultSet rs) throws Exception {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        double price = rs.getDouble("price");
        String desc = rs.getString("description");
        String imageUrl = rs.getString("image_url");
        String category = rs.getString("category");

        if ("Laptops".equalsIgnoreCase(category)) {
            double screenSize = rs.getDouble("screen_size");
            String processor = rs.getString("processor");
            int cores = rs.getInt("cores");
            int ramGB = rs.getInt("ram_gb");
            int storageGB = rs.getInt("storage_gb");
            String graphicsCard = rs.getString("graphics_card");
            int refreshRate = rs.getInt("refresh_rate");
            double weight = rs.getDouble("weight");
            int batteryCapacity = rs.getInt("battery_capacity");
            String model = rs.getString("model");

            return new Laptop(
                    id, name, price, desc, imageUrl, category,
                    screenSize, processor, cores, ramGB, storageGB,
                    graphicsCard, refreshRate, weight, batteryCapacity, model
            );
        } else if ("Smartphones".equalsIgnoreCase(category)) {
            double screenSize = rs.getDouble("screen_size");
            int ramGB = rs.getInt("ram_gb");
            int storageGB = rs.getInt("storage_gb");
            int batteryCapacity = rs.getInt("battery_capacity");
            String camera = rs.getString("camera");
            String model = rs.getString("model");

            return new Smartphone(
                    id, name, price, desc, imageUrl, category,
                    screenSize, ramGB, storageGB, batteryCapacity, camera, model
            );
        }

        throw new IllegalStateException("Белгісіз category: " + category);
    }
}