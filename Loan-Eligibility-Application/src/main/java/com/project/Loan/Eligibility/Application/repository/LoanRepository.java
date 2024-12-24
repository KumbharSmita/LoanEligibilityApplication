//package com.project.Loan.Eligibility.Application.repository;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import com.project.Loan.Eligibility.Application.entity.Loan;
//
//public interface LoanRepository extends JpaRepository<Loan, String> {
//
//}


package com.project.Loan.Eligibility.Application.repository;

import com.project.Loan.Eligibility.Application.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, String> {

    @Query("SELECT l.approvedLoanAmount FROM Loan l WHERE l.approvedLoanAmount IS NOT NULL")
    List<Integer> findApprovedLoanAmounts();

    @Query("SELECT l.rejectionReason, COUNT(l) FROM Loan l WHERE l.rejectionReason IS NOT NULL GROUP BY l.rejectionReason")
    List<Object[]> findRejectionReasons();
}
