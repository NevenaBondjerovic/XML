package com.app.DTO;

import java.util.ArrayList;

public class ActDTO {
	public enum Type {
		Akt, Glava, Deo, Odeljak, Pododeljak, Clan, Stav, Tacka, Podtacka, Alineja
	}
	
	private String name;
	private String predlagac;
	private int id;
	private Type type;
	private int parentId;
	private ArrayList<ActDTO> children;
	private ArrayList<Object> content;
	private Type childType;
	
	public ActDTO() {
		super();
	}
	public ActDTO(String name, String predlagac, int id, Type type, int parentId,
			ArrayList<ActDTO> children, ArrayList<Object> content, Type childType) {
		super();
		this.name = name;
		this.predlagac = predlagac;
		this.id = id;
		this.type = type;
		this.parentId = parentId;
		this.children = children;
		this.content = content;
		this.childType = childType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public ArrayList<ActDTO> getChildren() {
		return children;
	}
	public void setChildren(ArrayList<ActDTO> children) {
		this.children = children;
	}
	public ArrayList<Object> getContent() {
		return content;
	}
	public void setContent(ArrayList<Object> content) {
		this.content = content;
	}
	public Type getChildType() {
		return childType;
	}
	public void setChildType(Type childType) {
		this.childType = childType;
	}
	public String getPredlagac() {
		return predlagac;
	}
	public void setPredlagac(String predlagac) {
		this.predlagac = predlagac;
	}
}
