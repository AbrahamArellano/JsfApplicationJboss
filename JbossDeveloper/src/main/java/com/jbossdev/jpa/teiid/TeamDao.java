package com.jbossdev.jpa.teiid;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.hibernate.engine.spi.SessionBuilderImplementor;
import org.hibernate.internal.SessionFactoryImpl;

import com.jbossdev.beans.TeamObject;

public class TeamDao implements Serializable {

	private static final long serialVersionUID = 1L;

	// @PersistenceContext(name = "teiid", unitName = "teiidUnit")
	// private EntityManager em;

	@PersistenceUnit(unitName = "teiidUnit")
	EntityManagerFactory factory;

	@SuppressWarnings("unchecked")
	public List<TeamObject> getListOfTeams(Connection conn) throws SQLException {
		/* Setting datasource */

		HibernateEntityManagerFactory factoryHib = (HibernateEntityManagerFactory) factory;

		SessionFactoryImpl sessionFac = (SessionFactoryImpl) factoryHib.getSessionFactory();

		SessionBuilderImplementor builder = sessionFac.withOptions();
		Session session = builder.connection(conn).openSession();
		Transaction tx = session.beginTransaction();

		Query namedQuery = session.getNamedQuery("TeamObject.findAll");
		List<TeamObject> resultList = namedQuery.list();
		// Hibernate.initialize(resultList);

		String string = resultList.toString();
		tx.commit();
		// sessionFac.close();

		return resultList;
	}
}
