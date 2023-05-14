package cart.controller.unit;

import cart.controller.ProductApiController;
import cart.dto.RequestCreateProductDto;
import cart.dto.RequestUpdateProductDto;
import cart.service.MemberService;
import cart.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@WebMvcTest(ProductApiController.class)
class ProductApiControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ProductService productService;
    @MockBean
    private MemberService memberService;

    @Test
    void 상품을_생성한다() throws Exception {
        // given
        final RequestCreateProductDto productDto = new RequestCreateProductDto("치킨", 10_000, "치킨 주소");

        // expect
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void 상품을_수정한다() throws Exception {
        // given
        final RequestUpdateProductDto productDto = new RequestUpdateProductDto(1L, "치킨", 10_000, "치킨 주소");

        // expect
        mockMvc.perform(put("/products/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productDto)))
                .andExpect(status().isOk());
    }

    @Test
    void 상품을_삭제한다() throws Exception {
        mockMvc.perform(delete("/products/{id}", "1"))
                .andExpect(status().isOk());
    }
}
