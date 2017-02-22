package com.jbossdev.view;

import static com.jbossdev.jdbc.JDBCUtils.executeGetString;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.teiid.deployers.VirtualDatabaseException;
import org.teiid.dqp.internal.datamgr.ConnectorManagerRepository.ConnectorManagerException;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.TranslatorException;

import com.jbossdev.interceptors.Logged;

@Named
@Stateful
@ConversationScoped
public class TeiidExecutor {

	private static final Logger logger = Logger.getLogger(TeiidExecutor.class.getName());

	@Inject
	private Conversation conversation;

	@Produces
	private static EmbeddedServer embeddedServer = new EmbeddedServer();

	private String federatedView;
	private String jobType;
	private String result;

	@PostConstruct
	public void startUp() throws VirtualDatabaseException, ConnectorManagerException, TranslatorException, IOException,
			URISyntaxException {
		embeddedServer.start(new EmbeddedConfiguration());
		embeddedServer.deployVDB(TeiidExecutor.class.getClassLoader().getResourceAsStream("object-vdb.xml"));
		URL url = TeiidExecutor.class.getClassLoader().getResource("ProcedureFedView.vdb");
		String rawPath = url.toURI().getRawPath();
		rawPath = "file:" + rawPath;
		URL newurl = new URL(rawPath);
		embeddedServer.deployVDBZip(newurl);
	}

	@Logged
	public void loadTeiidEngineSearch() {
		if (conversation.isTransient()) {
			conversation.begin();
		}

	}

	public void executeDesignerFederatedView() throws VirtualDatabaseException, ConnectorManagerException,
			TranslatorException, IOException, InterruptedException, SQLException, Exception {

		if (federatedView.equals("objectVdb")) {
			objectVdb();
		} else if (federatedView.equals("visualDesignerVdbProcedureExecutionWithParameters")) {
			visualDesignerVdbProcedureExecutionWithParameters();
		}
	}

	@Logged
	private void objectVdb() throws VirtualDatabaseException, ConnectorManagerException, TranslatorException,
			IOException, InterruptedException, SQLException, Exception {

		Connection connection = embeddedServer.getDriver().connect("jdbc:teiid:objectExampleVDB", null);
		result = executeGetString(connection, "SELECT * from Team", false);

		logger.info(result);
	}

	@Logged
	public void visualDesignerVdbProcedureExecutionWithParameters() throws VirtualDatabaseException,
			ConnectorManagerException, TranslatorException, IOException, InterruptedException, SQLException, Exception {

		Connection connection = embeddedServer.getDriver().connect("jdbc:teiid:ProcedureFedView", null);
		result = executeGetString(connection, "SELECT * FROM Person_Table where jobType='" + jobType + "'", false);

		logger.info(result);
	}

	public String stopAndBack() {
		// embeddedServer.undeployVDB("objectExampleVDB");
		embeddedServer.undeployVDB("PersonListFedView");
		embeddedServer.stop();
		conversation.end();
		logger.info("Conversation ended");
		return "/secure/data?faces-redirect=true";
	}

	public String getFederatedView() {
		return federatedView;
	}

	public void setFederatedView(String federatedView) {
		this.federatedView = federatedView;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}

/*
 * 
 * @Logged public void visualDesignerVdbViewExecutionNoParameters() throws
 * VirtualDatabaseException, ConnectorManagerException, TranslatorException,
 * IOException, InterruptedException, SQLException, Exception {
 * 
 * Connection connection =
 * embeddedServer.getDriver().connect("jdbc:teiid:PersonListFedView", null);
 * result = executeGetString(connection, "SELECT * FROM PersonList", false);
 * 
 * logger.info(result); }
 */