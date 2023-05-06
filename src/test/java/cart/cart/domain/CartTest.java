package cart.cart.domain;

import cart.member.domain.MemberId;
import cart.product.domain.ProductId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class CartTest {
    @DisplayName("생성한다.")
    @Test
    void create() {
        assertDoesNotThrow(() -> new Cart(CartId.from(1L), MemberId.from(1L), ProductId.from(1L)));
    }
}
