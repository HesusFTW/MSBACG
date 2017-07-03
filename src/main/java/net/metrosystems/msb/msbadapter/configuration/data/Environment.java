package net.metrosystems.msb.msbadapter.configuration.data;

import org.jdom.Element;

/**
 * Class which represents the information about QueueManager data from the InputSource
 * @author georgiana.zota
 *
 */
public class Environment {

    private static final String HOST_XML_TAG = "Host";
    private static final String NODE_ID_XML_TAG = "NodeId";
    private static final String PORT_XML_TAG = "Port";
    private static final String QUEUE_MANAGER_XML_TAG = "QueueManager";
    
    private String nodeId;
    private String queueManager;
    private String hostname;
    private String port;

    public Environment(Element el) {
        initFromElement(el);
    }

    private void initFromElement(Element el) {
        nodeId = el.getChild(NODE_ID_XML_TAG).getValue();
        queueManager = el.getChild(QUEUE_MANAGER_XML_TAG).getValue();
        hostname = el.getChild(HOST_XML_TAG).getValue();
        port = el.getChild(PORT_XML_TAG).getValue();
    }
    
    public String getQueueManager() {
        return queueManager;
    }

    public String getHostname() {
        return hostname;
    }
    
    public String getPort() {
        return port;
    }

    public String getNodeId() {
        return nodeId;
    }

    @Override
    public String toString() {
        String output = null;
        output = nodeId + ";" + queueManager + ";" + hostname + ";" + port;
        return output;
    }
   
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.nodeId != null ? this.nodeId.hashCode() : 0);
        hash = 97 * hash + (this.queueManager != null ? this.queueManager.hashCode() : 0);
        hash = 97 * hash + (this.hostname != null ? this.hostname.hashCode() : 0);
        hash = 97 * hash + (this.port != null ? this.port.hashCode() : 0);
        return hash;
}

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Environment other = (Environment) obj;
        if ((this.nodeId == null) ? (other.nodeId != null) : !this.nodeId.equals(other.nodeId)) {
            return false;
        }
        if ((this.queueManager == null) ? (other.queueManager != null) : !this.queueManager.equals(other.queueManager)) {
            return false;
        }
        if ((this.hostname == null) ? (other.hostname != null) : !this.hostname.equals(other.hostname)) {
            return false;
        }
        if ((this.port == null) ? (other.port != null) : !this.port.equals(other.port)) {
            return false;
        }
        return true;
    }
}
