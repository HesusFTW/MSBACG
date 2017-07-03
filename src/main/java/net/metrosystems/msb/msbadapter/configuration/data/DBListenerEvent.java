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

public class DBListenerEvent extends Event {

	private static final String JDBC_USER_XML_TAG = "JDBCUser";
	private static final String JDBC_DRIVER_XML_TAG = "JDBCDriver";
	private static final String PLAIN_PASSWORD_XML_TAG = "Password";
	private static final String PASSWORD_XML_TAG = "EncryptedPassword";
	private static final String DATABASE_PORT_XML_TAG = "DatabasePort";
	private static final String DATABASE_SERVER_XML_TAG = "DatabaseServer";
	private static final String DEFAULT_TARGET_QUEUE = "msb.upload";
	private static final String BATCHSIZE_XML_TAG = "BatchSize";
	private static final String ORDER_BY_COLUMN_TAG = "OrderByColumn";
	private static final String SLEEPRECOVERABLEERROR_XML_TAG = "SleepRecoverableError";
	private static final String POLLINTERVAL_XML_TAG = "PollInterval";
	private static final String INTERFACETABLE_XML_TAG = "InterfaceTable";
	private static final String MSBHEADER_XML_TAG = "MSBHeaderMapping";
	private static final String HEADER_VERSION = "headerversion";
	private static final String HEADER_VERSION_TAG = "HeaderVersion";
	private static final String WHERE_TAG = "Where";

	private static final String sleepRecoverableErrorDefault = "60000";
	private static final String pollIntervalDefault = "1000";
	private static final String interfaceTableDefault = "MSB_XML_OUT";


	private String jdbcUser;
	private String password;
	private String databasePort;
	private String databaseServer;
	private String source;
	private List<String> targets;
	private String batchSize;
	private String sleepRecoverableError;
	private String pollInterval;
	private String interfaceTable;
	private String orderByTag;
	private String whereTag;
	private String msbHeaderMappingVersion;
	private String jdbcDriver;

	public DBListenerEvent(Element element, Environment env) {
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

	public String getOrderByTag() {
		return orderByTag;
	}

	public String getWhereTag() {
		return whereTag;
	}

	public String getBatchSize() {
		return batchSize;
	}

	public String getMsbHeaderMappingVersion() {
		return msbHeaderMappingVersion;
	}

	public String getJdbcDriver() {
		return jdbcDriver;
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

		if (e.getChild(BATCHSIZE_XML_TAG) != null) batchSize = e.getChild(BATCHSIZE_XML_TAG).getValue();
		if (e.getChild(SLEEPRECOVERABLEERROR_XML_TAG) != null)
			sleepRecoverableError = e.getChild(SLEEPRECOVERABLEERROR_XML_TAG).getValue();
		if (e.getChild(POLLINTERVAL_XML_TAG) != null) pollInterval = e.getChild(POLLINTERVAL_XML_TAG).getValue();
		if (e.getChild(INTERFACETABLE_XML_TAG) != null) interfaceTable = e.getChild(INTERFACETABLE_XML_TAG).getValue();
		if (e.getChild(ORDER_BY_COLUMN_TAG) != null) orderByTag = e.getChild(ORDER_BY_COLUMN_TAG).getValue();
		if (e.getChild(WHERE_TAG) != null) whereTag = e.getChild(WHERE_TAG).getValue();
	}

	@Override
	public String toString() {
		return getEnv() + " " + getServer() + " "
				+ getSource() + " " + getTargets().toString()
				+ " " + jdbcUser + " " + password + " " + databaseServer;
	}

	public Element generateEvent() throws GeneratorException {
		XMLTemplate xmlTemplate = getTemplateFromTemplateManager();

		templateRoot = xmlTemplate.getContent();
		enrichTemplate();

		return templateRoot;
	}

	private XMLTemplate getTemplateFromTemplateManager() throws GeneratorException {
		XMLTemplate xmlTemplate = null;
		try {
//          TODO Remove this MCES bullshit after they get LC tables with version 1.1 like they should have by now
			if (msbHeaderMappingVersion != null && msbHeaderMappingVersion.equalsIgnoreCase("0.1")) {
				xmlTemplate = TEMPLATE_MANAGER.getDBListenerEventMCESTemplate();
			} else {
				xmlTemplate = TEMPLATE_MANAGER.getDBListenerEventTemplate();
			}
		} catch (TemplateManagerException e) {
			throw new GeneratorException(e);
		}
		return xmlTemplate;
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
			enrichElement(MSBHEADER_XML_TAG, HEADER_VERSION, getMsbHeaderMappingVersion());

		String targetQueue = DEFAULT_TARGET_QUEUE;
		if (!targets.isEmpty()) {
			targetQueue = QueueNameAssembler.assembleToMipQueues(targets);
		}
		enrichElement(TARGET_QUEUE_TAG, targetQueue);

		if (batchSize != null) enrichElement(BATCHSIZE_XML_TAG, getBatchSize());
		else detachElement(BATCHSIZE_XML_TAG);

		if (sleepRecoverableError != null) enrichElement(SLEEPRECOVERABLEERROR_XML_TAG, getSleepRecoverableError());
		else enrichElement(SLEEPRECOVERABLEERROR_XML_TAG, sleepRecoverableErrorDefault);

		if (pollInterval != null) enrichElement(POLLINTERVAL_XML_TAG, getPollInterval());
		else enrichElement(POLLINTERVAL_XML_TAG, pollIntervalDefault);

		if (interfaceTable != null) enrichElement(INTERFACETABLE_XML_TAG, getInterfaceTable());
		else enrichElement(INTERFACETABLE_XML_TAG, interfaceTableDefault);

		if (orderByTag != null) enrichElement(ORDER_BY_COLUMN_TAG, getOrderByTag());
		else detachElement(ORDER_BY_COLUMN_TAG);

		if (whereTag != null) enrichElement(WHERE_TAG, getWhereTag());
		else detachElement(WHERE_TAG);
	}

	@Override
	public List<String> getQueueNames() {
		List<String> queueNames = new ArrayList<String>();
		queueNames.add(QueueNameAssembler.assembleDetailQueue(getEnv().getNodeId(), getTargets().get(0), getSource()));

		return queueNames;
	}
}
