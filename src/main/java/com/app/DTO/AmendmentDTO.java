package com.app.DTO;

import java.util.List;

public class AmendmentDTO {

	public enum AmendmentType {
		Dodavanje, Izmena, Brisanje
	}
	
	public enum AmendmentStatus {
		Prihvacen, U_procesu
	}
	
	private String actName;
	private List<AmendmentProposalDTO> proposalList;
	private List<Object> description;
	private AmendmentType type;
	private AmendmentStatus status;
	private String id;
	private String user;
	private String name;
	
	public AmendmentDTO() {
		super();
	}

	public AmendmentDTO(String actName, List<AmendmentProposalDTO> proposalList,
			List<Object> description, AmendmentType type,
			AmendmentStatus status, String id, String user, String name) {
		this.actName = actName;
		this.proposalList = proposalList;
		this.description = description;
		this.type = type;
		this.status = status;
		this.id = id;
		this.user = user;
		this.name = name;
	}

	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName;
	}

	public List<AmendmentProposalDTO> getProposalList() {
		return proposalList;
	}

	public void setProposalList(List<AmendmentProposalDTO> proposalList) {
		this.proposalList = proposalList;
	}

	public List<Object> getDescription() {
		return description;
	}

	public void setDescription(List<Object> description) {
		this.description = description;
	}

	public AmendmentType getType() {
		return type;
	}

	public void setType(AmendmentType type) {
		this.type = type;
	}

	public AmendmentStatus getStatus() {
		return status;
	}

	public void setStatus(AmendmentStatus status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
