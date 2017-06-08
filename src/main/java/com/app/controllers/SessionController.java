package com.app.controllers;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.DTO.SessionParamsDTO;

@RestController
@RequestMapping(value = "/session")
public class SessionController {


	@Autowired
    private ServletContext servletContext;
	
	public static final String sessionKeyword = "isSessionInProgress";
	public static final String councilorsKeyword = "numberOfCouncilors";
	public static final String currentActKeyword = "actInDetailDiscussion";
	
	@RequestMapping(path = "/inProgress", method = RequestMethod.GET)
	public ResponseEntity<Object> getSessionInfo() {
		boolean isSessionInProgress = false;
		if(servletContext.getAttribute(sessionKeyword) != null) {
			isSessionInProgress  = (boolean) servletContext.getAttribute(sessionKeyword);
		}
		
		if(isSessionInProgress && servletContext.getAttribute(councilorsKeyword) != null) {
			int councilorsNumber = (int) servletContext.getAttribute(councilorsKeyword);
			return new ResponseEntity<Object>(councilorsNumber, HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
		}		
	}
	
	@RequestMapping(path = "/startSession", method = RequestMethod.POST)
	public ResponseEntity<Object> startSession(@RequestBody int numberOfCouncilors) {
		
		if(isSessionStarted()) {
			return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
		}
		
		servletContext.setAttribute(councilorsKeyword, numberOfCouncilors);
		servletContext.setAttribute(sessionKeyword, true);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@RequestMapping(path = "/endSession", method = RequestMethod.POST)
	public ResponseEntity<Object> endSession() {
		if(!isSessionStarted()) {
			return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
		}
		
		servletContext.removeAttribute(councilorsKeyword);
		servletContext.removeAttribute(sessionKeyword);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@RequestMapping(path = "/startDiscuissionInDetail", method = RequestMethod.POST)
	public ResponseEntity<Object> startDiscuissionInDetail(@RequestBody String actName) {
		if(!isSessionStarted()) {
			return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
		}
		
		servletContext.setAttribute(currentActKeyword, actName);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@RequestMapping(path = "/getActForAmendments", method = RequestMethod.GET)
	public ResponseEntity<Object> startDiscuissionInDetail() {
		String actName = "";
		if(!isSessionStarted() || servletContext.getAttribute(currentActKeyword) == null) {
			actName = null;
		}		
		if(servletContext.getAttribute(currentActKeyword) != null) {
			actName = (String)servletContext.getAttribute(currentActKeyword);
		}
		return new ResponseEntity<Object>(new SessionParamsDTO(actName), HttpStatus.OK);
	}
	
	@RequestMapping(path = "/endDiscuissionInDetail", method = RequestMethod.POST)
	public ResponseEntity<Object> endDiscuissionInDetail() {
		if(!isSessionStarted() || servletContext.getAttribute(currentActKeyword) == null) {
			return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
		}
		
		servletContext.removeAttribute(currentActKeyword);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}	
	
	private boolean isSessionStarted() {
		return servletContext.getAttribute(councilorsKeyword) != null || servletContext.getAttribute(sessionKeyword) != null; 
	}
}
