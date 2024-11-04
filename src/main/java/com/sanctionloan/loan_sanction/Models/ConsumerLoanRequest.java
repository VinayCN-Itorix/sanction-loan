package com.sanctionloan.loan_sanction.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerLoanRequest {
private int loanAmount;
private String loanProductId;
private String sourceBranchId;
private String consumerId;
}
