package net.metrosystems.msb.msbadapter.configuration.template;

import net.metrosystems.msb.msbadapter.configuration.template.data.TextTemplate;
import net.metrosystems.msb.msbadapter.configuration.template.data.XMLTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * This class enables access to the default Templates under
 * <i>/src/main/resources</i>.
 *
 * @author benjamin.stein
 */
public class SimpleTemplateManager implements TemplateManager {
	private static SimpleTemplateManager instance;
	private static final String HOUSEKEEPING_TEMPLATE_PATH = "/templates/configs/housekeeping.xml";
	private static final String FILE_EVENT_TEMPLATE_PATH = "/templates/events/file_event.xml";
	private static final String BULKING_FILE_EVENT_TEMPLATE_PATH = "/templates/events/bulking_file_event.xml";
	private static final String QUEUE_EVENT_TEMPLATE_PATH = "/templates/events/queue_event.xml";
	private static final String QUEUE_DISTRIBUTOR_EVENT_TEMPLATE_PATH = "/templates/events/queue_distributor_event.xml";
	private static final String DBLISTENER_EVENT_TEMPLATE_PATH = "/templates/events/dblistener_event.xml";
	private static final String DBLISTENER_EVENT_MCES_TEMPLATE_PATH = "/templates/events/dblistener_mces_event.xml";
	private static final String QUEUE_TO_INTERFACE_EVENT_TEMPLATE_PATH = "/templates/events/queue_to_interface_table_event.xml";
	private static final String CREATE_CHANNEL_TEMPLATE_PATH = "/templates/mq/createChannel.mqsc";
	private static final String CREATE_QUEUE_TEMPLATE_PATH = "/templates/mq/createQueue.mqsc";
	private static final String FTP_PUT_EVENT_TEMPLATE_PATH = "/templates/events/ftp_put_event.xml";
	private static final String FTP_GET_EVENT_TEMPLATE_PATH = "/templates/events/ftp_get_event.xml";
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SimpleTemplateManager.class);

	private SimpleTemplateManager() {

	}

	public static synchronized SimpleTemplateManager getInstance() {
		if (instance == null) {
			instance = new SimpleTemplateManager();
		}
		return instance;
	}

	public XMLTemplate getFileEventTemplate() throws TemplateManagerException {
		LOGGER.debug("getting FileEvent Template");
		return new XMLTemplate(getTemplateURL(FILE_EVENT_TEMPLATE_PATH));
	}

	public XMLTemplate getBulkingFileEventTemplate() throws TemplateManagerException {
		LOGGER.debug("getting BulkingFileEvent Template");
		return new XMLTemplate(getTemplateURL(BULKING_FILE_EVENT_TEMPLATE_PATH));
	}

	public XMLTemplate getQueueEventTemplate() throws TemplateManagerException {
		LOGGER.debug("getting QueueEvent Template");
		return new XMLTemplate(getTemplateURL(QUEUE_EVENT_TEMPLATE_PATH));
	}

	public XMLTemplate getQueueDistributorEventTemplate() throws TemplateManagerException {
		LOGGER.debug("getting QueueDistributorEvent Template");
		return new XMLTemplate(getTemplateURL(QUEUE_DISTRIBUTOR_EVENT_TEMPLATE_PATH));
	}

	public XMLTemplate getDBListenerEventTemplate() throws TemplateManagerException {
		LOGGER.debug("getting DBListenerEvent Template");
		return new XMLTemplate(getTemplateURL(DBLISTENER_EVENT_TEMPLATE_PATH));
	}

	public XMLTemplate getDBListenerEventMCESTemplate() throws TemplateManagerException {
		LOGGER.debug("getting DBListenerEvent MCES Template");
		return new XMLTemplate(getTemplateURL(DBLISTENER_EVENT_MCES_TEMPLATE_PATH));
	}

	@Override
	public XMLTemplate getQueueToInterfaceEventTemplate() throws TemplateManagerException {
		LOGGER.debug("getting QueueToInterfaceEvent Template");
		return new XMLTemplate(getTemplateURL(QUEUE_TO_INTERFACE_EVENT_TEMPLATE_PATH));
	}

	public XMLTemplate getHousekeepingTemplate()
			throws TemplateManagerException {
		LOGGER.debug("getting Housekeeping Template");
		return new XMLTemplate(getTemplateURL(HOUSEKEEPING_TEMPLATE_PATH));
	}

	public TextTemplate getChannelTemplate() throws TemplateManagerException {
		LOGGER.debug("getting createChannel Template");
		return new TextTemplate(getTemplateURL(CREATE_CHANNEL_TEMPLATE_PATH));
	}

	public TextTemplate getQueueTemplate() throws TemplateManagerException {
		LOGGER.debug("getting createQueue Template");
		return new TextTemplate(getTemplateURL(CREATE_QUEUE_TEMPLATE_PATH));
	}

	/**
	 * Returns the absolute Path from a resource under <i>src/main/resources</i>
	 *
	 * @param relativePath the relative path starting at <i>src/main/resources</i> as
	 *                     root
	 * @return the absolute Path for the file
	 */
	private URL getTemplateURL(String relativePath) {
		return getClass().getResource(relativePath);
	}

	public XMLTemplate getFTPPutEventTemplate() throws TemplateManagerException {
		LOGGER.debug("getting FTPPutEvent Template");
		return new XMLTemplate(getTemplateURL(FTP_PUT_EVENT_TEMPLATE_PATH));
	}

	public XMLTemplate getFTPGetEventTemplate() throws TemplateManagerException {
		LOGGER.debug("getting FTPGetEvent Template");
		return new XMLTemplate(getTemplateURL(FTP_GET_EVENT_TEMPLATE_PATH));
	}
}
