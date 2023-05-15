package cart.service;

import cart.dao.product.ProductDao;
import cart.dao.product.ProductEntity;
import cart.domain.product.Product;
import cart.global.exception.product.ProductNotFoundException;
import cart.service.dto.product.ProductModifyRequest;
import cart.service.dto.product.ProductAddRequest;
import cart.service.dto.product.ProductSearchResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public Long registerProduct(final ProductAddRequest productAddRequest) {
        ProductEntity productEntity = new ProductEntity(
                productAddRequest.getName(),
                productAddRequest.getPrice(),
                productAddRequest.getImageUrl()
        );

        return productDao.save(productEntity);
    }

    @Transactional(readOnly = true)
    public List<ProductSearchResponse> searchAllProducts() {
        List<ProductEntity> productEntities = productDao.findAll();

        return productEntities.stream()
                .map(entity -> new ProductSearchResponse(
                        entity.getId(),
                        entity.getName(),
                        entity.getPrice(),
                        entity.getImageUrl())
                )
                .collect(Collectors.toList());
    }

    public void modifyProduct(final Long productId, final ProductModifyRequest productModifyRequest) {
        final Product modifiedProduct = Product.of(
                productModifyRequest.getName(),
                productModifyRequest.getPrice(),
                productModifyRequest.getImageUrl()
        );

        final ProductEntity modifiedProductEntity =
                new ProductEntity(
                        productId,
                        modifiedProduct.getName(),
                        modifiedProduct.getPrice(),
                        modifiedProduct.getImageUrl()
                );

        int affectedRowCount = productDao.update(modifiedProductEntity);
        if (affectedRowCount == 0) {
            throw new ProductNotFoundException();
        }
    }

    public void deleteProduct(final Long productId) {
        int affectedRowCount = productDao.deleteById(productId);
        if (affectedRowCount == 0) {
            throw new ProductNotFoundException();
        }
    }
}
