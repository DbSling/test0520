package com.sling.test0520;

import com.core.epril.repository.BaseRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class)
public class Test0520WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(Test0520WebApplication.class, args);
    }

}
