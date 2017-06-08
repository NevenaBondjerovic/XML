package com.app.DTO;

import java.util.List;

public class AmendmentProposalDTO {

	private RefDTO ref;
	private List<Object> content;
	private NodeDataDTO node;
	
	public AmendmentProposalDTO() {
		super();
	}

	public AmendmentProposalDTO(RefDTO ref, List<Object> content, NodeDataDTO node) {
		super();
		this.ref = ref;
		this.content = content;
		this.node = node;
	}

	public RefDTO getRef() {
		return ref;
	}

	public void setRef(RefDTO ref) {
		this.ref = ref;
	}

	public List<Object> getContent() {
		return content;
	}

	public void setContent(List<Object> content) {
		this.content = content;
	}

	public NodeDataDTO getNode() {
		return node;
	}

	public void setNode(NodeDataDTO node) {
		this.node = node;
	}
	
}
