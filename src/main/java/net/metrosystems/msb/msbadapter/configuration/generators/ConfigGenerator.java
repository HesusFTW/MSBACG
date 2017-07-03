package net.metrosystems.msb.msbadapter.configuration.generators;

/**
 * Interface which is used by all ConfigurationGenerators. Provides all required
 * methods.
 *
 * @author benjamin.stein
 */
public interface ConfigGenerator {

	/**
	 * Creates events out of the given input file and puts the resulting files
	 * to the output directory
	 *
	 * @param inputFilePath       - Name of the Input File
	 * @param outputDirectoryPath - Directory to put all generated config files to (will also
	 *                            create subfolder <i>conf</i> and <i>configurations</i>
	 * @throws GeneratorException if no configuration can be generated from inputFilePath to outputDirectoryPath
	 */
	void generateConfiguration(String inputFilePath, String outputDirectoryPath)
			throws GeneratorException;

}
