package cart.product.controller;

import cart.product.dto.request.ProductAddRequest;
import cart.product.dto.request.ProductUpdateRequest;
import cart.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/products")
public class ProductApiController {

    private final ProductService productService;

    @Autowired
    public ProductApiController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody @Valid ProductAddRequest productAddRequest) {
        final long id = productService.register(productAddRequest);
        return ResponseEntity.created(URI.create("/products/" + id)).build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid ProductUpdateRequest productUpdateRequest) {
        final long id = productService.update(productUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        System.out.println("404??");
        return ResponseEntity.noContent().build();
    }
}
