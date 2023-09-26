package com.organization.walletapi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.organization.walletapi.exceptions.exceptionhandler.ErrorMessage;

/**
 *
 * @author Geeta Saluja
 */
public class TransactionDto {
	@NotBlank(message=ErrorMessage.CALLER_SUPPLY_TRANSACTION_ID_NOTNULL)
	private String callerSupplyTransId;
	private String transactionType;
	@NotNull(message=ErrorMessage.TRANSACTION_AMOUNT_NOTNULL)
	private BigDecimal transactionAmount;  
	@NotNull(message=ErrorMessage.TRANSACTION_DATE_NOTNULL)
    	private LocalDateTime transactionDate;
    	@NotNull(message=ErrorMessage.WALLET_ID_NOTNULL)
    	private Long walletId;
    
    public TransactionDto() {}
	public TransactionDto(String callerSupplyTransId, Long walletId, BigDecimal transactionAmount,String transactionType,LocalDateTime transactionDate) {
		this.callerSupplyTransId = callerSupplyTransId;
		this.walletId = walletId;		
		this.transactionAmount = transactionAmount;
		this.transactionType = transactionType;
		this.transactionDate = transactionDate;
	}
	public Long getWalletId() {
		return walletId;
	}
	public void setWalletId(Long walletId) {
		this.walletId = walletId;
	}	
	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}
	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getCallerSupplyTransId() {
		return callerSupplyTransId;
	}

	public void setCallerSupplyTransId(String callerSupplyTransId) {
		this.callerSupplyTransId = callerSupplyTransId;
	}	
}
