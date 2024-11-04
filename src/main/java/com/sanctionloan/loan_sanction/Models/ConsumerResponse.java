package com.sanctionloan.loan_sanction.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsumerResponse {
private int id;
private double loanAmount;
private String loanProductId;
private String sourceBranchId;
private String consumerId;
private String status;
private LoanType loanType;
private String cts;
private String mts;


}
