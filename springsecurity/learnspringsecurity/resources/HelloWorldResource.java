package com.springsecurity.learnspringsecurity.resources;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//How does Formbased authentication work?
//Uses a session cookie, this cookie is then sent with every request, spring security enables form based authentication by default
@RestController
public class HelloWorldResource {

	@GetMapping("/hello-world")
	public String helloWorld() {
		return "Hello World";
	}
	
}