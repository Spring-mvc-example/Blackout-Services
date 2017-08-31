package com.cox.fscm.commontypes;

import java.util.List;

public class FscmRequest {
	private TransactionHeader transactionHeader;
	private List<FscmBlackout> blackoutList;
	private String exceptionRegion;
	private String exceptionStartDate;
	private String exceptionEndDate;

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

	public String getExceptionRegion() {
		return exceptionRegion;
	}

	public void setExceptionRegion(String exceptionRegion) {
		this.exceptionRegion = exceptionRegion;
	}

	public String getExceptionStartDate() {
		return exceptionStartDate;
	}

	public void setExceptionStartDate(String exceptionStartDate) {
		this.exceptionStartDate = exceptionStartDate;
	}

	public String getExceptionEndDate() {
		return exceptionEndDate;
	}

	public void setExceptionEndDate(String exceptionEndDate) {
		this.exceptionEndDate = exceptionEndDate;
	}

}
