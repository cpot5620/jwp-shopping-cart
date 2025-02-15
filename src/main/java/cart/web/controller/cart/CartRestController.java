package cart.web.controller.cart;

import cart.service.cart.CartService;
import cart.service.cart.dto.CartAdditionProductDto;
import cart.service.cart.dto.CartDto;
import cart.service.user.UserService;
import cart.service.user.dto.UserDto;
import cart.service.user.dto.UserLoginDto;
import cart.web.auth.Auth;
import cart.web.auth.UserInfo;
import cart.web.controller.cart.dto.response.CartProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/carts")
@RestController
public class CartRestController {
    private final CartService cartService;
    private final UserService userService;

    public CartRestController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @PostMapping("/{productId}")
    public ResponseEntity<Void> addProduct(@PathVariable Long productId, @Auth UserInfo userInfo) {
        UserDto loginUser = userService.login(new UserLoginDto(userInfo.getEmail(), userInfo.getPassword()));
        CartAdditionProductDto cartAdditionProductDto = new CartAdditionProductDto(loginUser.getId(), productId);

        Long savedId = cartService.addProduct(cartAdditionProductDto);

        return ResponseEntity.created(URI.create("/carts/" + savedId)).build();
    }

    @GetMapping("/products")
    public ResponseEntity<List<CartProductResponse>> loadCartProducts(@Auth UserInfo userInfo) {
        UserDto userDto = userService.login(new UserLoginDto(userInfo.getEmail(), userInfo.getPassword()));

        List<CartDto> cartsOfUser = cartService.getCartOfUser(userDto.getId());

        return ResponseEntity.ok(
                cartsOfUser.stream()
                        .map(this::toCartProductResponse)
                        .collect(Collectors.toList())
        );
    }

    private CartProductResponse toCartProductResponse(CartDto cartDto) {
        return new CartProductResponse(
                cartDto.getId(),
                cartDto.getName(),
                cartDto.getPrice(),
                cartDto.getCategory(),
                cartDto.getImageUrl()
        );
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long cartId) {
        cartService.deleteProduct(cartId);

        return ResponseEntity.noContent().build();
    }
}
