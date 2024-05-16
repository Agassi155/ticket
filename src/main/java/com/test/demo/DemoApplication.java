package com.test.demo;

import com.test.config.SecurityConfiguration;
import com.test.controller.TicketController;
import com.test.controller.UserController;
import com.test.model.Status;
import com.test.model.Ticket;
import com.test.model.User;
import com.test.repository.TicketRepository;
import com.test.repository.UserRepository;
import com.test.service.TicketService;
import com.test.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EntityScan(basePackages = { "com.test.model" })
@EnableJpaRepositories(basePackages = "com.test.repository")
@ComponentScan(basePackageClasses = { UserController.class, TicketController.class, SecurityConfiguration.class,
        UserService.class,
        TicketService.class })

public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    //default user
    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, TicketRepository ticketRepository) {
        return args -> {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            User user1 = User.builder().username("toto")
                    .email("toto@mail.com")
                    .password(passwordEncoder.encode("toto"))
                    .roles("ADMIN").build();
            User user2 = User.builder().username("tata")
                    .email("tata@mail.com")
                    .password(passwordEncoder.encode("tata"))
                    .roles("USER").build();
            userRepository.save(user1);
            userRepository.save(user2);

            Ticket ticket1 = Ticket.builder().title("ticket1")
                    .description("desc")
                    .status(Status.IN_PROGRESS)
                    .userOwner(user1).build();
            ticketRepository.save(ticket1);

        };
    }
}

