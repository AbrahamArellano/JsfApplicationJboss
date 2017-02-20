package com.jbossdev.jpa;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;


public class UserBean {

	private EntityManager em;

	@Inject	
	public UserBean(EntityManager em) {
		this.em = em;
	}
	
	public List<User> getListUsers() {
		TypedQuery<User> createNamedQuery = em.createNamedQuery("User.findAll", User.class);
		List<User> resultList = createNamedQuery.getResultList();
		return resultList;
	}
}
