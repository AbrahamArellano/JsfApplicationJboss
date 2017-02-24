package com.jbossdev.teiid;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;

import org.teiid.deployers.VirtualDatabaseException;
import org.teiid.dqp.internal.datamgr.ConnectorManagerRepository.ConnectorManagerException;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.TranslatorException;

import com.jbossdev.view.TeiidExecutor;

/**
 * Class that provides a eager initialization of the Teiid embedded server.
 * 
 * @author aarellan
 *
 */
@Singleton
@Startup
public class TeiidManager {

	private final static Logger logger = Logger.getLogger(TeiidManager.class.getName());

	@Produces
	private static EmbeddedServer embeddedServer = new EmbeddedServer();

	@PostConstruct
	public void startUp() throws VirtualDatabaseException, ConnectorManagerException, TranslatorException, IOException,
			URISyntaxException {
		logger.info("TeiidManager.startUp");
		embeddedServer.start(new EmbeddedConfiguration());
		embeddedServer.deployVDB(TeiidExecutor.class.getClassLoader().getResourceAsStream("object-vdb.xml"));
		URL url = TeiidExecutor.class.getClassLoader().getResource("ProcedureFedView.vdb");
		String rawPath = url.toURI().getRawPath();
		rawPath = "file:" + rawPath;
		URL newurl = new URL(rawPath);
		embeddedServer.deployVDBZip(newurl);

		// hibernate loading

	}

	@PreDestroy
	public void shutdown() {
		logger.info("TeiidManager.shutdown");
		embeddedServer.undeployVDB("objectExampleVDB");
		embeddedServer.undeployVDB("PersonListFedView");
		embeddedServer.stop();
	}

	public static EmbeddedServer getEmbeddedServer() {
		return embeddedServer;
	}

}
