package com.ftd.smartshare.entity;
import java.sql.Timestamp;
import java.io.File;



public class Files {
	private String fileName;
	private Integer id;
	private byte[] file;
	private Timestamp timeCreated;
	private Timestamp expiryTime;
	private Integer maxDownloads;
	private Integer totalDownloads;
	private String password;
	
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	public byte[] getFile() {
		return file;
	}
	public void setFile(byte[] file) {
		this.file = file;
	}
	
	
	public Timestamp getTimeCreated() {
		return timeCreated;
	}
	public void setTimeCreated(Timestamp timeCreated) {
		this.timeCreated = timeCreated;
	}
	
	
	public Timestamp getExpiryTime() {
		return expiryTime;
	}
	public void setExpiryTime(Timestamp expiryTime) {
		this.expiryTime = expiryTime;
	}
	
	
	public Integer getMaxDownloads() {
		return maxDownloads;
	}
	public void setMaxDownloads(Integer maxDownloads) {
		this.maxDownloads = maxDownloads;
	}
	
	
	public Integer getTotalDownloads() {
		return totalDownloads;
	}
	public void setTotalDownloads(Integer totalDownloads) {
		this.totalDownloads = totalDownloads;
	}
	
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	@Override
	public String toString() {
		return "Files [timeCreated=" + timeCreated + ", expiryTime=" + 
					expiryTime + ", remainingDownloads=" + (maxDownloads - totalDownloads) + "]";
	}
	
	

}
