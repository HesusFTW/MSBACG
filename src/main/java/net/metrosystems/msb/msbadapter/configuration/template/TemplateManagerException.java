package net.metrosystems.msb.msbadapter.configuration.template;

/**
 * Exception which is thrown by the Template Managers
 * @author benjamin.stein
 *
 */
public class TemplateManagerException extends Exception {
	private static final long serialVersionUID = 3020831271541558790L;

	public TemplateManagerException() {
		super();
	}

	public TemplateManagerException(String message, Throwable cause) {
		super(message, cause);
	}

	public TemplateManagerException(String message) {
		super(message);
	}

	public TemplateManagerException(Throwable cause) {
		super(cause);
	}


}
