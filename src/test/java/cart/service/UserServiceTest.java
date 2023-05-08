package cart.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.repository.UserRepository;
import cart.repository.dao.UserDao;
import cart.exception.user.SignInFailureException;
import cart.service.dto.UserDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class UserServiceTest {

    UserService userService;

    @BeforeEach
    void setUp(@Autowired JdbcTemplate jdbcTemplate) {
        UserDao userDao = new UserDao(jdbcTemplate);
        UserRepository userRepository = new UserRepository(userDao);

        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("존재하는 email과 그에 일치하는 password를 전달하면 성공한다.")
    void signInSuccess() {
        UserDto userDto = userService.signIn("a@a.com", "a");

        assertAll(
                () -> assertThat(userDto.getEmail()).isEqualTo("a@a.com"),
                () -> assertThat(userDto.getPassword()).isEqualTo("a")
        );
    }

    @ParameterizedTest(name = "email {0}, password {1}을 전달하면 실패한다.")
    @CsvSource(value = {"a@a.com:b", "c@c.com:c"}, delimiter = ':')
    void signInFail(String email, String password) {
        assertThatThrownBy(() -> userService.signIn(email, password))
                .isInstanceOf(SignInFailureException.class)
                .hasMessage("로그인에 실패했습니다.");
    }

    @Test
    @DisplayName("모든 사용자를 찾는다.")
    void findAllSuccess() {
        List<UserDto> users = userService.findAll();

        assertThat(users).hasSize(2);
    }
}
