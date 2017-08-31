/*****************************************************************************
 *                                                                            *
 *               Copyright (c) 2014: Cox Communications, Inc.                 *
 *            P R O P R I E T A R Y & C O N F I D E N T I A L                 *
 *                                                                            *
 *    Cox Communications reserves all rights in this Program as delivered.    *
 *    This Program or any portion thereof may not be reproduced in any form   *
 *    whatsoever except as provided by license from Cox Communications, Inc.  *
 *    Use of this material without the express written consent of             *
 *    Cox Communications, Inc. shall be an infringement of copyright and      *
 *    any other intellectual property rights that may be incorporated into    *
 *    this Program.                                                           *
 *                                                                            *
 *****************************************************************************/

package com.cox.fscm.logfilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import com.cox.fscm.common.Constants;

public class TransactionFilter implements Filter {
	private static Logger logger = null;

	private static String loggerName = null;

	/**
	 * {@inheritDoc}
	 */
	public void destroy() {
	}

	/**
	 * {@inheritDoc}
	 */
	public void init(final FilterConfig config) throws ServletException {
		loggerName = config.getInitParameter("LoggerName");
		logger = LogManager.getLogger(loggerName);
	}

	/**
	 * {@inheritDoc}
	 */
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {
		final long start = System.currentTimeMillis();
		// If No Response, then move to next filter.
		if (!(response instanceof HttpServletResponse)) {
			logger.trace("no response.  Moving on.");
			chain.doFilter(request, response);
			return;
		}

		FsServletRequestWrapper mRequest = new FsServletRequestWrapper((HttpServletRequest) request);
		HttpServletResponse mResponse = (HttpServletResponse) response;

		/*
		 * Get base thread name Make sure id is in the name
		 */

		String newTransId = "";

		try {
			// Update Logging Level if Parameter

			String logLevel = mRequest.getParameter("logLevel");
			if (logLevel != null && isValidLevel(logLevel.toUpperCase())) {
				updateLogLevel(logLevel.toUpperCase(), loggerName);
			}

			/*
			 * Get external transaction id
			 */
			final String ext1 = ((HttpServletRequest) request).getHeader("transId");
			final String ext2 = ((HttpServletRequest) request).getHeader("transactionid");
			final String ext3 = ((HttpServletRequest) request).getHeader("clientTransactionId");
			final String ext4 = ((HttpServletRequest) request).getHeader("fig-request-id");
			final String ext5 = UUID.randomUUID().toString();

			/*
			 * Set transId name for logging
			 */
			final String cid = !StringUtils.isBlank(ext1) ? ext1
					: !StringUtils.isBlank(ext2) ? ext2
							: !StringUtils.isBlank(ext3) ? ext3 : !StringUtils.isBlank(ext4) ? ext4 : ext5;

			newTransId = cid.equals(ext5) ? cid : cid + "-" + ext5;
			mRequest.addHeader("fig-request-id", newTransId, logger);
			mResponse.setHeader("fig-request-id", newTransId);

			logRequest((HttpServletRequest) mRequest, newTransId);

			if (logger.isTraceEnabled()) {
				final String url = ((HttpServletRequest) request).getRequestURL().toString();
				if (((HttpServletRequest) request).getQueryString() != null) {
					logger.trace("Received request from " + request.getRemoteAddr() + ": " + url + "?"
							+ ((HttpServletRequest) request).getQueryString());
				} else {
					logger.trace("Received request from " + request.getRemoteAddr() + ": " + url);
				}
			}
		} catch (Exception ex) {
			logger.error("Request Id= " + newTransId + ", TransactionFilter Fault=" + ex.toString());
		} finally {
			chain.doFilter((FsServletRequestWrapper) mRequest, mResponse);			
			logResponse(mResponse, Long.toString(System.currentTimeMillis() - start), newTransId);
		}

	}

	private void updateLogLevel(String logLevel, String LogFile) {
		LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		Configuration config = ctx.getConfiguration();
		LoggerConfig loggerConfig = config.getLoggerConfig(LogFile);
		loggerConfig.setLevel(Level.valueOf(logLevel));
		ctx.updateLoggers();
	}

	public static void logRequest(HttpServletRequest servletRequest, String reqId) {
		StringBuffer headers = new StringBuffer();
		StringBuffer params = new StringBuffer();
		Enumeration<String> headerNames = servletRequest.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = (String) headerNames.nextElement();
			headers.append("Header=" + headerName + ", Value=" + servletRequest.getHeader(headerName));
		}
		Enumeration<String> paramNames = servletRequest.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			params.append("Param=" + paramName + ", Value=" + servletRequest.getParameter(paramName));
		}
		StringBuffer infoMessage = new StringBuffer();
		infoMessage.append(Constants.APPLICATION_ID).append(Constants.WHITE_SPACE).append("Action= TransactionFilter,")
				.append("FigRequestId= ").append(reqId).append(Constants.COMMA_SEPERATOR).append("Headers:\"")
				.append(headers.toString()).append("\",").append("Params=\" ").append(params.toString())
				.append("cmd=\" ").append(servletRequest.getParameter("cmd")).append("\"");
		logger.info(infoMessage.toString());
	}

	public static void logResponse(HttpServletResponse servletResponse, String duration, String reqId) {
		StringBuffer headers = new StringBuffer();
		Collection<String> headerNames = servletResponse.getHeaderNames();
		for (String headerName : headerNames) {
			String value = servletResponse.getHeader(headerName);
			headers.append(headerName + "=\"" + value + "\",");
		}
		headers.deleteCharAt(headers.lastIndexOf(","));
		StringBuffer infoMessage = new StringBuffer();
		infoMessage.append(Constants.APPLICATION_ID).append(Constants.WHITE_SPACE).append("Action= TransactionFilter,")
				.append("FigRequestId= ").append(reqId).append(Constants.COMMA_SEPERATOR).append("Status= ")
				.append(servletResponse.getStatus()).append(Constants.COMMA_SEPERATOR).append("Headers:")
				.append(headers.toString()).append(Constants.TOTAL_DURATION).append(duration);
		logger.info(infoMessage.toString());
	}

	private static boolean isValidLevel(String test) {

		for (Level l : Level.values()) {
			if (l.name().equals(test)) {
				return true;
			}
		}

		return false;
	}
}