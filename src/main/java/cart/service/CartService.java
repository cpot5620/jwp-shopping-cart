package cart.service;

import cart.dao.CartDao;
import cart.dao.CartProductDao;
import cart.dao.dto.CartProductResultMap;
import cart.dto.CartResponse;
import cart.dto.CartResponses;
import cart.dto.CartSaveRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.toList;

@Service
public class CartService {

    private final CartDao cartDao;
    private final CartProductDao cartProductDao;
    private final CartMapper cartMapper;

    public CartService(final CartDao cartDao, final CartProductDao cartProductDao, final CartMapper cartMapper) {
        this.cartDao = cartDao;
        this.cartProductDao = cartProductDao;
        this.cartMapper = cartMapper;
    }

    public Long save(final CartSaveRequest saveRequest) {
        return cartDao.save(cartMapper.mapFrom(saveRequest));
    }

    public CartResponses findAllByUserId(final Long userId) {
        final List<CartProductResultMap> carts = cartProductDao.findAllByUserId(userId);

        final List<CartResponse> cartResponses = carts.stream()
                .map(CartResponse::new)
                .collect(toList());
        return new CartResponses(cartResponses);
    }

    public void delete(Long userId, Long productId) {
        final int affectedRows = cartDao.delete(userId, productId);

        if (affectedRows == 0) {
            throw new NoSuchElementException("장바구니 정보를 찾을 수 없습니다.");
        }
    }
}
