package cart.service;

import cart.JdbcMySqlDialectTest;
import cart.dao.CartDao;
import cart.dao.ProductDao;
import cart.dto.ProductResponse;
import cart.dto.ProductSaveRequest;
import cart.dto.ProductUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectUpdateSemanticsDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcMySqlDialectTest
@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest {

    private ProductService productService;
    private ProductDao productDao;
    private CartDao cartDao;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    void init() {
        cartDao = new CartDao(jdbcTemplate);
        productDao = new ProductDao(jdbcTemplate);

        productService = new ProductService(productDao, cartDao, new ProductMapper());
    }

    @Test
    @DisplayName("저장된 결과를 반환한다.")
    void saveAndFindTest() {
        saveProduct("치킨", 10000);

        final List<ProductResponse> result = productService.findAll();
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result.get(0).getName()).isEqualTo("치킨"),
                () -> assertThat(result.get(0).getPrice()).isEqualTo(10000),
                () -> assertThat(result.get(0).getImgUrl()).isEqualTo("img")
        );
    }

    @Nested
    @DisplayName("삭제 테스트를 진행한다.")
    class Delete {

        @Test
        void 상품을_삭제를_성공한다() {
            // given
            final Long id = saveProduct("샐러드", 20000);

            // when
            productService.delete(id);

            // then
            final List<ProductResponse> results = productService.findAll();
            assertThat(results).isEmpty();
        }
    }

    @Nested
    @DisplayName("Update 테스트를 진핸한다.")
    class Update {
        @Test
        void id가_존재하면_수정한다() {
            // given
            final Long id = saveProduct("샐러드", 20000);
            final ProductUpdateRequest request = new ProductUpdateRequest("치킨", 10000, "changedImg");

            // when
            productService.update(id, request);

            // then
            final List<ProductResponse> results = productService.findAll();
            assertAll(
                    () -> assertThat(results.get(0).getName()).isEqualTo("치킨"),
                    () -> assertThat(results.get(0).getPrice()).isEqualTo(10000),
                    () -> assertThat(results.get(0).getImgUrl()).isEqualTo("changedImg")
            );
        }

        @Test
        void id가_존재하지_않으면_수정되지_않는다() {
            // given
            final ProductUpdateRequest request = new ProductUpdateRequest("치킨", 10000, "changedImg");
            final long noneSavedId = 1000L;

            // when, then
            assertThatThrownBy(() -> productService.update(noneSavedId, request))
                    .isInstanceOf(IncorrectUpdateSemanticsDataAccessException.class);
        }
    }

    private Long saveProduct(final String name, final int price) {
        final ProductSaveRequest request = new ProductSaveRequest(name, price, "img");
        return productService.save(request);
    }
}
