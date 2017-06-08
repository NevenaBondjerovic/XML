package com.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.model.User;
import com.app.repository.UserRepository;

@Component
public class UserServiceImpl implements IUserService{

	@Autowired
	UserRepository userRepo;
	
	@Override
	public boolean registerUser(User user) {
		return userRepo.registerUser(user);
	}

	@Override
	public boolean logUser(String email, String password) {
		return userRepo.login(email, password);
	}

	@Override
	public User getByEmail(String email) {
		return userRepo.getByEmail(email);
	}

}
