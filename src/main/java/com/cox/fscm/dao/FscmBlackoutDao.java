package com.cox.fscm.dao;

import java.util.Date;
import java.util.List;

import com.cox.fscm.commontypes.TransactionHeader;
import com.cox.fscm.dto.FscmBlackoutModel;
import com.cox.fscm.exception.AdminGenericException;

public interface FscmBlackoutDao {

	/**
	 * Fetch all the Blackout Dates from the database.
	 * 
	 * @param transactionId
	 * 
	 * @return FscmBlackoutModel
	 * @throws AdminGenericException
	 */
	List<FscmBlackoutModel> getBlackoutDates(String transactionId)
			throws AdminGenericException;

	/**
	 * Insert or update a list of Blackout dates into the database.
	 * 
	 * @param transactionId
	 * 
	 * @return void
	 */
	List<Integer> setBlackoutDates(List<FscmBlackoutModel> blackoutModels,
			String transactionId) throws AdminGenericException;

	/**
	 * Delete a list of Blackout dates into the database.
	 * 
	 * @return void
	 */
	void removeBlackoutDates(List<FscmBlackoutModel> inputDates,
			String transactionId) throws AdminGenericException;

	List<FscmBlackoutModel> fetchBlackoutDates(String exceptionRegion,
			Date exceptionStartDate, Date exceptionEndDate,
			String transactionId) throws AdminGenericException;

}
