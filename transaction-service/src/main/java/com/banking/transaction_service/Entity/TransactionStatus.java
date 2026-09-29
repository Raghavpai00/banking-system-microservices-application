package com.banking.transaction_service.Entity;

/*
 * Transaction Lifecycle flow:
 * 
 * PENDING -> PROCESSING ->COMPLETED(clean transaction)
 * 
 * 						 ->PENDING_VERIFACTION(suspicious detected)
 * 						 		->COMPLETED(verified)
 * 								->FLAGGED(SAGA REFUND)
 * 
 * 						->FAILED
 * 						->FLAGGED
 * 
 * 
 * 
 * */
public enum TransactionStatus {
	PENDING,
	PROCESSING,
	PENDING_VERIFACTION,
	FAILED,
	FLAGGED,
	COMPLETED
}
