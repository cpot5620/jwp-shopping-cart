package cart.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cart.controller.auth.AuthorizedUserId;
import cart.controller.dto.CartItemRequest;
import cart.controller.dto.CartItemResponse;
import cart.domain.CartItem;
import cart.service.CartService;

@RestController
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("cart/items")
    @ResponseStatus(HttpStatus.CREATED)
    public void addItem(@RequestBody CartItemRequest cartItemRequest, @AuthorizedUserId Integer userId) {
        cartService.addToCart(userId, cartItemRequest.getProductId());
    }

    @GetMapping("cart/items")
    public List<CartItemResponse> getCartItems(@AuthorizedUserId Integer userId) {
        final List<CartItem> products = cartService.getCartItemsOf(userId);
        return mapProducts(products);
    }

    @DeleteMapping("cart/items/{id}")
    public void deleteItem(@PathVariable("id") Integer itemId, @AuthorizedUserId Integer userId) {
        cartService.deleteFromCart(userId, itemId);
    }

    private List<CartItemResponse> mapProducts(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(cartItem -> new CartItemResponse(
                        cartItem.getId(),
                        cartItem.getProduct().getName(),
                        cartItem.getProduct().getImage(),
                        cartItem.getProduct().getPrice())
                ).collect(Collectors.toList());
    }
}
