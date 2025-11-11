package com.turkcell.etradedemoai.common;

/**
 * Custom exception for business rule violations
 */
public class BusinessException extends RuntimeException {
    private String businessCode;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, String businessCode) {
        super(message);
        this.businessCode = businessCode;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }
}
