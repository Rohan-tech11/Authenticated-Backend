/**
 * Created by Rohan
 */

package com.Genesis.SwiftSend.Security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.Genesis.SwiftSend.Client.ClientRepository;
import com.Genesis.SwiftSend.Registration.Token.JwtAuthenticationFilter;
import com.Genesis.SwiftSend.User.UserRepository;
import com.Genesis.SwiftSend.Utils.RSAKeyProperties;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

/**
 * @author rohan
 *
 */
@Configuration
@EnableWebSecurity
public class UserRegistrationSecurityConfig {

	private final UserDetailsService userDetailsService;
	private final UserDetailsService clientDetailsService; // Separate UserDetailsService for clients
	private final ClientRepository clientRepository;
	private final UserRepository userRepository;

	private final RSAKeyProperties keys;

	public UserRegistrationSecurityConfig(RSAKeyProperties keys,
			@Qualifier("userRegistrationDetailsService") @Lazy UserDetailsService userDetailsService,
			@Qualifier("clientRegistrationDetailsService") @Lazy UserDetailsService clientDetailsService,
			ClientRepository clientRepository, UserRepository userRepository) {
		this.keys = keys;
		this.userDetailsService = userDetailsService;
		this.clientDetailsService = clientDetailsService;
		this.clientRepository = clientRepository;
		this.userRepository = userRepository;
	}

	private static final String[] WHITE_LIST_URL = { "/api/register/**", "/v2/api-docs", "/v3/api-docs/**",
			"/swagger-resources", "/swagger-resources/**", "/configuration/ui", "/configuration/security",
			"/swagger-ui/**", "/webjars/**", "/swagger-ui.html" };

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}

	// When you define a method annotated with @Bean and returning an
	// AuthenticationManager, Spring will automatically call that method to create
	// an instance of AuthenticationManager when needed.
	@Bean
	@Lazy
	public AuthenticationManager authenticationManagerBean() throws Exception {
		DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
		daoProvider.setUserDetailsService(userDetailsService);
		daoProvider.setPasswordEncoder(passwordEncoder());

		DaoAuthenticationProvider clientDaoProvider = new DaoAuthenticationProvider();
		clientDaoProvider.setUserDetailsService(clientDetailsService);
		clientDaoProvider.setPasswordEncoder(passwordEncoder());
//		///**
//		 * Attempts to authenticate the passed {@link Authentication} object.
//		 * <p>
//		 * The list of {@link AuthenticationProvider}s will be successively tried until an
//		 * <code>AuthenticationProvider</code> indicates it is capable of authenticating the
//		 * type of <code>Authentication</code> object passed. Authentication will then be
//		 * attemp
		return new ProviderManager(daoProvider, clientDaoProvider);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> {
			auth.requestMatchers(WHITE_LIST_URL).permitAll();
			auth.requestMatchers("/api/admin/**").hasRole("ADMIN");
			auth.requestMatchers("/api/users/**").hasAnyRole("ADMIN", "USER");
			auth.anyRequest().authenticated().and().addFilterBefore(jwtAuthenticationFilter(),
					UsernamePasswordAuthenticationFilter.class);

		});

		// converter for checking the roles to access endpoints
		http.oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter());
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}

	@Bean
	public JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withPublicKey(keys.getPublicKey()).build();

	}

	// decoder uses public keysd to verify and authenticiy of the token

//	@Bean
//	public JwtDecoder jwtDecoder() {
//		return new CustomJwtDecoder(NimbusJwtDecoder.withPublicKey(keys.getPublicKey()).build());
//	}

	@Bean
	public JwtEncoder jwtEncoder() {
		JWK jwk = new RSAKey.Builder(keys.getPublicKey()).privateKey(keys.getPrivateKey()).build();
		JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
		return new NimbusJwtEncoder(jwks);
	}

	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
		jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
		JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
		jwtConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
		return jwtConverter;
	}

	@Bean("userRegistrationDetailsService")
	public UserDetailsService userDetailsService() {
		return new UserRegistrationDetailsService(userRepository);
	}

	@Bean("clientRegistrationDetailsService")
	public UserDetailsService clientDetailsService() {
		return new ClientRegistrationDetailsService(clientRepository);
	}

}
