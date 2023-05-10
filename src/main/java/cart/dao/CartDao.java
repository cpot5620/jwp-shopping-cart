package cart.dao;

import cart.entity.CartEntity;

import java.util.List;

public interface CartDao {
    int insert(CartEntity cart);

    List<CartEntity> selectByUserId(int userId);

    int delete(final CartEntity cartEntity);
}
