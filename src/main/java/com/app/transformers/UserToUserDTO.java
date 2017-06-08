package com.app.transformers;

import com.app.DTO.UserDTO;
import com.app.DTO.UserDTO.Role;
import com.app.model.User;

public class UserToUserDTO implements ITransformer {

	@Override
	public Object transform(Object o) {
		User user = (User)o;
		UserDTO userDTO = new UserDTO();
		userDTO.setName(user.getName());
		userDTO.setEmail(user.getEmail());
		userDTO.setPassword(user.getPassword());
		userDTO.setSurname(user.getSurname());
		if (user.getType().equals("Odbornik")){
			userDTO.setRole(Role.Odbornik);
		}else{
			userDTO.setRole(Role.Predsednik);
		}
		return userDTO;
	}

}
