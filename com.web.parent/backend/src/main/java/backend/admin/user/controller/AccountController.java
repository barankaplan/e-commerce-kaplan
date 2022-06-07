package backend.admin.user.controller;

import backend.admin.FileUploadUtil;
import backend.admin.user.security.KaplanShopDetails;
import backend.admin.user.service.UserService;
import common.data.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
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

    @PostMapping("/account/update")
    public String saveUser(User user, RedirectAttributes redirectAttributes,
                           @RequestParam("image") MultipartFile multipartFile,
                           @AuthenticationPrincipal KaplanShopDetails loggedUser ) throws IOException {


        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            User savedUser = userService.updateAccount(user);
            String uploadDir = "user-photos/" + savedUser.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            if (user.getPhotos().isEmpty()) {
                user.setPhotos(null);
            }

            userService.updateAccount(user);
        }
        loggedUser.setFirstName(user.getFirstName());
        loggedUser.setLastName(user.getLastName());
        redirectAttributes.addFlashAttribute("message", "The account details of the user has been updated successfully !");
        return "redirect:/account";
    }

}
