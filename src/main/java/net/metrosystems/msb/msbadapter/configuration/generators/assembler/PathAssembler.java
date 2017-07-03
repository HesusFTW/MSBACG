package net.metrosystems.msb.msbadapter.configuration.generators.assembler;


import net.metrosystems.msb.msbadapter.configuration.generators.GeneratorException;


/**
 * Assembles Pathnamess
 *
 * @author benjamin.stein
 */
public class PathAssembler {
	private static final String REGEX_MARKER = "regex:";

	/**
	 * Assembles the Staging directory by the given event directory.
	 *
	 * @param outputDirPath output path
	 * @return assembled Staging Directory Path
	 * @throws GeneratorException if stagingDirectory couldn't be created
	 */
	public static String assembleStagingDirectoryPath(String outputDirPath) throws GeneratorException {
		outputDirPath = cutLastSlash(outputDirPath);
		StringBuilder sb = new StringBuilder(outputDirPath);
		sb.append("_staging/");
		return sb.toString().replace("//", "/");
	}

	/**
	 * Assembles the Archive directory by the given event directory.
	 *
	 * @param eventDirPath - the complete path for the event directory
	 * @return the archive directory, which is on the same Level as the event
	 * directory
	 * @throws GeneratorException if archiveDirectory couldn't be created
	 */
	public static String assembleArchiveDirectoryPath(String eventDirPath) throws GeneratorException {
		eventDirPath = cutLastSlash(eventDirPath);
		eventDirPath = eventDirPath.substring(0, eventDirPath.lastIndexOf("/"));

		StringBuilder sb = new StringBuilder(eventDirPath);
		sb.append("/archive/");
		return sb.toString().replace("//", "/");
	}

	/**
	 * Assembles the EventDir path.<br>
	 * <p>
	 * <b>Example:</b><br>
	 * <b>Input:</b> Folder: D:/MAI/MPOS/OUT/ + Regexp: *<br>
	 * <b>Result:</b> D:/MAI/MPOS/OUT/*<br>
	 * <br>
	 * <b>Input:</b> Folder: D:/MAI/MPOS/OUT/ + Regexp: ^RPA.*<br>
	 * <b>Result:</b> D:/MAI/MPOS/OUT/regex:^RPA.*<br>
	 * <br>
	 *
	 * @param eventDirectoryPath The defined event Directory
	 * @param regexp             The defined regular expression
	 * @return assembled directory event string
	 */
	public static String assembleEventDirString(String eventDirectoryPath,
												String regexp) {
		if (!"*".equals(regexp)) {
			regexp = REGEX_MARKER + regexp;
		}

		StringBuilder sb = new StringBuilder(eventDirectoryPath);
		sb.append("/");
		sb.append(regexp);
		return sb.toString().replace("//", "/");
	}

	private static String cutLastSlash(String outputDirPath) throws GeneratorException {
		try {
			int length = outputDirPath.length() - 1;
			if ('/' == (outputDirPath.charAt(length))) {
				outputDirPath = outputDirPath.substring(0, length);
			}
			return outputDirPath;
		} catch (Exception e) {
			throw new GeneratorException("One of the input parameters given are not correct.");
		}

	}
}
