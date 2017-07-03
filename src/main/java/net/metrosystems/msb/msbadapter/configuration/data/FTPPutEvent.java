package net.metrosystems.msb.msbadapter.configuration.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.metrosystems.msb.msbadapter.configuration.generators.GeneratorException;
import net.metrosystems.msb.msbadapter.configuration.generators.assembler.QueueNameAssembler;
import net.metrosystems.msb.msbadapter.configuration.template.TemplateManagerException;
import net.metrosystems.msb.msbadapter.configuration.template.data.XMLTemplate;

import org.jdom.Element;

/**
 *
 * @author alexandru.raducan
 */
public class FTPPutEvent extends Event {

    private static final String REGEX_DATA_RXML_TAG = "RegEx";
    private static final String PORT_TAG = "Port";
    private static final String USER_TAG = "User";
    private static final String PRIVATEKEY_TAG = "PrivateKey";
    private static final String PRIVATEKEYPASSWORD_TAG = "PrivateKeyPassword";
    private static final String TARGET_DIR_TAG = "TargetDir";
    private static final String DESTINATION_TAG = "Destination";
    
    private static final String LOCAL_NAME_TAG = "Name";
    private static final String HOST_TAG = "Host";
    private static final String FTP_CONNECTION_TAG = "FTPConnection";
    private static final String ARCHIVEDIRECTORY_XML_TAG = "ArchiveDirectory";
    private static final String SLEEPRECOVERABLEERROR_XML_TAG = "SleepRecoverableError";
    private static final String POLLINTERVAL_XML_TAG = "PollInterval";
    private static final String PASSWORD_XML_TAG = "Password";
    private static final String ENCRYPTEDPASSWORD_XML_TAG = "EncryptedPassword";
    private static final String PRIVATEKEYENCRYPTEDPASSWORD_XML_TAG = "PrivateKeyEncryptedPassword";
    private static final String RETRY_XML_TAG = "Retry";
    private static final String OVERWRITE_XML_TAG = "Overwrite";
    private static final String READSUBFOLDERS_XML_TAG = "ReadSubfolders";

    private static final String sleepRecoverableErrorDefault = "60000";
    private static final String pollIntervalDefault = "1000";
    private static final String retryDefault = "1";
    private static final String overwriteDefault = "false";

    private Element templateFTPConnectionTag;
    private Element templateFTPConnectionTagTemp;

    
    private String regexRule;
    private String name;
    private String ftpConnectionName;
    private String source;
    private String destination;
    private String port;
    private List<String> targets;
    private String target;
    private String user;
    private String privateKey;
    private String privateKeyPassword;
    private String archiveDirectory;
    private String sleepRecoverableError;
    private String pollInterval;
    private String password;
    private String encryptedPassword;
    private String privateKeyEncryptedPassword;
    private String retry;
    private String overwrite;
    private String readSubfolders;
    
	public FTPPutEvent(Element element, Environment env) {
		this.env = env;	
        initFromElement(element);
    }

	public String getRegexRule() {
        return regexRule;
    }

    public String getName() {
        return name;
    }
    
    public String getFTPConnectionName() {
    	return ftpConnectionName;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }
    
    public String getPort() {
        return port;
    }

    public List<String> getTargets() {
        return targets;
    }
    
    public String getTarget() {
    	return target;
    }

