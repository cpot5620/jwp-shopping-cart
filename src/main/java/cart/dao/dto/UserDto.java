package cart.dao.dto;

public class UserDto {

    private final Integer id;
    private final String email;
    private final String password;

    public UserDto(Integer id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
