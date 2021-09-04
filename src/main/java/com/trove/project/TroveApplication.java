package com.trove.project;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.trove.project.models.entities.Authority;
import com.trove.project.models.entities.Portfolio;
import com.trove.project.models.entities.User;
import com.trove.project.repositories.AuthorityRepository;
import com.trove.project.repositories.PortfolioRepository;
import com.trove.project.repositories.UserRepository;

@SpringBootApplication
public class TroveApplication {

	public static void main(String[] args) {
		SpringApplication.run(TroveApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(UserRepository userRepository, PortfolioRepository portfolioRepository,
			AuthorityRepository authrep, PasswordEncoder pas) {
		return args -> {

			Authority userAuth = authrep.save(new Authority("user"));
			Authority adminAuth = authrep.save(new Authority("admin"));
			Set<Authority> authorities = new HashSet<>();
			authorities.add(userAuth);

			Portfolio portfolio = new Portfolio();

			User user = new User("John", "Good", "john", "John".toLowerCase() + "@domain.com",
					pas.encode("John".toLowerCase() + 1234), authorities);
			user.setPortfolio(portfolio);
			portfolio.setUser(user);
			userRepository.save(user);

			authorities.add(adminAuth);

			Portfolio portfolio1 = new Portfolio();
			User user1 = new User("Helen", "Good", "helen", "Helen".toLowerCase() + "@domain.com",
					pas.encode("Helen".toLowerCase() + 1234), authorities);
			user1.setPortfolio(portfolio1);
			portfolio1.setUser(user1);
			userRepository.save(user1);

			userRepository.findAll().forEach(System.out::println);
			portfolioRepository.findAll().forEach(System.out::println);

		};
	}

}
