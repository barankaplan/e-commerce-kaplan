package backend.admin.user.repository;


import common.data.entity.Brand;
import common.data.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    Long countById(Long id);

    Brand findByName(String name);

    @Query("select b from Brand b where b.name like %?1% ")
    Page<Brand> findAll (String keyword,Pageable pageable);


    @Query("select new Brand (b.id,b.name) from Brand b order by b.name asc ")
    List<Brand> findAllBrands();
}