package com.jbossdev.jpa;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.jbossdev.jpa.qualifiers.DataContext;
import com.jbossdev.jpa.qualifiers.OtherContext;

public class DataFactory {

	@Produces
	@DataContext
	@PersistenceContext(name="alfa", unitName="JbossDeveloper")
	private EntityManager em;

	@Produces
	@OtherContext
	@PersistenceContext(name="beta", unitName="OtherDeveloper")
	private EntityManager em2;


	public EntityManager getEm() {
		return em;
	}

	public EntityManager getEm2() {
		return em2;
	}
	
	
}
