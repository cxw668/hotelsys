package org.chore.hotelsys;

import org.chore.hotelsys.infrastructure.db.DatabaseConnectionVerifier;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
@MapperScan("org.chore.hotelsys.mapper")
public class HotelsysApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(HotelsysApplication.class, args);

        Environment environment = context.getEnvironment();
        String url = environment.getProperty("spring.datasource.url");
        String username = environment.getProperty("spring.datasource.username");
        String password = environment.getProperty("spring.datasource.password", "");

        DatabaseConnectionVerifier.VerificationResult result = DatabaseConnectionVerifier.verify(url, username, password);
        System.out.println("[HOTELSYS][DB] " + result.message());
    }

}
