package backend.admin.user.repository;

import common.data.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String name);

    @Query("update Product p set p.enabled= ?2 where p.id=?1")
    @Modifying
    void updateEnabledStatus(Long id, boolean enabled);

    Long countById(Long lng);

}
