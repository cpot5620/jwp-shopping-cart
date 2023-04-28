package cart.domain.product.service.dto;

import cart.domain.product.ImageUrl;
import cart.domain.product.Product;
import cart.domain.product.ProductCategory;
import cart.domain.product.ProductName;
import cart.domain.product.ProductPrice;

public class ProductModificationDto {
    private final Long id;
    private final String name;
    private final Integer price;
    private final String category;
    private final String imageUrl;

    public ProductModificationDto(Long id, String name, Integer price, String category, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    public Product toProduct() {
        return new Product(
                ProductName.from(name),
                ProductPrice.from(price),
                ProductCategory.valueOf(category),
                ImageUrl.from(imageUrl),
                id
        );
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
