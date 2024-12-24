package com.project.Loan.Eligibility.Application.entity;

import java.util.Map;

public class LoanStatistics {

    private double averageApprovedLoanAmount;
    private Map<String, Integer> rejectionReasons;

    public LoanStatistics(double averageApprovedLoanAmount, Map<String, Integer> rejectionReasons) {
        this.averageApprovedLoanAmount = averageApprovedLoanAmount;
        this.rejectionReasons = rejectionReasons;
    }

    public double getAverageApprovedLoanAmount() {
        return averageApprovedLoanAmount;
    }

    public void setAverageApprovedLoanAmount(double averageApprovedLoanAmount) {
        this.averageApprovedLoanAmount = averageApprovedLoanAmount;
    }

    public Map<String, Integer> getRejectionReasons() {
        return rejectionReasons;
    }

    public void setRejectionReasons(Map<String, Integer> rejectionReasons) {
        this.rejectionReasons = rejectionReasons;
    }
}
