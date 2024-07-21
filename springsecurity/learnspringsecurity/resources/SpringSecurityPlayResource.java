package com.springsecurity.learnspringsecurity.resources;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

//How does Formbased authentication work?
//Uses a session cookie, this cookie is then sent with every request, spring security enables form based authentication by default
@RestController
public class SpringSecurityPlayResource {

	//we are going to use the csrf token we get to use it in our post request header in TodoResource.java
	@GetMapping("/csrf-token")
	public CsrfToken retrieveCsrfToken(HttpServletRequest request) {
		return (CsrfToken) request.getAttribute("_csrf");
	}
	
}