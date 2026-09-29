package com.banking.account_service.service;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
 * flow=when transaction service publish the event of transaction
 * initiated the account event consumer consume that event
 * */

@Service
@Slf4j
@RequiredArgsConstructor

public class AccountEventConsumer {

	private final AccountService accountService;
	
	/*
	 * Consumer transaction.completed event from kafka
	 * this is a credits receiver account
	 * @Param = payload
	 * this method exicute when fraud is not detected
	 * */
	
	@KafkaListener(topics="transaction.completed")
	public void consumeTransactionCompleted(
			@Payload Map<String,Object> payload) {
		
		try{
			String receiverAccount=(String)payload.get("receiverAccountNumber");
			BigDecimal amount=new BigDecimal(payload.get("amount").toString());
			
			log.info("crediting account: {} amount: {}",receiverAccount,amount);
			accountService.creditBalance(receiverAccount, amount);
		}catch(Exception e) {
			log.error("error crediting account:{}",e.getMessage());
		}
		
	}
	/*
	 * Consume fraud detected event from kafka
	 * block the flagged account
	 * @param payload
	 * 
	 * */
	
	@KafkaListener(topics="fraud.detected")
	public void consumeFraudDetected(
			@Payload Map<String,Object> payload) {
		
		try {
			
			String accountNumber=(String)payload.get("accountNumber");
			log.info("Fraud detected - blocking account: {}",accountNumber);
			
			accountService.blockAccount(accountNumber);
			
			
		}catch(Exception e) {
		log.error("Error blocking accouunt: {}",e.getMessage());	
		}
		
	}
	
}
