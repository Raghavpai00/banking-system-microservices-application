package com.banking.account_service.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.banking.account_service.dto.AccountResponse;
import com.banking.account_service.dto.CreateAccountRequest;
import com.banking.account_service.entity.Account;
import com.banking.account_service.entity.AccountStatus;
import com.banking.account_service.entity.AccountType;
import com.banking.account_service.repository.AccountRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {
 private final AccountRepository accountRepository;
 
 public AccountResponse createAccount(CreateAccountRequest request) {
	 log.info("creating account for: {}" , request.getEmail());
	 if(accountRepository.existsByEmail(request.getEmail())) {
		 throw new RuntimeException("Account already exists for email: "+request.getEmail());
	 }
	 
	 Account account=new Account();
	account.setAccountHolderName(request.getAccountHolderName());		 
	account.setEmail(request.getEmail());
	account.setPhone(request.getPhone());
	account.setAccountType(request.getAccountType());
	account.setAccountStatus(AccountStatus.ACTIVE);
	account.setBalance(request.getInitialDeposit());
	
	//important logic
	
	account.setAccountNumber(generateAccountNumber());
	account.setDailyTransactionLimit(
			request.getAccountType()==AccountType.SAVINGS
			? new BigDecimal("100000")
			:new BigDecimal("500000")
			);
	
	Account savedAccount= accountRepository.save(account);
	log.info("Account created: {}",savedAccount.getAccountNumber());
	return mapToResponse(savedAccount);
 }
}
