package com.frankmoley.security.app.auth;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="AUTH_USER_GROUP")
public class AuthGroup {
	
	@Id
	@Column(name="AUTH_USER_GROUP_ID")
	private Long id;
	
	@Column(name="USERNAME", nullable=false)
	private String username;
	
	@Column(name="AUTH_GROUP", nullable=false)
	private String authGroup;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAuthGroup() {
		return authGroup;
	}

	public void setAuthGroup(String authGroup) {
		this.authGroup = authGroup;
	}
	
	
	
	

}
