package backend.admin.user.controller;


import backend.admin.FileUploadUtil;
import backend.admin.user.exceptions.CategoryNotFoundException;
import backend.admin.user.service.CategoryService;
import common.data.entity.Category;
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

import java.io.IOException;
import java.util.List;

@Controller
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public String listFirstPage(@Param("sortDir") String sortDir, Model model) {

        return listByPage(1,sortDir,null,model);
    }

    @GetMapping("/categories/new")
    public String newCategory(Model model) {
        List<Category> listCategories = categoryService.listCategoriesUsedInForm();
        model.addAttribute("category", new Category());
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("pageTitle", "Create new Category");
        return "categories/category_form";
    }


    @PostMapping("/categories/save")
    public String saveCategory(Category category, @RequestParam("fileImage") MultipartFile multipartFile,
                               RedirectAttributes redirectAttributes) throws IOException {

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            category.setImage(fileName);

            Category savedCategory = categoryService.save(category);
            String uploadDir = "../category-images/" + savedCategory.getCategoryId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            categoryService.save(category);
        }
        redirectAttributes.addFlashAttribute("message", "The category has been saved" +
                "successfully !");
        return "redirect:/categories";

    }

    @GetMapping("/categories/edit/{categoryId}")
    public String editCategory(@PathVariable(name = "categoryId") Long id, RedirectAttributes redirectAttributes, Model model) {
        try {
            Category category = categoryService.get(id);
            List<Category> listCategories = categoryService.listCategoriesUsedInForm();

            model.addAttribute("category", category);
            model.addAttribute("pageTitle", " Edit Category (ID :" + id + ")");
            model.addAttribute("listCategories", listCategories);


            return "categories/category_form";

        } catch (CategoryNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/categories";
        }
    }

    @GetMapping("/categories/{categoryId}/enabled/{status}")
    public String updateCategoryEnabledStatus(@PathVariable(name = "categoryId") Long id, @PathVariable(name = "status") boolean enabled,
                                          RedirectAttributes redirectAttributes) {
        categoryService.updateCategoryEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The category ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/categories";

    }

    @GetMapping("/categories/delete/{categoryId}")
    public String deleteCategory(@PathVariable(name = "categoryId") Long id, RedirectAttributes redirectAttributes,
                                 Model model) {
        try {
            categoryService.delete(id);
            String categoryDir="../category-images/"+id;
            FileUploadUtil.removeDir(categoryDir);
            redirectAttributes.addFlashAttribute("message", "The category ID " + id + " deleted successfully !");


        } catch (CategoryNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());

        }
        return "redirect:/categories";
    }

    @GetMapping("/categories/page/{pageNum}")
    public String listByPage(@PathVariable(name="pageNum")int pageNum,
                             @Param("sortDir") String sortDir,@Param("keyword") String keyword,
                             Model model){
        if (sortDir == null || sortDir.isEmpty()) {
            sortDir = "asc";

        }
        CategoryPageInfo categoryPageInfo= new CategoryPageInfo();
        List<Category> listCategories = categoryService.listByPage(categoryPageInfo,pageNum,sortDir,keyword);
        long startCount = (long) (pageNum - 1) * categoryService.getCategoriesPerPage()+1;
        long endCount = startCount + categoryService.getCategoriesPerPage()-1;
        if (endCount > categoryPageInfo.getTotalElements()) {
            endCount = categoryPageInfo.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("totalPages", categoryPageInfo.getTotalPages());
        model.addAttribute("totalItems", categoryPageInfo.getTotalElements());
        model.addAttribute("currentPage", pageNum );
        model.addAttribute("sortField", "name" );
        model.addAttribute("sortDir", sortDir );
        model.addAttribute("keyword", keyword );
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("reverseSortDir", reverseSortDir);

        return "categories/categories";

    }
}
