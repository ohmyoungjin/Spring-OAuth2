package iam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class IAMServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(IAMServerApplication.class, args);

        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        System.out.println("endcode = " + passwordEncoder.encode("clientPassword"));
        System.out.println("endcode = " + passwordEncoder.encode("test"));
    }
}