package cart.controller.authentication;

import cart.exception.AuthorizationException;
import cart.service.MemberService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final MemberService memberService;

    public AuthenticationInterceptor(final MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        final AuthInfo authInfo = BasicAuthorizationExtractor.extract(header);

        if (memberService.hasMember(authInfo.getEmail(), authInfo.getPassword())) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        throw new AuthorizationException("입력한 정보의 회원은 존재하지 않습니다.");
    }
}
