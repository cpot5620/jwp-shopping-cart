package cart.product.service;

import cart.product.dao.ProductDao;
import cart.product.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductDao productDao;

    public void add(final Product product) {
        productDao.insert(product);
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productDao.findAll();
    }

    public void updateById(final Long id, final Product product) {
        productDao.updateById(id, product);
    }

    public void deleteById(final Long id) {
        productDao.deleteById(id);
    }
}
