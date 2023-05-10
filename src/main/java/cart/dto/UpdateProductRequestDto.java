package cart.dto;

import cart.entity.ProductEntity;

public class UpdateProductRequestDto {
    private final int id;
    private final String image;
    private final String name;
    private final int price;

    public UpdateProductRequestDto(final int id, final String image, final String name, final int price) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public ProductEntity toEntity() {
        return new ProductEntity(id, name, price, image);
    }
}
