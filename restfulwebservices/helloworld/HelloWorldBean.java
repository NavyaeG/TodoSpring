package com.rest.webservices.restfulwebservices.helloworld;

public class HelloWorldBean {
	
	private String message;

	//constructor
	public HelloWorldBean(String message) {
		this.message=message;
	}

	//getter
	public String getMessage() {
		return message;
	}

	//setter
	public void setMessage(String message) {
		this.message = message;
	}

	//toString()
	@Override
	public String toString() {
		return "HelloWorldBean [message=" + message + "]";
	}

}
