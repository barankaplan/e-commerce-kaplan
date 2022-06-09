package backend.admin.user.service;


import backend.admin.user.repository.CategoryRepository;
import common.data.entity.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> listAll() {
        return categoryRepository.findAll();
    }

    public List<Category> listCategoriesUsedInForm() {
        List<Category> categoriesUsedInForm = new ArrayList<>();

        Iterable<Category> categoriesInDB = categoryRepository.findAll();

        for (Category category :
                categoriesInDB) {
            if (category.getParent() == null) {
                categoriesUsedInForm.add(new Category(category.getName()));
                Set<Category> children = category.getChildren();
                for (Category subcategory : children
                ) {
                    String name = "--" + subcategory.getName();
                    categoriesUsedInForm.add(new Category(name));
                    listChildren(categoriesUsedInForm,subcategory, 1);
                }
            }
        }


        return categoriesUsedInForm;
    }

    private void listChildren(List<Category> categoriesUsedInForm, Category parent, int subLevel) {
        int newSubLevel = subLevel + 1;
        Set<Category> children = parent.getChildren();
        for (Category subCategory : children) {
            String name = "";
            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }
            name += subCategory.getName();
            categoriesUsedInForm.add(new Category(name));
            listChildren(categoriesUsedInForm, subCategory, newSubLevel);
        }
    }

    public Category save(Category category){
        return categoryRepository.save(category);
    }

}
