package cart.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import cart.exception.DbNotAffectedException;
import cart.persistence.dao.ProductDao;
import cart.persistence.entity.Product;
import cart.service.AdminService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:schema-truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AdminServiceTest {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private AdminService adminService;

    @Test
    void create_메서드로_productEntity를_저장한다() {
        final Product product = new Product("modi", 10000, "https://woowacourse.github.io/");

        adminService.create(product);

        final Product findEntity = productDao.findByName("modi");
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(findEntity.getName()).isEqualTo("modi");
            softAssertions.assertThat(findEntity.getPrice()).isEqualTo(10000);
            softAssertions.assertThat(findEntity.getImageUrl()).isEqualTo("https://woowacourse.github.io/");
        });
    }

    @Test
    void readAll_메서드로_모든_productEntity를_불러온다() {
        final Product product = new Product("modi", 10000, "https://woowacourse.github.io/");
        productDao.save(product);

        final List<Product> products = adminService.findAll();

        final Product foundProduct = products.get(0);
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(products.size()).isEqualTo(1);
            softAssertions.assertThat(foundProduct.getName()).isEqualTo("modi");
            softAssertions.assertThat(foundProduct.getPrice()).isEqualTo(10000);
            softAssertions.assertThat(foundProduct.getImageUrl()).isEqualTo("https://woowacourse.github.io/");
        });
    }

    @Test
    void update_메서드로_productEntity를_변경한다() {
        final Product product = new Product("modi", 10000, "https://woowacourse.github.io/");
        final Long id = productDao.save(product);

        final Product newEntity = new Product("modi", 2000, "https://changed.com/");
        adminService.update(id, newEntity);

        final Product changedEntity = productDao.findByName("modi");
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(changedEntity.getId()).isEqualTo(id);
            softAssertions.assertThat(changedEntity.getName()).isEqualTo(newEntity.getName());
            softAssertions.assertThat(changedEntity.getPrice()).isEqualTo(newEntity.getPrice());
            softAssertions.assertThat(changedEntity.getImageUrl()).isEqualTo(newEntity.getImageUrl());
        });
    }

    @Test
    void 존재하지_않는_id를_update_하면_예외가_발생한다() {
        final Product changedEntity = new Product("modi", 2000, "https://changed.com/");
        final long wrongId = 1L;

        assertThrows(DbNotAffectedException.class, () -> adminService.update(wrongId, changedEntity));
    }

    @Test
    void 존재하지_않는_id를_delete_하면_예외가_발생한다() {
        final long wrongId = 0L;

        assertThrows(DbNotAffectedException.class, () -> adminService.delete(wrongId));
    }
}
