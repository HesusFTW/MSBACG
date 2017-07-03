package net.metrosystems.msb.msbadapter.configuration.generators;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.metrosystems.msb.msbadapter.configuration.data.DBListenerEvent;

import net.metrosystems.msb.msbadapter.configuration.data.Event;
import net.metrosystems.msb.msbadapter.configuration.data.FTPPutEvent;
import net.metrosystems.msb.msbadapter.configuration.data.FileEvent;
import net.metrosystems.msb.msbadapter.configuration.log.ErrorCode;
import net.metrosystems.msb.msbadapter.configuration.log.MessageFactory;
import net.metrosystems.msb.msbadapter.configuration.parsers.FileParserException;
import net.metrosystems.msb.msbadapter.configuration.render.TextFileRenderer;
import net.metrosystems.msb.msbadapter.configuration.render.TextFileRendererImpl;
import net.metrosystems.msb.msbadapter.configuration.template.SimpleTemplateManager;
import net.metrosystems.msb.msbadapter.configuration.template.TemplateManager;
import net.metrosystems.msb.msbadapter.configuration.template.TemplateManagerException;

import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MQSCFileGenerator extends AbstractConfigGenerator {

    private static final String LINE_SEPARATOR = "\n";
    private static MQSCFileGenerator instance;
    private static final String CONF_DIR = "/conf/";
    private static final String MQ_FILE_NAME = "run.mqsc";
    private static final String QUEUE_NAME_TAG = "@QUEUE_NAME@";
    private static final String TRANSFER_DIRECTION = "upload";
    private static final String SPECIAL_QUEUE = "mip";
    private Logger logger;
    private TextFileRenderer renderer;
    private TemplateManager templateManager;
    Set<String> queues;

    private MQSCFileGenerator() {
        logger = LoggerFactory.getLogger(EventConfigGenerator.class);
        renderer = TextFileRendererImpl.getInstance();
        templateManager = SimpleTemplateManager.getInstance();
    }

    public static synchronized MQSCFileGenerator getInstance() {
        if (instance == null) {
            instance = new MQSCFileGenerator();
        }
        return instance;
    }

    public void generateConfiguration(String inputFilePath,
            String outputDirectoryPath) throws GeneratorException {
        createNewQueueSet();
        try {
            String content = assemblingDataToFile(inputFilePath);
            URL url = assembleFileURL(outputDirectoryPath);
            renderer.renderTextFile(url, content);
        } catch (Exception e) {
            throw new GeneratorException(
                    MessageFactory.getErrorMessage(ErrorCode.MQ_FILE_GENERATION_FAILED),
                    e);
        }

    }

    private void createNewQueueSet() {
        queues = new HashSet<String>();
    }

    /**
     * This Method assembles the content of the MQ file : it puts channel and
     * queues all together
     * 
     * @param inputFilePath
     * @throws GeneratorException
     *             , FileParserException, TemplateManagerException
     * @throws JDOMException 
     */
    private String assemblingDataToFile(String inputFilePath)
            throws GeneratorException, FileParserException,
            TemplateManagerException {
        logger.debug("Start assembling data for file!");
        StringBuilder sb = new StringBuilder("");
        sb.append(assembleFileWithChannel());
        sb.append(LINE_SEPARATOR);

        List<Event> events;
        try {
            events = getEventsFromParser(inputFilePath);
        } catch (JDOMException ex) {
            throw new GeneratorException(ex);
        }
        logger.debug("Start appending queues");
        for (Event event : events) {
            sb.append(assembleFileWithQueue(event));

        }

        return sb.toString();

    }

    /**
     * This Method is taking the content of channel template
     * 
     * @throws TemplateManagerException
     */
    private String assembleFileWithChannel() throws TemplateManagerException {
        String channel = templateManager.getChannelTemplate().getContent();
        return channel;
    }

    /**
     * This Method is adding the queues to File content the template content is
     * replaced with proper name of queue
     * 
     * @param route
     * @throws TemplateManagerException
     */
    private String assembleFileWithQueue(Event event)
            throws TemplateManagerException {
        String template = "";
        if (isQueueNeededInScript(event)) {
            template = createQueueScript(event);
        }
        return template;
    }

    private String createQueueScript(Event event)
            throws TemplateManagerException {
        StringBuilder scriptBuilder = new StringBuilder();
        for (String queue : event.getQueueNames()) {
            if (!queues.contains(queue) && !queue.endsWith(TRANSFER_DIRECTION) && !queue.startsWith(SPECIAL_QUEUE)) {
                queues.add(queue);
                logger.debug("Added queueName " + queue + "in Set ");
                scriptBuilder.append(assembleQueueDefinition(queue));
            }
        }

        return scriptBuilder.toString();
    }

    /**
     * This method is checking if the corresponding route needs queue to be
     * created
     * 
     * @param route
     * @param table
     * @return
     */
    private boolean isQueueNeededInScript(Event event) {
        boolean queueNeeded = true;
        if (event instanceof DBListenerEvent || event instanceof FTPPutEvent) {
            queueNeeded = false;
        }
        return queueNeeded;
    }

    /**
     * This Method is replacing the value from template with the proper name of
     * the queue.
     * 
     * @param route
     * @return
     * @throws TemplateManagerException
     */
    private String assembleQueueDefinition(String queueName)
            throws TemplateManagerException {
        String queueDefinition = templateManager.getQueueTemplate().getContent();
        queueDefinition = queueDefinition.replaceAll(QUEUE_NAME_TAG, queueName)
                + LINE_SEPARATOR;
        logger.debug("Created the proper queue definition command! "
                + queueDefinition);
        return queueDefinition;

    }

    private URL assembleFileURL(String path) throws GeneratorException {
        path = path + CONF_DIR + MQ_FILE_NAME;
        path = path.replace("//", "/");

        URL url = null;
        try {
            File file = new File(path);
            url = file.toURI().toURL();
        } catch (Exception e) {
            throw new GeneratorException(e);
        }
        return url;
    }
}
