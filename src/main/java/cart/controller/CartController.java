package cart.controller;

import cart.config.auth.Authenticated;
import cart.service.CartService;
import cart.service.dto.MemberAuthDto;
import cart.service.dto.ProductDto;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/carts")
@RestController
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getProductsOfMyCart(@Authenticated MemberAuthDto memberAuthDto) {
        List<ProductDto> products = this.cartService.findProductsInCartByUser(memberAuthDto);
        return ResponseEntity.ok().body(products);
    }

    @PostMapping("/{productId}")
    public ResponseEntity<Void> addProductToMyCart(
            @Authenticated MemberAuthDto memberAuthDto,
            @NotNull @PathVariable Long productId
    ) {
        this.cartService.addProductToCartById(memberAuthDto, productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProductFromMyCart(
            @Authenticated MemberAuthDto memberAuthDto,
            @NotNull @PathVariable Long productId
    ) {
        this.cartService.deleteProductFromCartById(memberAuthDto, productId);
        return ResponseEntity.ok().build();
    }
}
