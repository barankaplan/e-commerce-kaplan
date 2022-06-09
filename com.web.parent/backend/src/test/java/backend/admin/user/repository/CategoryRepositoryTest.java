package backend.admin.user.repository;

import common.data.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testCreateRootCategory() {
        Category category = new Category("Electronics");
        Category savedCategory = categoryRepository.save(category);
        assertThat(savedCategory.getCategory_id()).isPositive();
    }

    @Test
    void testCreateSubCategory() {
        Category parent = new Category(1L);
        Category subCategory = new Category("Desktops", parent);
        Category savedCategory = categoryRepository.save(subCategory);
        assertThat(savedCategory.getCategory_id()).isPositive();
    }

    @Test
    void testCreateSubCategoryMultiple() {
        Category parent = new Category(7L);
        Category iPhone = new Category("iPhone", parent);
//        Category smartphones = new Category( "Smartphones",parent);
        List<Category> categories = categoryRepository.saveAll(List.of(iPhone));
        assertThat(categories.size()).isPositive();
    }

    @Test
    void testGetCategory() {
        Category category = categoryRepository.findById(1L).get();
        System.out.println(category.getName());
        Set<Category> categorySet = category.getChildren();
        categorySet.forEach(i -> System.out.println(i.getName()));
        assertThat(categorySet.size()).isGreaterThan(2);
    }

    @Test
    void testPrintHierarchicalCategories() {
        Iterable<Category> categories = categoryRepository.findAll();
        Consumer<? super Category> consumer = getConsumerHierarchical();
        categories.forEach(consumer);

    }

    private void printChildren(Category parent, int subLevel) {
        int newSubLevel = subLevel + 1;
        Set<Category> children = parent.getChildren();
        for (Category subCategory : children) {
            for (int i = 0; i < newSubLevel; i++) {
                System.out.print("--");
            }
            System.out.println(subCategory.getName());
            printChildren(subCategory, newSubLevel);
        }
    }

    private Consumer<? super Category> getConsumerHierarchical() {
        Consumer<? super Category> consumer = new Consumer<Category>() {
            @Override
            public void accept(Category category) {
                if (category.getParent() == null) {
                    System.out.println(category.getName());
                    Set<Category> children = category.getChildren();
                    for (Category subcategory : children
                    ) {
                        System.out.println("--" + subcategory.getName());
                        printChildren( subcategory,1);
                    }
                }
            }
        };
        return consumer;
    }


    @Test
     void testListRootCategories(){
        List<Category> rootCategories = categoryRepository.findRootCategories();
        rootCategories.forEach(cat -> System.out.println(cat.getName()));
    }

}