package net.metrosystems.msb.msbadapter.configuration.generators.assembler;

public class JDBCConnectionAssembler {
	private static final String CONNECTION_STRING_1 = "jdbc:sqlserver://";
	private static final String CONNECTION_STRING_2 = ";DatabaseName=";
	private static final String SEPARATOR_1 = ":";


	/**
	 * This Method assembles the JDBC Connection string used for connecting to source Database
	 *
	 * @param databaseServer server( hostname or IP)
	 * @param databasePort   port open above server
	 * @param databaseName   database name on above server
	 * @return the generated JDBC connection string
	 */
	public static String assemble(String databaseServer, String databasePort, String databaseName) {
		StringBuilder sb = new StringBuilder();
		sb.append(CONNECTION_STRING_1);
		sb.append(databaseServer);
		sb.append(SEPARATOR_1);
		sb.append(databasePort);
		sb.append(CONNECTION_STRING_2);
		sb.append(databaseName);
		return sb.toString();
	}

	public static String assemble(String databaseServer, String databaseName) {
		return databaseServer + ":" + databaseName;
	}
}
