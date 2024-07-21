package com.rest.webservices.restfulwebservices.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;
import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
public class UserResource {
	
	private UserDaoService service;
	
	//constructor injection
	public UserResource(UserDaoService service) {
		this.service=service;
	}
	
	@GetMapping("/users")
	public List<User> retrieveAllUsers() {
		return service.findAll();
	}
	
	@GetMapping(path="/users/{id}")
	public EntityModel<User> retriveUser(@PathVariable int id){
		User user=service.findOne(id);
		if(user==null) {
			throw new UserNotFoundException("id:"+id);
		}
		
		//A simple EntityModel wrapping a domain object and adding links to it.
		EntityModel<User> entityModel = EntityModel.of(user);//wrapping user class and creating an entity model
		
		WebMvcLinkBuilder link =  linkTo(methodOn(this.getClass()).retrieveAllUsers());
		entityModel.add(link.withRel("all-users"));
		
		
		return entityModel;
	}
	
	@DeleteMapping(path="/users/{id}")
	public void deleteUser(@PathVariable int id){
		service.deleteById(id);
	}
	
	//RequestBody: Annotation indicating a method parameter should be bound to the body of the web request.
	//The body of the request is passed through an HttpMessageConverter to resolve the method argument depending on the content type of the request
	@PostMapping(path="/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		User saved=service.save(user);
		// /users/4 => /users, user.getId
		URI location=ServletUriComponentsBuilder.fromCurrentRequest()// to get path of the current request
				.path("/{id}")//adding path to the current url
				.buildAndExpand(saved.getId())//replacing {id} with the id of the current user created
				.toUri();//converting it to a uri
		return ResponseEntity.created(location).build();
	}
	
}