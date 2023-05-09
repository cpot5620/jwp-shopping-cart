package cart.domain.product;

import java.util.Objects;

public class ProductName {
    private static final int MAX_LENGTH = 20;

    private final String value;

    public ProductName(final String name) {
        validateName(name);
        this.value = name;
    }

    private static void validateName(final String name) {
        if (name.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(MAX_LENGTH + "자 이하의 이름을 입력해 주세요.");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductName productName = (ProductName) o;
        return Objects.equals(value, productName.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
