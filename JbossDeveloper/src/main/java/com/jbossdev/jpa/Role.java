package com.jbossdev.jpa;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ROLE database table.
 * 
 */
@Entity
@Table(name="ROLE")
@NamedQuery(name="Role.findAll", query="SELECT r FROM Role r")
public class Role implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="USERID")
	private int userid;

	@Column(name="ROLE")
	private String role;

	public Role() {
	}

	public int getUserid() {
		return this.userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}