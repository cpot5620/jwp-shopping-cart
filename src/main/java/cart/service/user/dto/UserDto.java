package cart.service.user.dto;

import cart.domain.user.User;

public class UserDto {
    private final Long id;
    private final String email;
    private final String password;

    public UserDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
