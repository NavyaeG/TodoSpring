package com.rest.webservices.restfulwebservices.user;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;

@Entity
public class Post {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	@Size(min =10)
	private String description;
	
	// Whether the association should be lazily loaded or must be eagerly fetched. 
	//The EAGERstrategy is a requirement on the persistence provider runtime that the associated 
	//entity must be eagerly fetched. The LAZYstrategy is a hint to the persistence provider runtime.
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore////We are using user bean as part of rest api responses and we will also be using post bean as part of rest api responses we dont want users to be part of api responses for posts
	private User user;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", description=" + description + "]";
	}
}