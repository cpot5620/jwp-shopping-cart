package cart.domain.cart;

import cart.domain.product.Product;

public interface CartRepository {

    Cart findByMemberId(long userId);

    long saveProductToCart(Cart cartToAdd, Product product);

    void deleteProductFromCart(Cart cartToDelete, Product product);
}
