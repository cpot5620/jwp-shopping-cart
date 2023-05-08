package cart.user.persistence;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component
public class CartUserDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleInsert;

    public CartUserDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_user")
                .usingGeneratedKeyColumns("cart_user_id");
    }

    public int countByEmail(final String email) {
        final String countByEmailQuery = "SELECT count(*) FROM cart_user WHERE email = ?";

        return jdbcTemplate.queryForObject(countByEmailQuery, Integer.class, email);
    }

    public CartUserEntity findByEmail(final String email) {
        final String findByEmailQuery = "SELECT * FROM cart_user WHERE email = ?";

        return jdbcTemplate.queryForObject(findByEmailQuery, (rs, rowNum) -> {
            final long cartUserId = rs.getLong("cart_user_id");
            final String cartUserEmail = rs.getString("email");
            final String cartUserPassword = rs.getString("cart_password");
            return new CartUserEntity(cartUserId, cartUserEmail, cartUserPassword);
        }, email);
    }

    public Long insert(final CartUserEntity cartUserEntity) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(cartUserEntity);

        return simpleInsert.executeAndReturnKey(params).longValue();
    }

    public List<CartUserEntity> findAll() {
        final String findAllQuery = "SELECT * FROM cart_user";

        return jdbcTemplate.query(findAllQuery, (rs, rowNum) ->
                new CartUserEntity(
                        rs.getLong("cart_user_id"),
                        rs.getString("email"),
                        rs.getString("cart_password")
                ));
    }
}
