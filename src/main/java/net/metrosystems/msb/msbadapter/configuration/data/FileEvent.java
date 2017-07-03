package net.metrosystems.msb.msbadapter.configuration.data;

import net.metrosystems.msb.msbadapter.configuration.generators.GeneratorException;
import net.metrosystems.msb.msbadapter.configuration.generators.assembler.PathAssembler;
import net.metrosystems.msb.msbadapter.configuration.generators.assembler.ProviderURLAssembler;
import net.metrosystems.msb.msbadapter.configuration.generators.assembler.QueueNameAssembler;
import net.metrosystems.msb.msbadapter.configuration.template.TemplateManagerException;
import net.metrosystems.msb.msbadapter.configuration.template.data.XMLTemplate;
import org.jdom.Attribute;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author georgiana.zota
 */
public class FileEvent extends Event {

	private static final String PRIORITY_XML_TAG = "Prio";
	private static final String BULKING_XML_TAG = "Bulking";
	private static final String ARCHIVING_XML_TAG = "Archiving";
	private static final String REGEX_DATA_RXML_TAG = "RegEx";
	private static final String KEEPFILE_XML_TAG = "KeepFile";
	private static final String KEEP_FILE_LOGGER_XML_TAG = "KeepFileLogger";
	private static final String READSUBFOLDERS_XML_TAG = "ReadSubfolders";
	private static final String MSBHEADER_XML_TAG = "MSBHeader";
	private static final String DEFAULT_TRUE = "true";
	private static final String HEADERVERSION_XML_TAG = "headerversion";
	private static final String SOURCEAPP_XML_TAG = "SourceApplication";
	private static final String OPERATION_XML_TAG = "Operation";
	private static final String SERVICE_XML_TAG = "Service";
	private static final String SERVICEVERSION_XML_TAG = "ServiceVersion";
	private static final String COUNTRY_XML_TAG = "Country";
	private static final String SALESLINE_XML_TAG = "Salesline";
	private static final String STORE_XML_TAG = "Store";
	private static final String BLOCKSIZE_XML_TAG = "BlockSize";
	private static final String BINARY_XML_TAG = "Binary";
	private static final String SLEEPRECOVERABLEERROR_XML_TAG = "SleepRecoverableError";
	private static final String POLLINTERVAL_XML_TAG = "PollInterval";
	private static final String FILEENCODING_XML_TAG = "FileEncoding";
	private static final String FILEORDER_XML_TAG = "FileOrder";
	private static final String FILELOCK_XML_TAG = "FileLock";
	private static final String PERSISTENCE_XML_TAG = "Persistence";
	private static final String OKFILE_XML_TAG = "OKFile";
	private static final String CHECKINTERVAL_XML_TAG = "CheckInterval";

	private static final String BLOCKSIZE_DEFAULT = "100000";
	private static final String SLEEPRECOVERABLEERROR_DEFAULT = "60000";
	private static final String POLLINTERVAL_DEFAULT = "1000";

	private String regexRule;
	private String priority;
	private String bulking;
	private String archiving;
	private String source;
	private List<String> targets;
	private String keepFile;
	private String keepFileLogger;
	private String readSubfolders;
	private String msbHeader;
	private String headerVersion;
	private String sourceApp;
	private String operation;
	private String service;
	private String serviceVersion;
	private String country;
	private String salesLine;
	private String store;
	private String blockSize;
	private String binary;
	private String sleepRecoverableError;
	private String pollInterval;
	private String fileEncoding;
	private String fileOrder;
	private String fileLock;
	private String persistence;
	private String okFile;
	private String checkInterval;

	public FileEvent(Element element, Environment env) {
		super(element, env);
		initFromElement(element);
	}

	public String getRegexRule() {
		return regexRule;
	}

	public String getPriority() {
		return priority;
	}

	public String getBulking() {
		return bulking;
	}

	public String getArchiving() {
		return archiving;
	}

	public String getSource() {
		return source;
	}

	public List<String> getTargets() {
		return targets;
	}

	public String getKeepFile() {
		return keepFile;
	}

	public String getKeepFileLogger() {
		return keepFileLogger;
	}

	public String getReadSubfolders() {
		return readSubfolders;
	}

	public String getMsbHeader() {
		return msbHeader;
	}

	public String getHeaderVersion() {
		return headerVersion;
	}

	public String getSourceApp() {
		return sourceApp;
	}

	public String getOperation() {
		return operation;
	}

	public String getService() {
		return service;
	}

