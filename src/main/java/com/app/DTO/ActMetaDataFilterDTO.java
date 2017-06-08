package com.app.DTO;

import java.util.Date;

public class ActMetaDataFilterDTO {

	private String name;
	private String status;
	
	private String acceptType;
	private Date dateFrom;
	private Date dateTo;
	
	private int votesFor;
	private int votesReserved;
	private int votesAgainst;
	
	public ActMetaDataFilterDTO() {
	}
	
	public ActMetaDataFilterDTO(String name, String status, String acceptType, Date dateFrom, Date dateTo, int votesFor,
			int votesReserved, int votesAgainst) {
		super();
		this.name = name;
		this.status = status;
		this.acceptType = acceptType;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.votesFor = votesFor;
		this.votesReserved = votesReserved;
		this.votesAgainst = votesAgainst;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAcceptType() {
		return acceptType;
	}
	public void setAcceptType(String acceptType) {
		this.acceptType = acceptType;
	}
	public Date getDateFrom() {
		return dateFrom;
	}
	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}
	public Date getDateTo() {
		return dateTo;
	}
	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}
	public int getVotesFor() {
		return votesFor;
	}
	public void setVotesFor(int votesFor) {
		this.votesFor = votesFor;
	}
	public int getVotesReserved() {
		return votesReserved;
	}
	public void setVotesReserved(int votesReserved) {
		this.votesReserved = votesReserved;
	}
	public int getVotesAgainst() {
		return votesAgainst;
	}
	public void setVotesAgainst(int votesAgainst) {
		this.votesAgainst = votesAgainst;
	}
}
