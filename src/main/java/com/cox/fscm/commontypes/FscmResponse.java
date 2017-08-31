package com.cox.fscm.commontypes;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class FscmResponse {

	private String successFlag;
	private ErrorDetails errorDetails;
	private TransactionHeader transactionHeader;
	private List<FscmBlackout> blackoutList;
	private List<Integer> newBlackoutIds;
	private Boolean isBlackoutActive;

	public String getSuccessFlag() {
		return successFlag;
	}

	public void setSuccessFlag(String successFlag) {
		this.successFlag = successFlag;
	}

	public ErrorDetails getErrorDetails() {
		return errorDetails;
	}

	public void setErrorDetails(ErrorDetails errorDetails) {
		this.errorDetails = errorDetails;
	}

	public TransactionHeader getTransactionHeader() {
		return transactionHeader;
	}

	public void setTransactionHeader(TransactionHeader transactionHeader) {
		this.transactionHeader = transactionHeader;
	}

	public List<FscmBlackout> getBlackoutList() {
		return blackoutList;
	}

	public void setBlackoutList(List<FscmBlackout> blackoutList) {
		this.blackoutList = blackoutList;
	}

	public List<Integer> getNewBlackoutIds() {
		return newBlackoutIds;
	}

	public void setNewBlackoutIds(List<Integer> newBlackoutIds) {
		this.newBlackoutIds = newBlackoutIds;
	}

	public Boolean getIsBlackoutActive() {
		return isBlackoutActive;
	}

	public void setIsBlackoutActive(Boolean isBlackoutActive) {
		this.isBlackoutActive = isBlackoutActive;
	}

}
