package cart.controller;

import cart.dto.ProductAddRequest;
import cart.dto.ProductModifyRequest;
import cart.service.ProductService;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping()
    public ResponseEntity<Integer> productAdd(@Validated @RequestBody ProductAddRequest productAddRequest) {
        int productId = productService.save(productAddRequest);
        return ResponseEntity.status(HttpStatus.CREATED).location(URI.create("/products/" + productId)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> productModify(@Validated @RequestBody ProductModifyRequest productModifyRequest,
                                                @PathVariable int id) {
        productService.update(productModifyRequest, id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> productRemove(@PathVariable int id) {
        productService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
