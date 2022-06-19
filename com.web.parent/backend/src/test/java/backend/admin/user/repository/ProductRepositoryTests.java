package backend.admin.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import common.data.entity.Brand;
import common.data.entity.Category;
import common.data.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Resource
    private TestEntityManager testEntityManager;

    @Test
    void testCreateProduct() {
        Brand brand = testEntityManager.find(Brand.class, 37L);
        Category category = testEntityManager.find(Category.class, 5L);

        Product product = new Product();
        product.setName("Acer Aspire Desktop");
        product.setAlias("acer_aspire_desktop");
        product.setShortDescription("Short description for Acer Aspire");
        product.setFullDescription("Full description for Acer Aspire");

        product.setBrand(brand);
        product.setCategory(category);

        product.setPrice(678);
        product.setCost(600);
        product.setEnabled(true);
        product.setInStock(true);

        product.setCreatedTime(new Date());
        product.setUpdatedTime(new Date());

        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isGreaterThan(0);
    }

    @Test
    void testListAllProducts() {
        Iterable<Product> iterableProducts = productRepository.findAll();

        iterableProducts.forEach(System.out::println);
    }

    @Test
    void testGetProduct() {
        Long id = 1L;
        Product product = productRepository.findById(id).get();
        System.out.println(product);

        assertThat(product).isNotNull();
    }

    @Test
    void testUpdateProduct() {
        Long id = 1L;
        Product product = productRepository.findById(id).get();
        product.setPrice(499);

        productRepository.save(product);

        Product updatedProduct = testEntityManager.find(Product.class, id);

        assertThat(updatedProduct.getPrice()).isEqualTo(499);
    }

    @Test
    void testDeleteProduct() {
        Long id = 1L;
        productRepository.deleteById(id);

        Optional<Product> result = productRepository.findById(id);

        assertThat(!result.isPresent());
    }

    @Test
     void testSaveProductWithImages() {
        Long productId = 1L;
        Product product = productRepository.findById(productId).get();

        product.setMainImage("main image.jpg");
        product.addExtraImage("extra image 1.png");
        product.addExtraImage("extra_image_2.png");
        product.addExtraImage("extra-image3.png");

        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct.getImages().size()).isEqualTo(3);
    }
}
