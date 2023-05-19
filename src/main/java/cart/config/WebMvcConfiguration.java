package cart.config;

import cart.auth.interceptor.LoginInterceptor;
import cart.auth.resolver.BasicAuthenticationPrincipalArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;
    private final BasicAuthenticationPrincipalArgumentResolver basicAuthenticationPrincipalArgumentResolver;

    public WebMvcConfiguration(LoginInterceptor loginInterceptor,
                               BasicAuthenticationPrincipalArgumentResolver basicAuthenticationPrincipalArgumentResolver) {
        this.loginInterceptor = loginInterceptor;
        this.basicAuthenticationPrincipalArgumentResolver = basicAuthenticationPrincipalArgumentResolver;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/cart")
                .setViewName("cart");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/carts/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(basicAuthenticationPrincipalArgumentResolver);
    }
}
