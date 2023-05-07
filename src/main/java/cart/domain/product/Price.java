package cart.domain.product;

import java.util.Objects;

public class Price {

    public static final int MAXIMUM_PRICE = 1_000_000;
    public static final int MINIMUM_PRICE = 1_000;
    public static final String PRICE_VALID_MESSAGE = "[ERROR] 가격은 1000원 이상, 100만원 이하만 가능합니다.";
    private final int amount;

    public Price(int amount) {
        validate(amount);
        this.amount = amount;
    }

    private void validate(int amount) {
        if (MINIMUM_PRICE > amount || amount > MAXIMUM_PRICE) {
            throw new IllegalArgumentException(PRICE_VALID_MESSAGE);
        }
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return amount == price.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
