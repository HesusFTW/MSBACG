package net.metrosystems.msb.msbadapter.configuration.render;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;

import net.metrosystems.msb.msbadapter.configuration.data.ConfigMapItem;
import net.metrosystems.msb.msbadapter.configuration.log.ErrorCode;
import net.metrosystems.msb.msbadapter.configuration.log.MessageFactory;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates XML files out of JDOM trees.
 * 
 * @author benjamin.stein
 * 
 */
public class XMLFileRendererImpl implements XMLFileRenderer {

    private static final String SATTELLITE_NAME_ADDITION = "_";
    private static final String MAS = "S0";
    private static final String CONFIGURATIONS_DIR = "/configurations/";
    private static final String CONF_DIR = "/conf/";
    private static final String EVENTCONFIG_NAME = "eventconfig";
    private static final String ADMINCONFIG_NAME = "adminconfig";
    private static final String XML_SUFFIX = ".xml";
    private static XMLFileRendererImpl instance;
    private static final Logger LOGGER = LoggerFactory.getLogger(XMLFileRendererImpl.class);

    private XMLFileRendererImpl() {
    }

    public static synchronized XMLFileRendererImpl getInstance() {
        if (instance == null) {
            instance = new XMLFileRendererImpl();
        }
        return instance;
    }

    public void renderEventConfigs(String outputDirPath,
            Map<String, ConfigMapItem> configurations) throws RendererException {
        LOGGER.debug("Starting Render process of event configurations.");
        for (ConfigMapItem item : configurations.values()) {

            String fileName = assembleEventFileName(outputDirPath,
                    item.getServerName());
            createTargetDirectoryIfNeeded(fileName);
            renderConfigMapItem(item, fileName);
        }
        LOGGER.debug("Render process finished.");
    }

    public void renderAdminConfigs(String outputDirPath,
            Map<String, ConfigMapItem> configurations) throws RendererException {
        LOGGER.debug("Starting Render process of admin configurations.");
        for (ConfigMapItem item : configurations.values()) {

            String fileName = assembleAdminFileName(outputDirPath,
                    item.getServerName());
            createTargetDirectoryIfNeeded(fileName);
            renderConfigMapItem(item, fileName);
        }
        LOGGER.debug("Render process finished.");
    }

    private void renderConfigMapItem(ConfigMapItem item, String fileName)
            throws RendererException {
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(new File(fileName));
            renderFile(item.getDocument(), stream);

        } catch (Exception e) {
            throw new RendererException(e);
        } finally {
            try {
                if (stream != null) stream.close();
            } catch (Exception e) {
                LOGGER.error("Unable to close stream",e);
            }
        }
    }

    private String assembleEventFileName(String outputDirectory, String server) {
        StringBuilder sb = new StringBuilder(outputDirectory);
        if (MAS.equals(server.toUpperCase())) {
            sb.append(CONF_DIR);
            sb.append(EVENTCONFIG_NAME);
            sb.append(XML_SUFFIX);
        } else {
            sb.append(CONFIGURATIONS_DIR);
            sb.append(EVENTCONFIG_NAME);
            sb.append(SATTELLITE_NAME_ADDITION);
            sb.append(server.toLowerCase());
            sb.append(XML_SUFFIX);
        }
        return sb.toString().replace("//", "/");
    }

    private String assembleAdminFileName(String outputDirectory, String server) {
        StringBuilder sb = new StringBuilder(outputDirectory);
        if (MAS.equals(server.toUpperCase())) {
            sb.append(CONF_DIR);
            sb.append(ADMINCONFIG_NAME);
            sb.append(XML_SUFFIX);
        } else {
            sb.append(CONFIGURATIONS_DIR);
            sb.append(ADMINCONFIG_NAME);
            sb.append(SATTELLITE_NAME_ADDITION);
            sb.append(server.toLowerCase());
            sb.append(XML_SUFFIX);
        }
        return sb.toString().replace("//", "/");
    }

    private void renderFile(Document document, OutputStream destination)
            throws RendererException {
        if (document != null) {
            try {
                XMLOutputter outputter = new XMLOutputter(
                        Format.getPrettyFormat());
                outputter.output(document, destination);
            } catch (Exception e) {
                throw new RendererException(
                        MessageFactory.getErrorMessage(ErrorCode.EVENT_RENDERING_FAILED),
                        e);
            }
        }
    }

    private void createTargetDirectoryIfNeeded(String fileName) {
        File dir = new File(fileName.substring(0, fileName.lastIndexOf("/")));
        if (!dir.exists()) {
           if (!dir.mkdirs()) {
               throw new RuntimeException("Unable to create missing target Directories!");
           }
        }
    }
}