	public String getServiceVersion() {
		return serviceVersion;
	}

	public String getCountry() {
		return country;
	}

	public String getSalesLine() {
		return salesLine;
	}

	public String getStore() {
		return store;
	}

	public String getBlockSize() {
		return blockSize;
	}

	public String getBinary() {
		return binary;
	}

	public String getSleepRecoverableError() {
		return sleepRecoverableError;
	}

	public String getPollInterval() {
		return pollInterval;
	}

	public String getFileEncoding() {
		return fileEncoding;
	}

	public String getFileOrder() {
		return fileOrder;
	}

	public String getFileLock() {
		return fileLock;
	}

	public String getPersistence() {
		return persistence;
	}

	public String getOkFile() {
		return okFile;
	}

	public String getCheckInterval() {
		return checkInterval;
	}

	private void initFromElement(Element e) {
		source = e.getChild(SOURCE_XML_TAG).getValue();
		targets = getTargetList(e.getChild(TARGETS_XML_TAG));
		regexRule = e.getChild(REGEX_DATA_RXML_TAG).getValue();
		priority = e.getChild(PRIORITY_XML_TAG).getValue();
		bulking = e.getChild(BULKING_XML_TAG).getValue();
		archiving = e.getChild(ARCHIVING_XML_TAG).getValue();
		keepFile = e.getChild(KEEPFILE_XML_TAG).getValue();
		readSubfolders = e.getChild(READSUBFOLDERS_XML_TAG).getValue();
		msbHeader = e.getChild(MSBHEADER_XML_TAG).getValue();
		if (DEFAULT_TRUE.equals(msbHeader)) {
			headerVersion = e.getChild(HEADERVERSION_XML_TAG).getValue();
			sourceApp = e.getChild(SOURCEAPP_XML_TAG).getValue();
			operation = e.getChild(OPERATION_XML_TAG).getValue();
			service = e.getChild(SERVICE_XML_TAG).getValue();
			serviceVersion = e.getChild(SERVICEVERSION_XML_TAG).getValue();
			country = e.getChild(COUNTRY_XML_TAG).getValue();
			salesLine = e.getChild(SALESLINE_XML_TAG).getValue();
			store = e.getChild(STORE_XML_TAG).getValue();
		}
		if (e.getChild(BLOCKSIZE_XML_TAG) != null) blockSize = e.getChild(BLOCKSIZE_XML_TAG).getValue();
		if (e.getChild(BINARY_XML_TAG) != null) binary = e.getChild(BINARY_XML_TAG).getValue();
		if (e.getChild(SLEEPRECOVERABLEERROR_XML_TAG) != null)
			sleepRecoverableError = e.getChild(SLEEPRECOVERABLEERROR_XML_TAG).getValue();
		if (e.getChild(POLLINTERVAL_XML_TAG) != null) pollInterval = e.getChild(POLLINTERVAL_XML_TAG).getValue();
		if (e.getChild(FILEENCODING_XML_TAG) != null) fileEncoding = e.getChild(FILEENCODING_XML_TAG).getValue();
		if (e.getChild(FILEORDER_XML_TAG) != null) fileOrder = e.getChild(FILEORDER_XML_TAG).getValue();
		if (e.getChild(FILELOCK_XML_TAG) != null) fileLock = e.getChild(FILELOCK_XML_TAG).getValue();
		if (e.getChild(PERSISTENCE_XML_TAG) != null) persistence = e.getChild(PERSISTENCE_XML_TAG).getValue();
		if (e.getChild(OKFILE_XML_TAG) != null) okFile = e.getChild(OKFILE_XML_TAG).getValue();
		if (e.getChild(CHECKINTERVAL_XML_TAG) != null) checkInterval = e.getChild(CHECKINTERVAL_XML_TAG).getValue();
		if (e.getChild(KEEP_FILE_LOGGER_XML_TAG) != null)
			keepFileLogger = e.getChild(KEEP_FILE_LOGGER_XML_TAG).getValue();
	}

	@Override
	public String toString() {
		return getEnv() + " " + getServer() + " "
				+ getSource() + " " + regexRule + " "
				+ getTargets().toString() + priority + " " + bulking
				+ " " + archiving;
	}

	public Element generateEvent() throws GeneratorException {
		XMLTemplate template = getTemplateFromTemplateManager();
		templateRoot = template.getContent();
		enrichTemplate();

		return templateRoot;
	}

