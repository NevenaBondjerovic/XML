package com.app.DTO;

import java.util.List;

public class NodeDTO {

	public enum NodeType{
		Clan, Stav, Tacka, Podtacka, Alineja
	}
	
	private List<Object> content;
	private String name;
	private NodeType type;
	private String id;
	
	public NodeDTO() {
		super();
	}

	public NodeDTO(List<Object> content, String name, NodeType type, String id) {
		super();
		this.content = content;
		this.name = name;
		this.type = type;
		this.id = id;
	}

	public List<Object> getContent() {
		return content;
	}

	public void setContent(List<Object> content) {
		this.content = content;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public NodeType getType() {
		return type;
	}

	public void setType(NodeType type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
