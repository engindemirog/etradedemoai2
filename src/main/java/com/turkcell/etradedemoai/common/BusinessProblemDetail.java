package com.turkcell.etradedemoai.common;

/**
 * Business-specific Problem Details extending RFC 7807 standard
 * Used for business rule violations
 */
public class BusinessProblemDetail extends CustomProblemDetail {
    private String businessCode;

    public BusinessProblemDetail() {
        super();
    }

    public BusinessProblemDetail(String type, String title, int status, String detail, String instance, String businessCode) {
        super(type, title, status, detail, instance);
        this.businessCode = businessCode;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }
}
