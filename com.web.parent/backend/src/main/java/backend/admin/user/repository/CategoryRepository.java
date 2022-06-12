package backend.admin.user.repository;


import common.data.entity.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c where c.parent.categoryId is NULL  ")
    List<Category> findRootCategories(Sort sort);

    Category findByName(String name);

    Category findByAlias(String name);

    Long countByCategoryId(Long lng);

    @Query("update Category u set u.enabled= ?2 where u.categoryId=?1")
    @Modifying
//update !
    void updateEnabledStatus(Long id, boolean enabled);

}