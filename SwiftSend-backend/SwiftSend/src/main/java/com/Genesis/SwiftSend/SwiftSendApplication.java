package com.Genesis.SwiftSend;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.Genesis.SwiftSend.Role.Role;
import com.Genesis.SwiftSend.Role.RoleRepository;
import com.Genesis.SwiftSend.User.User;
import com.Genesis.SwiftSend.User.UserRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class SwiftSendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SwiftSendApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository,
			PasswordEncoder passwordEncode) {
		return args -> {
			if (roleRepository.findByAuthority("ADMIN").isPresent())
				return;
			Role adminRole = roleRepository.save(new Role("ADMIN"));
			roleRepository.save(new Role("USER"));
			Role userRole = roleRepository.findByAuthority("USER").get();
			log.info("adminrole " + adminRole + " " + userRole);

			Set<Role> roles = new HashSet<>();
			roles.add(adminRole);
			roles.add(userRole);
			log.info("roles are :" + roles);

			User admin = new User(1, "SwiftSendAdmin", "johndoe12345@gmail.com",
					passwordEncode.encode("SwiftSendAdmin"), true, "1234-6785-345", roles);

			userRepository.save(admin);
		};
	}
}
