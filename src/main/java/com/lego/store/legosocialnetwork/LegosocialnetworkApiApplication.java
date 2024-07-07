package com.lego.store.legosocialnetwork;

import com.lego.store.legosocialnetwork.role.Role;
import com.lego.store.legosocialnetwork.role.RoleManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

//@SpringBootApplication(scanBasePackages = "com.lego.store.legosocialnetwork")
//@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
//@EnableJpaRepositories(basePackages = "com.lego.store.legosocialnetwork.user")
@EnableAsync
@SpringBootApplication
public class LegosocialnetworkApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LegosocialnetworkApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(RoleManager roleManager) {
		return args -> {
			if (roleManager.findByName("USER").isEmpty()) {
				roleManager.save(Role.builder().name("USER").build());

			}
		};
	}

}
