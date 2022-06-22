package backend.admin.user.repository;

import common.data.entity.Category;
import common.data.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("select p from Product p where p.name like %?1% or p.shortDescription like %?1% or p.fullDescription like %?1% or p.brand.name like %?1% or p.category.name like %?1%"
            )
    Page<Product> findAll (String keyword, Pageable pageable);


    @Query(
            "select p from Product p where p.category.categoryId=?1  or p.category.allParentIDs like %?2% "
    )
    Page<Product> findAllInCategory (Long categoryId, String categoryIdMatch,Pageable pageable);


    @Query("select p from Product p where (p.category.categoryId = ?1 "
            + "or p.category.allParentIDs like %?2%) and "
            + "(p.name LIKE %?3% "
            + "or p.shortDescription like %?3% "
            + "or p.fullDescription like %?3% "
            + "or p.brand.name like %?3% "
            + "or p.category.name like %?3%)")
    public Page<Product> searchInCategory(Long categoryId, String categoryIdMatch,
                                          String keyword, Pageable pageable);
}
