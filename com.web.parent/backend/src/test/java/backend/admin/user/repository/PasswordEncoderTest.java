package backend.admin.user.repository;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
class PasswordEncoderTest {



    @Test
    void testEncoderPassword() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "kaplan22";
        String encodedPassword = bCryptPasswordEncoder.encode(rawPassword);
        System.out.println(encodedPassword);

        boolean matches = bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
        System.out.println(matches);
        assertThat(matches).isTrue();

    }
}
