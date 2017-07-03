package net.metrosystems.msb.msbadapter.configuration.parsers;

import net.metrosystems.msb.msbadapter.configuration.data.Event;
import org.jdom.JDOMException;

import java.util.List;

/**
 * Interface for getting Events Data from an input XML source
 *
 * @author georgiana.zota
 */
public interface FileParser {

	/**
	 * Parses a given InputFile and returns a Set with all found Events
	 *
	 * @param inputFilePath The path of the file to be parsed
	 * @return a List of Events
	 * @throws FileParserException if a file can't be parsed
	 * @throws JDOMException if JDOM can't be opened
	 */
	List<Event> parseFile(String inputFilePath) throws FileParserException, JDOMException;
}
