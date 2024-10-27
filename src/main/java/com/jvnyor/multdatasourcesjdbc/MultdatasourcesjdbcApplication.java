package com.jvnyor.multdatasourcesjdbc;

import com.jvnyor.multdatasourcesjdbc.infra.database.dao.todo.TodoDAO;
import com.jvnyor.multdatasourcesjdbc.infra.database.dao.topic.TopicDAO;
import com.jvnyor.multdatasourcesjdbc.model.todo.Todo;
import com.jvnyor.multdatasourcesjdbc.model.topic.Topic;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class MultdatasourcesjdbcApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultdatasourcesjdbcApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(TodoDAO todoDAO, TopicDAO topicDAO) {
        return args -> {
            todoDAO.save(new Todo("Learn Spring Boot"));
            todoDAO.findAll().forEach(System.out::println);
            todoDAO.findById(1L).ifPresent(System.out::println);
            todoDAO.update(new Todo(1L, "Learn Spring Boot and Spring Data JPA"));
            todoDAO.findAll().forEach(System.out::println);
            todoDAO.saveAll(List.of(new Todo("Learn Spring Boot"), new Todo("Learn Spring Data JPA")));
            todoDAO.findAll().forEach(System.out::println);
            todoDAO.updateAll(List.of(new Todo(1L, "Learn Spring Boot and Spring WebFlux"), new Todo(2L, "Learn Spring Boot and Spring Security")));
            todoDAO.findAll().forEach(System.out::println);

            topicDAO.save(new Topic("Spring Boot"));
            topicDAO.findAll().forEach(System.out::println);
            topicDAO.findById(1L).ifPresent(System.out::println);
            topicDAO.update(new Topic(1L, "Spring Boot and Spring Data JPA"));
            topicDAO.findAll().forEach(System.out::println);
            topicDAO.saveAll(List.of(new Topic("Learn Spring Boot"), new Topic("Learn Spring Data JPA")));
            topicDAO.findAll().forEach(System.out::println);
            topicDAO.updateAll(List.of(new Topic(1L, "Learn Spring Boot and Spring WebFlux"), new Topic(2L, "Learn Spring Boot and Spring Security")));
            topicDAO.findAll().forEach(System.out::println);
        };
    }
}
