package ru.template.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Класс для запуска приложения
 */
@SpringBootApplication(scanBasePackages = {
        "ru.template.example.*"
})
/*@EntityScan("ru.template.example.documents.entity")
@EnableJpaRepositories("ru.template.example.documents.repository")*/
@EnableAspectJAutoProxy
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {

    }
}
