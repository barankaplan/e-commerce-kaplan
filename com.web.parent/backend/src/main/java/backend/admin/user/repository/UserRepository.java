package backend.admin.user.repository;

import common.data.entity.Role;
import common.data.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByRolesAndAndFirstName(Role role, String firstName);

    //    @Query("select u from User u where u.email = ?1")
    Optional<User> getUserByEmail(String eMail);

    Long countById(Long lng);


    @Query("update User u set u.enabled= ?2 where u.id=?1")
    @Modifying
//update !
    void updateEnabledStatus(Long id, boolean enabled);

//    @Query("select u  from User u where u.firstName like %?1% or u.lastName like %?1%")
//    Page<User> findAll(String keyword, Pageable pageable);

    @Query("select u  from User u where concat(u.id,' ',u.firstName ,' ',u.lastName,' ',u.email)like %?1% ")
    Page<User> findAll(String keyword, Pageable pageable);


}