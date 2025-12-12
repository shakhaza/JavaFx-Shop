package Market_DB;

import Market_Model.Product_Category;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {
    public List<Product_Category> findAll() {
        List<Product_Category> categoryList = new ArrayList<>();
        categoryList.add(new Product_Category(1, "Laptops"));
        categoryList.add(new Product_Category(2, "Smartphones"));

        return categoryList;
    }

    public List<Product_Category> finAll() {
        return List.of();
    }
}