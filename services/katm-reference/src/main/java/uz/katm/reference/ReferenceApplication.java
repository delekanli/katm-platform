package uz.katm.reference;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ReferenceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReferenceApplication.class, args);
    }
}
