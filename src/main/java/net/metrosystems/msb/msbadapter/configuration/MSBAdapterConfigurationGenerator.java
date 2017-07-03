package net.metrosystems.msb.msbadapter.configuration;

import net.metrosystems.msb.msbadapter.configuration.generators.AdminConfigGenerator;
import net.metrosystems.msb.msbadapter.configuration.generators.ConfigGenerator;
import net.metrosystems.msb.msbadapter.configuration.generators.EventConfigGenerator;
import net.metrosystems.msb.msbadapter.configuration.generators.MQSCFileGenerator;
import net.metrosystems.msb.msbadapter.configuration.log.MessageFactory;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * This application is creating configurations from a given input file.
 * 
 * @author benjamin.stein
 * 
 */
public class MSBAdapterConfigurationGenerator {

	private static final String CONFIGURATIONS_FOLDER = "/configurations/";
	private static final int RC_EVENT_CONFIG_GENERATION_FAILED = 1;
	private static final int RC_ADMIN_GENERATION_FAILED = 2;
	private static final int RC_MQ_GENERATION_FAILED = 3;
	public static final int RC_PARAMETER_ERROR = 4;
	private static final int RC_RESOURCE_BUNDLE_MISSING = 5;
	private static final int RC_CLEANUP_FAILED = 6;

	private static String inputFile = null;
	private static String outputFolder = null;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(MSBAdapterConfigurationGenerator.class);


	public static void main(String[] args) {
		LOGGER.info("Config Generator started.");

		loadResourceBundle();
		parseArguments(args);
		cleanup();
		createEventConfigurations();
		createAdminConfigurations();
		createMQSCFile();

		LOGGER.info("Config generator finished.");

	}

	private static void loadResourceBundle() {
		try {
			LOGGER.debug("Loading Resource Bundles ...");
			MessageFactory.initResourceBundles(Locale.ENGLISH);
			LOGGER.debug("Resource Bundles loaded.");
		} catch (Exception ex) {
			LOGGER.error("Resource Bundle could not be loaded!", ex);
			System.exit(RC_RESOURCE_BUNDLE_MISSING);
		}
	}

	private static void createEventConfigurations() {
		try {
			ConfigGenerator eventConfigGenerator = EventConfigGenerator
					.getInstance();
			eventConfigGenerator.generateConfiguration(inputFile, outputFolder);
		} catch (Exception e) {
			LOGGER.error("Generation Failed:", e);
			System.exit(RC_EVENT_CONFIG_GENERATION_FAILED);
		}
	}

	private static void createAdminConfigurations() {
		try {
			ConfigGenerator adminConfigGenerator = AdminConfigGenerator
					.getInstance();
			adminConfigGenerator.generateConfiguration(inputFile, outputFolder);
		} catch (Exception e) {
			LOGGER.error("Generation Failed:", e);
			System.exit(RC_ADMIN_GENERATION_FAILED);
		}
	}
	
	private static void createMQSCFile() {
		try {
			ConfigGenerator fileGenerator = MQSCFileGenerator
					.getInstance();
			fileGenerator.generateConfiguration(inputFile, outputFolder);
		} catch (Exception e) {
			LOGGER.error("Generation MQ File Failed:", e);
			System.exit(RC_MQ_GENERATION_FAILED);
		}
	}

	private static void parseArguments(String[] args) {
		if (args.length != 4) {
			usage();
		}
		for (int i = 0; i < args.length; i++) {
			if ("-inputFile".equals(args[i])) {
				inputFile = args[++i];
			} else if ("-outputFolder".equals(args[i])) {
				outputFolder = args[++i];
			} else {
				usage();
			}
		}
	}

	private static void usage() {
		LOGGER.warn("Usage:");
		LOGGER.warn("Please check the given parameters!The outputFolder parameter should not contain blank spaces!");
		LOGGER.warn("For proper way of using the application, use the following call! The given paths are only examples!!");
		LOGGER.warn("MSBAdapterConfigurationGenerator.jar -inputFile C:/temp/inputFile.txt -outputFolder C:/temp/");
		System.exit(RC_PARAMETER_ERROR);
	}

	private static void cleanup() {
		File dir = new File(outputFolder + CONFIGURATIONS_FOLDER);
		if (dir.exists()) {
			try {
				FileUtils.cleanDirectory(dir);
			} catch (IOException e) {
				System.exit(RC_CLEANUP_FAILED);
			}
		}

	}
}
