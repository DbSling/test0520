package com.sling.test0520.support.security;

import com.sling.test0520.domain.User;
import lombok.Data;

@Data
public class Login {
	long id;
	String name;
	String email;
	String ipAddr;

	//이게 뭐하는 작업이지?
	public User getFakeUser() {
		User user = new User(id, name);
		return user;
	}

}
