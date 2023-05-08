package cart.dao.user;

import static org.assertj.core.api.Assertions.assertThat;

import cart.user.domain.CartUser;
import cart.user.domain.UserEmail;
import cart.user.persistence.CartUserDao;
import cart.user.persistence.CartUserRepositoryImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

@Import({CartUserRepositoryImpl.class, CartUserDao.class})
@AutoConfigureTestDatabase(replace = Replace.NONE)
@JdbcTest
class CartUserRepositoryImplTest {

    @Autowired
    private CartUserRepositoryImpl cartUserRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("사용자 이메일로 사용자 조회시 존재하지 않으면 null을 반환 한다.")
    @Test
    void findByEmailFailureNotExist() {
        final String findTargetEmail = "a@a.com";

        final Optional<CartUser> user = cartUserRepository.findByEmail(findTargetEmail);

        assertThat(user.isEmpty()).isTrue();
    }

    @DisplayName("사용자 이메일로 사용자 조회 테스트")
    @Test
    void findByEmail() {
        final String findTargetEmail = "a@a.com";
        jdbcTemplate.update("INSERT INTO cart_user (email, cart_password)\n"
                + "values ('a@a.com', 'password1'),\n"
                + "       ('b@b.com', 'password2');");

        final CartUser user = cartUserRepository.findByEmail(findTargetEmail).get();

        assertThat(user).isNotNull();
        assertThat(user.getUserEmail()).isEqualTo(findTargetEmail);
    }

    @DisplayName("사용자 저장 테스트")
    @Test
    void saveUser() {
        final String email = "a@a.com";
        final CartUser cartUser = new CartUser(
                UserEmail.from(email),
                "password"
        );

        cartUserRepository.save(cartUser);

        final CartUser user = cartUserRepository.findByEmail(email).get();

        assertThat(user).isNotNull();
        assertThat(user.getUserEmail()).isEqualTo(email);
    }

    @DisplayName("사용자 전체 조회 테스트")
    @Test
    void findAllCartUser() {
        jdbcTemplate.update("INSERT INTO cart_user (email, cart_password)\n"
                + "values ('a@a.com', 'password1'),\n"
                + "       ('b@b.com', 'password2');");

        final List<CartUser> cartUsers = cartUserRepository.findAll();

        assertThat(cartUsers).hasSize(2);
    }
}
