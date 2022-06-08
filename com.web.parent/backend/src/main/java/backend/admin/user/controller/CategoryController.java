package backend.admin.user.controller;


import backend.admin.user.service.CategoryService;
import common.data.entity.Category;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
