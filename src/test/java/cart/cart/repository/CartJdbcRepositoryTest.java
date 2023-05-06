package cart.cart.repository;

import cart.cart.domain.Cart;
import cart.cart.domain.CartId;
import cart.config.RepositoryTestConfig;
import cart.member.domain.Member;
import cart.member.domain.MemberId;
import cart.member.repository.MemberJdbcRepository;
import cart.member.repository.MemberRepository;
import cart.product.domain.Product;
import cart.product.domain.ProductId;
import cart.product.repository.ProductJdbcRepository;
import cart.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CartJdbcRepositoryTest extends RepositoryTestConfig {
    private static final Member MEMBER = new Member("헤나", "test@test.com", "test");
    private static final Product CHICKEN = new Product("치킨", 10000, "image-url");
    private static final Product PIZZA = new Product("피자", 20000, "image2-url");

    CartRepository cartRepository;
    MemberRepository memberRepository;
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        cartRepository = new CartJdbcRepository(jdbcTemplate);
        memberRepository = new MemberJdbcRepository(jdbcTemplate);
        productRepository = new ProductJdbcRepository(jdbcTemplate);
    }

    @DisplayName("회원 번호를 통해 장바구니에 상품을 저장한다.")
    @Test
    void saveByMemberId() {
        // given
        final MemberId memberId = memberRepository.save(MEMBER);

        final ProductId productChickenId = productRepository.save(CHICKEN);

        // when
        final CartId saveCartId = cartRepository.saveByMemberId(memberId, productChickenId);

        final Optional<Cart> maybeCart = cartRepository.findByCartId(saveCartId);

        assertThat(maybeCart).isPresent();
        final Cart cart = maybeCart.get();

        // then
        assertThat(cart)
                .usingRecursiveComparison()
                .isEqualTo(new Cart(
                        saveCartId,
                        memberId,
                        productChickenId
                ));
    }

    @DisplayName("상품 번호로 회원 장바구니에 있는 카트를 가져온다.")
    @Test
    void findByMemberIdAndProductId() {
        // given
        final MemberId memberId = memberRepository.save(MEMBER);
        final ProductId productChickenId = productRepository.save(CHICKEN);

        final CartId cartChickenId = cartRepository.saveByMemberId(memberId, productChickenId);

        // when
        final Optional<Cart> maybeCart = cartRepository.findByMemberIdAndProductId(memberId, productChickenId);

        assertThat(maybeCart).isPresent();
        final Cart cart = maybeCart.get();

        // then
        assertThat(cart)
                .usingRecursiveComparison()
                .isEqualTo(new Cart(
                        cartChickenId, memberId, productChickenId
                ));
    }

    @DisplayName("특정 회원의 장바구니 전체 상품 목록을 불러온다.")
    @Test
    void findAllByMemberId() {
        // given
        final MemberId memberId = memberRepository.save(MEMBER);
        final ProductId productChickenId = productRepository.save(CHICKEN);
        final ProductId productPizzaId = productRepository.save(PIZZA);

        final CartId cartChickenId = cartRepository.saveByMemberId(memberId, productChickenId);
        final CartId cartPizzaId = cartRepository.saveByMemberId(memberId, productPizzaId);

        // when
        final List<Cart> findCarts = cartRepository.findAllByMemberId(memberId);

        // then
        assertThat(findCarts)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(
                        new Cart(cartChickenId, memberId, productChickenId),
                        new Cart(cartPizzaId, memberId, productPizzaId)
                );
    }
}