	private XMLTemplate getTemplateFromTemplateManager()
			throws GeneratorException {
		XMLTemplate template = null;
		try {
			if (bulking.equalsIgnoreCase("true")) {
				template = TEMPLATE_MANAGER.getBulkingFileEventTemplate();
			} else {
				template = TEMPLATE_MANAGER.getFileEventTemplate();
			}
		} catch (TemplateManagerException e) {
			throw new GeneratorException(e);
		}
		return template;
	}

	private void enrichTemplate() throws GeneratorException {

		enrichElement(NAME_TAG, getName());
		enrichElement(PROVIDER_URL_TAG, ProviderURLAssembler.assemble(getEnv().getHostname(), getEnv().getPort()));
		enrichElement(CONNECTION_FACTORY_TAG, getEnv().getQueueManager());
		enrichElement(QUEUE_NAME_TAG, QueueNameAssembler.assembleMultiQueue(getEnv().getNodeId(), getTargets()));
		enrichElement(INPUT_DIR_TAG, getSource());
		enrichElement(REGEX_TAG, getRegexRule());
		enrichElement(PRIORITY_TAG, getPriority());
		enrichElement(KEEPFILE, getKeepFile());
		enrichElement(READSUBFOLDERS, getReadSubfolders());

		if (DEFAULT_TRUE.equals(archiving)) {
			enrichElement(ARCHIVEDIRECTORY_TAG, PathAssembler.assembleArchiveDirectoryPath(getSource()));
		} else {
			detachElement(ARCHIVEDIRECTORY_TAG);
		}

		if (blockSize != null) enrichElement(BLOCKSIZE_XML_TAG, getBlockSize());
		else enrichElement(BLOCKSIZE_XML_TAG, BLOCKSIZE_DEFAULT);

		if (binary != null) enrichElement(BINARY_XML_TAG, getBinary());
		else detachElement(BINARY_XML_TAG);

		if (sleepRecoverableError != null) enrichElement(SLEEPRECOVERABLEERROR_XML_TAG, getSleepRecoverableError());
		else enrichElement(SLEEPRECOVERABLEERROR_XML_TAG, SLEEPRECOVERABLEERROR_DEFAULT);

		if (pollInterval != null) enrichElement(POLLINTERVAL_XML_TAG, getPollInterval());
		else enrichElement(POLLINTERVAL_XML_TAG, POLLINTERVAL_DEFAULT);

		if (fileEncoding != null) enrichElement(FILEENCODING_XML_TAG, getFileEncoding());
		else detachElement(FILEENCODING_XML_TAG);

		if (fileOrder != null) enrichElement(FILEORDER_XML_TAG, getFileOrder());
		else detachElement(FILEORDER_XML_TAG);

		if (fileLock != null) enrichElement(FILELOCK_XML_TAG, getFileLock());
		else detachElement(FILELOCK_XML_TAG);

		if (persistence != null) enrichElement(PERSISTENCE_XML_TAG, getPersistence());
		else detachElement(PERSISTENCE_XML_TAG);

		if (okFile != null) enrichElement(OKFILE_XML_TAG, getOkFile());
		else detachElement(OKFILE_XML_TAG);

		if (checkInterval != null) enrichElement(CHECKINTERVAL_XML_TAG, getCheckInterval());
		else detachElement(CHECKINTERVAL_XML_TAG);

		if (keepFileLogger != null) enrichElement(KEEP_FILE_LOGGER_XML_TAG, getKeepFileLogger());
		else detachElement(KEEP_FILE_LOGGER_XML_TAG);

		if (DEFAULT_TRUE.equals(msbHeader)) {
			enrichAttributeInMSBHeader(HEADERVERSION_XML_TAG, getHeaderVersion());
			enrichElementInMSBHeader(SOURCEAPP_XML_TAG, getSourceApp());
			enrichElementInMSBHeader(OPERATION_XML_TAG, getOperation());
			enrichElementInMSBHeader(SERVICE_XML_TAG, getService());
			enrichElementInMSBHeader(SERVICEVERSION_XML_TAG, getServiceVersion());
			enrichElementInMSBHeader(COUNTRY_XML_TAG, getCountry());
			enrichElementInMSBHeader(SALESLINE_XML_TAG, getSalesLine());
			enrichElementInMSBHeader(STORE_XML_TAG, getStore());
		} else {
			detachElement(MSBHEADER_XML_TAG);
		}
	}

