package backend.admin.user.service;


import backend.admin.user.controller.CategoryPageInfo;
import backend.admin.user.exceptions.CategoryNotFoundException;
import backend.admin.user.repository.CategoryRepository;
import common.data.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private static final int ROOT_CATEGORIES_PER_PAGE = 4;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    public List<Category> listAll(String sortDir) {
        Sort sort = Sort.by("name");
        if (sortDir.equals("asc")) {
            sort = sort.ascending();
        } else if (sortDir.equals("desc")) {
            sort = sort.descending();
        }
        List<Category> rootCategories = categoryRepository.findRootCategories(sort);
        return listHierarchicalCategories(rootCategories, sortDir);
    }

    public List<Category> listByPage(CategoryPageInfo categoryPageInfo, int pageNum, String sortDir,
                                 String keyword) {
        Sort sort = Sort.by("name");
        if (sortDir.equals("asc")) {
            sort = sort.ascending();
        } else if (sortDir.equals("desc")) {
            sort = sort.descending();
        }
        Pageable pageable = PageRequest.of(pageNum - 1, ROOT_CATEGORIES_PER_PAGE, sort);
        Page<Category> pageCategories=null;
        if (keyword != null && !keyword.isEmpty()){
            pageCategories = categoryRepository.search(keyword.toLowerCase(Locale.ROOT),pageable);

        }else {
           pageCategories = categoryRepository.findRootCategories(pageable);

        }
        List<Category> rootCategories = pageCategories.getContent();
        categoryPageInfo.setTotalElements(pageCategories.getTotalElements());
        categoryPageInfo.setTotalPages(pageCategories.getTotalPages());

        if (keyword != null && !keyword.isEmpty()){
            List<Category> searchResult = pageCategories.getContent();
            for (Category category :
                    searchResult) {
                category.setHasChildren(category.getChildren().size() > 0);
            }return  searchResult;
        }else {
            return listHierarchicalCategories(rootCategories, sortDir);

        }
    }

    private List<Category> listHierarchicalCategories(List<Category> rootCategories,
                                                      String sortDir) {
        List<Category> hierarchicalCategories = new ArrayList<>();

        for (Category rootCategory : rootCategories) {
            hierarchicalCategories.add(Category.copyFull(rootCategory));

            Set<Category> children = sortSubCategories(rootCategory.getChildren(), sortDir);

            for (Category subCategory : children) {
                String name = "--" + subCategory.getName();
                hierarchicalCategories.add(Category.copyFull(subCategory, name));

                listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1, sortDir);
            }
        }

        return hierarchicalCategories;
    }


    private void listSubHierarchicalCategories(List<Category> hierarchicalCategories,
                                               Category parent, int subLevel, String sortDir) {
        Set<Category> children = sortSubCategories(parent.getChildren(), sortDir);
        int newSubLevel = subLevel + 1;

        for (Category subCategory : children) {
            String name = "";
            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }
            name += subCategory.getName();

            hierarchicalCategories.add(Category.copyFull(subCategory, name));

            listSubHierarchicalCategories(hierarchicalCategories, subCategory, newSubLevel, sortDir);
        }

    }


    public List<Category> listCategoriesUsedInForm() {
        List<Category> categoriesUsedInForm = new ArrayList<>();

        Iterable<Category> categoriesInDB = categoryRepository.findRootCategories(Sort.by("name").ascending());

        for (Category category : categoriesInDB) {
            if (category.getParent() == null) {
                categoriesUsedInForm.add(Category.copyIdAndName(category));

                Set<Category> children = sortSubCategories(category.getChildren());

                for (Category subCategory : children) {
                    String name = "--" + subCategory.getName();
                    categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getCategoryId(), name));

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

            categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getCategoryId(), name));

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
        Category byName = categoryRepository.findByName(name);
        boolean isCreatingNew = (id == null || id == 0);

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
            if (byName != null && byName.getCategoryId() != id) {
                return "Duplicated Name!";
            }
            Category byAlias = categoryRepository.findByAlias(alias);
            if (byAlias != null && byAlias.getCategoryId() != id) {
                return "Duplicated Alias!";

            }


        }
        return "OK";
    }

    private SortedSet<Category> sortSubCategories(Set<Category> children) {
        return sortSubCategories(children, "asc");
    }


    private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir) {
        SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {

            @Override
            public int compare(Category cat1, Category cat2) {

                if (sortDir.equals("asc")) {
                    return cat1.getName().compareTo(cat2.getName());

                } else {
                    return cat2.getName().compareTo(cat1.getName());
                }
            }
        });
        sortedChildren.addAll(children);
        return sortedChildren;
    }

    //should be transactional
    public void updateCategoryEnabledStatus(Long id, boolean enabled) {
        categoryRepository.updateEnabledStatus(id, enabled);
    }

    public void delete(Long id) throws CategoryNotFoundException {
        Long countById = categoryRepository.countByCategoryId(id);
        if (countById == null || countById == 0) {
            throw new CategoryNotFoundException("Could not find any category with ID" + id);
        }
        categoryRepository.deleteById(id);

    }
}
