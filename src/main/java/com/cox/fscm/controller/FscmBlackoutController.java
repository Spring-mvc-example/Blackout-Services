package com.cox.fscm.controller;

import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cox.fscm.common.Constants;
import com.cox.fscm.common.Utility;
import com.cox.fscm.commontypes.ErrorDetails;
import com.cox.fscm.commontypes.FscmBlackout;
import com.cox.fscm.commontypes.FscmRequest;
import com.cox.fscm.commontypes.FscmResponse;
import com.cox.fscm.exception.AdminGenericException;
import com.cox.fscm.service.FscmBlackoutService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("service")
public class FscmBlackoutController {

	@Autowired
	@Qualifier("adminProps")
	private Properties prop;

	@Autowired(required = true)
	@Qualifier(value = "blackoutService")
	private FscmBlackoutService blackoutService;
	@Autowired(required = true)
	private HttpServletRequest httpServletRequest;

	private static ObjectMapper om = new ObjectMapper();

	private static final Logger LOGGER = LogManager.getLogger(Constants.FSCMADMINBLACKOUT);

	@RequestMapping(value = "getBlackoutDates", method = RequestMethod.POST, produces = Constants.APPLICATION_JSON, consumes = Constants.APPLICATION_JSON)
	public String getBlackoutDates(@RequestBody FscmRequest request) {

		String response = null;
		String token = "";
		String transactionId = "";
		FscmResponse fscmResponse = new FscmResponse();
		try {
			token = request.getTransactionHeader().getToken();
			transactionId = httpServletRequest.getHeader("fig-request-id") + "-" + token;
			LOGGER.info(Utility.logInfoMessage(transactionId, "START getBlackoutDates input = ",
					om.writeValueAsString(request)));
			List<FscmBlackout> blackoutList = blackoutService.getBlackoutDates(transactionId);
			fscmResponse.setSuccessFlag(Boolean.TRUE.toString());
			fscmResponse.setBlackoutList(blackoutList);
			LOGGER.info(Utility.logInfoMessage(transactionId, "Number of rows (Blackout Dates) fetched from DB: ",
					blackoutList.size()));

		} catch (Exception e) {
			fscmResponse = fetchCatchBlockResponse(fscmResponse, e, transactionId,
					" Exception happened while getting BlackoutDates in controller ");
		} finally {
			fscmResponse.setTransactionHeader(request.getTransactionHeader());
			try {
				response = om.writeValueAsString(fscmResponse);
			} catch (JsonProcessingException e) {
				LOGGER.error(Utility.logErrorMessage(transactionId,
						" Exception happened while getting BlackoutDates in controller "), e);
			}
		}
		LOGGER.info(Utility.logInfoMessage(transactionId, "END getBlackoutDates Response : ", response));
		return response;

	}

	@RequestMapping(value = "removeBlackoutDates", method = RequestMethod.POST, produces = Constants.APPLICATION_JSON, consumes = Constants.APPLICATION_JSON)
	public String removeBlackoutDates(@RequestBody FscmRequest request) {

		String response = null;
		String token = "";
		String transactionId = "";
		FscmResponse fscmResponse = new FscmResponse();
		try {
			token = request.getTransactionHeader().getToken();
			transactionId = httpServletRequest.getHeader("fig-request-id") + "-" + token;

			LOGGER.info(Utility.logInfoMessage(transactionId, "START removeBlackoutDates input = ",
					om.writeValueAsString(request)));
			List<FscmBlackout> inputDates = request.getBlackoutList();
			blackoutService.removeBlackoutDates(inputDates, transactionId);
			fscmResponse.setSuccessFlag(Boolean.TRUE.toString());
			LOGGER.info(Utility.logInfoMessage(transactionId, " blackout dates removed."));
		} catch (Exception e) {

			fscmResponse = fetchCatchBlockResponse(fscmResponse, e, transactionId,
					" Exception happened while removing BlackoutDates in controller ");
		} finally {

			fscmResponse.setTransactionHeader(request.getTransactionHeader());
			try {
				response = om.writeValueAsString(fscmResponse);
			} catch (JsonProcessingException e) {
				LOGGER.error(Utility.logErrorMessage(transactionId,
						" Exception happened while remove BlackoutDates in controller "), e);
			}
		}
		LOGGER.info(Utility.logInfoMessage(transactionId, " END  removeBlackoutDates. Response : ", response));
		return response;

	}

