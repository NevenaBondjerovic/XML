package com.app.DTO;

public class AmandmanDataDTO {
	private String naziv;
	private String obrazlozenje;
	private String refAkt;
	
	public AmandmanDataDTO(){
		
	}
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	public String getObrazlozenje() {
		return obrazlozenje;
	}
	public void setObrazlozenje(String obrazlozenje) {
		this.obrazlozenje = obrazlozenje;
	}
	public String getRefAkt() {
		return refAkt;
	}
	public void setRefAkt(String refAkt) {
		this.refAkt = refAkt;
	}
	public AmandmanDataDTO(String naziv, String obrazlozenje, String refAkt) {
		super();
		this.naziv = naziv;
		this.obrazlozenje = obrazlozenje;
		this.refAkt = refAkt;
	}
	
	
}
