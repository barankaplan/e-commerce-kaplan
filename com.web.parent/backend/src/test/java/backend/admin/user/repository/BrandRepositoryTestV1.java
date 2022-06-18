//package backend.admin.user.repository;
//
//import backend.admin.user.service.BrandService;
//import common.data.entity.Brand;
//import common.data.entity.Category;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.annotation.Rollback;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//
////@DataJpaTest()
////@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
////@Rollback(false)
//@ExtendWith(MockitoExtension.class)
////@ExtendWith(SpringExtension.class)
//@SpringBootTest
//class BrandRepositoryTest {
//    @MockBean
//    private BrandRepository mockBrandRepository;
//    @Autowired
//    private BrandRepository brandRepository;
//    @Autowired
//    private BrandService brandService;
//
//    @Test
//    void testGetById(){
//        Brand brand= brandRepository.findById(1L).get();
//        assertThat(brand.getName()).isEqualTo("canon");
//    }
//    @Test
//    void testFindAll() {
//        Iterable<Brand>brands= brandRepository.findAll();
//        System.out.println("Brands");
//        brands.forEach(System.out::println);
//        assertThat(brands).isNotEmpty();
//
//    }
//    @Test
//    void testCreateBrand() {
//        Category laptops = new Category(6L);
//        Brand acer = new Brand("Acer");
//        acer.getCategories().add(laptops);
//        Brand savedBrand = mockBrandRepository.save(acer);
//        assertThat(savedBrand).isNotNull();
//    }
//
//    @Test
//    void testCreateBrand2() {
//        Category cellphones = new Category(4L);
//        Category tablets = new Category(7L);
//        Brand apple = new Brand("Apple");
//        apple.getCategories().add(cellphones);
//        apple.getCategories().add(tablets);
//        Brand savedBrand = mockBrandRepository.save(apple);
//        assertThat(savedBrand).isNotNull();
//        assertThat(savedBrand.getCategories().size()).isPositive();
//    }
//
//
//    @Test
//    void testCreateBrand3() {
//        Brand samsung = new Brand("Samsung");
//        samsung.getCategories().add(new Category(29L));
//        samsung.getCategories().add(new Category(24L));
//        Brand savedBrand = mockBrandRepository.save(samsung);
//        assertThat(savedBrand).isNotNull();
//        assertThat(savedBrand.getCategories()).isNotEmpty();
//    }
//
//    @Test
//    void testUpdateName() {
//        Optional<Brand> samsung = mockBrandRepository.findById(4L);
//        samsung.get().setName("Samsung Electronics");
//        Brand savedBrand = mockBrandRepository.save(samsung.get());
//        assertThat(savedBrand.getName()).isEqualTo("Samsung Electronics");
//    }
//
//    @Test
//    void testDeleteName() {
//        Optional<Brand> acer = mockBrandRepository.findById(5L);
//        mockBrandRepository.deleteById(5L);
//        assertThat(acer.isEmpty());
//    }
//
//    @Test
//    void testCheckUniqueInEditReturnDuplicate() {
//        Brand brand = new Brand(1L, "Canon");
//        Mockito.when(mockBrandRepository.findByName("Canon")).thenReturn(brand);
//        String result = brandService.checkUnique(2L, "Canon");
//        assertThat(result).isEqualTo("Duplicate");
//
//    }
//
//    @Test
//     void testCheckUniqueInNewModeReturnDuplicate() {
//        Long id = null;
//        String name = "Acer";
//        Brand brand = new Brand(name);
//
//        Mockito.when(mockBrandRepository.findByName(name)).thenReturn(brand);
//
//        String result = brandService.checkUnique(id, name);
//        assertThat(result).isEqualTo("Duplicate");
//    }
//
//    @Test
//     void testCheckUniqueInNewModeReturnOK() {
//        Long id = null;
//        String name = "AMD";
//
//        Mockito.when(mockBrandRepository.findByName(name)).thenReturn(null);
//
//        String result = brandService.checkUnique(id, name);
//        assertThat(result).isEqualTo("OK");
//    }
//
//    @Test
//     void testCheckUniqueInEditModeReturnDuplicate() {
//        Long id = 1L;
//        String name = "Canon";
//        Brand brand = new Brand(id, name);
//
//        Mockito.when(mockBrandRepository.findByName(name)).thenReturn(brand);
//
//        String result = brandService.checkUnique(2L, "Canon");
//        assertThat(result).isEqualTo("Duplicate");
//    }
//
//    @Test
//     void testCheckUniqueInEditModeReturnOK() {
//        Long id = 1L;
//        String name = "Acer";
//        Brand brand = new Brand(id, name);
//
//        Mockito.when(mockBrandRepository.findByName(name)).thenReturn(brand);
//
//        String result = brandService.checkUnique(id, "Acer Ltd");
//        assertThat(result).isEqualTo("OK");
//    }
//
//}