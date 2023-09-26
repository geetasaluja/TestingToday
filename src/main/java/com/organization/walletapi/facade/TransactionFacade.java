package com.organization.walletapi.facade;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import com.organization.walletapi.domain.entities.Transaction;
import com.organization.walletapi.domain.entities.Wallet;
import com.organization.walletapi.dto.OutputDto;
import com.organization.walletapi.dto.TransactionDto;
import com.organization.walletapi.dto.TransactionSetDto;

/**
 *
 * @author Geeta Saluja
*/
@Component
public class TransactionFacade {
	private  ModelMapper modelMapper = new ModelMapper();
	public  TransactionDto convertToTransactionDto(Transaction transaction) {
		TransactionDto transactionDto = modelMapper.map(transaction, TransactionDto.class);
		return transactionDto;
    }
	public  Transaction convertToTransactionDomain(TransactionDto transactionDto) {
		Transaction transaction = modelMapper.map(transactionDto, Transaction.class);
		transaction.setTransactionId(0L);
		return transaction;
    }
	public  OutputDto convertToOutputDto(Transaction transaction,Wallet wallet, BigDecimal previousBalance) {
		OutputDto outputDto = modelMapper.map(transaction, OutputDto.class);
		outputDto.setPlayerId(wallet.getPlayer().getPlayerId());
		outputDto.setPlayerName(wallet.getPlayer().getPlayerName());
		outputDto.setOldBalance(previousBalance);
		outputDto.setNewBalance(wallet.getWalletBalance());
		return outputDto;
    }
	public  TransactionSetDto domainToDtoTransactionSet(Set<Transaction> transactions,Wallet wallet) {
		Set<TransactionDto> transactionDtos = transactions.stream().map(transaction -> modelMapper.map(transaction, TransactionDto.class)).collect(Collectors.toSet());
	    return convertToTransactionSetDto(transactionDtos,wallet);
	}
	public TransactionSetDto convertToTransactionSetDto (Set<TransactionDto> transactionDtos, Wallet wallet) {
		TransactionSetDto transactionSetDto = new TransactionSetDto();
		transactionSetDto.setPlayerId(wallet.getPlayer().getPlayerId());
    	transactionSetDto.setPlayerName(wallet.getPlayer().getPlayerName());
    	transactionSetDto.setWalletId(wallet.getWalletId());
    	transactionSetDto.setTransactions(transactionDtos);
    	return transactionSetDto;
    }
}
