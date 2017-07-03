package net.metrosystems.msb.msbadapter.configuration.parsers;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.metrosystems.msb.msbadapter.configuration.data.DBListenerEvent;
import net.metrosystems.msb.msbadapter.configuration.data.Environment;
import net.metrosystems.msb.msbadapter.configuration.data.Event;
import net.metrosystems.msb.msbadapter.configuration.data.FTPPutEvent;
import net.metrosystems.msb.msbadapter.configuration.data.FTPGetEvent;
import net.metrosystems.msb.msbadapter.configuration.data.FileEvent;
import net.metrosystems.msb.msbadapter.configuration.data.QueueDistributorEvent;
import net.metrosystems.msb.msbadapter.configuration.data.QueueEvent;
import net.metrosystems.msb.msbadapter.configuration.log.ErrorCode;
import net.metrosystems.msb.msbadapter.configuration.log.MessageFactory;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class parses the input file
 * 
 * @author georgiana.zota
 * 
 */
/**
 * @author georgiana.zota
 * 
 */
public class InputFileXMLParser implements FileParser {

    private static InputFileXMLParser instance;
    private static final Logger LOGGER = LoggerFactory.getLogger(InputFileXMLParser.class);
    private static final String ENVIRONMENT_XML_TAG = "Environment";
    private static final String EVENTS_XML_TAG = "Events";
    private static final String FILE_EVENT_XML_TAG = "FileEvent";
    private static final String QUEUE_EVENT_XML_TAG = "QueueEvent";
    private static final String QUEUE_DISTRIBUTOR_XML_TAG = "QueueDistributor";
    private static final String DBLISTENER_EVENT_XML_TAG = "DblistenerEvent";
    private static final String FTP_PUT_EVENT_XML_TAG = "FTPPutEvent";
    private static final String FTP_GET_EVENT_XML_TAG = "FTPGetEvent";
    private static final Map<String, Class<?>> DATA_EVENTS;

    static {

        DATA_EVENTS = new HashMap<String, Class<?>>();
        DATA_EVENTS.put(FILE_EVENT_XML_TAG, FileEvent.class);
        DATA_EVENTS.put(QUEUE_EVENT_XML_TAG, QueueEvent.class);
        DATA_EVENTS.put(DBLISTENER_EVENT_XML_TAG, DBListenerEvent.class);
        DATA_EVENTS.put(QUEUE_DISTRIBUTOR_XML_TAG, QueueDistributorEvent.class);
        DATA_EVENTS.put(FTP_PUT_EVENT_XML_TAG, FTPPutEvent.class);
        DATA_EVENTS.put(FTP_GET_EVENT_XML_TAG, FTPGetEvent.class);
    }

    private InputFileXMLParser() {
    }

    public static synchronized InputFileXMLParser getInstance() {
        if (instance == null) {
            instance = new InputFileXMLParser();
        }
        return instance;
    }

    public List<Event> parseFile(String fileName) throws FileParserException,
            JDOMException, RuntimeException {

        Environment env = null;
        List<Event> dataCollector = new ArrayList<Event>();
        File inputFile = new File(fileName);

        try {

            LOGGER.debug("Start to parse given file: " + fileName);

            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(inputFile);
            Element root = doc.getRootElement();

            env = new Environment(root.getChild(ENVIRONMENT_XML_TAG));
            dataCollector = getEvents(root.getChild(EVENTS_XML_TAG), env);

            LOGGER.debug("Closed the file.");

        } catch (IOException e) {
            throw new FileParserException(MessageFactory.getErrorMessage(
                    ErrorCode.FILE_PARSING_ERROR, fileName), e);
        }
        return dataCollector;
    }

    @SuppressWarnings("unchecked")
    private List<Event> getEvents(Element elem, Environment env) {

        List<Event> allEvents = new ArrayList<Event>();
        List<Element> allElements = elem.getChildren();
        for (Element e : allElements) {
            Class<?> clazz = DATA_EVENTS.get(e.getName());
            if (clazz != null) {
                Event currentEvent = (Event) createEventObject(
                        clazz, e, env);
                allEvents.add(currentEvent);
            }
        }
        LOGGER.debug("Saved info for the corresponding events.");

        return allEvents;
    }

    private Event createEventObject(Class<?> clazz, Element element,
            Environment env) {
        Event event = null;
        try {
            Constructor<?> constructor = clazz.getConstructor(Element.class, Environment.class);
            event = (Event) constructor.newInstance(element, env);
        } catch (InstantiationException err) {
            LOGGER.error(MessageFactory.getErrorMessage(
                    ErrorCode.CLASS_INSTANTIATION_ERROR, clazz.getName()),
                    err);
            throw new RuntimeException(err);
        } catch (IllegalAccessException err) {
            LOGGER.error(MessageFactory.getErrorMessage(
                    ErrorCode.ILLEGAL_CLASS_ACCESS, clazz.getName()), err);
            throw new RuntimeException(err);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return event;

    }
}
