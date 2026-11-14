package com.banking.transaction_service.event;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionInitiatedEvent {

	private String transactionId;
	private String SenderAccountNumber;
	private String receiverAccountNumber;
	private BigDecimal amount;
	private String description;
}
