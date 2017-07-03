package net.metrosystems.msb.msbadapter.configuration.template.data;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import net.metrosystems.msb.msbadapter.configuration.log.ErrorCode;
import net.metrosystems.msb.msbadapter.configuration.log.MessageFactory;
import net.metrosystems.msb.msbadapter.configuration.template.TemplateManagerException;

/**
 * Template for any kind of text based File.
 * 
 * @author benjamin.stein
 * 
 */
public class TextTemplate {
	private String content;

	public String getContent() {
		return content;
	}

	public TextTemplate(URL url) throws TemplateManagerException {
		content = parseTextTemplate(url);
	}

	public String parseTextTemplate(URL url) throws TemplateManagerException {
		String result = "";
		try {
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(url.openStream()));
			StringBuffer contentOfFile = new StringBuffer();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				contentOfFile.append(line);
			}
			result = contentOfFile.toString();
			bufferedReader.close();
		} catch (Exception e) {
			throw new TemplateManagerException(MessageFactory.getErrorMessage(
					ErrorCode.TEMPLATE_PARSE_ERROR, url.toString()), e);
		}
		return result;
	}

}
