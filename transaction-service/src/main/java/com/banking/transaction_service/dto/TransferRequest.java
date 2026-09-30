package com.banking.transaction_service.dto;


import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class TransferRequest {
	
@NotBlank(message="Sender account number is required")
private String SenderAccountNumber;

@NotBlank(message="Receiver account number is required")
private String receiverAccountNumber;

@NotNull(message="amount is required")
@Positive(message="amount must be positive")
private BigDecimal amount;


private String description;
}
