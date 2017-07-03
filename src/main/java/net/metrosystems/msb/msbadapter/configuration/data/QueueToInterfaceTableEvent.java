package net.metrosystems.msb.msbadapter.configuration.data;

import net.metrosystems.msb.Encryptor;
import net.metrosystems.msb.msbadapter.configuration.generators.GeneratorException;
import net.metrosystems.msb.msbadapter.configuration.generators.assembler.JDBCConnectionAssembler;
import net.metrosystems.msb.msbadapter.configuration.generators.assembler.ProviderURLAssembler;
import net.metrosystems.msb.msbadapter.configuration.generators.assembler.QueueNameAssembler;
import net.metrosystems.msb.msbadapter.configuration.template.TemplateManagerException;
import net.metrosystems.msb.msbadapter.configuration.template.data.XMLTemplate;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.List;

public class QueueToInterfaceTableEvent extends Event {

	private static final String JDBC_USER_XML_TAG = "JDBCUser";
	private static final String JDBC_DRIVER_XML_TAG = "JDBCDriver";
	private static final String PLAIN_PASSWORD_XML_TAG = "Password";
	private static final String PASSWORD_XML_TAG = "EncryptedPassword";
	private static final String DATABASE_PORT_XML_TAG = "DatabasePort";
	private static final String DATABASE_SERVER_XML_TAG = "DatabaseServer";
	private static final String SLEEP_RECOVERABLE_ERROR_XML_TAG = "SleepRecoverableError";
	private static final String POLL_INTERVAL_XML_TAG = "PollInterval";
	private static final String INTERFACE_TABLE_XML_TAG = "InterfaceTable";
	private static final String MSB_HEADER_XML_TAG = "MSBHeaderMapping";
	private static final String HEADER_VERSION = "headerversion";
	private static final String HEADER_VERSION_TAG = "HeaderVersion";
	private static final String FAULT_QUEUE = "FaultQueue";

	private static final String sleepRecoverableErrorDefault = "60000";
	private static final String pollIntervalDefault = "1000";
	private static final String interfaceTableDefault = "MSB_XML_IN";


	private String jdbcUser;
	private String password;
	private String databasePort;
	private String databaseServer;
	private String source;
	private List<String> targets;
	private String sleepRecoverableError;
	private String pollInterval;
	private String interfaceTable;
	private String msbHeaderMappingVersion;
	private String jdbcDriver;
	private String faultQueue;

	public QueueToInterfaceTableEvent(Element element, Environment env) {
		super(element, env);
		initFromElement(element);
	}

	public String getPassword() {
		return password;
	}

	public String getJdbcUser() {
		return jdbcUser;
	}

	public String getDatabasePort() {
		return databasePort;
	}

	public String getDatabaseServer() {
		return databaseServer;
	}

	public String getSource() {
		return source;
	}

	public List<String> getTargets() {
		return targets;
	}

	public String getInterfaceTable() {
		return interfaceTable;
	}

	public String getPollInterval() {
		return pollInterval;
	}

	public String getSleepRecoverableError() {
		return sleepRecoverableError;
	}

	public String getMsbHeaderMappingVersion() {
		return msbHeaderMappingVersion;
	}

	public String getJdbcDriver() {
		return jdbcDriver;
	}

	public String getFaultQueue() {
		return faultQueue;
	}

