package net.metrosystems.msb.msbadapter.configuration.data;

import net.metrosystems.msb.msbadapter.configuration.generators.GeneratorException;
import net.metrosystems.msb.msbadapter.configuration.generators.assembler.PathAssembler;
import net.metrosystems.msb.msbadapter.configuration.generators.assembler.ProviderURLAssembler;
import net.metrosystems.msb.msbadapter.configuration.generators.assembler.QueueNameAssembler;
import net.metrosystems.msb.msbadapter.configuration.template.TemplateManagerException;
import net.metrosystems.msb.msbadapter.configuration.template.data.XMLTemplate;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author georgiana.zota
 */
public class QueueEvent extends Event {

	private static final String SLEEPRECOVERABLEERROR_XML_TAG = "SleepRecoverableError";
	private static final String POLLINTERVAL_XML_TAG = "PollInterval";
	private static final String STOPONDUPLICATEFILE_XML_TAG = "StopOnDuplicateFile";
	private static final String WRITER_TAG = "Writer";
	private static final String MOVE_WHEN_SIZE_TAG = "MoveWhenSize";
	private static final String MOVE_WHEN_TIME_TAG = "MoveWhenTime";
	private static final String HEADER_FILE_LOCATION_TAG = "HeaderFileLocation";
	private static final String FOOTER_FILE_LOCATION_TAG = "FooterFileLocation";
	private static final String FAULT_QUEUE = "FaultQueue";

	private static final String sleepRecoverableErrorDefault = "60000";
	private static final String pollIntervalDefault = "1000";
	private static final String WRITER_DEFAULT = "DefaultWriter";
	private static final String WRITER_STAGING = "StagingWriter";
	private static final String WRITER_HEADER_FOOTER = "HeaderFooterWriter";
	private static final String WRITER_MD5 = "MD5FileWriter";
	private static final String WRITER_REAL_EMERGENCY = "RealEmergencyWriter";

	private String source;
	private String encoding;
	private List<String> targets;
	private String sleepRecoverableError;
	private String pollInterval;
	private String stopOnDuplicateFile;
	private String writer;
	private String staging;
	private String headerFileLocation;
	private String footerFileLocation;
	private String moveWhenSize;
	private String moveWhenTime;
	private String faultQueue;
	private String queueName;

	public QueueEvent(Element element, Environment env) {
		super(element, env);
		initFromElement(element);
	}

	private void initFromElement(Element el) {
		source = el.getChild(SOURCE_XML_TAG).getValue();
		targets = getTargetList(el.getChild(TARGETS_XML_TAG));
		if (el.getChild(ENCODING_SOURCE_XML_TAG) != null) {
			encoding = el.getChild(ENCODING_SOURCE_XML_TAG).getValue();
		} else {
			encoding = null;
		}

		if (el.getChild(SLEEPRECOVERABLEERROR_XML_TAG) != null) {
			sleepRecoverableError = el.getChild(SLEEPRECOVERABLEERROR_XML_TAG).getValue();
		}
		if (el.getChild(POLLINTERVAL_XML_TAG) != null) {
			pollInterval = el.getChild(POLLINTERVAL_XML_TAG).getValue();
		}
		if (el.getChild(STOPONDUPLICATEFILE_XML_TAG) != null) {
			stopOnDuplicateFile = el.getChild(STOPONDUPLICATEFILE_XML_TAG).getValue();
		}
		if (el.getChild(WRITER_TAG) != null) {
			writer = el.getChild(WRITER_TAG).getValue();
		} else {
			writer = WRITER_STAGING;
		}

		staging = setNonMandatoryTagValue(el, STAGING_DIR_TAG);
		headerFileLocation = setNonMandatoryTagValue(el, HEADER_FILE_LOCATION_TAG);
		footerFileLocation = setNonMandatoryTagValue(el, FOOTER_FILE_LOCATION_TAG);
		moveWhenSize = setNonMandatoryTagValue(el, MOVE_WHEN_SIZE_TAG);
		moveWhenTime = setNonMandatoryTagValue(el, MOVE_WHEN_TIME_TAG);
		faultQueue = setNonMandatoryTagValue(el, FAULT_QUEUE);
		queueName = setNonMandatoryTagValue(el, QUEUE_NAME_TAG);
	}

