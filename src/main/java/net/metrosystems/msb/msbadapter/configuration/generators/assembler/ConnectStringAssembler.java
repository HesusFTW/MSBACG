package net.metrosystems.msb.msbadapter.configuration.generators.assembler;

import net.metrosystems.msb.msbadapter.configuration.data.Environment;

/**
 * Assembles MQ Connect Strings
 *
 * @author benjamin.stein
 */
public class ConnectStringAssembler {
	private static final String CHANNEL_DEF = "@MSB.S0.SVRCONN/TCP/";

	/**
	 * Creates the long version of the QMGR connect String used by events to
	 * establish connection.
	 *
	 * @param env Environment to assemble
	 * @return the assembled ConnectString
	 */
	public static String assemble(Environment env) {
		StringBuilder sb = new StringBuilder(env.getQueueManager());
		sb.append(CHANNEL_DEF);
		sb.append(env.getHostname());
		sb.append("(");
		sb.append(env.getPort());
		sb.append(")");
		return sb.toString();
	}
}
