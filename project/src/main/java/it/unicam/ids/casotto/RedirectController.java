package it.unicam.ids.casotto;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class RedirectController {
    @GetMapping("/")
    public RedirectView home() {
        return new RedirectView("/index.html");
    }
}
