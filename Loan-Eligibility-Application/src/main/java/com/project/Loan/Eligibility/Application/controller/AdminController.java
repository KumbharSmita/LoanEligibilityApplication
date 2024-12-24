package com.project.Loan.Eligibility.Application.controller;

import com.project.Loan.Eligibility.Application.entity.LoanStatistics;
import com.project.Loan.Eligibility.Application.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private LoanService loanService;

    @GetMapping("/loan-statistics")
    public ResponseEntity<LoanStatistics> getLoanStatistics() {
        LoanStatistics statistics = loanService.getLoanStatistics();
        return ResponseEntity.ok(statistics);
    }
}
