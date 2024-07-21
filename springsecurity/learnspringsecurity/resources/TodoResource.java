package com.springsecurity.learnspringsecurity.resources;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;

//How does Form based authentication work?
//Uses a session cookie, this cookie is then sent with every request, spring security enables form based authentication by default
@RestController
public class TodoResource {
	
	private Logger logger =LoggerFactory.getLogger(getClass());

	private static final List<Todo> TODOS_LIST=List.of(new Todo("nav","Learn AWS"), new Todo("nav","Learn DevOps"));
	
	@GetMapping("/todos")
	public List<Todo> retrieveAllTodos() {
		return TODOS_LIST;
	}
	
	@GetMapping("/users/{username}/todos")
	@PreAuthorize("hasRole('USER') and authentication.name == 'nav'")
	@PostAuthorize("returnObject.username == authentication.name")
	public Todo retriveTodosForSpecificUser(@PathVariable("username") String username) {
	    String authenticatedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
	    System.out.println(String.format("Path Variable Username: %s", username));
	    System.out.println(String.format("Authenticated Username: %s", authenticatedUsername));
	    return TODOS_LIST.get(0);
	}
	
	@PostMapping("/users/{username}/todos")
	public void createTodoForSpecificUser(@PathVariable("username")String username, @RequestBody Todo todo) {
		logger.info("Create {} for {}",todo,username);
	}
	
}

record Todo(String username,String description) {}


//How to protect against CSRF
//1.Synchronize Token pattern
//a token created for each request
//to make an update(POST,PUT, ..) you need a CSRF from previous request