package backend.admin.user.controller;


import backend.admin.FileUploadUtil;
import backend.admin.user.service.CategoryService;
import common.data.entity.Category;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public String listAll(Model model){
       List<Category> listCategories = categoryService.listAll();
       model.addAttribute("listCategories",listCategories);
       return "categories/categories";
    }
    @GetMapping("/categories/new")
    public String newCategory(Model model){
        List<Category> listCategories = categoryService.listCategoriesUsedInForm();
        model.addAttribute("category",new Category());
        model.addAttribute("listCategories",listCategories);
        model.addAttribute("pageTitle","Create new Category");
        return "categories/category_form";
    }

    @PostMapping("/categories/save")
    public String saveCategory(Category category, @RequestParam("fileImage")MultipartFile multipartFile,
                               RedirectAttributes redirectAttributes) throws IOException {
        String fileName= StringUtils.cleanPath(multipartFile.getOriginalFilename());
        category.setImage(fileName);
        Category savedCategory = categoryService.save(category);
        String uploadDir="../category-images/"+ savedCategory.getCategory_id().toString();
        FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
        redirectAttributes.addFlashAttribute("message","The category has been saved successfully!");
        return "redirect:/categories";

    }
}