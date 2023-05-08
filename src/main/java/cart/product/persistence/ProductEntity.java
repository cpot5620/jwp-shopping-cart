package cart.product.persistence;

import cart.product.domain.ImageUrl;
import cart.product.domain.Product;
import cart.product.domain.ProductCategory;
import cart.product.domain.ProductId;
import cart.product.domain.ProductName;
import cart.product.domain.ProductPrice;
import java.util.Objects;

public class ProductEntity {

    private final Long id;
    private final String name;
    private final Integer price;
    private final String category;
    private final String imageUrl;

    public ProductEntity(final Long id, final String name, final Integer price, final String category,
                         final String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    public ProductEntity(final String name, final Integer price, final String category, final String imageUrl) {
        this.id = null;
        this.name = name;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    public ProductEntity(final Product product) {
        this.id = null;
        this.name = product.getName();
        this.price = product.getPrice().intValue();
        this.category = product.getCategory().name();
        this.imageUrl = product.getImageUrl();
    }

    public Product toProduct() {
        return new Product(
                ProductName.from(this.name),
                ProductPrice.from(this.price),
                ProductCategory.valueOf(this.category),
                ImageUrl.from(this.imageUrl),
                ProductId.from(this.id)
        );
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductEntity that = (ProductEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public Long getId() {
        return id;
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
