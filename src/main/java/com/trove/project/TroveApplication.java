package com.trove.project;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.trove.project.models.entities.Authority;
import com.trove.project.models.entities.Portfolio;
import com.trove.project.models.entities.Stock;
import com.trove.project.models.entities.User;
import com.trove.project.repositories.AuthorityRepository;
import com.trove.project.repositories.PortfolioRepository;
import com.trove.project.repositories.StockRepository;
import com.trove.project.repositories.UserRepository;

/* 
 * start of the application
 * configures default and sample models for system.
 */
@SpringBootApplication
public class TroveApplication {

	public static void main(String[] args) {
		SpringApplication.run(TroveApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(UserRepository userRepository, PortfolioRepository portfolioRepository,
			AuthorityRepository authrep, PasswordEncoder pas, StockRepository stockRepository) {
		return args -> {

			// default authority
			Authority userAuth = authrep.save(new Authority("user"));
			// additional authority with privileges
			Authority adminAuth = authrep.save(new Authority("admin"));
			Set<Authority> authorities = new HashSet<>();
			authorities.add(userAuth);

			// sample user and new portfolio
			Portfolio portfolio = new Portfolio();
			User user = new User("John", "Good", "john", "John".toLowerCase() + "@domain.com",
					pas.encode("John".toLowerCase() + 1234), authorities);
			user.setPortfolio(portfolio);
			user.setWallet(new BigDecimal(50000));
			portfolio.setUser(user);
			userRepository.save(user);

			// sample buyable stocks
			stockRepository.save(new Stock("Amazon", "AMZN", new BigDecimal("500.0"), 250.0));
			stockRepository.save(new Stock("Google", "GOOGLE", new BigDecimal("600.0"), 300.0));
			stockRepository.save(new Stock("Dangote", "DANG", new BigDecimal("300.55"), 200.0));
			stockRepository.save(new Stock("Uber", "UBER", new BigDecimal("301.0"), 210.0));
			stockRepository.save(new Stock("Facebook", "FACEB", new BigDecimal("407.60"), 302.67));

			authorities.add(adminAuth);

			// sample administrative user
			Portfolio portfolio1 = new Portfolio();
			User user1 = new User("Helen", "Good", "helen", "Helen".toLowerCase() + "@domain.com",
					pas.encode("Helen".toLowerCase() + 1234), authorities);
			user1.setPortfolio(portfolio1);
			portfolio1.setUser(user1);
			userRepository.save(user1);

			userRepository.findAll().forEach(System.out::println);

		};
	}

}
