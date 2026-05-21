package uz.katm.contract;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class ContractApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContractApplication.class, args);
    }
}
