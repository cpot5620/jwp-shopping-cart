package cart.web.index;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/cart")
@Controller
public class CartViewController {

    @GetMapping
    public String loadCartPage() {
        return "cart";
    }
}
