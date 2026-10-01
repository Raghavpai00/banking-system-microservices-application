package com.banking.transaction_service.service;

import org.springframework.stereotype.Service;

import com.banking.transaction_service.Repository.TransactionRepository;
import com.banking.transaction_service.dto.TransactionResponse;
import com.banking.transaction_service.dto.TransferRequest;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@AllArgsConstructor
public class TransactionService {
private final TransactionRepository transactionRepository;

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
public TransactionResponse transafer(TransferRequest request) {
	
	//SAGA step-1 : initiate transfer
	log.info("SAGA START - Transfer: {} -> {} amount: {}",
	request.getSenderAccountNumber(),
	request.getReceiverAccountNumber(),
	request.getAmount());
	
	//deduct from sender via feign client
	
}

}
