package cn.homjie.guava.util.test;

import java.util.UUID;

public class User {

	private String id;

	private String username;

	public User() {
		this.id = UUID.randomUUID().toString().replaceAll("-", "");
	}

	public String getUsername() {
		return username;
	}

	public String getId() {
		return id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
