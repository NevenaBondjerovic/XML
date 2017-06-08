package com.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.DTO.UserDTO;
import com.app.DTO.UserDTO.Role;
import com.app.model.User;
import com.app.services.IUserService;
import com.app.transformers.UserDTOToUser;
import com.app.transformers.UserToUserDTO;

@RestController
@RequestMapping(value="/user")
public class UserController {

	@Autowired
	IUserService userService;
	
	@RequestMapping(path="/register", method=RequestMethod.POST)
	public ResponseEntity<Object> createUser(@RequestBody UserDTO user){
		User u = (User) new UserDTOToUser().transform(user);
		u.setType(Role.Odbornik.toString());
		boolean registered = userService.registerUser(u);
		if(registered){
			return new ResponseEntity<>(HttpStatus.CREATED);
		}else{
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		
	}
	
	@RequestMapping(path="/login", method=RequestMethod.POST)
	public ResponseEntity<Object> login(@RequestBody UserDTO user){
		boolean success = userService.logUser(user.getEmail(), user.getPassword());
		if (!success){
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}else{
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}
	
	@RequestMapping(path="/{email}/one", method=RequestMethod.GET)
	public ResponseEntity<UserDTO> getUser(@PathVariable String email){
		User u = userService.getByEmail(email);
		if (u!=null){
			return new ResponseEntity<UserDTO>((UserDTO) new UserToUserDTO().transform(u), HttpStatus.OK);
		}else{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
