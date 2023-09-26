package com.organization.walletapi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author Geeta Saluja
 */
public class OutputDto extends BaseDto{
	private String callerSupplyTransId;
	private BigDecimal oldBalance;
	private BigDecimal newBalance;   
    private LocalDateTime transactionDate;    
    public OutputDto() {}
    public OutputDto(String callerSupplyTransId, Long walletId,Long playerId,String playerName, BigDecimal oldBalance,BigDecimal newBalance,LocalDateTime transactionDate) {
    	super(walletId, playerId,playerName);
		this.callerSupplyTransId = callerSupplyTransId;
		this.oldBalance = oldBalance;
		this.newBalance = newBalance;
		this.transactionDate = transactionDate;
	}
    public String getCallerSupplyTransId() {
		return callerSupplyTransId;
	}
	public void setCallerSupplyTransId(String callerSupplyTransId) {
		this.callerSupplyTransId = callerSupplyTransId;
	}public LocalDateTime getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}	public BigDecimal getOldBalance() {
		return oldBalance;
	}
	public void setOldBalance(BigDecimal oldBalance) {
		this.oldBalance = oldBalance;
	}
	public BigDecimal getNewBalance() {
		return newBalance;
	}
	public void setNewBalance(BigDecimal newBalance) {
		this.newBalance = newBalance;
	}
}
