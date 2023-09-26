package com.organization.walletapi.service;

import com.organization.walletapi.dto.OutputDto;
import com.organization.walletapi.dto.TransactionDto;
import com.organization.walletapi.dto.TransactionSetDto;
import com.organization.walletapi.exceptions.NoEnoughBalanceException;
import com.organization.walletapi.exceptions.NoUniqueCallerSupplyTransIdException;
import com.organization.walletapi.exceptions.WalletException;

/**
 *
 * @author Geeta Saluja
 */
public interface TransactionService {
	public TransactionSetDto getTransactionByPlayerId(Long playerId) throws WalletException;
	public OutputDto registerTransaction(Long playerId,TransactionDto transactionDto) throws NoUniqueCallerSupplyTransIdException,NoEnoughBalanceException;
}
