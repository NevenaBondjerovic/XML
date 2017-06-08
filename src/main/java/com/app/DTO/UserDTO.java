package com.app.DTO;

public class UserDTO {

	public enum Role{
		Odbornik, Predsednik
	}

	private String name;
	private String email;
	private String surname;
	private String password;
	private Role role;
	
	public UserDTO() {
		super();
	}
	public UserDTO(String name, String email, String surname, String password,
			Role role) {
		super();
		this.name = name;
		this.email = email;
		this.surname = surname;
		this.password = password;
		this.role = role;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}

}
