package com.cox.fscm.filter;

import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cox.commons.configuration.PropertiesUtil;
import com.cox.commons.configuration.PropertyConstants;
import com.cox.fscm.common.Constants;

public class AuthenticationService {

	private static final Logger LOGGER = LogManager.getLogger(Constants.FSCMADMINBLACKOUT);

	public AuthResponse authenticate(String authCredentials, Properties authProps) {
		AuthResponse authResponse = new AuthResponse();
		try {
			if (null == authCredentials) {
				authResponse.setResultCode(1);
				authResponse.setErrorMessage("Missing username or password in authentication credentials.");
				return authResponse;
			}
			final String encodedUserPassword = authCredentials.replaceFirst("Basic" + " ", "");
			String usernameAndPassword = null;

			byte[] decodedBytes = Base64.decodeBase64(encodedUserPassword);
			usernameAndPassword = new String(decodedBytes, "UTF-8");

			final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
			final String username = tokenizer.nextToken();
			final String password = tokenizer.nextToken();

			// static userid and password pulled from prop file

			String fntcPassword = authProps.getProperty(username);

			if (null != fntcPassword && fntcPassword.equalsIgnoreCase(password)) {
				authResponse.setResultCode(0);
			} else {
				authResponse.setResultCode(1);
				authResponse.setErrorMessage("Provided username or password is incorrect.");
			}
		} catch (IOException e) {
			LOGGER.error(Constants.APPLICATION_ID + " Exception Ocurred while authentication.", e);
			authResponse.setResultCode(1);
			authResponse.setErrorMessage("Unexpected Error occurred while authentication.");
		}
		return authResponse;
	}

}
