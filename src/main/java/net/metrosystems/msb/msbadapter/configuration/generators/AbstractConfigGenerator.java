package net.metrosystems.msb.msbadapter.configuration.generators;

import net.metrosystems.msb.msbadapter.configuration.data.ConfigMapItem;
import net.metrosystems.msb.msbadapter.configuration.data.Event;
import net.metrosystems.msb.msbadapter.configuration.parsers.FileParser;
import net.metrosystems.msb.msbadapter.configuration.parsers.FileParserException;
import net.metrosystems.msb.msbadapter.configuration.parsers.InputFileXMLParser;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractConfigGenerator implements ConfigGenerator {

	protected static final String CONFIG_ROOT_TAG = "Configuration";
	protected Map<String, ConfigMapItem> configForServers;

	/**
	 * Calls the InputFileParser to get the Routes
	 *
	 * @param inputFilePath file input path
	 * @return the List of routes found on the input File
	 * @throws FileParserException if a file can't be parsed
	 * @throws JDOMException       if JDOM can't be opened
	 */
	protected List<Event> getEventsFromParser(String inputFilePath)
			throws FileParserException, JDOMException {
		FileParser parser = InputFileXMLParser.getInstance();
		return parser.parseFile(inputFilePath);
	}

	/**
	 * Adds the generated element to the Map containing all configurations for
	 * Servers
	 *
	 * @param element - The Event to store as JDOM Element
	 * @param server  - The Server Tag (e.g. S0, S1, S2 ...)
	 */
	protected void addElementToConfigForServers(Element element, String server) {
		ConfigMapItem item = configForServers.get(server);
		if (item == null) {
			Element root = new Element(CONFIG_ROOT_TAG);
			root.addContent(element);
			ConfigMapItem newItem = new ConfigMapItem(new Document(root),
					server);
			configForServers.put(server, newItem);
		} else {
			item.getDocument().getRootElement().addContent(element);
		}
	}

	protected void createNewConfigForServersMap() {
		configForServers = new HashMap<String, ConfigMapItem>();
	}
}
