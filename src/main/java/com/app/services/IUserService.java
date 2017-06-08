package com.app.services;

import com.app.model.User;

public interface IUserService {
	
	public boolean registerUser(User user);
	
	public boolean logUser(String name, String password);
	
	public User getByEmail(String name);

}
