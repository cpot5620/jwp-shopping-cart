package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Product;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql("classpath:schema.sql")
class ProductDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ProductDao productDao;

    @BeforeEach
    void setup() {
        this.productDao = new ProductDao(jdbcTemplate);
        productDao.insert(new Product("pizza", 1000,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a3/Eq_it-na_pizza-margherita_sep2005_sml.jpg/800px-Eq_it-na_pizza-margherita_sep2005_sml.jpg"));
        productDao.insert(
                new Product("salad", 2000,
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/9/94/Salad_platter.jpg/1200px-Salad_platter.jpg"));
    }

    @Test
    void 상품_데이터_삽입() {
        final Product product = new Product("chicken", 3000,
                "https://cdn.britannica.com/18/137318-050-29F7072E/rooster-Rhode-Island-Red-roosters-chicken"
                        + "-domestication.jpg");

        final Long id = productDao.insert(product);

        assertThat(id).isEqualTo(3L);
    }

    @Test
    void 상품_데이터_조회() {
        final Long id = 2L;

        final Product foundProduct = productDao.findById(id);

        assertAll(
                () -> assertThat(foundProduct.getName()).isEqualTo("salad"),
                () -> assertThat(foundProduct.getPrice()).isEqualTo(2000)
        );
    }

    @Test
    void 모든_상품_데이터_조회() {
        final List<Product> results = productDao.findAll();

        assertThat(results.size()).isEqualTo(2);
    }

    @Test
    void 상품_데이터_수정() {
        final Long id = 2L;
        final Product newProduct = new Product(id, "new salad", 3000, "url");

        productDao.update(newProduct);
        final Product foundProduct = productDao.findById(id);

        assertAll(
                () -> assertThat(foundProduct.getName()).isEqualTo(newProduct.getName()),
                () -> assertThat(foundProduct.getPrice()).isEqualTo(newProduct.getPrice())
        );
    }

    @Test
    void 존재하지_않는_상품_수정시_예외_발생() {
        final Product notExistProduct = new Product(3L, "커피", 2800, "매머드 커피");

        productDao.update(notExistProduct);
    }

    @Test
    void 상품_데이터_삭제() {
        final Long id = 2L;

        productDao.delete(id);

        assertThat(productDao.findAll().size()).isEqualTo(1);
    }
}
