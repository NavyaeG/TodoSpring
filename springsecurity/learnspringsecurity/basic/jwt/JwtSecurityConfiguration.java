package com.springsecurity.learnspringsecurity.basic.jwt;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

//@Configuration
public class JwtSecurityConfiguration {
	
	//Authentication is done as a part of the Spring Security Filter Chain
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());
		//.antMatchers is used in basicAuthCheck function in HelloWorldController.java
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.httpBasic();
		http.csrf().disable();
		http.headers().frameOptions().sameOrigin();
//		The http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt) configuration you provided is a part of Spring Security's OAuth 2.0 Resource Server setup.
//		In Spring Security, OAuth2ResourceServerConfigurer is a class used to configure OAuth 2.0 Resource Server support. When you call http.oauth2ResourceServer(), you're configuring the HTTP security for OAuth 2.0 Resource Server functionality.
//		The jwt() method is a shorthand configuration method provided by OAuth2ResourceServerConfigurer. It's used to configure JWT (JSON Web Token) authentication for the resource server. JWT authentication involves verifying JWT tokens provided in the requests to authenticate and authorize the client.
//		Here's a brief explanation of what this configuration does:
//		http.oauth2ResourceServer(): This configures the HTTP security for OAuth 2.0 Resource Server functionality.
//		OAuth2ResourceServerConfigurer::jwt: This is a method reference to the jwt() method in OAuth2ResourceServerConfigurer. It's a shorthand way to specify that you want to configure JWT authentication for the resource server.
		http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
		return http.build();
	}
	
	@Bean
	public UserDetailsService userDetailsService(DataSource dataSource) {
		
		var user=User.withUsername("nav")
			//.password("{noop}hello")
			.password("hello")
			.passwordEncoder(str -> passwordEncoder().encode(str))
			.roles("USER")
			.build();
		
		var admin=User.withUsername("admin")
				//.password("{noop}hello")
				.password("hello")
				.passwordEncoder(str -> passwordEncoder().encode(str))
				.roles("ADMIN")
				.build(); 
		
		//using jdbc in memory database detials manager to create users instead of InMemoryUserDetailsManager
		var jdbcUserDetailsManager=new JdbcUserDetailsManager(dataSource);
		jdbcUserDetailsManager.createUser(user);
		jdbcUserDetailsManager.createUser(admin);
		
		//return new InMemoryUserDetailsManager(user,admin);
		return jdbcUserDetailsManager;
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DataSource dataSource() {
		return new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
				.addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
				.build();
	}
	
	//1. Creating a Key pair, using java.security.KeyPairGenerator
	@Bean
	public KeyPair keyPair() {
		try {
			var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			return keyPairGenerator.generateKeyPair();
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	//2. Create RSA Key object using Key Pair
	@Bean
	public RSAKey rsaKey(KeyPair keyPair) {//image created of KeyPair above will automatically be autowired in here
		return new RSAKey
			.Builder((RSAPublicKey)keyPair.getPublic())
			.privateKey(keyPair.getPrivate())
			.keyID(UUID.randomUUID().toString())
			.build();
	}
	
	//3. Create JWSKSource(JSON Web Resource)
		//Create a JWSKSet(a new JSON Web Key set) with the rsa key
		//Create JWKSource using JWKSet
	@Bean
	public JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) {
		var jwkSet=new JWKSet(rsaKey);
		return (jwkSelector,context)->jwkSelector.select(jwkSet);
	}
	
	//4. Creating Decoder for JWT
	@Bean
	public JwtDecoder jwtDecoder(RSAKey rsaKey) throws JOSEException {
		return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey())
				.build();
	}
	
	//JWT Encoder
	@Bean
	public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
		return new NimbusJwtEncoder(jwkSource);
	}
}