    public String getUser() {
		return user;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public String getPrivateKeyPassword() {
		return privateKeyPassword;
	}

    public String getOverwrite() {
        return overwrite;
    }

    public String getRetry() {
        return retry;
    }

    public String getPrivateKeyEncryptedPassword() {
        return privateKeyEncryptedPassword;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public String getPassword() {
        return password;
    }

    public String getPollInterval() {
        return pollInterval;
    }

    public String getSleepRecoverableError() {
        return sleepRecoverableError;
    }

    public String getArchiveDirectory() {
        return archiveDirectory;
    }

    public String getReadSubfolders() {
        return readSubfolders;
    }

    private void initFromElement(Element e) {
    	
    	server = e.getChild(SERVER_XML_TAG).getValue();
    	name = e.getChild(NAME_XML_TAG).getValue();
        source = e.getChild(SOURCE_XML_TAG).getValue();
        regexRule = e.getChild(REGEX_DATA_RXML_TAG).getValue();
        destination = e.getChild(DESTINATION_TAG).getValue();
        port = e.getChild(PORT_TAG).getValue();
        targets = getTargetList(e.getChild(TARGETS_XML_TAG));
        target = targets.get(0);
        user = e.getChild(USER_TAG).getValue();
        ftpConnectionName = name + "-" + "Conn0";

        if (e.getChild(READSUBFOLDERS_XML_TAG) != null) readSubfolders = e.getChild(READSUBFOLDERS_XML_TAG).getValue();
        if (e.getChild(ARCHIVEDIRECTORY_XML_TAG) != null) archiveDirectory = e.getChild(ARCHIVEDIRECTORY_XML_TAG).getValue();
        if (e.getChild(SLEEPRECOVERABLEERROR_XML_TAG) != null) sleepRecoverableError = e.getChild(SLEEPRECOVERABLEERROR_XML_TAG).getValue();
        if (e.getChild(POLLINTERVAL_XML_TAG) != null) pollInterval = e.getChild(POLLINTERVAL_XML_TAG).getValue();
        if (e.getChild(PASSWORD_XML_TAG) != null) password = e.getChild(PASSWORD_XML_TAG).getValue();
        if (e.getChild(ENCRYPTEDPASSWORD_XML_TAG) != null) encryptedPassword = e.getChild(ENCRYPTEDPASSWORD_XML_TAG).getValue();
        if (e.getChild(PRIVATEKEYENCRYPTEDPASSWORD_XML_TAG) != null) privateKeyEncryptedPassword = e.getChild(PRIVATEKEYENCRYPTEDPASSWORD_XML_TAG).getValue();
        if (e.getChild(RETRY_XML_TAG) != null) retry = e.getChild(RETRY_XML_TAG).getValue();
        if (e.getChild(OVERWRITE_XML_TAG) != null) overwrite = e.getChild(OVERWRITE_XML_TAG).getValue();
        if (e.getChild(PRIVATEKEY_TAG) != null) privateKey = e.getChild(PRIVATEKEY_TAG).getValue();
        if (e.getChild(PRIVATEKEYPASSWORD_TAG) != null) privateKeyPassword = e.getChild(PRIVATEKEYPASSWORD_TAG).getValue();
        
	}

        @Override
    public String toString() {
        return getEnv() + " " + getServer() + " "
                + getSource() + " " + regexRule + " "
                + getTargets().toString() + destination + " " + port
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
        	
            template = TEMPLATE_MANAGER.getFTPPutEventTemplate();
            
        } catch (TemplateManagerException e) {
            throw new GeneratorException(e);
        }
        return template;
    }

    private void enrichTemplate() throws GeneratorException {

        enrichElement(NAME_TAG, getName());
        enrichElement(INPUT_DIR_TAG, getSource());
        enrichElement(REGEX_TAG, getRegexRule());

        if (readSubfolders != null) enrichElement(READSUBFOLDERS_XML_TAG, getReadSubfolders());
        else detachElement(READSUBFOLDERS_XML_TAG);

        if (archiveDirectory != null) enrichElement(ARCHIVEDIRECTORY_XML_TAG, getArchiveDirectory());
        else detachElement(ARCHIVEDIRECTORY_XML_TAG);

        if (sleepRecoverableError != null) enrichElement(SLEEPRECOVERABLEERROR_XML_TAG, getSleepRecoverableError());
        else enrichElement(SLEEPRECOVERABLEERROR_XML_TAG, sleepRecoverableErrorDefault);

        if (pollInterval != null) enrichElement(POLLINTERVAL_XML_TAG, getPollInterval());
        else enrichElement(POLLINTERVAL_XML_TAG, pollIntervalDefault);
        
        enrichFTPElement(LOCAL_NAME_TAG, getFTPConnectionName());
        enrichFTPElement(HOST_TAG, getDestination());
        enrichFTPElement(PORT_TAG, getPort());
        enrichFTPElement(USER_TAG, getUser());

        if (password != null) {
            enrichFTPElement(PASSWORD_XML_TAG, getPassword());
            detachFTPElement(ENCRYPTEDPASSWORD_XML_TAG);
            detachFTPElement(PRIVATEKEY_TAG);
            detachFTPElement(PRIVATEKEYPASSWORD_TAG);
            detachFTPElement(PRIVATEKEYENCRYPTEDPASSWORD_XML_TAG);
        }
        else if (encryptedPassword != null) {
            enrichFTPElement(ENCRYPTEDPASSWORD_XML_TAG, getEncryptedPassword());
            detachFTPElement(PASSWORD_XML_TAG);
            detachFTPElement(PRIVATEKEY_TAG);
            detachFTPElement(PRIVATEKEYPASSWORD_TAG);
            detachFTPElement(PRIVATEKEYENCRYPTEDPASSWORD_XML_TAG);
        }
        else if (privateKey != null) {
            enrichFTPElement(PRIVATEKEY_TAG, getPrivateKey());
            detachFTPElement(PASSWORD_XML_TAG);
            detachFTPElement(ENCRYPTEDPASSWORD_XML_TAG);
            if (privateKeyPassword != null) {
                enrichFTPElement(PRIVATEKEYPASSWORD_TAG, getPrivateKeyPassword());
                detachFTPElement(PRIVATEKEYENCRYPTEDPASSWORD_XML_TAG);
            }
            else if (privateKeyEncryptedPassword != null) {
                enrichFTPElement(PRIVATEKEYENCRYPTEDPASSWORD_XML_TAG, getPrivateKeyEncryptedPassword());
                detachFTPElement(PRIVATEKEYPASSWORD_TAG);
            }
            else {
                detachFTPElement(PRIVATEKEYPASSWORD_TAG);
                detachFTPElement(PRIVATEKEYENCRYPTEDPASSWORD_XML_TAG);
            }
        }
        else {
            detachFTPElement(PASSWORD_XML_TAG);
            detachFTPElement(ENCRYPTEDPASSWORD_XML_TAG);
            detachFTPElement(PRIVATEKEY_TAG);
            detachFTPElement(PRIVATEKEYPASSWORD_TAG);
            detachFTPElement(PRIVATEKEYENCRYPTEDPASSWORD_XML_TAG);
        }

        enrichFTPElement(TARGET_DIR_TAG, getTarget());

        if (sleepRecoverableError != null) enrichFTPElement(SLEEPRECOVERABLEERROR_XML_TAG, getSleepRecoverableError());
        else enrichFTPElement(SLEEPRECOVERABLEERROR_XML_TAG, sleepRecoverableErrorDefault);

        if (pollInterval != null) enrichFTPElement(POLLINTERVAL_XML_TAG, getPollInterval());
        else enrichFTPElement(POLLINTERVAL_XML_TAG, pollIntervalDefault);

        if (retry != null) enrichFTPElement(RETRY_XML_TAG, getRetry());
        else enrichFTPElement(RETRY_XML_TAG, retryDefault);

        if (archiveDirectory != null) enrichFTPElement(ARCHIVEDIRECTORY_XML_TAG, getArchiveDirectory());
        else detachFTPElement(ARCHIVEDIRECTORY_XML_TAG);

        if (overwrite != null) enrichFTPElement(OVERWRITE_XML_TAG, getOverwrite());
        else enrichFTPElement(OVERWRITE_XML_TAG, overwriteDefault);
        
        templateFTPConnectionTag = templateRoot.getChild(FTP_CONNECTION_TAG);
        
        for (int i = 1; i< targets.size(); i++) {
        	
        	target = targets.get(i);
        	ftpConnectionName = name + "-" + "Conn" + i;
        	templateFTPConnectionTagTemp = (Element) templateFTPConnectionTag.clone();
        	enrichAdditionalFTPElement(LOCAL_NAME_TAG, ftpConnectionName);
        	enrichAdditionalFTPElement(TARGET_DIR_TAG, target);
        	templateRoot.addContent(templateFTPConnectionTagTemp);
        }
    }
    
    protected void enrichFTPElement(String tag, String value) {
    	Element el = templateRoot.getChild(FTP_CONNECTION_TAG).getChild(tag);
    	addContentToElement(el, value);
}
    
    protected void enrichAdditionalFTPElement(String tag, String value) {
    	Element el = templateFTPConnectionTagTemp.getChild(tag);
    	addContentToElement(el, value);
    }

    protected void detachFTPElement(String tag) {
        Element el = templateRoot.getChild(FTP_CONNECTION_TAG).getChild(tag);
        el.detach();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FTPPutEvent other = (FTPPutEvent) obj;
        if ((this.regexRule == null) ? (other.regexRule != null)
                : !this.regexRule.equals(other.regexRule)) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.source == null) ? (other.source != null) : !this.source.equals(other.source)) {
            return false;
        }
        if ((this.destination == null) ? (other.destination != null)
                : !this.destination.equals(other.destination)) {
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
        if ((this.privateKey == null) ? (other.privateKey != null)
                : !this.privateKey.equals(other.privateKey)) {
            return false;
        }
        if ((this.privateKeyPassword == null) ? (other.privateKeyPassword != null)
                : !this.privateKeyPassword.equals(other.privateKeyPassword)) {
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
        hash = 97 * hash + (this.source != null ? this.source.hashCode() : 0);
        hash = 97 * hash
                + (this.destination != null ? this.destination.hashCode() : 0);
        hash = 97 * hash
                + (this.port != null ? this.port.hashCode() : 0);
        hash = 97 * hash
                + (this.user != null ? this.user.hashCode() : 0);
        hash = 97 * hash
                + (this.privateKey != null ? this.privateKey.hashCode() : 0);
        hash = 97 * hash
                + (this.privateKeyPassword != null ? this.privateKeyPassword.hashCode() : 0);

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
