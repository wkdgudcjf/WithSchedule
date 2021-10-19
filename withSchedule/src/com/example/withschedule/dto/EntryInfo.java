package com.example.withschedule.dto;

public class EntryInfo {
	private String name;
	private String email;
	private boolean isOwner;
	public EntryInfo(String name, String email, boolean isOwner) {
		super();
		this.name = name;
		this.email = email;
		this.isOwner = isOwner;
	}
	public String getName() {
		return name;
	}
	public String getEmail() {
		return email;
	}
	public boolean isOwner() {
		return isOwner;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setOwner(boolean isOwner) {
		this.isOwner = isOwner;
	}
	@Override
	public String toString() {
		return "EntryInfo [name=" + name + ", email=" + email + ", isOwner="
				+ isOwner + "]";
	}	
}
