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

//    public List<Category> listAll() {
//        return categoryRepository.findAll();
//    }

    public List<Category> listAll() {
        List<Category> rootCategories = categoryRepository.findRootCategories();
        return listHierarchicalCategories(rootCategories);
    }

    private List<Category> listHierarchicalCategories(List<Category> rootCategories) {
        List<Category> hierarchicalCategories = new ArrayList<>();
//        for (Category rootCategory : rootCategories) {
//            hierarchicalCategories.add(Category.copyFull(rootCategory));
//            Set<Category> children = rootCategory.getChildren();
//            for (Category subCategory :
//                    children) {
//                String name = "--" + subCategory.getName();
//                hierarchicalCategories.add(Category.copyFull(subCategory, name));
//                listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1);
//            }
//        }

        Consumer<? super Category> consumer = new Consumer<Category>() {
            @Override
            public void accept(Category rootCategories) {
                hierarchicalCategories.add(Category.copyFull(rootCategories));
                Set<Category> children = rootCategories.getChildren();
                for (Category subCategory :
                        children) {
                    String name = "--" + subCategory.getName();
                    hierarchicalCategories.add(Category.copyFull(subCategory, name));
                    listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1);

                }
            }
        };
        rootCategories.forEach(consumer);
        return hierarchicalCategories;
    }

    private void listSubHierarchicalCategories(List<Category> hierarchicalCategories,
                                               Category parent, int subLevel) {
        Set<Category> children = parent.getChildren();
        int newSubLevel = subLevel + 1;

        for (Category subCategory :
                children) {
            String name = "";
            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }
            name += subCategory.getName();

            hierarchicalCategories.add(Category.copyFull(subCategory, name));
            listSubHierarchicalCategories(hierarchicalCategories,
                    subCategory, newSubLevel);

        }
    }


    public List<Category> listCategoriesUsedInForm() {
        List<Category> categoriesUsedInForm = new ArrayList<>();

        Iterable<Category> categoriesInDB = categoryRepository.findAll();

        for (Category category :
                categoriesInDB) {
            if (category.getParent() == null) {
                categoriesUsedInForm.add(new Category(category.getName()));
                Set<Category> children = category.getChildren();
                for (Category subCategory : children
                ) {
                    String name = "--" + subCategory.getName();
                    categoriesUsedInForm.add(Category.copyIdAndName(category.getCategory_id(), name));
                    listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, 1);
                }
            }
        }


        return categoriesUsedInForm;
    }

    private void listSubCategoriesUsedInForm(List<Category> categoriesUsedInForm, Category parent, int subLevel) {
        int newSubLevel = subLevel + 1;
        Set<Category> children = parent.getChildren();
        for (Category subCategory : children) {
            String name = "";
            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }
            name += subCategory.getName();
            categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getCategory_id(), name));
            listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, newSubLevel);
        }
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

}
