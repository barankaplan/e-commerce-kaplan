package backend.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan({"common.data.entity"})
//@ComponentScan({"backend.admin.user.controller"})
public class BackEndRun {
    public static void main(String[] args) {
        SpringApplication.run(BackEndRun.class, args);
    }

}
