package cart.auth.infrastructure;

import cart.domain.cart.AuthInfo;
import org.springframework.web.context.request.NativeWebRequest;

public interface AuthorizationExtractor {

    String AUTHORIZATION = "Authorization";

    AuthInfo extract(NativeWebRequest webRequest);
}
