package cart.controller;

import cart.controller.dto.ProductRequest;
import cart.controller.dto.ProductResponse;
import cart.service.ProductService;
import cart.service.dto.ProductDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductsApiController {

    private static final String PRODUCTS_LOCATION_PREFIX = "/products/";

    private final ProductService productService;

    public ProductsApiController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public void insertProduct(
            @Valid @RequestBody final ProductRequest productRequest,
            final HttpServletResponse httpServletResponse
    ) {
        final Long productId = productService.insertProduct(new ProductDto.Builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .imageUrl(productRequest.getImageUrl())
                .build());
        httpServletResponse.setStatus(HttpStatus.CREATED.value());
        httpServletResponse.setHeader("Location", PRODUCTS_LOCATION_PREFIX + productId);
    }

    @GetMapping
    public List<ProductResponse> readAllProducts(final HttpServletResponse httpServletResponse) {
        httpServletResponse.setStatus(HttpStatus.OK.value());
        return productService.findAll()
                .stream()
                .map(productDto -> new ProductResponse(
                        productDto.getId(),
                        productDto.getName(),
                        productDto.getPrice(),
                        productDto.getImageUrl()
                ))
                .collect(Collectors.toUnmodifiableList());
    }

    @PutMapping("/{id}")
    public void updateProduct(
            @PathVariable final Long id,
            @Valid @RequestBody final ProductRequest productRequest,
            final HttpServletResponse httpServletResponse
    ) {
        productService.updateById(new ProductDto.Builder()
                .id(id)
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .imageUrl(productRequest.getImageUrl())
                .build());
        httpServletResponse.setStatus(HttpStatus.OK.value());
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(
            @PathVariable final Long id,
            final HttpServletResponse httpServletResponse
    ) {
        productService.deleteById(id);
        httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
    }
}
