package cart.service;

import cart.dao.product.ProductDao;
import cart.domain.Product;
import cart.dto.request.ProductRequest;
import cart.dto.response.ProductResponse;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public Long create(ProductRequest productRequest) {
        Product product = productRequest.toEntity();
        Long savedId = productDao.save(product);
        return savedId;
    }

    public List<ProductResponse> findAll() {
        List<Product> products = productDao.findAll();
        return products.stream()
                .map(ProductResponse::new)
                .collect(toList());
    }

    public void deleteById(Long id) {
        productDao.deleteById(id);
    }

    public void update(Long productId, ProductRequest productRequest) {
        Product product = productRequest.toEntity();
        productDao.updateById(productId, product);
    }

    public Product findById(Long productId) {
        return productDao.findById(productId);
    }
}
