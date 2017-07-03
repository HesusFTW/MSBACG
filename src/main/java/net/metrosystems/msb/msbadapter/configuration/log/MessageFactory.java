package net.metrosystems.msb.msbadapter.configuration.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This Class provides Actions to use the applications ResourceBundles
 * 
 * @author benjamin.stein
 * 
 */
public class MessageFactory {

    public static final String MESSAGE_NOT_FOUND = "The Message could not be found!";
    public static final String NO_ERROR_MSG = "No message defined.";
    private static final String ERROR_MESSAGES_FILE = "ErrorMessages";
    private static ResourceBundle errorMessageBundle;
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageFactory.class);

    /**
     * This method initialize the ResourceBundles. It has to be called in
     * general Application Setup (main method for example).
     * 
     */
    public static synchronized void initResourceBundles(Locale locale) {
        if (errorMessageBundle == null) {
            errorMessageBundle = ResourceBundle.getBundle(ERROR_MESSAGES_FILE,
                    locale);

        }
    }

    /**
     * This method returns a Error message from the ResourceBundle with the
     * given key and fills parameters of the message.
     * 
     * @param code
     *            The ErrorMessage code
     * @param args
     *            The parametric arguments (which will replace the {0},{1}, ...)
     * @return the Error Message - Will return NULL if the key is not found.
     */
    public static String getErrorMessage(ErrorCode code, String[] args) {
        String msg;
        try {
            msg = MessageFormat.format(
                    errorMessageBundle.getString(code.toString()),
                    (Object[]) args);
        } catch (Exception e) {
            msg = NO_ERROR_MSG;
            LOGGER.warn(MESSAGE_NOT_FOUND);
        }
        return msg;
    }

    /**
     * This method returns a Error message from the ResourceBundle with the
     * given key and fills one parameter.
     * 
     * @param code
     *            The ErrorMessage code
     * @param arguments
     *            The parametric arguments (which will replace the {0},{1}, ...)
     * @return the Error Message - Will return NULL if the key is not found.
     */
    public static String getErrorMessage(ErrorCode code, String arguments) {
        String msg;
        String[] args = new String[]{arguments};
        try {
            msg = MessageFormat.format(
                    errorMessageBundle.getString(code.toString()),
                    (Object[]) args);
        } catch (Exception e) {
            msg = NO_ERROR_MSG;
            LOGGER.warn(MESSAGE_NOT_FOUND);
        }
        return msg;
    }

    /**
     * This method returns a Error message from the ResourceBundle with the
     * given key and fills one parameter.
     * 
     * @param code
     *            The ErrorMessage code
     * @param arguments
     *            The parametric arguments (which will replace the {0},{1}, ...)
     * @return the Error Message - Will return NULL if the key is not found.
     */
    public static String getErrorMessage(ErrorCode code, int arguments) {
        String msg;
        Integer[] args = new Integer[]{arguments};
        try {
            msg = MessageFormat.format(
                    errorMessageBundle.getString(code.toString()),
                    (Object[]) args);
        } catch (Exception e) {
            msg = NO_ERROR_MSG;
            LOGGER.warn(MESSAGE_NOT_FOUND);
        }
        return msg;
    }

    /**
     * This method returns a Error message from the ResourceBundle with the
     * given key
     * 
     * @param code
     * @return the Error Message - Will return NULL if the key is not found.
     */
    public static String getErrorMessage(ErrorCode code) {
        String msg;
        try {
            msg = errorMessageBundle.getString(code.toString());
        } catch (Exception e) {
            msg = NO_ERROR_MSG;
            LOGGER.warn(MESSAGE_NOT_FOUND);
        }
        return msg;
    }
}