	@RequestMapping(value = "setBlackoutDates", method = RequestMethod.POST, produces = Constants.APPLICATION_JSON, consumes = Constants.APPLICATION_JSON)
	public String setBlackoutDates(@RequestBody FscmRequest request) {

		String response = null;
		String token = "";
		String transactionId = "";
		FscmResponse fscmResponse = new FscmResponse();
		try {
			token = request.getTransactionHeader().getToken();
			transactionId = httpServletRequest.getHeader("fig-request-id") + "-" + token;
			LOGGER.info(Utility.logInfoMessage(transactionId, "START setBlackoutDates input = ",
					om.writeValueAsString(request)));
			List<FscmBlackout> inputDates = request.getBlackoutList();
			if (!CollectionUtils.isEmpty(inputDates)) {
				List<Integer> newBlackoutIds = blackoutService.setBlackoutDates(inputDates, transactionId);
				fscmResponse.setSuccessFlag(Boolean.TRUE.toString());
				fscmResponse.setNewBlackoutIds(newBlackoutIds);
				LOGGER.info(Utility.logInfoMessage(transactionId, String.valueOf(inputDates.size()),
						"blackout dates added/updated."));
			} else {
				throw new AdminGenericException("Missing blackout dates in request.");
			}
		} catch (Exception e) {
			fscmResponse = fetchCatchBlockResponse(fscmResponse, e, transactionId,
					" Exception happened while setting BlackoutDates in controller ");
		} finally {

			fscmResponse.setTransactionHeader(request.getTransactionHeader());
			try {
				response = om.writeValueAsString(fscmResponse);
			} catch (JsonProcessingException e) {
				LOGGER.error(Utility.logErrorMessage(transactionId,
						" Exception happened while setting BlackoutDates in controller "), e);
			}
		}
		LOGGER.info(Utility.logInfoMessage(transactionId, " END removeBlackoutDates Response :", response));
		return response;
	}

	@RequestMapping(value = "checkBlackoutStatus", method = RequestMethod.POST, produces = Constants.APPLICATION_JSON, consumes = Constants.APPLICATION_JSON)
	public String checkBlackoutStatus(@RequestBody FscmRequest request) {

		String response = null;
		String token = "";
		String transactionId = "";
		FscmResponse fscmResponse = new FscmResponse();
		try {
			token = request.getTransactionHeader().getToken();
			transactionId = httpServletRequest.getHeader("fig-request-id") + "-" + token;
			LOGGER.info(Utility.logInfoMessage(transactionId, "START checkBlackoutStatus input = ",
					om.writeValueAsString(request)));
			String exceptionStartDate = request.getExceptionStartDate();
			String exceptionEndDate = request.getExceptionEndDate();
			String exceptionRegion = request.getExceptionRegion();
			if (null != exceptionStartDate && null != exceptionEndDate && null != exceptionRegion) {
				Boolean isBlackoutActive = blackoutService.checkInvalidException(exceptionRegion, exceptionStartDate,
						exceptionEndDate, transactionId);
				fscmResponse.setSuccessFlag(Boolean.TRUE.toString());
				fscmResponse.setIsBlackoutActive(isBlackoutActive);
			} else {
				throw new AdminGenericException("Missing Exception region or date in input.");
			}
		} catch (Exception e) {
			fscmResponse = fetchCatchBlockResponse(fscmResponse, e, transactionId,
					" Exception happened while checking BlackoutStatus in controller ");
		} finally {

			fscmResponse.setTransactionHeader(request.getTransactionHeader());
			try {
				response = om.writeValueAsString(fscmResponse);
			} catch (JsonProcessingException e) {
				LOGGER.error(Utility.logErrorMessage(transactionId,
						" Exception happened while checking BlackoutStatus in controller "), e);
			}
		}
		LOGGER.info(Utility.logInfoMessage(transactionId, " END checkBlackoutStatus Response : ", response));
		return response;
	}

	private FscmResponse fetchCatchBlockResponse(FscmResponse fscmResponse, Exception e, String transactionId,
			String errorMessage) {
		fscmResponse.setSuccessFlag(Boolean.FALSE.toString());
		ErrorDetails ed = new ErrorDetails();
		ed.setErrorCode(prop.getProperty(Constants.FIG_FAULT_CODE));
		ed.setErrMessage(prop.getProperty(Constants.FIG_FAULT_MESSAGE));
		ed.setEndSysErrorMessage(e.getMessage());
		fscmResponse.setErrorDetails(ed);
		LOGGER.error(Utility.logErrorMessage(transactionId, errorMessage), e);
		return fscmResponse;
	}
}
