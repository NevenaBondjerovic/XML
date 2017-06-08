package com.app.DTO;

public class SessionParamsDTO {

	private String currentAct;
	
	public SessionParamsDTO() {	}

	public SessionParamsDTO(String currentAct) {
		super();
		this.currentAct = currentAct;
	}

	public String getCurrentAct() {
		return currentAct;
	}

	public void setCurrentAct(String currentAct) {
		this.currentAct = currentAct;
	}	
}
