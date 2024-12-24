//package com.project.Loan.Eligibility.Application.service;
//
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.project.Loan.Eligibility.Application.entity.Loan;
//import com.project.Loan.Eligibility.Application.entity.LoanResponse;
//import com.project.Loan.Eligibility.Application.repository.LoanRepository;
//
//@Service
//public class LoanService {
//
//	private final String url = "https://prepstripe.com/loan_task_payloads.json";
//
//	@Autowired
//	private LoanRepository loanRepository;
//
//	public void fetchAndSaveData() {
//
////		throw new RuntimeException("Simulated Exception: Something went wrong while saving data."); intentionally wrote to test the exception message
//
//		RestTemplate restTemplate = new RestTemplate();
//		JsonNode loanDetails = restTemplate.getForObject(url, JsonNode.class);
//
//		loanDetails.forEach(entry -> {
//			Loan data = new Loan();
//			data.setUserId(entry.get("userId").asText());
//			data.setMonthlyIncome(entry.get("monthlyIncome").asInt());
//			data.setExistingLoanObligations(entry.get("existingLoanObligations").asInt());
//			data.setCreditScore(entry.get("creditScore").asInt());
//			data.setRequestedLoanAmount(entry.get("requestedLoanAmount").asInt());
//
//			loanRepository.save(data);
//		});
//	}
//
//	public LoanResponse calculateEligibility(Loan loan) {
//
//		int monthlyIncome = loan.getMonthlyIncome();
//		int existingLons = loan.getExistingLoanObligations();
//		int creditScore = loan.getCreditScore();
//		int requestedAmount = loan.getRequestedLoanAmount();
//
//		LoanResponse response = new LoanResponse();
//
//		if(monthlyIncome < 30000) {
//			response.setEligible(false);
//			response.setRejectionReason("Monthly income is below the required minimum.");
//			return response;
//		}
//
//		if(existingLons > monthlyIncome * 0.4) {
//			response.setEligible(false);
//			response.setRejectionReason("Existing loan obligations exceed 40% of monthly income.");
//			return response;
//		}
//
//		if(creditScore < 700) {
//			response.setEligible(false);
//			response.setRejectionReason("Credit score is below 700.");
//			return response;
//		}
//
//		int maxLoanAmount = 10 * monthlyIncome;
//		if(requestedAmount > maxLoanAmount) {
//			response.setEligible(false);
//			response.setRejectionReason("Requested loan amount exceeds the maximum allowed limit.");
//			return response;
//		}
//
//		Map<String, Integer> emiBreakDown = Map.of(
//				"8%", calculateEMI(requestedAmount, 8, 12),
//				"10%", calculateEMI(requestedAmount, 10, 12),
//				"12%", calculateEMI(requestedAmount, 12, 12)
//		);
//
//		response.setEligible(true);
//		response.setApprovedLoanAmount(requestedAmount);
//		response.setEmiBreakdown(emiBreakDown);
//
//		return response;
//	}
//
//	private int calculateEMI(int principal, double rate, int months) {
//		double monthlyRate = rate / (12 * 100);
//		return (int) ((principal * monthlyRate) / (1 - Math.pow(1 + monthlyRate, -months)));
//	}
//
//}





package com.project.Loan.Eligibility.Application.service;

import com.project.Loan.Eligibility.Application.entity.Loan;
import com.project.Loan.Eligibility.Application.entity.LoanResponse;
import com.project.Loan.Eligibility.Application.entity.LoanStatistics;
import com.project.Loan.Eligibility.Application.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoanService {

	// Declare the URL as a class-level constant
	private static final String URL = "https://prepstripe.com/loan_task_payloads.json";  // This should be your actual URL

	@Autowired
	private LoanRepository loanRepository;

	public void fetchAndSaveData() {
		// Initialize RestTemplate to make the API call
		RestTemplate restTemplate = new RestTemplate();

		// Fetch the loan details from the URL
		JsonNode loanDetails = restTemplate.getForObject(URL, JsonNode.class);

		// Iterate through the loan details and save to the database
		loanDetails.forEach(entry -> {
			Loan data = new Loan();
			data.setUserId(entry.get("userId").asText());
			data.setMonthlyIncome(entry.get("monthlyIncome").asInt());
			data.setExistingLoanObligations(entry.get("existingLoanObligations").asInt());
			data.setCreditScore(entry.get("creditScore").asInt());
			data.setRequestedLoanAmount(entry.get("requestedLoanAmount").asInt());

			// Save each loan to the database
			loanRepository.save(data);
		});
	}

	public LoanResponse calculateEligibility(Loan loan) {
		int monthlyIncome = loan.getMonthlyIncome();
		int existingLoanObligations = loan.getExistingLoanObligations();
		int creditScore = loan.getCreditScore();
		int requestedAmount = loan.getRequestedLoanAmount();

		LoanResponse response = new LoanResponse();

		if (monthlyIncome < 30000) {
			response.setEligible(false);
			response.setRejectionReason("Monthly income is below the required minimum.");
			return response;
		}

		if (existingLoanObligations > monthlyIncome * 0.4) {
			response.setEligible(false);
			response.setRejectionReason("Existing loan obligations exceed 40% of monthly income.");
			return response;
		}

		if (creditScore < 700) {
			response.setEligible(false);
			response.setRejectionReason("Credit score is below 700.");
			return response;
		}

		int maxLoanAmount = 10 * monthlyIncome;
		if (requestedAmount > maxLoanAmount) {
			response.setEligible(false);
			response.setRejectionReason("Requested loan amount exceeds the maximum allowed limit.");
			return response;
		}

		Map<String, Integer> emiBreakdown = Map.of(
				"8%", calculateEMI(requestedAmount, 8, 12),
				"10%", calculateEMI(requestedAmount, 10, 12),
				"12%", calculateEMI(requestedAmount, 12, 12)
		);

		response.setEligible(true);
		response.setApprovedLoanAmount(requestedAmount);
		response.setEmiBreakdown(emiBreakdown);

		return response;
	}

	private int calculateEMI(int principal, double rate, int months) {
		double monthlyRate = rate / (12 * 100);
		return (int) ((principal * monthlyRate) / (1 - Math.pow(1 + monthlyRate, -months)));
	}

	public LoanStatistics getLoanStatistics() {
		List<Integer> approvedLoanAmounts = loanRepository.findApprovedLoanAmounts();
		List<Object[]> rejectionReasonCounts = loanRepository.findRejectionReasons();

		double averageApprovedLoanAmount = approvedLoanAmounts.stream().mapToInt(Integer::intValue).average().orElse(0);
		Map<String, Integer> rejectionReasons = new HashMap<>();
		for (Object[] result : rejectionReasonCounts) {
			rejectionReasons.put((String) result[0], ((Long) result[1]).intValue());
		}

		return new LoanStatistics(averageApprovedLoanAmount, rejectionReasons);
	}
}
