package cart.dto.response;

import cart.dto.application.ProductEntityDto;
import java.util.Objects;

public class ProductResponse {

    private final long id;
    private final String name;
    private final int price;
    private final String imageUrl;

    public ProductResponse(final long id, final String name, final int price, final String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public ProductResponse(final ProductEntityDto productEntityDto) {
        this(
                productEntityDto.getId(),
                productEntityDto.getName(),
                productEntityDto.getPrice(),
                productEntityDto.getImageUrl()
        );
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductResponse that = (ProductResponse) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
