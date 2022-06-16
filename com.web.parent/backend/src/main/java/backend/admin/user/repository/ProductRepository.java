package backend.admin.user.repository;

import common.data.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface ProductRepository extends JpaRepository<Product, Long> {

}
