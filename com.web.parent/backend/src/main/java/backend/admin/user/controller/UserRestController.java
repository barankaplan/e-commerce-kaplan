package backend.admin.user.controller;


import backend.admin.user.service.UserService;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {
    private final UserService userService;


    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users/check_email")
    public String checkDuplicateEMail(@Param("email") String email,@Param("id")Long id) {
        return userService.isEmailUnique(id,email) ? "OK" : "Duplicated";
    }
}
