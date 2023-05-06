package cart.common.data;

import cart.member.domain.Member;
import cart.member.repository.MemberRepository;
import cart.product.domain.Product;
import cart.product.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitialization implements CommandLineRunner {
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public DataInitialization(final MemberRepository memberRepository, final ProductRepository productRepository) {
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(final String... args) throws Exception {
        memberRepository.save(new Member("헤나", "hyena@hyena.com", "hyena"));
        memberRepository.save(new Member("토니", "tony@tony.com", "tony"));

        productRepository.save(new Product("치킨", 20000, "https://pelicana.co.kr/resources/images/menu/original_menu10_210705.png"));
    }
}
