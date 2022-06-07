package backend.admin.user.controller;

import backend.admin.user.security.KaplanShopDetails;
import backend.admin.user.service.UserService;
import common.data.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;


@Controller
public class AccountController {
    private final UserService userService;

    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/account")
    public String viewDetails(@AuthenticationPrincipal KaplanShopDetails loggedUser,
                              Model model) {
        String email = loggedUser.getUsername();
        Optional<User> user = userService.getByEmail(email);
        model.addAttribute("user", user.get());
        return "account_form";

    }
}
