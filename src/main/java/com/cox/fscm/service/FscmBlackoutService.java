package com.cox.fscm.service;

import java.util.List;

import com.cox.fscm.commontypes.FscmBlackout;
import com.cox.fscm.exception.AdminGenericException;

public interface FscmBlackoutService {

	/**
	 * Fetch all the Blackout Dates from the database.
	 * 
	 * @return FscmBlackoutModel
	 * @throws AdminGenericException
	 */
	List<FscmBlackout> getBlackoutDates(String transactionId) throws AdminGenericException;

	/**
	 * Insert or update a list of Blackout dates into the database.
	 * 
	 * @return void
	 */
	List<Integer> setBlackoutDates(List<FscmBlackout> inputDates, String transactionId)
			throws AdminGenericException;

	/**
	 * Delete a list of Blackout dates into the database.
	 * 
	 * @param transactionId
	 * 
	 * @return void
	 */
	void removeBlackoutDates(List<FscmBlackout> inputDates, String transactionId)
			throws AdminGenericException;

	Boolean checkInvalidException(String exceptionRegion, String exceptionStartStr, String exceptionEndStr,
			String transactionId) throws AdminGenericException;;
}
