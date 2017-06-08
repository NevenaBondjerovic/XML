package com.app.transformers;

import com.app.DTO.UserDTO;
import com.app.model.User;

public class UserDTOToUser implements ITransformer {

	@Override
	public Object transform(Object o) {
		UserDTO ud = (UserDTO)o;
		User u = new User();
		u.setName(ud.getName());
		u.setEmail(ud.getEmail());
		u.setPassword(ud.getPassword());
		u.setSurname(ud.getSurname());
		if (ud.getRole()!=null)
			u.setType(ud.getRole().toString());
		return u;
	}

	
}
