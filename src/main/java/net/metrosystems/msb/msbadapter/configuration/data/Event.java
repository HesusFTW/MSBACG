package net.metrosystems.msb.msbadapter.configuration.data;

import net.metrosystems.msb.msbadapter.configuration.generators.GeneratorException;
import net.metrosystems.msb.msbadapter.configuration.template.SimpleTemplateManager;
import net.metrosystems.msb.msbadapter.configuration.template.TemplateManager;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class which represents the Event data from the InputSource
 *
 * @author georgiana.zota
 */
public abstract class Event {

	protected Element templateRoot;
	protected Environment env;
	protected String server;
	private String name;
	protected static final String SERVER_XML_TAG = "Server";
	protected static final String SOURCE_XML_TAG = "Source";
	protected static final String TARGETS_XML_TAG = "Targets";
	protected static final String NAME_XML_TAG = "Name";
	protected static final String DATA_SUFFIX = "_DATA";
	protected static final String ARCHIVEDIRECTORY_TAG = "ArchiveDirectory";
	protected static final String QUEUEMANAGER_TAG = "QueueManager";
	protected static final String NAME_TAG = "Name";
	protected static final String MCD_BO_TYPE_TAG = "MCDBOType";
	protected static final String EVENT_FILE_TAG = "EventFile";
	protected static final String OUTPUT_QUEUE_TAG = "OutputQueue";
	protected static final String PRIORITY_TAG = "Priority";
	protected static final String BULKING_TAG = "Bulking";
	protected static final String BOTYPE_TAG = "BOType";
	protected static final String USR_TAG = "USR";
	protected static final String QUEUE_NAME_TAG = "QueueName";
	protected static final String OUTPUT_DIR_TAG = "OutputDir";
	protected static final String STAGING_DIR_TAG = "StagingDirectory";
	protected static final String TARGET_QUEUE_TAG = "TargetQueue";
	protected static final String PROVIDER_URL_TAG = "ProviderURL";
	protected static final String CONNECTION_FACTORY_TAG = "ConnectionFactory";
	protected static final String JDBC_CONNECTION_TAG = "JDBCConnection";
	protected static final String JDBC_USER_TAG = "JDBCUser";
	protected static final String PASSWORD_TAG = "EncryptedPassword";
	protected static final String INPUT_DIR_TAG = "InputDir";
	protected static final String REGEX_TAG = "RegEx";
	protected static final String ENCODING_SOURCE_XML_TAG = "Encoding";
	protected static final String ENCODING_XML_TAG = "FileEncoding";
	protected static final String KEEPFILE = "KeepFile";
	protected static final String KEEP_FILE_LOGGER = "KeepFileLogger";
	protected static final String READSUBFOLDERS = "ReadSubfolders";
	protected static final String MSBHEADER = "MSBHeader";

	protected static final TemplateManager TEMPLATE_MANAGER = SimpleTemplateManager.getInstance();

	public Event() {

	}

	public Event(Element element, Environment env) {
		initFromElement(element, env);
	}

	private void initFromElement(Element e, Environment env) {

		this.env = env;
		name = e.getChild(NAME_XML_TAG).getValue();
		server = e.getChild(SERVER_XML_TAG).getValue();

	}

	public Environment getEnv() {
		return env;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getName() {
		return name;
	}

	@SuppressWarnings("unchecked")
	public List<String> getTargetList(Element e) {
		List<String> queuesNames = new ArrayList<>();
		List<Element> targetElements = e.getChildren();
		for (Element queue : targetElements) {
			queuesNames.add(queue.getValue());
		}
		return queuesNames;
	}

	public abstract Element generateEvent() throws GeneratorException;

	public abstract List<String> getQueueNames();

	protected void enrichElement(String tag, String value) {
		Element el = templateRoot.getChild(tag);
		addContentToElement(el, value);
	}

	protected void enrichElement(String tag, List<Element> values) {
		Element el = templateRoot.getChild(tag);
		addContentToElement(el, values);
	}

	protected void detachElement(String tag) {
		Element el = templateRoot.getChild(tag);
		el.detach();
	}

	protected void addContentToElement(Element el, String value) {
		if (value != null) {
			el.removeContent();
			el.addContent(value);
		} else el.detach();
	}

	private void addContentToElement(Element parent, List<Element> childs) {
		parent.removeContent();
		for (Element child : childs) {
			Element clone = (Element) child.clone();
			parent.addContent(clone);
		}
	}

	protected void enrichElement(String tag, String attributeName, String attributeValue) {
		Element el = templateRoot.getChild(tag);
		el.setAttribute(attributeName, attributeValue);
	}

	protected String setNonMandatoryTagValue(Element el, final String TAG_NAME) {
		String valueToSet;
		if (el.getChild(TAG_NAME) != null) {
			valueToSet = el.getChild(TAG_NAME).getValue();
			if (valueToSet.trim().isEmpty()) {
				valueToSet = null;
			}
		} else {
			valueToSet = null;
		}
		return valueToSet;
	}
}
