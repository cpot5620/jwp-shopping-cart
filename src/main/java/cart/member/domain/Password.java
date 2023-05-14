package cart.member.domain;

import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
public class Password {

    private final String password;

    public Password(final String password) {
        validate(password);

        this.password = password;
    }

    private void validate(final String password) {
        if (!StringUtils.hasText(password)) {
            throw new IllegalArgumentException("비밀번호가 존재하지 않습니다");
        }
    }
}