	private void enrichAttributeInMSBHeader(String element, String value) {

		Element msbHeaderElement = templateRoot.getChild(MSBHEADER_XML_TAG);
		Attribute el = msbHeaderElement.getAttribute(element);
		if (value != null) {
			el.setValue(value);
		} else el.detach();
	}


	private void enrichElementInMSBHeader(String element, String value) {
		Element msbHeaderElement = templateRoot.getChild(MSBHEADER_XML_TAG);
		Element el = msbHeaderElement.getChild(element);
		if (value != null) {
			el.removeContent();
			el.addContent(value);
		} else el.detach();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final FileEvent other = (FileEvent) obj;
		if ((this.regexRule == null) ? (other.regexRule != null)
				: !this.regexRule.equals(other.regexRule)) {
			return false;
		}
		if ((this.priority == null) ? (other.priority != null) : !this.priority.equals(other.priority)) {
			return false;
		}
		if ((this.bulking == null) ? (other.bulking != null) : !this.bulking.equals(other.bulking)) {
			return false;
		}
		if ((this.archiving == null) ? (other.archiving != null)
				: !this.archiving.equals(other.archiving)) {
			return false;
		}
		if ((this.keepFile == null) ? (other.keepFile != null)
				: !this.keepFile.equals(other.keepFile)) {
			return false;
		}
		if ((this.keepFileLogger == null) ? (other.keepFileLogger != null)
				: !this.keepFileLogger.equals(other.keepFileLogger)) {
			return false;
		}
		if ((this.msbHeader == null) ? (other.msbHeader != null)
				: !this.msbHeader.equals(other.msbHeader)) {
			return false;
		}
		if ((this.headerVersion == null) ? (other.headerVersion != null)
				: !this.headerVersion.equals(other.headerVersion)) {
			return false;
		}
		if ((this.sourceApp == null) ? (other.sourceApp != null)
				: !this.sourceApp.equals(other.sourceApp)) {
			return false;
		}
		if ((this.operation == null) ? (other.operation != null)
				: !this.operation.equals(other.operation)) {
			return false;
		}
		if ((this.service == null) ? (other.service != null)
				: !this.service.equals(other.service)) {
			return false;
		}
		if ((this.serviceVersion == null) ? (other.serviceVersion != null)
				: !this.serviceVersion.equals(other.serviceVersion)) {
			return false;
		}
		if ((this.country == null) ? (other.country != null)
				: !this.country.equals(other.country)) {
			return false;
		}
		if ((this.salesLine == null) ? (other.salesLine != null)
				: !this.salesLine.equals(other.salesLine)) {
			return false;
		}
		if ((this.store == null) ? (other.store != null)
				: !this.store.equals(other.store)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 97 * hash
				+ (this.regexRule != null ? this.regexRule.hashCode() : 0);
		hash = 97 * hash
				+ (this.priority != null ? this.priority.hashCode() : 0);
		hash = 97 * hash + (this.bulking != null ? this.bulking.hashCode() : 0);
		hash = 97 * hash
				+ (this.archiving != null ? this.archiving.hashCode() : 0);
		hash = 97 * hash
				+ (this.keepFile != null ? this.keepFile.hashCode() : 0);
		hash = 97 * hash
				+ (this.keepFileLogger != null ? this.keepFileLogger.hashCode() : 0);
		hash = 97 * hash
				+ (this.msbHeader != null ? this.msbHeader.hashCode() : 0);
		hash = 97 * hash
				+ (this.headerVersion != null ? this.headerVersion.hashCode() : 0);
		hash = 97 * hash
				+ (this.sourceApp != null ? this.sourceApp.hashCode() : 0);
		hash = 97 * hash
				+ (this.operation != null ? this.operation.hashCode() : 0);
		hash = 97 * hash
				+ (this.service != null ? this.service.hashCode() : 0);
		hash = 97 * hash
				+ (this.serviceVersion != null ? this.serviceVersion.hashCode() : 0);
		hash = 97 * hash
				+ (this.country != null ? this.country.hashCode() : 0);
		hash = 97 * hash
				+ (this.salesLine != null ? this.salesLine.hashCode() : 0);
		hash = 97 * hash
				+ (this.store != null ? this.store.hashCode() : 0);
		return hash;
	}

	@Override
	public List<String> getQueueNames() {
		List<String> queueNames = new ArrayList<String>();
		Iterator<String> iterator = getTargets().iterator();
		while (iterator.hasNext()) {
			queueNames.add(QueueNameAssembler.assembleShortQueue(
					getEnv().getNodeId(), iterator.next()));
		}

		return queueNames;
	}
}
