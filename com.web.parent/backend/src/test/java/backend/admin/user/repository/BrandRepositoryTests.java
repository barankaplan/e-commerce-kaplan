package backend.admin.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import common.data.entity.Brand;
import common.data.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;


@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
 class BrandRepositoryTests {
	
	@Autowired
	private BrandRepository repo;
	
	@Test
	 void testCreateBrand1() {
		Category laptops = new Category(6L);
		Brand acer = new Brand("Acer");
		acer.getCategories().add(laptops);
		
		Brand savedBrand = repo.save(acer);
		
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	 void testCreateBrand2() {
		Category cellphones = new Category(4L);
		Category tablets = new Category(7L);
		
		Brand apple = new Brand("Apple");
		apple.getCategories().add(cellphones);
		apple.getCategories().add(tablets);
		
		Brand savedBrand = repo.save(apple);
		
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	 void testCreateBrand3() {
		Brand samsung = new Brand("Samsung");
		
		samsung.getCategories().add(new Category(29L));	// category memory
		samsung.getCategories().add(new Category(24L));	// category internal hard drive
		
		Brand savedBrand = repo.save(samsung);
		
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	 void testFindAll() {
		Iterable<Brand> brands = repo.findAll();
		brands.forEach(System.out::println);
		
		assertThat(brands).isNotEmpty();
	}
	
	@Test
	 void testGetById() {
		Brand brand = repo.findById(1L).get();
		
		assertThat(brand.getName()).isEqualTo("canon");
	}
	
	@Test
	 void testUpdateName() {
		String newName = "Samsung Electronics";
		Brand samsung = repo.findById(3L).get();
		samsung.setName(newName);
		
		Brand savedBrand = repo.save(samsung);
		assertThat(savedBrand.getName()).isEqualTo(newName);
	}
	
	@Test
	 void testDelete() {
		Long id = 2L;
		repo.deleteById(id);
		
		Optional<Brand> result = repo.findById(id);
		
		assertThat(result.isEmpty());
	}
}
