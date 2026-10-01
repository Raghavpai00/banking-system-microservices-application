package com.banking.transaction_service.client;

import java.math.BigDecimal;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface AccountServiceClient {

	@PutMapping("/api/v1/accountsa/{accountNumber}/deduct")
	String deductBalance(
			@PathVariable String accountNumber,
			@RequestParam BigDecimal amount)
}
