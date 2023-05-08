package cart.repository.dao;

import cart.domain.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class UserDao {

    private final RowMapper<User> actorRowMapper = (resultSet, rowNum) -> new User(
            resultSet.getLong("id"),
            resultSet.getString("email"),
            resultSet.getString("password")
    );
    private final JdbcTemplate jdbcTemplate;

    public UserDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT id, email, password FROM USERS WHERE email = ?";

        return jdbcTemplate.query(sql, actorRowMapper, email)
                .stream()
                .findAny();
    }

    public List<User> findAll() {
        String sql = "SELECT id, email, password FROM USERS";

        return jdbcTemplate.query(sql, actorRowMapper);
    }
}
