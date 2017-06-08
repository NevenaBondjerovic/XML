package com.app.services;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.datatype.DatatypeConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.DTO.ActMetaDataDTO;
import com.app.DTO.ActMetaDataDTO.AktStatus;
import com.app.DTO.ActMetaDataFilterDTO;
import com.app.model.Act;
import com.app.repository.ActRepository;
import com.app.repository.XHTML_PDF_Repository;

@Component
public class ActServiceImpl implements IActService {

	@Autowired
	private ActRepository actRepo;
	@Autowired
	private XHTML_PDF_Repository xhtml_pdf;

	@Override
	public boolean writeAct(Act act) {
		return actRepo.writeAct(act);
	}

	@Override
	public ArrayList<String> getAllActNames(){
		return actRepo.allActNames();
	}
	
	@Override
	public ArrayList<ActMetaDataDTO> getAllActMetadata() {
		return transformToMetadataDTO(actRepo.getActsmetadataFilteredByContent(""));
	}

	@Override
	public ArrayList<ActMetaDataDTO> getActsmetadataFilteredByContent(String keyword) {
		return transformToMetadataDTO(actRepo.getActsmetadataFilteredByContent(keyword));
	}
	
	@Override
	public ArrayList<ActMetaDataDTO> getActsMetaDataFilteredByMetadata(ActMetaDataFilterDTO filter){
		return transformToMetadataDTO(actRepo.getActsMetaDataFilteredByMetadata(filter));
	}
	
	@Override
	public ArrayList<ActMetaDataDTO> getActMetadataByUser(String userEmail) {
		return transformToMetadataDTO(actRepo.getMetadataByUser(userEmail));
	}

	@Override
	public boolean withdrawAct(String actName) {
		return actRepo.withdrawAct(actName);
	}

	@Override
	public boolean acceptAct(ActMetaDataDTO actName) throws DatatypeConfigurationException {
		return actRepo.acceptAct(actName);
	}

	@Override
	public ArrayList<ActMetaDataDTO> getActsByRef(String actName) {
		return transformToMetadataDTO(actRepo.getActMetadataByRef(actName));
	}
	
	@Override
	public boolean generateXHTML(String actName) {
		Act act=actRepo.getActByName(actName);
		String xmlPath="src/main/resources/data/act.xml";
		String xslPath="src/main/resources/data/htmlAct.xsl";
		if(act!=null){
			if(xhtml_pdf.generateXml(act, xmlPath)){
				return xhtml_pdf.generateXHTML(xslPath,xmlPath);
			}else
				return false;
		}else{
			return false;
		}
		
	}

	@Override
	public boolean generatePDF(String actName) {
		Act act=actRepo.getActByName(actName);
		String xmlPath="src/main/resources/data/act.xml";
		String xslPath="src/main/resources/data/pdfAct.xsl";
		if(act!=null){
			if(xhtml_pdf.generateXml(act, xmlPath)){
				return xhtml_pdf.generatePDF(xslPath,xmlPath);
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	private ArrayList<ActMetaDataDTO> transformToMetadataDTO(
			ArrayList<HashMap<String, String>> allMetadata) {
		ArrayList<ActMetaDataDTO> dtos = new ArrayList<ActMetaDataDTO>();
		for (HashMap<String, String> actMetadata : allMetadata) {
			String name = actMetadata.get("Naziv");
			String userInfo = actMetadata.get("User info");
			String status = actMetadata.get("Status");
			int deleteAm = Integer.parseInt(actMetadata
					.get("Delete amendments"));
			int updateAm = Integer.parseInt(actMetadata
					.get("Update amendments"));
			int appendAm = Integer.parseInt(actMetadata
					.get("Append amendments"));
			ActMetaDataDTO meta = new ActMetaDataDTO(name,
					AktStatus.valueOf(status), userInfo, deleteAm, updateAm,
					appendAm);
			dtos.add(meta);
		}
		return dtos;
	}

}
