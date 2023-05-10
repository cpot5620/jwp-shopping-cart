package cart.dto;

import cart.entity.ProductEntity;

public class InsertProductRequestDto {
    private final String name;
    private final String image;
    private final int price;

    public InsertProductRequestDto(final String name, final String image, final int price) {
        this.name = name;
        this.image = image;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public int getPrice() {
        return price;
    }

    public ProductEntity toEntity() {
        return new ProductEntity(null, name, price, image);
    }
}
