package net.metrosystems.msb.msbadapter.configuration.parsers;

/**
 * Exception which is thrown by the File Parsers 
 * @author georgiana.zota
 *
 */
public class FileParserException extends Exception {

	private static final long serialVersionUID = -8111229511889029430L;

	public FileParserException() {
		super();
	}

	public FileParserException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileParserException(String message) {
		super(message);
	}

}
