package backend.admin.user.controller;


import backend.admin.FileUploadUtil;
import backend.admin.user.export.UserCsvExporter;
import backend.admin.user.export.UserExcelExporter;
import backend.admin.user.export.UserPdfExporter;
import backend.admin.user.exceptions.UserNotFoundException;
import backend.admin.user.service.UserService;
import common.data.entity.Role;
import common.data.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


//    @GetMapping("/users")
//    public String listAll(Model model) {
//        List<User> users = userService.listAll();
//        model.addAttribute("listUsers", users);
//        return "users";
//    }

    @GetMapping("/users")
    public String listFirstPage(Model model) {
        //http://127.0.0.1:8080/KaplanShopAdmin/users/page/1?sortField=email&sortDir=asc
        //http://127.0.0.1:8080/KaplanShopAdmin/users/page/3?sortField=firstName&sortDir=desc
        //http://127.0.0.1:8080/KaplanShopAdmin/users/page/1?sortField=email&sortDir=asc
        //http://127.0.0.1:8080/KaplanShopAdmin/users/page/1?sortField=email&sortDir=asc&keyword=bruce
        return listByPage(1, model,"firstName","asc",null);
        //return listByPage(1, model,"email","asc");


    }

    @GetMapping("/users/new")
    public String newUser(Model model) {
        List<Role> listRoles = userService.listRoles();
        User user = new User();
        user.setEnabled(true);

        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        model.addAttribute("pageTitle", " Create New User");

        return "users/user_form";
    }

    //    @RequestMapping(value = "/users/save", method = RequestMethod.POST)
    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        // System.out.println(user);
        // System.out.println(multipartFile.getOriginalFilename());
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            User savedUser = userService.save(user);
            String uploadDir = "user-photos/" + savedUser.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            if (user.getPhotos().isEmpty()) {
                user.setPhotos(null);
            }
            userService.save(user);
        }

        //userService.save(user);

        redirectAttributes.addFlashAttribute("message", "The user has been saved successfully !");

//        return "redirect:/users";

        return getRedirectURLtoAffectedUser(user);
    }

    private String getRedirectURLtoAffectedUser(User user) {
        String firstPartOfEmail= user.getEmail().split("@")[0];
        return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
    }

    @GetMapping("/users/edit/{user_id}")
    public String editUser(@PathVariable(name = "user_id") Long id, RedirectAttributes redirectAttributes, Model model) {
        try {
            User user = userService.get(id);
            List<Role> listRoles = userService.listRoles();

            model.addAttribute("user", user);
            model.addAttribute("pageTitle", " Edit User (ID :" + id + ")");
            model.addAttribute("listRoles", listRoles);

            return "users/user_form";

        } catch (UserNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/users";

        }
    }

    @GetMapping("/users/delete/{user_id}")
    public String deleteUser(@PathVariable(name = "user_id") Long id, RedirectAttributes redirectAttributes, Model model) {
        try {
            userService.delete(id);
            redirectAttributes.addFlashAttribute("message", "The user ID " + id + " deleted successfully !");

        } catch (UserNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());

        }
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String updateUserEnabledStatus(@PathVariable(name = "id") Long id, @PathVariable(name = "status") boolean enabled,
                                          RedirectAttributes redirectAttributes) {
        userService.updateUserEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The user ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/users";

    }

    @GetMapping("/users/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
                             @Param("sortField")String sortField, @Param("sortDir")String sortDir,
                             @Param("keyword")String keyword) {

        System.out.println("Sort Field: "+ sortField);
        System.out.println("Sort Order: "+ sortDir);
        Page<User> page = userService.listByPage(pageNum,sortField,sortDir,keyword);
        List<User> listUsers = page.getContent();

//        System.out.println("Page Number = "+pageNum);
//        System.out.println("Total elements = "+page.getTotalElements());
//        System.out.println("Total pages = "+page.getTotalPages());

        long startCount = (long) (pageNum - 1) * userService.getUsersPerPage();
        long endCount = startCount + userService.getUsersPerPage();
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }


        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);

        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listUsers", listUsers);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);
        String reverseSortDir=sortDir.equals("asc")?"desc":"asc";
        model.addAttribute("reverseSortDir", reverseSortDir);
        return "users/users";

    }

    @GetMapping("/users/export/csv")
    public void exportToCSV(HttpServletResponse httpServletResponse) throws IOException {
        List<User> listUsers= userService.listAll();
        UserCsvExporter exporter =new UserCsvExporter();
        exporter.export(listUsers,httpServletResponse);

    }
    @GetMapping("/users/export/excel")
    public void exportToExcel(HttpServletResponse httpServletResponse) throws IOException {
        List<User> listUsers= userService.listAll();
        UserExcelExporter exporter =new UserExcelExporter();
        exporter.export(listUsers,httpServletResponse);

    }
    @GetMapping("/users/export/pdf")
    public void exportToPDF(HttpServletResponse httpServletResponse) throws IOException {
        List<User> listUsers= userService.listAll();
        UserPdfExporter exporter =new UserPdfExporter();
        exporter.export(listUsers,httpServletResponse);

    }




}
