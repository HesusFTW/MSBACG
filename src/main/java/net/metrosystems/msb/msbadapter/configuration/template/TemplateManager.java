package net.metrosystems.msb.msbadapter.configuration.template;

import net.metrosystems.msb.msbadapter.configuration.template.data.TextTemplate;
import net.metrosystems.msb.msbadapter.configuration.template.data.XMLTemplate;

/**
 * Template Manager provide Event templates as an DOM structure.
 * 
 * @author benjamin.stein
 * 
 */
public interface TemplateManager {

	XMLTemplate getFileEventTemplate() throws TemplateManagerException;
	XMLTemplate getBulkingFileEventTemplate() throws TemplateManagerException;

	XMLTemplate getQueueEventTemplate() throws TemplateManagerException;
    XMLTemplate getQueueDistributorEventTemplate() throws TemplateManagerException;
	XMLTemplate getDBListenerEventTemplate() throws TemplateManagerException;
//	TODO Remove this MCES bullshit after they fully adhere to LC 1.1
	XMLTemplate getDBListenerEventMCESTemplate() throws TemplateManagerException;
	XMLTemplate getQueueToInterfaceEventTemplate() throws TemplateManagerException;

	XMLTemplate getHousekeepingTemplate()
			throws TemplateManagerException;

	TextTemplate getChannelTemplate() throws TemplateManagerException;
	TextTemplate getQueueTemplate() throws TemplateManagerException;
	
	XMLTemplate getFTPPutEventTemplate() throws TemplateManagerException;
	XMLTemplate getFTPGetEventTemplate() throws TemplateManagerException;
}
