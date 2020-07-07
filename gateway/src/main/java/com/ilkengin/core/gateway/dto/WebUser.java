package com.ilkengin.core.gateway.dto;

import javax.validation.constraints.NotNull;

public class WebUser {
	@NotNull
	private String username;
	@NotNull
	private String password;

	public WebUser() {
	}

	public WebUser(@NotNull String username, @NotNull String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}