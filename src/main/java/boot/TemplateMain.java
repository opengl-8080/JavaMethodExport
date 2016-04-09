package boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class TemplateMain {
    
    public static void main(String[] args) {
        try (ConfigurableApplicationContext ctx = SpringApplication.run(TemplateMain.class, args)) {
            System.out.println("Hello Spring Boot!!");

            Path src = Paths.get("./src");
            Path test = src.resolve("test");

            System.out.println(test);

        }
    }
}
