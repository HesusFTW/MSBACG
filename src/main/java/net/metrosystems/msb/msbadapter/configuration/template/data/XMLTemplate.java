package net.metrosystems.msb.msbadapter.configuration.template.data;

import java.net.URL;

import net.metrosystems.msb.msbadapter.configuration.log.ErrorCode;
import net.metrosystems.msb.msbadapter.configuration.log.MessageFactory;
import net.metrosystems.msb.msbadapter.configuration.template.TemplateManagerException;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * Template for XML based files
 * @author benjamin.stein
 *
 */
public class XMLTemplate {
	
	private Element content;
	
	public Element getContent() {
		return content;
	}

	public XMLTemplate(URL url) throws TemplateManagerException {
		parseXMLTemplate(url);
	}
	
	private void parseXMLTemplate(URL url)
			throws TemplateManagerException {
		SAXBuilder builder = new SAXBuilder();
		try {
			content = builder.build(url).detachRootElement();
		} catch (Exception e) {
			throw new TemplateManagerException(MessageFactory.getErrorMessage(
					ErrorCode.FILE_PARSING_ERROR, url.toString()), e);
		}
	}

}
