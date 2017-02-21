package com.jbossdev.view;

import static com.jbossdev.jdbc.JDBCUtils.executeGetString;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Named;

import org.teiid.deployers.VirtualDatabaseException;
import org.teiid.dqp.internal.datamgr.ConnectorManagerRepository.ConnectorManagerException;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.TranslatorException;

import com.jbossdev.interceptors.Logged;

@Named
@Stateless
public class TeiidExecutor {

	private static final Logger logger = Logger.getLogger(TeiidExecutor.class.getName());

	@Logged
	public void executeTeiidEngineSearch() throws Exception {
		objectVdb();
		visualDesignerVdb();
	}

	private void objectVdb() throws VirtualDatabaseException, ConnectorManagerException, TranslatorException,
			IOException, InterruptedException, SQLException, Exception {
		logger.info("On TeiidExecutor search");

		EmbeddedServer embeddedServer = new EmbeddedServer();
		embeddedServer.start(new EmbeddedConfiguration());
		embeddedServer.deployVDB(TeiidExecutor.class.getClassLoader().getResourceAsStream("object-vdb.xml"));

		Thread.sleep(1000);

		Connection connection = embeddedServer.getDriver().connect("jdbc:teiid:objectExampleVDB", null);

		String result = executeGetString(connection, "SELECT * from Team", false);

		embeddedServer.stop();

		logger.info(result);
	}

	private void visualDesignerVdb() throws VirtualDatabaseException, ConnectorManagerException, TranslatorException,
			IOException, InterruptedException, SQLException, Exception {
		logger.info("On TeiidExecutor search");

		EmbeddedServer embeddedServer = new EmbeddedServer();
		embeddedServer.start(new EmbeddedConfiguration());
//		embeddedServer.deployVDB(TeiidExecutor.class.getClassLoader().getResourceAsStream("PersonListFedView.vdb"));
		
		URL url = TeiidExecutor.class.getClassLoader().getResource("PersonListFedView.vdb");
		String rawPath = url.toURI().getRawPath();
		rawPath = "file:" + rawPath;
		URL newurl = new URL(rawPath);
		embeddedServer.deployVDBZip(newurl);
		
		Thread.sleep(1000);

		Connection connection = embeddedServer.getDriver().connect("jdbc:teiid:PersonListFedView", null);

		String result = executeGetString(connection, "SELECT * FROM PersonList", false);


		embeddedServer.stop();

		logger.info(result);
	}
}
