package com.example;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@RestController
 public class UserController {
	
	private final UserService userService;
	
	
 

	public UserController(UserService userService) {
		super();
		this.userService = userService;
//		userService.createTableIfNotExists();
	}

	@PostMapping("/users")
	public String createUser(@RequestBody User user) {
		userService.createUser(user);
		return "successfully created user with id: " + user.getUserId();
	}
	
	@GetMapping("/users/{id}")
	public User getUser(@PathVariable String id) {
		return userService.getUserById(id);
	}
	
	@GetMapping("/users")
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}

	 
}
