package com.app.DTO;

public class FileDownloadDTO {

	private String path;
	private String fileName;
	
	public FileDownloadDTO() {}
	
	public FileDownloadDTO(String path, String fileName) {
		super();
		this.path = path;
		this.fileName = fileName;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public String getPath() {
		return path;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
