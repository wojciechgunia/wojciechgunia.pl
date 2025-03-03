package pl.wojciechgunia.wgapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class WgApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WgApiApplication.class, args);
    }

}
