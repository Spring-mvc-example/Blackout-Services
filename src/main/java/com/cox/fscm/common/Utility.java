package com.cox.fscm.common;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Utility {

	private static final Logger LOGGER = LogManager.getLogger(Constants.FSCMADMINBLACKOUT);

	private Utility() {
	}

	public static String logInfoMessage(String transactionId, String infoMessage, Object value) {
		StringBuffer logEntry = new StringBuffer();

		if (StringUtils.isNotEmpty(transactionId)) {
			logEntry.append(Constants.APPLICATION_ID).append(Constants.WHITE_SPACE).append(Constants.TRANSACTIONID);
			logEntry.append(Constants.LOG_MESSAGE_SEPERATOR);
			logEntry.append(transactionId);
			logEntry.append(Constants.COMMA_SEPERATOR);
		}

		logEntry.append(infoMessage);

		logEntry.append((null != value) ? value.toString() : value);

		String logMessage = StringUtils.isNotBlank(logEntry.toString()) ? logEntry.toString() : StringUtils.EMPTY;
		return logMessage;
	}

	public static String logInfoMessage(final String transactionId, final String infoMessage) {
		StringBuffer logEntry = new StringBuffer();

		if (StringUtils.isNotEmpty(transactionId)) {
			logEntry.append(Constants.APPLICATION_ID).append(Constants.WHITE_SPACE).append(Constants.TRANSACTIONID);
			logEntry.append(Constants.LOG_MESSAGE_SEPERATOR);
			logEntry.append(transactionId);
			logEntry.append(Constants.COMMA_SEPERATOR);
		}

		logEntry.append(infoMessage);
		String logMessage = StringUtils.isNotBlank(logEntry.toString()) ? logEntry.toString() : StringUtils.EMPTY;
		return logMessage;
	}

	public static String logErrorMessage(final String transactionId, final String errorMessage) {
		StringBuffer logEntry = new StringBuffer();
		logEntry.append("Error Message :: ");
		if (StringUtils.isNotBlank(transactionId)) {
			logEntry.append(Constants.APPLICATION_ID).append(Constants.WHITE_SPACE).append(Constants.TRANSACTIONID);
			logEntry.append(Constants.LOG_MESSAGE_SEPERATOR);
			logEntry.append(transactionId);
			logEntry.append(Constants.COMMA_SEPERATOR);
		}

		logEntry.append(errorMessage);

		String logMessage = StringUtils.isNotBlank(logEntry.toString()) ? logEntry.toString() : StringUtils.EMPTY;
		return logMessage;
	}
}
