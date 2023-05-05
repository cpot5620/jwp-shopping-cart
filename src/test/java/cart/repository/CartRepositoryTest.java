package cart.repository;

import cart.authentication.entity.Member;
import cart.authentication.exception.MemberPersistenceFailedException;
import cart.authentication.repository.JdbcMemberRepository;
import cart.authentication.repository.MemberRepository;
import cart.cart.entity.Cart;
import cart.cart.exception.CartPersistenceFailedException;
import cart.cart.repository.CartRepository;
import cart.cart.repository.JdbcCartRepository;
import cart.product.entity.Product;
import cart.product.repository.JdbcProductRepository;
import cart.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@Import({JdbcMemberRepository.class, JdbcProductRepository.class, JdbcCartRepository.class})
class CartRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartRepository cartRepository;

    @Test
    @DisplayName("주어진 Member 정보와 Product 정보로 Cart를 저장할 수 있다.")
    void save() throws MemberPersistenceFailedException, CartPersistenceFailedException {
        // given : 멤버와 상품을 저장한다.
        Member member = memberRepository.save(new Member("test@gmail.com", "password1234!"));
        Product product = productRepository.save(new Product("product", "product.png", new BigDecimal(4000)));

        // when
        Cart cart = cartRepository.save(new Cart(member.getEmail(), product.getId()));
        assertThat(cart.getMemberEmail()).isEqualTo(member.getEmail());
        assertThat(cart.getProductId()).isEqualTo(product.getId());
    }

    @Test
    @DisplayName("존재하지 않는 멤버 이메일로 상품을 장바구니에 담을 수 없다.")
    void invalidEmail() throws CartPersistenceFailedException {
        Product product = productRepository.save(new Product("product", "product.png", new BigDecimal(4000)));

        assertThatThrownBy(() -> cartRepository.save(new Cart("invalid@gmail.com", product.getId())))
                .isInstanceOf(CartPersistenceFailedException.class)
                .hasMessage("존재하지 않는 Member 또는 Product로 Cart를 저장할 수 없습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 상품을 장바구니에 담을 수 없다.")
    void invalidProduct() throws MemberPersistenceFailedException {
        Member member = memberRepository.save(new Member("test@gmail.com", "password1234!"));

        assertThatThrownBy(() -> cartRepository.save(new Cart(member.getEmail(), 99L)))
                .isInstanceOf(CartPersistenceFailedException.class)
                .hasMessage("존재하지 않는 Member 또는 Product로 Cart를 저장할 수 없습니다.");
    }

    @Test
    @DisplayName("장바구니에 담은 상품이 삭제되면 장바구니 내역도 함께 사라진다.")
    void deleteProduct() throws MemberPersistenceFailedException, CartPersistenceFailedException {
        // given : 장바구니에 하나가 저장되어 있다.
        Member member = memberRepository.save(new Member("test@gmail.com", "password1234!"));
        Product product = productRepository.save(new Product("product", "product.png", new BigDecimal(4000)));
        cartRepository.save(new Cart(member.getEmail(), product.getId()));
        assertThat(cartRepository.findAllByMemberEmail(member.getEmail())).hasSize(1);

        // when : 상품을 삭제한다.
        productRepository.deleteById(product.getId());

        // then : 삭제된 상품에 대한 장바구니 내역도 사라져있어야 한다.
        assertThat(cartRepository.findAllByMemberEmail(member.getEmail())).hasSize(0);
    }

    @Test
    @DisplayName("같은 멤버가 같은 상품을 두 번 담을 수는 없다.")
    void duplicatingCart() throws CartPersistenceFailedException, MemberPersistenceFailedException {
        // given
        Member member = memberRepository.save(new Member("test@gmail.com", "password1234!"));
        Product product = productRepository.save(new Product("product", "product.png", new BigDecimal(4000)));
        cartRepository.save(new Cart(member.getEmail(), product.getId()));

        // when & then
        assertThatThrownBy(() -> cartRepository.save(new Cart(member.getEmail(), product.getId())))
                .isInstanceOf(CartPersistenceFailedException.class)
                .hasMessage("동일한 Member와 동일한 Product 정보가 담긴 Cart를 저장할 수 없습니다.");
    }

    @Test
    @DisplayName("주어진 이메일과 상품 아이디로 저장된 장바구니를 삭제할 수 있다.")
    void deleteByEmailAndId() throws MemberPersistenceFailedException, CartPersistenceFailedException {
        // given
        Member member = memberRepository.save(new Member("test@gmail.com", "password1234!"));
        Product product = productRepository.save(new Product("product", "product.png", new BigDecimal(4000)));
        cartRepository.save(new Cart(member.getEmail(), product.getId()));
        assertThat(cartRepository.findAllByMemberEmail(member.getEmail())).hasSize(1);

        // when
        cartRepository.deleteByMemberEmailAndProductId(member.getEmail(), product.getId());
        assertThat(cartRepository.findAllByMemberEmail(member.getEmail())).hasSize(0);
    }

    @Test
    @DisplayName("존재하지 않는 상품 아이디로는 장바구니를 삭제할 수 없다.")
    void invalidDeletingWithId() throws MemberPersistenceFailedException, CartPersistenceFailedException {
        // given
        Member member = memberRepository.save(new Member("test@gmail.com", "password1234!"));
        Product product = productRepository.save(new Product("product", "product.png", new BigDecimal(4000)));
        cartRepository.save(new Cart(member.getEmail(), product.getId()));
        assertThat(cartRepository.findAllByMemberEmail(member.getEmail())).hasSize(1);

        // when
        assertThatThrownBy(() -> cartRepository.deleteByMemberEmailAndProductId("invalid@gmail.com", product.getId()))
                .isInstanceOf(CartPersistenceFailedException.class)
                .hasMessage("삭제할 대상이 데이터베이스에 존재하지 않습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 이메일로는 장바구니를 삭제할 수 없다.")
    void invalidDeletingWithEmail() throws MemberPersistenceFailedException, CartPersistenceFailedException {
        // given
        Member member = memberRepository.save(new Member("test@gmail.com", "password1234!"));
        Product product = productRepository.save(new Product("product", "product.png", new BigDecimal(4000)));
        cartRepository.save(new Cart(member.getEmail(), product.getId()));
        assertThat(cartRepository.findAllByMemberEmail(member.getEmail())).hasSize(1);

        // when
        assertThatThrownBy(() -> cartRepository.deleteByMemberEmailAndProductId(member.getEmail(), 2L))
                .isInstanceOf(CartPersistenceFailedException.class)
                .hasMessage("삭제할 대상이 데이터베이스에 존재하지 않습니다.");
    }
}