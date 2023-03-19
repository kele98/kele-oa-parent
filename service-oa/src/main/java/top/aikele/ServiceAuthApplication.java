package top.aikele;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class ServiceAuthApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ServiceAuthApplication.class);
    }
}
