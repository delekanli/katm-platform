package uz.katm.mip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MipApplication {
    public static void main(String[] args) {
        SpringApplication.run(MipApplication.class, args);
    }
}
