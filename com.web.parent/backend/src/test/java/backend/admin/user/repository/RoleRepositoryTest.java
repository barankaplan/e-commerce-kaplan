package backend.admin.user.repository;

import common.data.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testCreateFirstRole() {
        Role roleAdmin = new Role("Admin", "manages everything");
        Role savedRole = roleRepository.save(roleAdmin);
        assertThat(savedRole.getId()).isPositive();
    }

    @Test
    void testCreateRestRole() {
        Role roleSalesperson = new Role("Salesperson", "manages product price,customers,shipping," +
                "orders,and sales report");
        Role roleEditor = new Role("Editor", "manages categories,brands,products,articles and menus" +
                "orders,and sales report");
        Role roleShipper = new Role("Shipper", "view products,orders and update order status" +
                "orders,and sales report");
        Role roleAssistant = new Role("Assistant", "manage questions and reviews" +
                "orders,and sales report");

        //returns an immutable list
        roleRepository.saveAll(List.of(roleAssistant, roleEditor, roleShipper, roleSalesperson));

    }


}