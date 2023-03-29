package com.java.prs.capstone.project.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserRepository userRepo;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping("{username}/{password}")
	public ResponseEntity<User> loginUser(@PathVariable String username, @PathVariable String password){
		Optional<User> User = userRepo.findByUsernameAndPassword(username, password);
		if(User.isEmpty()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<User>(User.get(),HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<Iterable<User>> getUsers(){
		Iterable<User> Users = userRepo.findAll();
		return new ResponseEntity<Iterable<User>>(Users, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<User> getUser(@PathVariable int id){
		Optional<User> User = userRepo.findById(id);
		if(User.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<User>(User.get(), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<User> postUser(@RequestBody User user){
		User newUser = userRepo.save(user);
		return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	public ResponseEntity putUser(@PathVariable int id, @RequestBody User user) {
		if(user.getId() != id) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		userRepo.save(user);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	public ResponseEntity deleteUser(@PathVariable int id) {
		Optional<User> User = userRepo.findById(id);
		if(User.isEmpty()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		userRepo.delete(User.get());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
