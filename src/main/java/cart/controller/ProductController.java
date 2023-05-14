package cart.controller;

import cart.dto.request.ProductCreateDto;
import cart.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid ProductCreateDto productCreateDto) {
        productService.create(productCreateDto);
        return ResponseEntity.created(URI.create("/admin")).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(
            @RequestBody @Valid final ProductCreateDto productCreateDto,
            @PathVariable final int id) {
        productService.update(productCreateDto, id);
        return ResponseEntity.created(URI.create("/admin")).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final int id) {
        productService.delete(id);
        return ResponseEntity.ok().build();
    }

}
