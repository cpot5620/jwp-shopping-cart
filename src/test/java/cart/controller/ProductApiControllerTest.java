package cart.controller;

import cart.dto.ProductResponseDto;
import cart.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductApiController.class)
class ProductApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @DisplayName("POST /product 성공 테스트")
    @Test
    void insertTest() throws Exception {
        this.mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"name\", \"image\": \"image\", \"price\": \"1000\"}"))
                .andExpect(status().isOk());
    }

    @DisplayName("POST /product 실패 테스트")
    @Test
    void insertTest_fail() throws Exception {
        this.mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"name\", \"image\": \"image\", \"price\": \"abc\"}"))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("GET /product 성공 테스트")
    @Test
    void getTest() throws Exception {
        when(productService.getProducts()).thenReturn(List.of(
                new ProductResponseDto(1, "image1", "name1", 1000),
                new ProductResponseDto(2, "image2", "name2", 2000)));

        this.mockMvc.perform(get("/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("name1"))
                .andExpect(jsonPath("$[0].price").value("1000"))
                .andExpect(jsonPath("$[0].image").value("image1"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].name").value("name2"))
                .andExpect(jsonPath("$[1].price").value("2000"))
                .andExpect(jsonPath("$[1].image").value("image2"));
    }

    @DisplayName("PATCH /product 성공 테스트")
    @Test
    void updateTest() throws Exception {
        this.mockMvc.perform(patch("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": \"1\", \"name\": \"name\", \"image\": \"image\", \"price\": \"1000\"}"))
                .andExpect(status().isOk());
    }

    @DisplayName("PATCH /product 실패 테스트")
    @Test
    void updateTest_fail() throws Exception {
        this.mockMvc.perform(patch("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": \"1\", \"name\": \"name\", \"image\": \"image\", \"price\": \"abc\"}"))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("DELETE /product 성공 테스트")
    @Test
    void deleteTest() throws Exception {
        this.mockMvc.perform(delete("/product/1"))
                .andExpect(status().isOk());
    }

    @DisplayName("DELETE /product 실패 테스트")
    @Test
    void deleteTest_fail() throws Exception {
        this.mockMvc.perform(delete("/product/a"))
                .andExpect(status().isBadRequest());
    }
}
