package com.app.services;

import java.util.List;
import java.util.Map;

import com.app.DTO.NodeDataDTO;
import com.app.model.Amendment;

public interface IAmendmentService {

	public boolean createNewAmendment(Amendment am);
	
	public boolean deleteAmendment(String id);
	
	public List<Amendment> getAllAmendmentsByUser(String user);
	
	public List<Amendment> getAllAmendmetnsByActWithOperation(String act, String operation);
	
	public boolean acceptAmendment(String id);
	
	public boolean generateXHTML(String id);
	
	public boolean generatePDF(String id);
	
	public List<NodeDataDTO> getNodeMembersByAct(String actName);
	
	public NodeDataDTO getMemberByActAndId(String actName,String memberId);
	
	public int getAmendmentID();
	
	public Map<String,Object> getReferenceElement(Map<String,String> path);
	
	public String getUserInfoByEmail(String email);
	
}