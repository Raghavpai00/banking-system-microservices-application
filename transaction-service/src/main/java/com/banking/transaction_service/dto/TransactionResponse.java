package com.banking.transaction_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.banking.transaction_service.Entity.TransactionStatus;
import com.banking.transaction_service.Entity.TransactionType;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {

	private String id;
	private String SenderAccountNumber;
	private String receiverAccountNumber;
	private BigDecimal amount;
	private TransactionType type;
	private TransactionStatus status;
	private String description;
	private String failureReason;
	private String referenceNumber;
	private LocalDateTime createdAt;
	private LocalDateTime completedAt;
	
	
	
	
	
}
