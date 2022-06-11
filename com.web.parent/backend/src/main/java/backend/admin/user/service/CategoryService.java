package backend.admin.user.service;


import backend.admin.user.exceptions.CategoryNotFoundException;
import backend.admin.user.repository.CategoryRepository;
import common.data.entity.Category;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

//    public List<Category> listAll() {
//        return categoryRepository.findAll();
//    }

    public List<Category> listAll(String sortDir) {
        Sort sort = Sort.by("name");
        if (sortDir == null || sortDir.isEmpty()){
            sort = sort.ascending();

        }
        else if (sortDir.equals("asc")) {
            sort = sort.ascending();
        } else  if (sortDir.equals("desc")) {
            sort = sort.descending();
        }
        List<Category> rootCategories = categoryRepository.findRootCategories(sort);
        return listHierarchicalCategories(rootCategories,sortDir);
    }

    private List<Category> listHierarchicalCategories(List<Category> rootCategories,
                                                      String sortDir) {
        List<Category> hierarchicalCategories = new ArrayList<>();

        for (Category rootCategory : rootCategories) {
            hierarchicalCategories.add(Category.copyFull(rootCategory));

            Set<Category> children = sortSubCategories(rootCategory.getChildren(),sortDir);

            for (Category subCategory : children) {
                String name = "--" + subCategory.getName();
                hierarchicalCategories.add(Category.copyFull(subCategory, name));

                listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1,sortDir);
            }
        }

        return hierarchicalCategories;
    }

//    private List<Category> listHierarchicalCategories(List<Category> rootCategories) {
//        List<Category> hierarchicalCategories = new ArrayList<>();
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
//
////        Consumer<? super Category> consumer = new Consumer<Category>() {
////            @Override
////            public void accept(Category rootCategories) {
////                hierarchicalCategories.add(Category.copyFull(rootCategories));
////                Set<Category> children = rootCategories.getChildren();
////                for (Category subCategory :
////                        children) {
////                    String name = "--" + subCategory.getName();
////                    hierarchicalCategories.add(Category.copyFull(subCategory, name));
////                    listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1);
////
////                }
////            }
////        };
////        rootCategories.forEach(consumer);
//        return hierarchicalCategories;
//    }

    private void listSubHierarchicalCategories(List<Category> hierarchicalCategories,
                                               Category parent, int subLevel,String sortDir) {
        Set<Category> children = sortSubCategories(parent.getChildren(),sortDir);
        int newSubLevel = subLevel + 1;

        for (Category subCategory : children) {
            String name = "";
            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }
            name += subCategory.getName();

            hierarchicalCategories.add(Category.copyFull(subCategory, name));

            listSubHierarchicalCategories(hierarchicalCategories, subCategory, newSubLevel,sortDir);
        }

    }


//    public List<Category> listCategoriesUsedInForm() {
//        List<Category> categoriesUsedInForm = new ArrayList<>();
//
//        Iterable<Category> categoriesInDB = categoryRepository.findAll();
//
//        for (Category category :
//                categoriesInDB) {
//            if (category.getParent() == null) {
//                categoriesUsedInForm.add(new Category(category.getName()));
//                Set<Category> children = category.getChildren();
//                for (Category subCategory : children
//                ) {
//                    String name = "--" + subCategory.getName();
//                    categoriesUsedInForm.add(Category.copyIdAndName(category.getCategory_id(), name));
//                    listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, 1);
//                }
//            }
//        }
//
//
//        return categoriesUsedInForm;
//    }

//    private void listSubCategoriesUsedInForm(List<Category> categoriesUsedInForm, Category parent, int subLevel) {
//        int newSubLevel = subLevel + 1;
//        Set<Category> children = parent.getChildren();
//        for (Category subCategory : children) {
//            String name = "";
//            for (int i = 0; i < newSubLevel; i++) {
//                name += "--";
//            }
//            name += subCategory.getName();
//            categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getCategory_id(), name));
//            listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, newSubLevel);
//        }
//    }


    public List<Category> listCategoriesUsedInForm() {
        List<Category> categoriesUsedInForm = new ArrayList<>();

        Iterable<Category> categoriesInDB = categoryRepository.findRootCategories(Sort.by("name").ascending());

        for (Category category : categoriesInDB) {
            if (category.getParent() == null) {
                categoriesUsedInForm.add(Category.copyIdAndName(category));

                Set<Category> children = sortSubCategories(category.getChildren());

                for (Category subCategory : children) {
                    String name = "--" + subCategory.getName();
                    categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getCategory_id(), name));

                    listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, 1);
                }
            }
        }

        return categoriesUsedInForm;
    }


    private void listSubCategoriesUsedInForm(List<Category> categoriesUsedInForm,
                                             Category parent, int subLevel) {
        int newSubLevel = subLevel + 1;
        Set<Category> children = sortSubCategories(parent.getChildren());

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

    public Category get(Long id) throws CategoryNotFoundException {
        try {
            return categoryRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new CategoryNotFoundException("Couldn't find any category with ID: " + id);
        }
    }

    public String checkUnique(Long id, String name, String alias) {
        boolean isCreatingNew = (id == null || id == 0);
        Category byName = categoryRepository.findByName(name);
        if (isCreatingNew) {
            if (byName != null) {
                return "Duplicated Name!";
            } else {
                Category byAlias = categoryRepository.findByAlias(alias);
                if (byAlias != null) {
                    return "Duplicated Alias!";

                }
            }
        } else {
            if (byName != null && byName.getCategory_id() != id) {
                return "Duplicated Name!";
            }
            Category byAlias = categoryRepository.findByAlias(alias);
            if (byAlias != null && byAlias.getCategory_id() != id) {
                return "Duplicated Alias!";

            }


        }
        return "OK";
    }
    private SortedSet<Category> sortSubCategories(Set<Category> children) {
        return sortSubCategories(children,"asc");
    }


    private SortedSet<Category> sortSubCategories(Set<Category> children,String sortDir) {
        SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {

            @Override
            public int compare(Category cat1, Category cat2) {

                if(sortDir.equals("asc")){
                    return cat1.getName().compareTo(cat2.getName());

                }else{
                    return cat2.getName().compareTo(cat1.getName());
                }
            }
        });
        sortedChildren.addAll(children);
        return sortedChildren;
    }
}
