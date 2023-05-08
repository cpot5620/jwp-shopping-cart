package cart.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import cart.service.CartService;
import cart.service.ItemService;
import cart.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@WebMvcTest(
        controllers = ViewController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class)
        }
)
class ViewControllerTest {

    @MockBean
    ItemService itemService;

    @MockBean
    UserService userService;

    @MockBean
    CartService cartService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("/을 요청하면 메인 페이지 이름을 반환한다.")
    void redirectMainPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", is("text/html;charset=UTF-8")))
                .andExpect(model().attribute("products", notNullValue()))
                .andExpect(view().name("index"))
                .andDo(print());
    }

    @Test
    @DisplayName("/admin을 요청하면 어드민 페이지 이름을 반환한다.")
    void redirectAdminPage() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", is("text/html;charset=UTF-8")))
                .andExpect(model().attribute("products", notNullValue()))
                .andExpect(view().name("admin"))
                .andDo(print());
    }

    @Test
    @DisplayName("/settings를 요청하면 사용자 선택 페이지 이름을 반환한다.")
    void redirectSettingsPage() throws Exception {
        mockMvc.perform(get("/settings"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", is("text/html;charset=UTF-8")))
                .andExpect(model().attribute("members", notNullValue()))
                .andExpect(view().name("settings"))
                .andDo(print());
    }

    @Test
    @DisplayName("/cart를 요청하면 장바구니 페이지 이름을 반환한다.")
    void redirectCartPage() throws Exception {
        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", is("text/html;charset=UTF-8")))
                .andExpect(view().name("cart"))
                .andDo(print());
    }
}