	public String getSource() {
		return source;
	}

	public String getEncoding() {
		return encoding;
	}

	public List<String> getTargets() {
		return targets;
	}

	public String getSleepRecoverableError() {
		return sleepRecoverableError;
	}

	public String getPollInterval() {
		return pollInterval;
	}

	public String getStopOnDuplicateFile() {
		return stopOnDuplicateFile;
	}

	public String getStaging() {
		return staging;
	}

	public void setStaging(String staging) {
		this.staging = staging;
	}

	public String getWriter() {
		return writer;
	}

	public String getHeaderFileLocation() {
		return headerFileLocation;
	}

	public String getFooterFileLocation() {
		return footerFileLocation;
	}

	public String getMoveWhenSize() {
		return moveWhenSize;
	}

	public String getMoveWhenTime() {
		return moveWhenTime;
	}

	public String getFaultQueue() {
		return faultQueue;
	}

	@Override
	public String toString() {
		return getEnv() + " " + getServer() + " "
				+ getSource();
	}

	public Element generateEvent() throws GeneratorException {
		XMLTemplate template = getTemplateFromTemplateManager();
		templateRoot = template.getContent();
		enrichTemplate();

		return templateRoot;
	}

	private XMLTemplate getTemplateFromTemplateManager() throws GeneratorException {
		XMLTemplate template = null;
		try {
			template = TEMPLATE_MANAGER.getQueueEventTemplate();
		} catch (TemplateManagerException e) {
			throw new GeneratorException(e);
		}
		return template;
	}

	private void enrichTemplate() throws GeneratorException {
		enrichElement(NAME_TAG, getName());
		enrichElement(PROVIDER_URL_TAG, ProviderURLAssembler.assemble(getEnv().getHostname(), getEnv().getPort()));
		enrichElement(CONNECTION_FACTORY_TAG, getEnv().getQueueManager());
		if (queueName != null) {
			enrichElement(QUEUE_NAME_TAG, queueName);
		} else {
			enrichElement(QUEUE_NAME_TAG, QueueNameAssembler.assembleShortQueue(getEnv().getNodeId(), getSource()));
		}

		String target = getTargets().get(0);
		if (!target.endsWith("/")) {
			target += "/";
		}
		enrichElement(OUTPUT_DIR_TAG, target);
		enrichElement(WRITER_TAG, getWriter());
		if (writer.equalsIgnoreCase(WRITER_STAGING) || writer.equalsIgnoreCase(WRITER_HEADER_FOOTER)) {
			if (getStaging() == null) {
				setStaging(PathAssembler.assembleStagingDirectoryPath(target));
			}
		}
		enrichElement(STAGING_DIR_TAG, getStaging());
		enrichElement(ENCODING_XML_TAG, getEncoding());

		if (sleepRecoverableError != null) {
			enrichElement(SLEEPRECOVERABLEERROR_XML_TAG, getSleepRecoverableError());
		} else {
			enrichElement(SLEEPRECOVERABLEERROR_XML_TAG, sleepRecoverableErrorDefault);
		}

		if (pollInterval != null) {
			enrichElement(POLLINTERVAL_XML_TAG, getPollInterval());
		} else {
			enrichElement(POLLINTERVAL_XML_TAG, pollIntervalDefault);
		}

		if (stopOnDuplicateFile != null) {
			enrichElement(STOPONDUPLICATEFILE_XML_TAG, getStopOnDuplicateFile());
		} else {
			detachElement(STOPONDUPLICATEFILE_XML_TAG);
		}

		enrichElement(HEADER_FILE_LOCATION_TAG, getHeaderFileLocation());
		enrichElement(FOOTER_FILE_LOCATION_TAG, getFooterFileLocation());
		enrichElement(MOVE_WHEN_SIZE_TAG, getMoveWhenSize());
		enrichElement(MOVE_WHEN_TIME_TAG, getMoveWhenTime());
		enrichElement(FAULT_QUEUE, getFaultQueue());
	}

	@Override
	public List<String> getQueueNames() {
		List<String> queueNames = new ArrayList<>();
		queueNames.add(QueueNameAssembler.assembleShortQueue(getEnv().getNodeId(), getSource()));
		return queueNames;
	}
}
