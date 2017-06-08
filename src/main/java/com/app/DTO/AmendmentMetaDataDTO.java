package com.app.DTO;

public class AmendmentMetaDataDTO {
	
	public enum AmendmentOperations {
		Dodavanje, Izmena, Brisanje
	}
	
	private String referredActName;
	
	//broj clana, 'clause' == clan
	private int referredClauseNumber;
	
	private AmendmentOperations operationType;
	
	private String id;
	
	//userInfo == ime +  " " + prezime
	private String userInfo;
	
	public AmendmentMetaDataDTO() {}
		
	public AmendmentMetaDataDTO(String referredActName, int referredClauseNumber, AmendmentOperations operationType, String id, String userInfo) {
		super();
		this.referredActName = referredActName;
		this.referredClauseNumber = referredClauseNumber;
		this.operationType = operationType;
		this.id = id;
		this.userInfo = userInfo;
	}

	public String getReferredActName() {
		return referredActName;
	}

	public void setReferredActName(String referredActName) {
		this.referredActName = referredActName;
	}

	public int getReferredClauseNumber() {
		return referredClauseNumber;
	}

	public void setReferredClauseNumber(int referredClauseNumber) {
		this.referredClauseNumber = referredClauseNumber;
	}

	public AmendmentOperations getOperationType() {
		return operationType;
	}

	public void setOperationType(AmendmentOperations operationType) {
		this.operationType = operationType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}	
}
