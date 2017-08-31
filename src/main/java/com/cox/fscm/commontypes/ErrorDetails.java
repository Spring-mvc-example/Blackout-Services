package com.cox.fscm.commontypes;

public class ErrorDetails {

    protected String errorCode;
    protected String errMessage;
    protected String endSysErrorMessage;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public String getEndSysErrorMessage() {
        return endSysErrorMessage;
    }

    public void setEndSysErrorMessage(String endSysErrorMessage) {
        this.endSysErrorMessage = endSysErrorMessage;
    }
}
