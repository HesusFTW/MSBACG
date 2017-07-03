package net.metrosystems.msb.msbadapter.configuration.generators;

public class GeneratorException extends Exception {
	private static final long serialVersionUID = 6566419731051814982L;

	public GeneratorException() {
	}

	public GeneratorException(String message) {
		super(message);
	}

	public GeneratorException(Throwable cause) {
		super(cause);
	}

	public GeneratorException(String message, Throwable cause) {
		super(message, cause);
	}

}
