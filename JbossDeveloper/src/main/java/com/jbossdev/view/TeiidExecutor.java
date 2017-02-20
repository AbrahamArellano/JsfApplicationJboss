package com.jbossdev.view;

import static com.jbossdev.jdbc.JDBCUtils.execute;

import java.sql.Connection;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Named;

import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;

import com.jbossdev.interceptors.Logged;

@Named
@Stateless
public class TeiidExecutor {

	private static final Logger logger = Logger.getLogger(TeiidExecutor.class.getName());
	
	@Logged
	public void executeTeiidEngineSearch() throws Exception {
		logger.info("On TeiidExecutor search");
		
		EmbeddedServer embeddedServer = new EmbeddedServer();
        embeddedServer.start(new EmbeddedConfiguration());
        embeddedServer.deployVDB(TeiidExecutor.class.getClassLoader().getResourceAsStream("object-vdb.xml"));

        Thread.sleep(1000);

        Connection connection = embeddedServer.getDriver().connect("jdbc:teiid:objectExampleVDB", null);

        execute(connection, "SELECT * from Team", false);
        
//        execute(connection, "SELECT * from Player", true); 
        
        embeddedServer.stop();
	}
}
