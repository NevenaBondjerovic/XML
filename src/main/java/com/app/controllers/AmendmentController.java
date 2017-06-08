package com.app.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.DTO.AmendmentDTO;
import com.app.DTO.AmendmentMetaDataDTO;
import com.app.DTO.FileDownloadDTO;
import com.app.DTO.NodeDataDTO;
import com.app.model.Amendment;
import com.app.services.AmendmentServiceImpl;
import com.app.transformers.AmToAmMetaDataDTO;
import com.app.transformers.AmendmentDTOToAmendment;

@RestController
@RequestMapping(value = "/amendment")
public class AmendmentController {

	@Autowired
	public AmendmentServiceImpl service;
	
	@RequestMapping(path="/getByUser/{email}/all", method=RequestMethod.GET)
	public ResponseEntity<List<AmendmentMetaDataDTO>> getAllAmendmentsByUser(@PathVariable String email){
		List<Amendment> amendments=service.getAllAmendmentsByUser(email);
		List<AmendmentMetaDataDTO> amendmentsDTO=new ArrayList<>();
		for(Amendment am:amendments){
			AmendmentMetaDataDTO amDTO=(AmendmentMetaDataDTO)new AmToAmMetaDataDTO().transform(am);
			String userINfo=service.getUserInfoByEmail(am.getPredlagac());
			amDTO.setUserInfo(userINfo);
			amendmentsDTO.add(amDTO);
		}
		return new ResponseEntity<>(amendmentsDTO, HttpStatus.OK);
	}
	
	@RequestMapping(value = {"/getReferenceElement/{act}","/getReferenceElement/{act}/{member}",
			"/getReferenceElement/{act}/{member}/{paragraph}","/getReferenceElement/{act}/{member}/{paragraph}/{clause}",
			"/getReferenceElement/{act}/{member}/{paragraph}/{clause}/{subclause}"}, method = RequestMethod.GET)
	public ResponseEntity<Object> getReferenceElement(@PathVariable Map<String, String> pathVariables) {
		
		Map<String,Object> retVal=service.getReferenceElement(pathVariables);
		
		return new ResponseEntity<Object>(retVal,HttpStatus.OK);
	}
	
	@RequestMapping(path = "/withdraw/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> withdrawAmendment(@PathVariable String id) {
		if(service.deleteAmendment(id))
			return new ResponseEntity<>(HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(path="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Object> deleteAmendment(@PathVariable String id){
		if(service.deleteAmendment(id))
			return new ResponseEntity<>(HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	

	@RequestMapping(path="/accept", method=RequestMethod.PATCH)
	public ResponseEntity<Object> acceptAmendment(@RequestBody AmendmentMetaDataDTO amendment){
		if(service.acceptAmendment(amendment.getId()))
			return new ResponseEntity<>(HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(path = "/getMembersByAct/{name}", method = RequestMethod.GET)
	public ResponseEntity<Object> getMembersByAct(@PathVariable String name) {
		List<NodeDataDTO> nodeDTOs=service.getNodeMembersByAct(name);
		return new ResponseEntity<Object>(nodeDTOs, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/getMember/{act}/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> getMember(@PathVariable String act, @PathVariable String id) {

		 NodeDataDTO node=service.getMemberByActAndId(act,id);
		  List<NodeDataDTO> nodeData=new ArrayList<>();
		  nodeData.add(node);
		  return new ResponseEntity<Object>(nodeData, HttpStatus.OK);

	}
	
	@RequestMapping(path="/getByAct/delete/{actName}", method=RequestMethod.GET)
	public ResponseEntity<Object> getAllAmendmentsByActWithDeleteOperation(@PathVariable String actName){
		List<Amendment> amendments=service.getAllAmendmetnsByActWithOperation(actName, "Brisanje");
		List<AmendmentMetaDataDTO> amendmentsDTO=new ArrayList<>();
		for(Amendment am:amendments){
			AmendmentMetaDataDTO amDTO=(AmendmentMetaDataDTO)new AmToAmMetaDataDTO().transform(am);
			String userINfo=service.getUserInfoByEmail(am.getPredlagac());
			amDTO.setUserInfo(userINfo);
			amendmentsDTO.add(amDTO);
		}
		return new ResponseEntity<Object>(amendmentsDTO, HttpStatus.OK);
	}
	
	@RequestMapping(path="/getByAct/update/{actName}", method=RequestMethod.GET)
	public ResponseEntity<Object> getAllAmendmentsByActWithUpdateOperation(@PathVariable String actName){
		List<Amendment> amendments=service.getAllAmendmetnsByActWithOperation(actName, "Izmena");
		List<AmendmentMetaDataDTO> amendmentsDTO=new ArrayList<>();
		for(Amendment am:amendments){
			AmendmentMetaDataDTO amDTO=(AmendmentMetaDataDTO)new AmToAmMetaDataDTO().transform(am);
			String userINfo=service.getUserInfoByEmail(am.getPredlagac());
			amDTO.setUserInfo(userINfo);
			amendmentsDTO.add(amDTO);
		}
		return new ResponseEntity<Object>(amendmentsDTO, HttpStatus.OK);
	}

	@RequestMapping(path="/getByAct/append/{actName}", method=RequestMethod.GET)
	public ResponseEntity<Object> getAllAmendmentsByActWithAppendOperation(@PathVariable String actName){
		List<Amendment> amendments=service.getAllAmendmetnsByActWithOperation(actName, "Dodavanje");
		List<AmendmentMetaDataDTO> amendmentsDTO=new ArrayList<>();
		for(Amendment am:amendments){
			AmendmentMetaDataDTO amDTO=(AmendmentMetaDataDTO)new AmToAmMetaDataDTO().transform(am);
			String userINfo=service.getUserInfoByEmail(am.getPredlagac());
			amDTO.setUserInfo(userINfo);
			amendmentsDTO.add(amDTO);
		}
		return new ResponseEntity<Object>(amendmentsDTO, HttpStatus.OK);
	}

	@RequestMapping(path="/generatePDF/{id}", method=RequestMethod.GET)
	public ResponseEntity<Object> generatePDF(@PathVariable String id){
		FileDownloadDTO download = new FileDownloadDTO("downloads/data.pdf", "data.pdf");
		if(service.generatePDF(id))
			return new ResponseEntity<Object>(download, HttpStatus.OK); 
		return new ResponseEntity<Object>(download, HttpStatus.NOT_FOUND);		
	}

	@RequestMapping(path="/generateXHTML/{id}", method=RequestMethod.GET)
	public ResponseEntity<Object> generateXHTML(@PathVariable String id){
		FileDownloadDTO download = new FileDownloadDTO("downloads/data.html", "data.html");
		if(service.generateXHTML(id))
			return new ResponseEntity<Object>(download, HttpStatus.OK); 
		return new ResponseEntity<Object>(download, HttpStatus.NOT_FOUND);	
	}
	
		
	@RequestMapping(path = "/addAmendment", method = RequestMethod.POST)
	public ResponseEntity<Object> addAmendment(@RequestBody AmendmentDTO amendment) {
		amendment.setId(service.getAmendmentID()+"");
		amendment.setName("Amandman "+amendment.getId());
		Amendment am=(Amendment)new AmendmentDTOToAmendment().transform(amendment);
		if(service.createNewAmendment(am))
			return new ResponseEntity<>(HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.CONFLICT);
	}
	
	
}