	private void initFromElement(Element e) {
		jdbcUser = e.getChild(JDBC_USER_XML_TAG).getValue();
		jdbcDriver = env.getDbDriver();

		if (e.getChild(PASSWORD_XML_TAG) != null) {
			password = e.getChild(PASSWORD_XML_TAG).getValue();
		} else {
			try {
				password = Encryptor.encrypt(e.getChild(PLAIN_PASSWORD_XML_TAG).getValue());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		if (env.getDbConnection() == null) {
			databasePort = e.getChild(DATABASE_PORT_XML_TAG).getValue();
		}
		databaseServer = e.getChild(DATABASE_SERVER_XML_TAG).getValue();
		source = e.getChild(SOURCE_XML_TAG).getValue();
		targets = getTargetList(e.getChild(TARGETS_XML_TAG));

		if (e.getChild(HEADER_VERSION_TAG) != null) msbHeaderMappingVersion = e.getChild(HEADER_VERSION_TAG).getValue();

		if (e.getChild(SLEEP_RECOVERABLE_ERROR_XML_TAG) != null)
			sleepRecoverableError = e.getChild(SLEEP_RECOVERABLE_ERROR_XML_TAG).getValue();
		if (e.getChild(POLL_INTERVAL_XML_TAG) != null) pollInterval = e.getChild(POLL_INTERVAL_XML_TAG).getValue();
		if (e.getChild(INTERFACE_TABLE_XML_TAG) != null)
			interfaceTable = e.getChild(INTERFACE_TABLE_XML_TAG).getValue();
		faultQueue = setNonMandatoryTagValue(e, FAULT_QUEUE);
	}

	@Override
	public String toString() {
		return getEnv() + " " + getServer() + " "
				+ getSource() + " " + getTargets().toString()
				+ " " + jdbcUser + " " + password + " " + databaseServer;
	}

	public Element generateEvent() throws GeneratorException {
		XMLTemplate xmlTemplate;
		try {
			xmlTemplate = TEMPLATE_MANAGER.getQueueToInterfaceEventTemplate();
		} catch (TemplateManagerException e) {
			throw new GeneratorException(e);
		}

		templateRoot = xmlTemplate.getContent();
		enrichTemplate();

		return templateRoot;
	}

	private void enrichTemplate() {

		enrichElement(NAME_TAG, getName());
		enrichElement(PROVIDER_URL_TAG, ProviderURLAssembler.assemble(getEnv().getHostname(), getEnv().getPort()));
		enrichElement(CONNECTION_FACTORY_TAG, getEnv().getQueueManager());
		if (env.getDbConnection() == null) {
			enrichElement(JDBC_CONNECTION_TAG, JDBCConnectionAssembler.assemble(getDatabaseServer(), getDatabasePort(), getSource()));
		} else {
			enrichElement(JDBC_CONNECTION_TAG, JDBCConnectionAssembler.assemble(env.getDbConnection(), getDatabaseServer()));
		}

		enrichElement(JDBC_USER_TAG, getJdbcUser());
		enrichElement(JDBC_DRIVER_XML_TAG, getJdbcDriver());
		enrichElement(PASSWORD_TAG, getPassword());


		if (msbHeaderMappingVersion != null)
			enrichElement(MSB_HEADER_XML_TAG, HEADER_VERSION, getMsbHeaderMappingVersion());

		String targetQueue = "";
		String faultQueue = "";
		if (!targets.isEmpty()) {
			targetQueue = QueueNameAssembler.assembleFromMipQueues(targets);
			faultQueue = QueueNameAssembler.assembleFromMipFaultQueues(targets);
		}
		enrichElement(QUEUE_NAME_TAG, targetQueue);

		if (sleepRecoverableError != null) enrichElement(SLEEP_RECOVERABLE_ERROR_XML_TAG, getSleepRecoverableError());
		else enrichElement(SLEEP_RECOVERABLE_ERROR_XML_TAG, sleepRecoverableErrorDefault);

		if (pollInterval != null) enrichElement(POLL_INTERVAL_XML_TAG, getPollInterval());
		else enrichElement(POLL_INTERVAL_XML_TAG, pollIntervalDefault);

		if (interfaceTable != null) enrichElement(INTERFACE_TABLE_XML_TAG, getInterfaceTable());
		else enrichElement(INTERFACE_TABLE_XML_TAG, interfaceTableDefault);

		enrichElement(FAULT_QUEUE, faultQueue);

	}

	@Override
	public List<String> getQueueNames() {
		List<String> queueNames = new ArrayList<>();
		queueNames.add(QueueNameAssembler.assembleDetailQueue(getEnv().getNodeId(), getTargets().get(0), getSource()));

		return queueNames;
	}

}
