package cart.service;

import cart.JdbcSaveUser;
import cart.dao.UserDao;
import cart.dao.entity.User;
import cart.dto.UserResponse;
import cart.dto.UserResponses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.RowMapper;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
class UserServiceTest extends JdbcSaveUser {

    private UserService userService;

    private UserDao userDao;

    @BeforeEach
    void init() {
        userDao = new UserDao(jdbcTemplate);
        userService = new UserService(userDao);
    }

    @Test
    void 단건_조회를_한다() {
        // given
        final long savedId = 사용자를_저장한다("test@test.com", "test");

        // when
        final UserResponse result = findById(savedId);

        // then
        assertAll(
                () -> assertThat(result.getEmail()).isEqualTo("test@test.com"),
                () -> assertThat(result.getPassword()).isEqualTo("test")
        );
    }

    private UserResponse findById(final Long id) {
        final String sql = "SELECT id, email, password, created_at FROM users WHERE id = :id";

        final User user = jdbcTemplate.queryForObject(
                sql,
                Collections.singletonMap("id", id),
                createUsersRowMapper()
        );

        return new UserResponse(user);
    }

    private static RowMapper<User> createUsersRowMapper() {
        return (rs, rowNum) -> new User(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }

    @Test
    void 전체_조회를_한다() {
        // given
        사용자를_저장한다("test@test.com", "test");
        사용자를_저장한다("user@user.com", "user");

        // when
        final UserResponses userResponses = userService.findAll();

        // then
        final List<UserResponse> results = userResponses.getUserResponses();
        assertAll(
                () -> assertThat(results).hasSize(2),
                () -> assertThat(getEmail(0, results)).isEqualTo("test@test.com"),
                () -> assertThat(getPassword(0, results)).isEqualTo("test"),
                () -> assertThat(getEmail(1, results)).isEqualTo("user@user.com"),
                () -> assertThat(getPassword(1, results)).isEqualTo("user")
        );
    }

    private static String getEmail(final int index, final List<UserResponse> results) {
        return results.get(index).getEmail();
    }

    private static String getPassword(final int index, final List<UserResponse> results) {
        return results.get(index).getPassword();
    }
}
