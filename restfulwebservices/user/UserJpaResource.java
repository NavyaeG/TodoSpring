package com.rest.webservices.restfulwebservices.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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

import com.rest.webservices.restfulwebservices.jpa.PostRepository;
import com.rest.webservices.restfulwebservices.jpa.UserRepository;

import jakarta.validation.Valid;

@RestController
public class UserJpaResource {
	
	private UserRepository repository;
	
	private PostRepository postRepository;
	
	//constructor injection
	public UserJpaResource( UserRepository repository, PostRepository postRepository) {
		this.repository=repository;
		this.postRepository=postRepository;
	}
	
	@GetMapping("/jpa/users")
	public List<User> retrieveAllUsers() {
		return repository.findAll();
	}
	
	@GetMapping(path="/jpa/users/{id}")
	public EntityModel<User> retriveUser(@PathVariable int id){
		Optional<User> user=repository.findById(id);
		if(user.isEmpty()) {
			throw new UserNotFoundException("id:"+id);
		}
		
		//A simple EntityModel wrapping a domain object and adding links to it.
		
		//wrapping user class and creating an entity model
		EntityModel<User> entityModel = EntityModel.of(user.get());
		
		WebMvcLinkBuilder link =  linkTo(methodOn(this.getClass()).retrieveAllUsers());
		entityModel.add(link.withRel("all-users"));
		
		return entityModel;
	}
	
	@DeleteMapping(path="/jpa/users/{id}")
	public void deleteUser(@PathVariable int id){
		repository.deleteById(id);
	}
	
	@GetMapping(path="/jpa/users/{id}/posts")
	public List<Post> retrievePostsForUser(@PathVariable int id){
		Optional<User> user=repository.findById(id);
		if(user.isEmpty()) {
			throw new UserNotFoundException("id:"+id);
		}
		return user.get().getPosts();
	}
	
	//RequestBody: Annotation indicating a method parameter should be bound to the body of the web request.
	//The body of the request is passed through an HttpMessageConverter to resolve the method argument depending on the content type of the request
	@PostMapping(path="/jpa/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		User saved=repository.save(user);
		// /users/4 => /users, user.getId
		URI location=ServletUriComponentsBuilder.fromCurrentRequest()// to get path of the current request
				.path("/{id}")//adding path to the current url
				.buildAndExpand(saved.getId())//replacing {id} with the id of the current user created
				.toUri();//converting it to a uri
		return ResponseEntity.created(location).build();
	}
	
	@PostMapping(path="/jpa/users/{id}/posts")
	public ResponseEntity<Object> createPostsForUser(@PathVariable int id, @Valid @RequestBody Post post){
		Optional<User> user=repository.findById(id);
		if(user.isEmpty()) {
			throw new UserNotFoundException("id:"+id);
		}
		post.setUser(user.get());
		Post savedPost=postRepository.save(post);
		
		URI location=ServletUriComponentsBuilder.fromCurrentRequest()// to get path of the current request
				.path("/{id}")//adding path to the current url
				.buildAndExpand(savedPost.getId())//replacing {id} with the id of the current user created
				.toUri();//converting it to a uri
		
		return ResponseEntity.created(location).build();
	}
	
}