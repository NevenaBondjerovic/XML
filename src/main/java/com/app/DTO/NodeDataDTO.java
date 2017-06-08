package com.app.DTO;

import java.util.ArrayList;

public class NodeDataDTO {
	
	public enum Type{
		act, member, paragraph, clause, subclause, indent
	}

	private String id; 
	private String parentId; 
	private String name; //ime elementa
	private Type type; 
	private RefDTO ref; //referenca kome pripada element (ne sadrzi content)
	private ArrayList<Object> text;  // tekst i reference u tekstu (reference sadrze i content zbog linka)
	private ArrayList<NodeDataDTO> content; //podelementi za dati element
	
	public NodeDataDTO() {
		// TODO Auto-generated constructor stub
	}

	public NodeDataDTO(String id, String parentId, String name, Type type, RefDTO ref, ArrayList<Object> text,
			ArrayList<NodeDataDTO> content) {
		super();
		this.id = id;
		this.parentId = parentId;
		this.name = name;
		this.type = type;
		this.ref = ref;
		this.text = text;
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public RefDTO getRef() {
		return ref;
	}

	public void setRef(RefDTO ref) {
		this.ref = ref;
	}

	public ArrayList<Object> getText() {
		return text;
	}

	public void setText(ArrayList<Object> text) {
		this.text = text;
	}

	public ArrayList<NodeDataDTO> getContent() {
		return content;
	}

	public void setContent(ArrayList<NodeDataDTO> content) {
		this.content = content;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	
}
