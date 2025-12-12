package Market_Service;

import Market_DB.CategoryRepository;
import Market_DB.ProductRepository;
import Market_Model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ShopService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final List<CardItem> card = new ArrayList<>();
    private int nextOrderId = 1;


    public ShopService(CategoryRepository categoryRepository, ProductRepository productRepository, int nextOrderId) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.nextOrderId = nextOrderId;
    }

    public ShopService() {
        this(new CategoryRepository(), new ProductRepository(), 1);
    }


    public List<Product_Category> getAllCategories() {
        return categoryRepository.finAll();
    }

    public List<Product> getAllByCategories(Product_Category category) {
        return productRepository.findByCategoryName(category.getName());
    }


    public void addCard(int productID) {
        Product product1 = productRepository.findById(productID);
        if (product1 == null)
            throw new RuntimeException("Өкінішке орай тауар табылмады (id: " + productID + " ) ");
        for (CardItem item : card) {
            if (item.getProduct().getId() == productID) {
                item.getQuantity(1);
                return;
            }
        }

        card.add(new CardItem(product1, 1));
    }


    public List<CardItem> getCardItems() {
        return card;
    }

    public double getCartTotal() {
        return card.stream().mapToDouble(CardItem::getTotalPrice).sum();
    }

    public Order checOut() {
        if (card.isEmpty())
            throw new RuntimeException("Корзина пустой ");

        List<OrderItem> orderItems = new ArrayList<>();
        for (CardItem cardItem : card) {
            orderItems.add(new OrderItem(
                    cardItem.getProduct(),
                    cardItem.getQuantity(),  // Без параметра 1
                    cardItem.getProduct().getPrice()
            ));

        Order order = new Order(nextOrderId++, orderItems, getCartTotal(), LocalDateTime.now());

        card.clear();
        return order;
    }


        return null;
    }}
