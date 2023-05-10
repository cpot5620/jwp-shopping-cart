package cart.dao;

import cart.entity.CartEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcTemplateCartDao implements CartDao {

    private final SimpleJdbcInsert insertCarts;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTemplateCartDao(JdbcTemplate jdbcTemplate) {
        this.insertCarts = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("carts")
                .usingGeneratedKeyColumns("num");

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insert(CartEntity cart) {
        Map<String, Object> parameters = new HashMap<>(2);
        parameters.put("user_id", cart.getUserId());
        parameters.put("product_id", cart.getProductId());
        return insertCarts.executeAndReturnKey(parameters).intValue();
    }

    @Override
    public List<CartEntity> selectByUserId(int userId) {
        String sql = "select * from carts where user_id = ?";
        return jdbcTemplate.query(sql, cartEntityRowMapper, userId);
    }

    private final RowMapper<CartEntity> cartEntityRowMapper = (resultSet, rowNumber) -> new CartEntity(
            resultSet.getInt("user_id"),
            resultSet.getInt("product_id")
    );

    @Override
    public int delete(final CartEntity cartEntity) {
        String sql = "delete from carts where user_id = ? and product_id = ?";
        return jdbcTemplate.update(sql, cartEntity.getUserId(), cartEntity.getProductId());
}
}
