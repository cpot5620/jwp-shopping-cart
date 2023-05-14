package cart.domain.product;

public class ProductImageUrl {

    private final String url;

    public ProductImageUrl(String url) {
        validate(url);
        this.url = url;
    }

    private void validate(String url) {
        validateIsNull(url);
        validateIsBlank(url);
    }

    private void validateIsNull(String url) {
        if (url == null) {
            throw new IllegalArgumentException("상품 이미지 url이 null입니다.");
        }
    }

    private void validateIsBlank(String url) {
        if (url.strip().isBlank()) {
            throw new IllegalArgumentException("상품 이미지 url이 비어있습니다.");
        }
    }

    public String getUrl() {
        return url;
    }
}
