package backend.admin.user.repository;

import common.data.entity.Role;
import common.data.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;


    @Resource
    private TestEntityManager testEntityManager;

    @Test
    void testCreateUserWithOneRole() {
        Role role = testEntityManager.find(Role.class, 1L);
        User user = new User("info@barankaplan.com", "kaplan22", "baran", "kaplan");
        user.addRole(role);

        User savedUser = userRepository.save(user);
        assertThat(savedUser.getId()).isPositive();
    }

    @Test
    void testCreateUserWithTwoRole() {
        User user = new User("user2@barankaplan.com", "hzimmer", "hans", "zimmer");
        Role roleEditor = new Role(3L);
        Role roleAssistant = new Role(5L);
        user.addRole(roleEditor);
        user.addRole(roleAssistant);

        User savedUser = userRepository.save(user);
        assertThat(savedUser.getId()).isPositive();
    }

    @Test
    void testAllUsers() {
        Iterable<User> userRepositoryAll = userRepository.findAll();
        Predicate<User> isEnabledFalse;
        isEnabledFalse = e -> e.getEmail().contains("@");
        Boolean allMatch = StreamSupport.stream(userRepositoryAll.spliterator(), false)
                .allMatch(isEnabledFalse);
        assertThat(allMatch).isTrue();
        assertThat(userRepositoryAll).size().isGreaterThan(1);
        userRepositoryAll.forEach(System.out::println);
    }

    @Test
    void getUserById() {
        User user = userRepository.findById(1L).get();
        assertThat(user).isNotNull();
    }

    @Test
    void testUpdateUserDetails() {
        User user = userRepository.findById(1L).get();
        user.setEnabled(true);
        user.setEmail("updated-info@barankaplan.com");
        userRepository.save(user);
        assertThat(user.getEmail()).isEqualTo("updated-info@barankaplan.com");
    }

    @Test
    void testUpdateUserRoles() {
        User user = userRepository.findById(2L).get();
        Role roleEditor = new Role(3L);
        user.getRoles().remove(roleEditor);
        Role roleSalesPerson = new Role(2L);
        user.addRole(roleSalesPerson);
        userRepository.save(user);

        Optional<User> baran = userRepository.findByRolesAndAndFirstName(roleSalesPerson, "hans");
        assertThat(baran.get()).isNotNull();
    }

    @Test
    void testDeleteUser() {
        User user = userRepository.findById(2L).get();
        userRepository.delete(user);
    }

    @Test
    void testGetUserByEMail() {
        Optional<User> user = userRepository.getUserByEmail("info@barankaplan.com");
        user.ifPresent(System.out::println);
        assertThat(user).isNotNull();
    }

    @Test
    void testCountById() {
        Long id = 1L;
        Long countById = userRepository.countById(id);
        assertThat(countById).isNotNull().isPositive();
    }

    @Test
    void testDisabledUser() {
        Long id = 1L;
        userRepository.updateEnabledStatus(id, false);
        assertThat(userRepository.findById(id).get().isEnabled()).isFalse();

    }

    @Test
    void testEnableUser() {
        Long id = 2L;
        userRepository.updateEnabledStatus(id, true);
        assertThat(userRepository.findById(id).get().isEnabled()).isTrue();

    }

    @Test
    void testListFIrstPage() {
        int pageNumber = 1;
        int pageSize = 4;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = userRepository.findAll(pageable);
        List<User> listUsers = page.getContent();
        listUsers.forEach(System.out::println);
        assertThat(listUsers.size()).isEqualTo(pageSize);

    }


}