package com.sling.test0520.web.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;


@Data
public class Login {
	public static final String LOGIN_SESSION_ID = "__LOGIN_SESSION__";

	int id;
	
	@NotEmpty
	String eMail;
	
	@NotEmpty
	String password;

	long siteId;

	long processId;

	String name;
//
//	public String getPassword() {
//		return password;
//	}
//	public void setPassword(String password) {
//		this.password = password;
//	}
//	public int getId() {
//		return id;
//	}
//	public void setId(int id) {
//		this.id = id;
//	}
//	public String getEmail() {
//		return email;
//	}
//	public void setEmail(String email) {
//		this.email = email;
//	}
//	public String getName() {
//		return name;
//	}
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public long getSiteId() {
//		return siteId;
//	}
//
//	public void setSiteId(long siteId) {
//		this.siteId = siteId;
//	}
//
//	public long getProcessId() {
//		return processId;
//	}
//
//	public void setProcessId(long processId) {
//		this.processId = processId;
//	}
}
