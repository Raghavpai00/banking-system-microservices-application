package com.banking.transaction_service.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="transaction")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

}
