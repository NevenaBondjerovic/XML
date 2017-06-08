package com.app.DTO;

import java.util.ArrayList;

public class ActDataDTO {
	public enum Type {
		Akt, Glava, Odeljak, Pododeljak, Clan, Stav, Tacka, Podtacka, Alineja
	}

	private String naziv;
	private Type type;
	private ArrayList<NodeDataDTO> children;

	public ActDataDTO(String naziv, Type type, ArrayList<NodeDataDTO> children) {
		super();
		this.naziv = naziv;
		this.type = type;
		this.children = children;
	}

	public ActDataDTO() {
		// TODO Auto-generated constructor stub
	}
	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public ArrayList<NodeDataDTO> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<NodeDataDTO> children) {
		this.children = children;
	}
	
}
