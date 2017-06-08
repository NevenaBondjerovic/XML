package com.app.DTO;

import com.app.model.Ref;

public class ActMetaDataDTO {

	public enum AktStatus {
		Prihvacen, U_procesu
	}

	private String name;
	private AktStatus status;
	//userInfo == ime +  " " + prezime
	private String userInfo;
	private Ref ref;

	//broj amandmana nad ovim aktom koji imaju operaciju brisanja
	private int numberOfDeleteAmendments;
	
	//broj amandmana nad ovim aktom koji imaju operaciju izmene
	private int numberOfUpdateAmendments;
	
	//broj amandmana nad ovim aktom koji imaju operaciju dodavanja
	private int numberOfAppendAmendments;
	
	private int votesFor;
	private int votesReserved;
	private int votesAgainst;

	public ActMetaDataDTO() {
	}

	public ActMetaDataDTO(String name, AktStatus status, String userInfo, int numberOfDeleteAmendments,
			int numberOfUpdateAmendments, int numberOfAppendAmendments, Ref ref) {
		super();
		this.name = name;
		this.status = status;
		this.userInfo = userInfo;
		this.numberOfDeleteAmendments = numberOfDeleteAmendments;
		this.numberOfUpdateAmendments = numberOfUpdateAmendments;
		this.numberOfAppendAmendments = numberOfAppendAmendments;
		this.ref = ref;
	}
	public ActMetaDataDTO(String name, AktStatus status, String userInfo, int numberOfDeleteAmendments,
			int numberOfUpdateAmendments, int numberOfAppendAmendments) {
		super();
		this.name = name;
		this.status = status;
		this.userInfo = userInfo;
		this.numberOfDeleteAmendments = numberOfDeleteAmendments;
		this.numberOfUpdateAmendments = numberOfUpdateAmendments;
		this.numberOfAppendAmendments = numberOfAppendAmendments;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStatus(AktStatus status) {
		this.status = status;
	}

	public AktStatus getStatus() {
		return status;
	}

	public String getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

	public int getNumberOfDeleteAmendments() {
		return numberOfDeleteAmendments;
	}

	public void setNumberOfDeleteAmendments(int numberOfDeleteAmendments) {
		this.numberOfDeleteAmendments = numberOfDeleteAmendments;
	}

	public int getNumberOfUpdateAmendments() {
		return numberOfUpdateAmendments;
	}

	public void setNumberOfUpdateAmendments(int numberOfUpdateAmendments) {
		this.numberOfUpdateAmendments = numberOfUpdateAmendments;
	}

	public int getNumberOfAppendAmendments() {
		return numberOfAppendAmendments;
	}

	public void setNumberOfAppendAmendments(int numberOfAppendAmendments) {
		this.numberOfAppendAmendments = numberOfAppendAmendments;
	}

	public int getVotesAgainst() {
		return votesAgainst;
	}
	
	public int getVotesFor() {
		return votesFor;
	}
	
	public int getVotesReserved() {
		return votesReserved;
	}
	
	public void setVotesAgainst(int votesAgainst) {
		this.votesAgainst = votesAgainst;
	}
	
	public void setVotesFor(int votesFor) {
		this.votesFor = votesFor;
	}
	
	public void setVotesReserved(int votesReserved) {
		this.votesReserved = votesReserved;
	}

	public Ref getRef() {
		return ref;
	}
	
	public void setRef(Ref ref) {
		this.ref = ref;
	}	
}
