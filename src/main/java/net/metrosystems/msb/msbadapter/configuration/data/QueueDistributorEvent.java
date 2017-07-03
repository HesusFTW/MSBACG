/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.metrosystems.msb.msbadapter.configuration.data;

import java.util.ArrayList;
import java.util.List;
import net.metrosystems.msb.msbadapter.configuration.generators.GeneratorException;
import net.metrosystems.msb.msbadapter.configuration.generators.assembler.ProviderURLAssembler;
import net.metrosystems.msb.msbadapter.configuration.generators.assembler.QueueNameAssembler;
import net.metrosystems.msb.msbadapter.configuration.log.ErrorCode;
import net.metrosystems.msb.msbadapter.configuration.log.MessageFactory;
import net.metrosystems.msb.msbadapter.configuration.template.TemplateManagerException;
import net.metrosystems.msb.msbadapter.configuration.template.data.XMLTemplate;

import org.jdom.Attribute;
import org.jdom.Element;

/**
 *
 * @author benjamin.stein
 */
public class QueueDistributorEvent extends Event {

    private static final String SERVICES_XML_TAG = "Services";
    private static final String SERVICE_XML_TAG = "Service";
    private static final String OPERATION_XML_TAG = "Operation";
    private static final String TARGET_XML_TAG = "Target";
    private static final String QUEUENAME_XML_TAG = "queueName";
    private static final String FAULTQUEUE_XML_TAG = "FaultQueue";
    private static final String SLEEPRECOVERABLEERROR_XML_TAG = "SleepRecoverableError";
    private static final String POLLINTERVAL_XML_TAG = "PollInterval";

    private static final String queueNameDefault = "msb.download";
    private static final String faultQueueDefault = "msb.error";
    private static final String sleepRecoverableErrorDefault = "60000";
    private static final String pollIntervalDefault = "1000";

    private String queueName;
    private String faultQueue;
    private String sleepRecoverableError;
    private String pollInterval;
    private List<Element> services;
    List<Element> newServices;

    public QueueDistributorEvent(Element element, Environment env) throws GeneratorException  {
        super(element, env);
        initFromElement(element);
    }

    public String getQueueName() {
        return queueName;
    }

    public String getFaultQueue() {
        return faultQueue;
    }

    public String getSleepRecoverableError() {
        return sleepRecoverableError;
    }

    public String getPollInterval() {
        return pollInterval;
    }

    private void initFromElement(Element e) throws GeneratorException {
        services = e.getChildren(SERVICE_XML_TAG);
        if (services.size()==0 ) throw new GeneratorException("No Services configured!");
        newServices = new ArrayList<Element>();

        if (e.getChild(QUEUENAME_XML_TAG) != null) queueName = e.getChild(QUEUENAME_XML_TAG).getValue();
        if (e.getChild(FAULTQUEUE_XML_TAG) != null) faultQueue = e.getChild(FAULTQUEUE_XML_TAG).getValue();
        if (e.getChild(SLEEPRECOVERABLEERROR_XML_TAG) != null) sleepRecoverableError = e.getChild(SLEEPRECOVERABLEERROR_XML_TAG).getValue();
        if (e.getChild(POLLINTERVAL_XML_TAG) != null) pollInterval = e.getChild(POLLINTERVAL_XML_TAG).getValue();

    }

    @Override
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
            template = TEMPLATE_MANAGER.getQueueDistributorEventTemplate();
        } catch (TemplateManagerException e) {
            throw new GeneratorException(e);
        }
        return template;
    }

    private void enrichTemplate() throws GeneratorException { 
    	changeServiceStructure();
        enrichElement(NAME_TAG, getName());
        enrichElement(PROVIDER_URL_TAG, ProviderURLAssembler.assemble(getEnv().getHostname(), getEnv().getPort()));
        enrichElement(CONNECTION_FACTORY_TAG, getEnv().getQueueManager());
        enrichElement(SERVICES_XML_TAG, newServices);

        if (queueName != null) enrichElement(QUEUENAME_XML_TAG, getQueueName());
        else enrichElement(QUEUENAME_XML_TAG, queueNameDefault);

        if (faultQueue != null) enrichElement(FAULTQUEUE_XML_TAG, getFaultQueue());
        else enrichElement(FAULTQUEUE_XML_TAG, faultQueueDefault);

        if (sleepRecoverableError != null) enrichElement(SLEEPRECOVERABLEERROR_XML_TAG, getSleepRecoverableError());
        else enrichElement(SLEEPRECOVERABLEERROR_XML_TAG, sleepRecoverableErrorDefault);

        if (pollInterval != null) enrichElement(POLLINTERVAL_XML_TAG, getPollInterval());
        else enrichElement(POLLINTERVAL_XML_TAG, pollIntervalDefault);

    }

    private void changeServiceStructure() throws GeneratorException {
    	try{
	        for (Element service : services) {
	        	
	        	Element newService = new Element("Service");
	        	newService.setAttribute("name",service.getAttribute("name").getValue());

	        	List<Element> operations = service.getChildren(OPERATION_XML_TAG);
	        	if (operations.size()==0 ) throw new GeneratorException("No Operations configured!");
	            for (Element operation : operations) {
	            	
	            	Element newOperation = new Element("Operation");
	                if ("default".equals(operation.getAttribute("name").getValue())) {
	                	newOperation.setAttribute("name", operation.getAttribute("name").getValue());
	                	newOperation.setAttribute("default", "true");
	                }else {
	                	newOperation.setAttribute("name", operation.getAttribute("name").getValue());
	                }
	
	                Element targets = operation.getChild(TARGETS_XML_TAG);         	
	            	
	            	StringBuilder qNames = new StringBuilder();
	                for (Element target : (List<Element>) targets.getChildren(TARGET_XML_TAG)) {
	                    qNames.append("#");
	                    qNames.append(QueueNameAssembler.assembleShortQueue(getEnv().getNodeId(), target.getValue()));
	                }
	                
	                Element newTarget = new Element("Target");    
	                newTarget.addContent(qNames.toString().substring(1));
	                newOperation.addContent(newTarget);
	                //operation.removeContent(targets);
	                newService.addContent(newOperation);
	            }
	            newServices.add(newService); 
	            
	        }
    	 } catch (Exception e) {
    		 throw new GeneratorException(MessageFactory.getErrorMessage(ErrorCode.INPUT_PARAMETERS_NOT_FOUND),e);
       	}
    }

    @Override
    public List<String> getQueueNames() {
        List<String> queueNames = new ArrayList<String>();

        for (Element service : services) {
            for (Element operation : (List<Element>) service.getChildren(OPERATION_XML_TAG)) {
                for (Element target : (List<Element>) operation.getChild(TARGETS_XML_TAG).getChildren()) {
                    queueNames.add(QueueNameAssembler.assembleShortQueue(getEnv().getNodeId(), target.getValue()));
                }
            }
        }
        return queueNames;
    }
}
