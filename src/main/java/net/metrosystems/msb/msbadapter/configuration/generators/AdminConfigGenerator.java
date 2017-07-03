package net.metrosystems.msb.msbadapter.configuration.generators;

import java.util.List;


import net.metrosystems.msb.msbadapter.configuration.data.Event;
import net.metrosystems.msb.msbadapter.configuration.log.ErrorCode;
import net.metrosystems.msb.msbadapter.configuration.log.MessageFactory;
import net.metrosystems.msb.msbadapter.configuration.parsers.FileParserException;
import net.metrosystems.msb.msbadapter.configuration.render.XMLFileRenderer;
import net.metrosystems.msb.msbadapter.configuration.render.XMLFileRendererImpl;
import net.metrosystems.msb.msbadapter.configuration.template.SimpleTemplateManager;
import net.metrosystems.msb.msbadapter.configuration.template.TemplateManager;
import net.metrosystems.msb.msbadapter.configuration.template.TemplateManagerException;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Class generates the adminconfig.xml
 * 
 * @author benjamin.stein
 * 
 */
public class AdminConfigGenerator extends AbstractConfigGenerator {

    private static AdminConfigGenerator instance;
    private Logger logger;
    private XMLFileRenderer renderer;
    private TemplateManager templateManager;

    private AdminConfigGenerator() {
        logger = LoggerFactory.getLogger(EventConfigGenerator.class);
        renderer = XMLFileRendererImpl.getInstance();
        templateManager = SimpleTemplateManager.getInstance();
    }

    public static synchronized AdminConfigGenerator getInstance() {
        if (instance == null) {
            instance = new AdminConfigGenerator();
        }
        return instance;
    }

    public void generateConfiguration(String inputFilePath,
            String outputDirectoryPath) throws GeneratorException {

        try {
            createNewConfigForServersMap();
            generateAdminConfiguration(inputFilePath);
            renderer.renderAdminConfigs(outputDirectoryPath, configForServers);
        } catch (Exception e) {
            throw new GeneratorException(
                    MessageFactory.getErrorMessage(ErrorCode.ADMIN_CONFIG_GENERATION_FAILED),
                    e);
        }

    }

    /**
     * Creates a map contianing admin configurations for all the Servers
     * 
     * @param inputFilePath
     * @throws GeneratorException
     * @throws FileParserException
     * @throws TemplateManagerException
     */
    private void generateAdminConfiguration(String inputFilePath)
            throws GeneratorException, FileParserException,
            TemplateManagerException {
        logger.debug("Creating event configurations");
        Element adminconfig = assembleAdminConfig();

        List<Event> events;
        try {
            events = getEventsFromParser(inputFilePath);
        } catch (JDOMException ex) {
            throw new GeneratorException(ex);
        }
        for (Event event : events) {
            addAdminConfigToServers(event, adminconfig);
        }
        logger.debug("Event creation finished.");
    }

    /**
     * Assembles a new admin config
     * @return
     * @throws TemplateManagerException
     */
    private Element assembleAdminConfig() throws TemplateManagerException {
        return templateManager.getHousekeepingTemplate().getContent();
    }

    /**
     * Puts all admin configs together
     * 
     * @param route
     * @param adminconfig
     */
    private void addAdminConfigToServers(Event event, Element adminconfig) {
        String server = event.getServer();
        if (configForServers.get(server) == null) {
            addElementToConfigForServers((Element) adminconfig.clone(), server);
        }
    }
}
