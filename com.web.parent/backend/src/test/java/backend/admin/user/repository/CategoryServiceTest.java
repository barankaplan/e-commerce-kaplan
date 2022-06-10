package backend.admin.user.repository;


import backend.admin.user.service.CategoryService;
import common.data.entity.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
class CategoryServiceTest {

    //injecting a fake mock object to not test whole repository !
    @MockBean
    private CategoryRepository categoryRepository;
    //inject fake category object into category service class
    @Autowired
    private CategoryService categoryService;

    @Test
    void testCheckUniqueInNewModeDuplicateName() {
        Long id = null;
        String name = "Computers";
        String alias = "abc";
        Category category = new Category(id, name, alias);
        Mockito.when(categoryRepository.findByName(name)).thenReturn(category);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(null);
        String result = categoryService.checkUnique(id, name, alias);
        assertThat(result).isEqualTo("Duplicated Name!");

    }
    @Test
    void testCheckUniqueInNewModeReturnDuplicateAlias() {
        Long id = null;
        String name = "abc";
        String alias = "Computers";
        Category category = new Category(id, name, alias);
        Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(category);
        String result = categoryService.checkUnique(id, name, alias);
        assertThat(result).isEqualTo("Duplicated Alias!");
    }

    @Test
    void testCheckUniqueInNewModeReturnOK() {
        Long id = null;
        //no existing categories accordint to the given name alias
        String name = "Computers";
        String alias = "Computers";
        Category category = new Category(id, name, alias);
        Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(null);
        String result = categoryService.checkUnique(id, name, alias);
        assertThat(result).isEqualTo("OK");
    }

    @Test
    void testCheckUniqueInEditModeDuplicateName() {
        Long id = 1L;
        String name = "Computers";
        String alias = "abc";
        Category category = new Category(2L, name, alias);
        Mockito.when(categoryRepository.findByName(name)).thenReturn(category);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(null);
        String result = categoryService.checkUnique(id, name, alias);
        assertThat(result).isEqualTo("Duplicated Name!");

    }

    @Test
    void testCheckUniqueInEditModeReturnDuplicateAlias() {
        Long id = 1L;
        String name = "abc";
        String alias = "Computers";
        Category category = new Category(2L, name, alias);
        Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(category);
        String result = categoryService.checkUnique(id, name, alias);
        assertThat(result).isEqualTo("Duplicated Alias!");
    }



    @Test
    void testCheckUniqueInEditModeReturnOK() {
        Long id = 1L;
        //no existing categories accordint to the given name alias
        String name = "Computers";
        String alias = "Computers";
        Category category = new Category(id, name, alias);
        Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(category);
        String result = categoryService.checkUnique(id, name, alias);
        assertThat(result).isEqualTo("OK");
    }

}
