package net.metrosystems.msb.msbadapter.configuration.render;

import java.net.URL;

/**
 * Provides Methods to Render Text files
 * 
 * @author benjamin.stein
 * 
 */
public interface TextFileRenderer {

	public void renderTextFile(URL targetURL, String content) throws RendererException;

}
