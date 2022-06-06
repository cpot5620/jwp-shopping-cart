package woowacourse.shoppingcart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static woowacourse.Fixtures.BIRTHDAY_VALUE_1;
import static woowacourse.Fixtures.CONTACT_VALUE_1;
import static woowacourse.Fixtures.CUSTOMER_ENTITY_1;
import static woowacourse.Fixtures.GENDER_MALE;
import static woowacourse.Fixtures.NAME_VALUE_1;

import java.time.LocalDate;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import woowacourse.shoppingcart.entity.PrivacyEntity;

@JdbcTest
class JdbcPrivacyDaoTest {
    private final PrivacyDao privacyDao;
    private final CustomerDao customerDao;

    @Autowired
    public JdbcPrivacyDaoTest(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        privacyDao = new JdbcPrivacyDao(jdbcTemplate, dataSource);
        customerDao = new JdbcCustomerDao(jdbcTemplate, dataSource);
    }

    @DisplayName("PrivacyEntity를 전달받아 데이터베이스에 추가한다.")
    @Test
    void save() {
        // given
        int customerId = customerDao.save(CUSTOMER_ENTITY_1);
        PrivacyEntity privacyEntity = new PrivacyEntity(customerId, NAME_VALUE_1, GENDER_MALE, BIRTHDAY_VALUE_1,
                CONTACT_VALUE_1);

        // when
        privacyDao.save(privacyEntity);
        PrivacyEntity actual = privacyDao.findById(customerId);

        // then
        assertThat(actual).isNotNull();
    }

    @DisplayName("Customer id를 전달받아 해당하는 객체를 조회하여 PrivacyEntity를 반환한다.")
    @Test
    void findById() {
        // given
        int customerId = customerDao.save(CUSTOMER_ENTITY_1);
        PrivacyEntity privacyEntity = new PrivacyEntity(customerId, NAME_VALUE_1, GENDER_MALE, BIRTHDAY_VALUE_1,
                CONTACT_VALUE_1);
        privacyDao.save(privacyEntity);

        // when
        PrivacyEntity actual = privacyDao.findById(customerId);

        // then
        assertThat(actual).extracting("customerId", "name", "gender", "birthday", "contact")
                .containsExactly(customerId, privacyEntity.getName(), privacyEntity.getGender(),
                        privacyEntity.getBirthday(), privacyEntity.getContact());
    }

    @DisplayName("PrivacyEntity와 Customer id를 전달받아 해당하는 Privacy를 수정한다.")
    @Test
    void update() {
        // given
        int customerId = customerDao.save(CUSTOMER_ENTITY_1);
        PrivacyEntity privacyEntity = new PrivacyEntity(customerId, NAME_VALUE_1, GENDER_MALE, BIRTHDAY_VALUE_1,
                CONTACT_VALUE_1);
        privacyDao.save(privacyEntity);

        // when
        PrivacyEntity newPrivacyEntity = new PrivacyEntity(customerId, "새로운 이름", "female", LocalDate.of(1999, 12, 21),
                "01033334444");
        privacyDao.update(newPrivacyEntity);

        PrivacyEntity actual = privacyDao.findById(customerId);

        // then
        assertThat(actual).extracting("customerId", "name", "gender", "birthday", "contact")
                .containsExactly(customerId, newPrivacyEntity.getName(), newPrivacyEntity.getGender(),
                        newPrivacyEntity.getBirthday(), newPrivacyEntity.getContact());
    }

    @DisplayName("id를 전달받아 해당하는 Privacy를 삭제한다.")
    @Test
    void delete() {
        // given
        int customerId = customerDao.save(CUSTOMER_ENTITY_1);
        PrivacyEntity privacyEntity = new PrivacyEntity(customerId, NAME_VALUE_1, GENDER_MALE, BIRTHDAY_VALUE_1,
                CONTACT_VALUE_1);
        privacyDao.save(privacyEntity);

        // when
        privacyDao.delete(customerId);

        // then
        assertThatThrownBy(() -> privacyDao.findById(customerId))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}
