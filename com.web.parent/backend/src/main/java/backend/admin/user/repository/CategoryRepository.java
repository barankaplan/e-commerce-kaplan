package backend.admin.user.repository;


import common.data.entity.Category;
import common.data.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c where c.parent.category_id is NULL  ")
    List<Category> listRootCategories();
}