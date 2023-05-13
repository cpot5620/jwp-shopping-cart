package cart.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.controller.dto.ItemRequest;
import cart.controller.dto.ItemResponse;
import cart.dao.ItemDao;
import cart.exception.ItemException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class ItemServiceTest {

    @Autowired
    JdbcTemplate jdbcTemplate;
    ItemService itemService;

    @BeforeEach
    void setUp() {
        ItemDao itemDao = new ItemDao(jdbcTemplate);
        itemService = new ItemService(itemDao);
    }

    @Test
    @DisplayName("상품을 저장한다.")
    void addItemSuccess() {
        ItemRequest itemRequest = createItemRequest();

        ItemResponse itemResponse = itemService.add(itemRequest);

        assertAll(
                () -> assertThat(itemResponse.getId()).isPositive(),
                () -> assertThat(itemResponse.getName()).isEqualTo(itemRequest.getName()),
                () -> assertThat(itemResponse.getImageUrl()).isEqualTo(itemRequest.getImageUrl()),
                () -> assertThat(itemResponse.getPrice()).isEqualTo(itemRequest.getPrice())
        );
    }

    @Test
    @DisplayName("모든 상품을 찾는다.")
    void findAllItemSuccess() {
        int initialSize = itemService.findAll().size();
        ItemRequest itemRequest1 = createItemRequest();
        ItemRequest itemRequest2 = createItemRequest();
        itemService.add(itemRequest1);
        itemService.add(itemRequest2);

        List<ItemResponse> itemResponses = itemService.findAll();

        assertThat(itemResponses).hasSize(initialSize + 2);
    }

    @Test
    @DisplayName("상품을 변경한다.")
    void updateItemSuccess() {
        ItemRequest itemRequest = createItemRequest();
        ItemResponse itemResponse = itemService.add(itemRequest);

        ItemRequest updateItemRequest = new ItemRequest("자전거", "http://image.url", 1_500_000);
        ItemResponse updateItemResponse = itemService.update(itemResponse.getId(), updateItemRequest);

        assertAll(
                () -> assertThat(updateItemResponse.getId()).isEqualTo(itemResponse.getId()),
                () -> assertThat(updateItemResponse.getName()).isEqualTo(updateItemRequest.getName())
        );
    }

    @Test
    @DisplayName("상품을 삭제한다.")
    void deleteItemSuccess() {
        ItemRequest itemRequest = createItemRequest();
        ItemResponse itemResponse = itemService.add(itemRequest);

        assertDoesNotThrow(() -> itemService.delete(itemResponse.getId()));
    }

    @Test
    @DisplayName("존재하지 않는 상품 ID를 조회하면 예외가 발생한다.")
    void updateItemRequestFailWithNotExistID() {
        ItemRequest itemRequest = createItemRequest();

        assertThatThrownBy(() -> itemService.update(Long.MAX_VALUE, itemRequest))
                .isInstanceOf(ItemException.class)
                .hasMessage("일치하는 상품을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 상품 ID를 삭제하면 예외가 발생한다.")
    void deleteItemRequestFailWithNotExistID() {
        assertThatThrownBy(() -> itemService.delete(Long.MAX_VALUE))
                .isInstanceOf(ItemException.class)
                .hasMessage("일치하는 상품을 찾을 수 없습니다.");
    }

    private static ItemRequest createItemRequest() {
        return new ItemRequest("맥북", "http://image.url", 1_500_000);
    }
}
