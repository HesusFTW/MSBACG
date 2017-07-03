package net.metrosystems.msb.msbadapter.configuration.data;

import java.util.ArrayList;
import java.util.List;

import net.metrosystems.msb.msbadapter.configuration.generators.GeneratorException;
import net.metrosystems.msb.msbadapter.configuration.template.TemplateManagerException;
import net.metrosystems.msb.msbadapter.configuration.template.data.XMLTemplate;

import org.jdom.Element;

public class FTPGetEvent extends Event {
	
	
    private static final String HOST_TAG = "Host";
    private static final String PORT_TAG = "Port";
    private static final String USER_TAG = "User";
    private static final String PASSWORD_TAG = "Password";
    private static final String ARCHIVEDIRECTORY_XML_TAG = "ArchiveDir";
    private static final String SLEEPRECOVERABLEERROR_XML_TAG = "SleepRecoverableError";
    private static final String POLLINTERVAL_XML_TAG = "PollInterval";
    private static final String STOPONDUPLICATEFILE_XML_TAG = "StopOnDuplicateFile";
    private static final String RETRY_XML_TAG = "Retry";

    private static final String sleepRecoverableErrorDefault = "60000";
    private static final String pollIntervalDefault = "1000";
    private static final String stopOnDuplicateFileDefault = "false";
    private static final String retryDefault = "2";

    private String name;
    private String inputDir;
    private String regexRule;
    private String outputDir;
    private String stagingDir;
    private String host;
    private String port;
    private String user;
    private String password;
    private String archiveDirectory;
    private String sleepRecoverableError;
    private String pollInterval;
    private String stopOnDuplicateFile;
    private String retry;
    
	public FTPGetEvent(Element element, Environment env) {
		this.env = env;
        initFromElement(element);
    }

    public String getName() {
        return name;
    }
    
    public String getInputDir() {
        return inputDir;
    }
    
	public String getRegexRule() {
        return regexRule;
    }
	
	public String getOutputDir() {
        return outputDir;
    }
	
	public String getStagingDir() {
        return stagingDir;
    }
	
	public String getHost() {
        return host;
    }
    
    public String getPort() {
        return port;
    }

    public String getUser() {
		return user;
	}

    public String getPassword() {
        return password;
    }

    public String getArchiveDirectory() {
        return archiveDirectory;
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

    public String getRetry() {
        return retry;
    }

    private void initFromElement(Element e) {
    	
    	server = e.getChild(SERVER_XML_TAG).getValue();
    	name = e.getChild(NAME_XML_TAG).getValue();
    	inputDir = e.getChild(INPUT_DIR_TAG).getValue();
        regexRule = e.getChild(REGEX_TAG).getValue();
        outputDir = e.getChild(OUTPUT_DIR_TAG).getValue();
        stagingDir = e.getChild(STAGING_DIR_TAG).getValue();
        host = e.getChild(HOST_TAG).getValue();
        port = e.getChild(PORT_TAG).getValue();
        user = e.getChild(USER_TAG).getValue();
        password = e.getChild(PASSWORD_TAG).getValue();

        if (e.getChild(ARCHIVEDIRECTORY_XML_TAG) != null) archiveDirectory = e.getChild(ARCHIVEDIRECTORY_XML_TAG).getValue();
        if (e.getChild(SLEEPRECOVERABLEERROR_XML_TAG) != null) sleepRecoverableError = e.getChild(SLEEPRECOVERABLEERROR_XML_TAG).getValue();
        if (e.getChild(POLLINTERVAL_XML_TAG) != null) pollInterval = e.getChild(POLLINTERVAL_XML_TAG).getValue();
        if (e.getChild(STOPONDUPLICATEFILE_XML_TAG) != null) stopOnDuplicateFile = e.getChild(STOPONDUPLICATEFILE_XML_TAG).getValue();
        if (e.getChild(RETRY_XML_TAG) != null) retry = e.getChild(RETRY_XML_TAG).getValue();
        
	}

    @Override
    public String toString() {
        return getEnv() + " " + getServer() + " "
                + getOutputDir() + " " + regexRule + " "
                + getHost() + " " + getInputDir() + " " + port
                + " " + user;
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
        	
            template = TEMPLATE_MANAGER.getFTPGetEventTemplate();
        } catch (TemplateManagerException e) {
            throw new GeneratorException(e);
        }
        return template;
    }

