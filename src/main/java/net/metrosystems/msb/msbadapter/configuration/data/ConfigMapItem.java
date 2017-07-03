package net.metrosystems.msb.msbadapter.configuration.data;

import org.jdom.Document;

/**
 * Holds the Document object and also the associated ServerName
 * @author benjamin.stein
 *
 */
public class ConfigMapItem {
	private Document document;
	private String serverName;
	public ConfigMapItem(Document document, String serverName) {
		super();
		this.document = document;
		this.serverName = serverName;
	}
	public Document getDocument() {
		return document;
	}
	public String getServerName() {
		return serverName;
	}
	
	

}
