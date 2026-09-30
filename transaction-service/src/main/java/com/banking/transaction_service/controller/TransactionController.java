package com.banking.transaction_service.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.transaction_service.Entity.Transaction;
import com.banking.transaction_service.dto.TransactionResponse;
import com.banking.transaction_service.dto.TransferRequest;
import com.banking.transaction_service.service.TransactionService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/transaction")
@Slf4j
public class TransactionController {

	private final TransactionService transactionService;
	
	@PostMapping("/transfer")
	public ResponseEntity<TransactionResponse> transafer(
		@Valid @RequestBody TransferRequest request	){
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(transactionService.transfer(request));
		
	}
	@GetMapping("/{transactionId}")
	public ResponseEntity<Transaction> getTransaction(
			@PathVariable String transactionId){
		
		return ResponseEntity.ok(transactionService.getTransaction(transactionId));
	}
	
}
