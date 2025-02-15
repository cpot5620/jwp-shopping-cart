package cart.domain.product;

import java.math.BigDecimal;

public class Product {
    private final ProductName name;
    private final ProductPrice price;
    private final ProductCategory category;
    private final ImageUrl imageUrl;
    private final Long id;

    public Product(ProductName name, ProductPrice price, ProductCategory category, ImageUrl imageUrl, Long id) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
        this.id = id;
    }

    public Product(ProductName name, ProductPrice price, ProductCategory category, ImageUrl imageUrl) {
        this(name, price, category, imageUrl, null);
    }

    public String getName() {
        return name.getName();
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public ProductCategory getCategory() {
        return category;
    }

    public String getImageUrl() {
        return imageUrl.getUrl();
    }

    public Long getId() {
        return id;
    }
}
