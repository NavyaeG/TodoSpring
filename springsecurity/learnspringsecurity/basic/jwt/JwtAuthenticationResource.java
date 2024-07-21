package com.springsecurity.learnspringsecurity.basic.jwt;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
public class JwtAuthenticationResource {

	//1. Authentication Manager-Responsible for authentication- can interact with multiple authentication providers
	//2. Authentication providers-perform specific authentication type
	//3. UserDetailsService-core interface to load user data
	
	//How is authentication result stored?
	//SecurityContextHolder->SecurityContext->Authentication->GrantedAuthority
	//		Authentication-(After authentication) Holds user(Principal) details
	//		GrantedAuthority-An authority granted to principal(roles, scopes)
	
	private JwtEncoder jwtEncoder;
	
	public JwtAuthenticationResource(JwtEncoder jwtEncoder) {
		this.jwtEncoder=jwtEncoder;
	}
	
	//creating a jwt response object and returning it back
	
//	When a user logs in or is otherwise authenticated, Spring Security creates an Authentication object to represent that user's authentication status. This object contains various details about the authenticated user, such as:
	
//	Principal: This represents the identity of the user who has been authenticated. Typically, it's an instance of UserDetails, which holds information like the username and password (or other authentication details) of the user.
//	Credentials: These are the credentials (e.g., password) presented by the user during authentication. In most cases, especially for security reasons, the credentials are not stored directly in the Authentication object but are instead represented by the principal (e.g., in a UserDetails object).
//	Authorities: These represent the permissions or roles associated with the authenticated user. Authorities are typically granted to users based on their roles or privileges. For example, a user might have authorities like "ROLE_USER", "ROLE_ADMIN", etc., indicating the level of access they have within the application.

	//Principal and Authorities details only come in when the credentials and verified
	
//	The Authentication object is typically created by Spring Security's authentication mechanisms (such as form-based login, OAuth, JWT, etc.) and is passed around by Spring Security internally to handle authentication and authorization decisions throughout the application.
	@PostMapping("/authenticate")
	public JwtResponse authenticate(Authentication authentication) {
		return new JwtResponse(createToken(authentication));
	}

	//gathering all the details from the authentication object
	private String createToken(Authentication authentication) {
		var claims=JwtClaimsSet.builder()
								.issuer("self")
								.issuedAt(Instant.now())
								.expiresAt(Instant.now().plusSeconds(60*20))
								.subject(authentication.getName())
								.claim("scope", createScope(authentication))//what all authorities it has
								.build();
		//encoding the token value
		return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}

	//taking all the authorities appending them together and sending them to scope
//	it extracts authorities from the Authentication object.
//	It maps each authority to its string representation using map.
//	It collects the mapped authorities into a single string separated by spaces using Collectors.joining.
	private String createScope(Authentication authentication) {
		return authentication.getAuthorities().stream()
			.map(a->a.getAuthority())
			.collect(Collectors.joining(" "));
	}
}

record JwtResponse(String token) {}