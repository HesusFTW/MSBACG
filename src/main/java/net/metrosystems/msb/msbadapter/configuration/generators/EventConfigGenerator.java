package net.metrosystems.msb.msbadapter.configuration.generators;

import java.util.List;


import net.metrosystems.msb.msbadapter.configuration.data.Event;

import net.metrosystems.msb.msbadapter.configuration.log.ErrorCode;
import net.metrosystems.msb.msbadapter.configuration.log.MessageFactory;
import net.metrosystems.msb.msbadapter.configuration.parsers.FileParserException;
import net.metrosystems.msb.msbadapter.configuration.render.XMLFileRenderer;
import net.metrosystems.msb.msbadapter.configuration.render.XMLFileRendererImpl;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Class generates the eventconfig.xml
 * 
 * @author benjamin.stein
 * 
 */
public class EventConfigGenerator extends AbstractConfigGenerator {

    private static EventConfigGenerator instance;
    private Logger logger;
    private XMLFileRenderer renderer;

    private EventConfigGenerator() {
        logger = LoggerFactory.getLogger(EventConfigGenerator.class);
        renderer = XMLFileRendererImpl.getInstance();
    }

    public static synchronized EventConfigGenerator getInstance() {
        if (instance == null) {
            instance = new EventConfigGenerator();
        }
        return instance;
    }

    public void generateConfiguration(String inputFilePath,
            String outputDirectoryPath) throws GeneratorException {
        try {
            createNewConfigForServersMap();
            generateEvents(inputFilePath);
            renderer.renderEventConfigs(outputDirectoryPath, configForServers);
        } catch (Exception e) {
            throw new GeneratorException(
                    MessageFactory.getErrorMessage(ErrorCode.EVENT_CONFIG_GENERATION_FAILED),
                    e);
        }

    }

    /**
     * Generates all Events for the given InputFile
     * 
     * @param inputFilePath
     * @throws GeneratorException
     * @throws FileParserException
     * @throws JDOMException 
     */
    private void generateEvents(String inputFilePath)
            throws GeneratorException, FileParserException {
        logger.debug("Creating event configurations");
        List<Event> events;
        try {
            events = getEventsFromParser(inputFilePath);
        } catch (JDOMException ex) {
            throw new GeneratorException(ex);
        }
        
        for (Event event : events) {
            Element eventXML = event.generateEvent();
            addElementToConfigForServers(eventXML, event.getServer());
        }
        logger.debug("Event creation finished.");
    }
}
