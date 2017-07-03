package net.metrosystems.msb.msbadapter.configuration.render;

import java.io.File;
import java.net.URL;

import net.metrosystems.msb.msbadapter.configuration.log.ErrorCode;
import net.metrosystems.msb.msbadapter.configuration.log.MessageFactory;

import org.apache.commons.io.FileUtils;

/**
 * Writes any kind of text data to a File
 * @author benjamin.stein
 *
 */
public class TextFileRendererImpl implements TextFileRenderer {
	private static TextFileRendererImpl instance;
	
	private TextFileRendererImpl() {

	}

	public static synchronized TextFileRendererImpl getInstance() {
		if (instance == null) {
			instance = new TextFileRendererImpl();
		}
		return instance;
	}
	
	public void renderTextFile(URL targetURL, String content)
			throws RendererException {

		try {
			File file = new File(targetURL.toURI());
			FileUtils.writeStringToFile(file, content);
		} catch (Exception e) {
			throw new RendererException(MessageFactory.getErrorMessage(
					ErrorCode.EVENT_RENDERING_FAILED, targetURL.toString()), e);
		}

	}
}
