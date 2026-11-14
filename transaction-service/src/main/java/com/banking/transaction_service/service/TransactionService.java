package com.banking.transaction_service.service;

import org.apache.kafka.common.Uuid;
import org.springframework.stereotype.Service;

import com.banking.transaction_service.Entity.Transaction;
import com.banking.transaction_service.Entity.TransactionStatus;
import com.banking.transaction_service.Entity.TransactionType;
import com.banking.transaction_service.Repository.TransactionRepository;
import com.banking.transaction_service.client.AccountServiceClient;
import com.banking.transaction_service.dto.TransactionResponse;
import com.banking.transaction_service.dto.TransferRequest;
import com.banking.transaction_service.event.TransactionInitiatedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor

public class TransactionService {
	
private final TransactionRepository transactionRepository;
private final AccountServiceClient accountServiceClient;

private final kafkaTemplate<String,Object> kafkaTemplate;

//kafka topic variables

private static final String TRANSACTION_INITIATED_TOPIC="transaction_initiated";
private static final String TRANSACTION_COMPLETED_TOPIC="transaction_completed";
private static final String TRANSACTION_REFUNDED_TOPIC="transaction_refunded";


/*
 * SAGA step-1 : initiate transfer
 * deduct from sender via feign client
 * saves transaction as PROCESSIG
 * publish event to kafka for fraud check
 * @param = request
 * @return
 * */

public TransactionResponse transfer(TransferRequest request) {
	
	//SAGA step-1 : initiate transfer
	log.info("SAGA START - Transfer: {} -> {} amount: {}",
	request.getSenderAccountNumber(),
	request.getReceiverAccountNumber(),
	request.getAmount()
	);
	
	//deduct from sender via feign client
	AccountServiceClient.deductBalance(
			request.getSenderAccountNumber(),
			request.getAmount());
	
	Transaction transaction=new Transaction();
	transaction.setSenderAccountNumber(request.getSenderAccountNumber());
	transaction.setReceiverAccountNumber(request.getReceiverAccountNumber());
	transaction.setAmount(request.getAmount());
	transaction.setType(TransactionType.TRANSFER);  
	transaction.setStatus(TransactionStatus.PROCESSING);
	transaction.setDescription(request.getDescription());
	transaction.setReferenceNumber(Uuid.randomUuid().toString());
	
	Transaction savedTransaction=transactionRepository.save(transaction);
	log.info("Transaction saved as PROCESSING: {}",savedTransaction.getId());

	//  SAGA STEP 2 : publish event to kafka for fraud check
	
	TransactionInitiatedEvent event=new TransactionInitiatedEvent(
			savedTransaction.getId(),
			savedTransaction.getSenderAccountNumber(),
			savedTransaction.getReceiverAccountNumber(),
			savedTransaction.getAmount(),
			savedTransaction.getDescription()
			);
	kafkaTemplate.send(TRANSACTION_INITIATED_TOPIC,savedTransaction.getId(),event);
	log.info("SAGA STEP 2 - TransactionInitiatedEvent published: {}", savedTransaction.getId());
	
	return mapToResponse(savedTransaction);
}

private TransactionResponse mapToResponse(Transaction transaction) {
	TransactionResponse response=new TransactionResponse();
	response.setId(transaction.getId());
	response.setSenderAccountNumber(transaction.getSenderAccountNumber());
	response.setReceiverAccountNumber(transaction.getReceiverAccountNumber());
	response.setAmount(transaction.getAmount());
	response.setType(transaction.getType());
	response.setStatus(transaction.getStatus());
	response.setDescription(transaction.getDescription());
	response.setReferenceNumber(transaction.getReferenceNumber());
	response.setFailureReason(transaction.getFailureReason());
	response.setCreatedAt(transaction.getCreatedAt());
	response.setCompletedAt(transaction.getCompletedAt());
	
	return response;
	
}

}