    private void enrichTemplate() throws GeneratorException {

        enrichElement(NAME_TAG, getName());
        enrichElement(INPUT_DIR_TAG, getInputDir());
        enrichElement(REGEX_TAG, getRegexRule());
        enrichElement(OUTPUT_DIR_TAG, getOutputDir());
        enrichElement(STAGING_DIR_TAG, getStagingDir());
        enrichElement(HOST_TAG, getHost());
        enrichElement(PORT_TAG, getPort());
        enrichElement(USER_TAG, getUser());
        enrichElement(PASSWORD_TAG, getPassword());

        if (archiveDirectory != null) enrichElement(ARCHIVEDIRECTORY_XML_TAG, getArchiveDirectory());
        else detachElement(ARCHIVEDIRECTORY_XML_TAG);

        if (sleepRecoverableError != null) enrichElement(SLEEPRECOVERABLEERROR_XML_TAG, getSleepRecoverableError());
        else enrichElement(SLEEPRECOVERABLEERROR_XML_TAG, sleepRecoverableErrorDefault);

        if (pollInterval != null) enrichElement(POLLINTERVAL_XML_TAG, getPollInterval());
        else enrichElement(POLLINTERVAL_XML_TAG, pollIntervalDefault);

        if (stopOnDuplicateFile != null) enrichElement(STOPONDUPLICATEFILE_XML_TAG, getStopOnDuplicateFile());
        else enrichElement(STOPONDUPLICATEFILE_XML_TAG, stopOnDuplicateFileDefault);

        if (retry != null) enrichElement(RETRY_XML_TAG, getRetry());
        else enrichElement(RETRY_XML_TAG, retryDefault);

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FTPGetEvent other = (FTPGetEvent) obj;
        if ((this.regexRule == null) ? (other.regexRule != null)
                : !this.regexRule.equals(other.regexRule)) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.inputDir == null) ? (other.inputDir != null) : !this.inputDir.equals(other.inputDir)) {
            return false;
        }
        if ((this.outputDir == null) ? (other.outputDir != null)
                : !this.outputDir.equals(other.outputDir)) {
            return false;
        }
        if ((this.stagingDir == null) ? (other.stagingDir != null)
                : !this.stagingDir.equals(other.stagingDir)) {
            return false;
        }
        if ((this.host == null) ? (other.host != null)
                : !this.host.equals(other.host)) {
            return false;
        }
        if ((this.port == null) ? (other.port != null)
                : !this.port.equals(other.port)) {
            return false;
        }
        if ((this.user == null) ? (other.user != null)
                : !this.user.equals(other.user)) {
            return false;
        }
        if ((this.password == null) ? (other.password != null)
                : !this.password.equals(other.password)) {
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
                + (this.name != null ? this.name.hashCode() : 0);
        hash = 97 * hash + (this.inputDir != null ? this.inputDir.hashCode() : 0);
        hash = 97 * hash
                + (this.outputDir != null ? this.outputDir.hashCode() : 0);
        hash = 97 * hash
        		+ (this.host != null ? this.host.hashCode() : 0);
        hash = 97 * hash
                + (this.port != null ? this.port.hashCode() : 0);
        hash = 97 * hash
                + (this.user != null ? this.user.hashCode() : 0);
        hash = 97 * hash
                + (this.password != null ? this.password.hashCode() : 0);

        return hash;
    }

    @Override
    public List<String> getQueueNames() {
        List<String> queueNames = new ArrayList<String>();

        return queueNames;
    }

}
