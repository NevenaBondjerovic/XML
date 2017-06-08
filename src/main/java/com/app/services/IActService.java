package com.app.services;

import java.util.ArrayList;

import javax.xml.datatype.DatatypeConfigurationException;

import com.app.DTO.ActMetaDataDTO;
import com.app.DTO.ActMetaDataFilterDTO;
import com.app.model.Act;

public interface IActService {

	public boolean writeAct(Act act);

	public ArrayList<ActMetaDataDTO> getAllActMetadata();

	public ArrayList<ActMetaDataDTO> getActMetadataByUser(
			String userEmail);

	public boolean withdrawAct(String actName);

	public boolean acceptAct(ActMetaDataDTO actName) throws DatatypeConfigurationException;

	public ArrayList<ActMetaDataDTO> getActsByRef(String actName);

	public ArrayList<ActMetaDataDTO> getActsmetadataFilteredByContent(String keyword);
	
	public boolean generateXHTML(String actName);
	
	public boolean generatePDF(String actName);

	public ArrayList<String> getAllActNames();

	public ArrayList<ActMetaDataDTO> getActsMetaDataFilteredByMetadata(
			ActMetaDataFilterDTO filter);
	
}
