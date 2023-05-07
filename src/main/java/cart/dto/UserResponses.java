package cart.dto;

import java.util.List;

public class UserResponses {

    private final List<UserResponse> userResponses;

    public UserResponses(final List<UserResponse> userResponses) {
        this.userResponses = userResponses;
    }

    public List<UserResponse> getUserResponses() {
        return userResponses;
    }
}
