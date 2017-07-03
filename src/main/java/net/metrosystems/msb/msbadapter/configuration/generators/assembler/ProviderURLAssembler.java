package net.metrosystems.msb.msbadapter.configuration.generators.assembler;


public class ProviderURLAssembler {

	private static final String SEPARATOR = ":";
	private static final String CHANNEL = "MSB.S0.SVRCONN";
	private static final String CHANNEL_SEPARATOR = "/";


	/**
	 * This Method assembles the string used for connecting to QueueManager.
	 *
	 * @param hostname hostName
	 * @param port     port
	 * @return the generated connection string
	 */
	public static String assemble(String hostname, String port) {
		StringBuilder sb = new StringBuilder();
		sb.append(hostname);
		sb.append(SEPARATOR);
		sb.append(port);
		sb.append(CHANNEL_SEPARATOR);
		sb.append(CHANNEL);
		return sb.toString();
	}


}
