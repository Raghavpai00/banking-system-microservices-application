package com.banking.account_service.dto;

import java.math.BigDecimal;

import com.banking.account_service.entity.AccountType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequest {
	
	@NotBlank(message="Account holder name is required")
	private String accountHolderName;
	
	@NotBlank(message="Email is required")
	@Email(message = "Please provide a valid email address")
	private String email;
	
	@NotBlank(message="Phone is required")
	private String phone;
	
	@NotNull(message="Account type is required")
	private AccountType accountType;
	
	@NotNull(message="initial Deposit is required")
	@Positive(message="initial deposit must be positive")
	private BigDecimal initialDeposit;

}
