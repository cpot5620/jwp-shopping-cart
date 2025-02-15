package cart.web.controller.setting;

import cart.config.CartConfiguration;
import cart.domain.TestFixture;
import cart.service.user.UserService;
import cart.service.user.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = SettingViewController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = CartConfiguration.class)})
class SettingViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @DisplayName("/settings 요청시, settings.html을 반환한다.")
    @Test
    void loadSettingsPage() throws Exception {
        List<UserDto> expectedUsers = List.of(
                new UserDto(TestFixture.ZUNY),
                new UserDto(TestFixture.ADMIN)
        );
        when(userService.getAllUsers())
                .thenReturn(expectedUsers);

        mockMvc.perform(get("/settings"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("users", hasSize(2)))
                .andExpect(view().name("settings"))
                .andDo(print());
    }
}
