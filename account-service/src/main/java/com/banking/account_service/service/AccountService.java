package com.banking.account_service.service;

import java.math.BigDecimal;
import java.security.SecureRandom;

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
 
 private static SecureRandom secureRandom=new SecureRandom();
 
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
 
 /*
  * get account by account number
  * */
 
 public AccountResponse getAccount(String accountNumber) {
	 Account account=accountRepository.findByAccountNumber(accountNumber)
			 			.orElseThrow(() -> new RuntimeException("Account not found"));
	 return mapToResponse(account);
 }
 
 
 /*
  * 
  * get account balance
  * 
  * */
 
 public BigDecimal getBalance(String accountNumber) {
	 Account account=accountRepository.findByAccountNumber(accountNumber)
			 			.orElseThrow(() -> new RuntimeException("Account not found"));
	 return account.getBalance();
 }

 /*
  * Block Account-called by fraud detection service via kafka
  * 
  * */
 public void blockAccount(String accountNumber) {
	log.info("Blocking the account:{}",accountNumber); 
	 Account account=accountRepository.findByAccountNumber(accountNumber)
	 			.orElseThrow(() -> new RuntimeException("Account not found"));
	 account.setAccountStatus(AccountStatus.BLOCKED);
	 accountRepository.save(account);
	 log.info("account blocked: {}" , accountNumber);
	
	
 }
 
 /*
  * deduct balance from sender account.
  * it is called by transaction service.
  * 
  * */
 public void deductBalance(String accountNumber , BigDecimal amount) {
	 log.info("deducting balance{} from account: {}",amount,accountNumber);
 
	 Account account=accountRepository.findByAccountNumber(accountNumber)
	 			.orElseThrow(() -> new RuntimeException("Account not found"));
	if(account.getAccountStatus() !=AccountStatus.ACTIVE) {
		throw new RuntimeException("Account is not active"+accountNumber);
	}
	
	if(account.getBalance().compareTo(amount)<0) {
		throw new RuntimeException("insufficient funds for account "+accountNumber);
	}
	account.setBalance(account.getBalance().subtract(amount));
	accountRepository.save(account);
	log.info("Balance updated. new balance: {}",account.getBalance());
	
 }
 
 
 /*
  * Credit balance 
  * called by transaction service via kafka
  * 
  * */
 public void creditBalance(String accountNumber,BigDecimal amount) {
	 log.info("crediting {} to account: {}",amount,accountNumber);
	 
	 Account account=accountRepository.findByAccountNumber(accountNumber)
	 			.orElseThrow(() -> new RuntimeException("Account not found"));
	
	 account.setBalance(account.getBalance().add(amount));
	 accountRepository.save(account);
	 
	 log.info("balance credited, new balance: {}",account.getBalance());
	 
 }
 
 
 
 //generate unique 12 digit account number
 private String generateAccountNumber() {
	 
	 String accountNumber;
	 
	 do {
		 
		 long number=secureRandom.nextLong(1_000_000_000_000L);

		 accountNumber=String.format("%012d", number);
		 
	 }while(accountRepository.existsByAccountNumber(accountNumber));
	 
	 return accountNumber;
 }
 
 
 private AccountResponse mapToResponse(Account account) {
	 AccountResponse response=new AccountResponse();
	 response.setId(account.getId());
	 response.setAccountNumber(account.getAccountNumber());
	 response.setAccountHolderName(account.getAccountHolderName());
	 response.setEmail(account.getEmail());
	 response.setPhone(account.getPhone());
	 response.setAccountType(account.getAccountType());
	 response.setAccountStatus(account.getAccountStatus());
	 response.setBalance(account.getBalance());
	 response.setDailyTransactionLimit(account.getDailyTransactionLimit());
	 response.setCreatedAt(account.getCreatedAt());
	 
	 
	 return response;
	 }
}
