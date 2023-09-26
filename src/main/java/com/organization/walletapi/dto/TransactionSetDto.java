package com.organization.walletapi.dto;

import java.util.Set;

/**
 *
 * @author Geeta Saluja
 */
public class TransactionSetDto extends BaseDto {
	Set<TransactionDto> transactions;
    public Set<TransactionDto> getTransactions() {
		return transactions;
	}
	public void setTransactions(Set<TransactionDto> transactions) {
		this.transactions = transactions;
	}
	public TransactionSetDto() {}
	public TransactionSetDto(Long walletId, Long playerId,String playerName,Set<TransactionDto> transactions) {
		super(walletId, playerId,playerName);
		this.transactions = transactions;
	}
	public TransactionSetDto(Set<TransactionDto> transactions) {
    	super();
    	this.transactions=transactions;
    }
}
