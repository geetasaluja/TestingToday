package com.organization.walletapi.service.impl;

import java.math.BigDecimal;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.organization.walletapi.domain.entities.Wallet;
import com.organization.walletapi.dto.OutputDto;
import com.organization.walletapi.dto.TransactionDto;
import com.organization.walletapi.dto.TransactionSetDto;
import com.organization.walletapi.dto.WalletDto;
import com.organization.walletapi.facade.TransactionFacade;
import com.organization.walletapi.facade.WalletFacade;
import com.organization.walletapi.exceptions.NoEnoughBalanceException;
import com.organization.walletapi.exceptions.NoUniqueCallerSupplyTransIdException;
import com.organization.walletapi.exceptions.WalletException;
import com.organization.walletapi.exceptions.exceptionhandler.ErrorMessage;
import com.organization.walletapi.domain.entities.Transaction;
import com.organization.walletapi.repository.TransactionRepository;
import com.organization.walletapi.service.WalletService;
import com.organization.walletapi.service.TransactionService;

/**
 *
 * @author Geeta Saluja
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TransactionServiceImpl implements TransactionService {	
	public static final String DEBIT = "DEBIT";
	
	@Autowired
    private TransactionRepository transactionRepository;
	@Autowired
	private WalletService walletService;
	@Autowired
	private TransactionFacade transactionFacade;
	@Autowired
	private WalletFacade walletFacade;
	
	/**
	 * Get transaction history per player id
	 *  
	 * @param playerId
	 * @return TransactionSetDto
	 */
	@Override
    public TransactionSetDto getTransactionByPlayerId(Long playerId)  {
		WalletDto walletDto =walletService.findByPlayerId(playerId);
		Wallet wallet = walletFacade.convertToWallet(walletDto);
    	Set<Transaction> transactions = transactionRepository.findByWallet(wallet);
        return transactionFacade.domainToDtoTransactionSet(transactions,wallet);
    }
	
	/**
	 * Register for transaction on behalf of player and perform
	 * debit/credit transaction and check for validations:
	 * - validates no mismatch between given player id and given wallet id
	 * - validates for unique callerSupplyTransId
	 * - validates for sufficient balance in wallet to perform Debit transaction
	 * 
	 * @param playerId
	 * @param transactionDto
	 * @return OutputDto
     * @throws NotEnoughBalanceException 
     * @throws NotUniqueTransactionRefIdException 
	 */
	@Override
	public OutputDto registerTransaction(Long playerId,TransactionDto transactionDto) throws NoEnoughBalanceException, NoUniqueCallerSupplyTransIdException {
		Wallet wallet = getValidWalletData(playerId,transactionDto);
		validateUniqueCallerSupplyTransId(transactionDto.getCallerSupplyTransId());
		if((DEBIT).equals(transactionDto.getTransactionType())) {
			checkBalance(transactionDto,wallet);
		}
		return insertTransaction(transactionDto, wallet);
	}
	
	/**
	 * Update wallet balance with new balance to wallet table and also insert transaction
	 * record to transaction table
	 * 
	 * @param transactionDto
	 * @param wallet
	 * @return OutputDto
	 */
	public OutputDto insertTransaction(TransactionDto transactionDto, Wallet wallet) {
		BigDecimal transAmt= transactionDto.getTransactionAmount().abs();
		BigDecimal previousBalance= wallet.getWalletBalance().abs();
		BigDecimal currentBalance = (DEBIT).equals(transactionDto.getTransactionType())? previousBalance.subtract(transAmt):previousBalance.add(transAmt);
		Wallet updatedWallet = walletService.updateWalletById(currentBalance, wallet.getWalletId());
		Transaction transaction = transactionFacade.convertToTransactionDomain(transactionDto);
		Transaction updatedTransaction = transactionRepository.save(transaction);
		return transactionFacade.convertToOutputDto(updatedTransaction, updatedWallet,previousBalance);
	}
	
	/** Check if given player id is associated with given wallet id
	 *  If there is mismatch it will throw WalletException.
	 * 
	 * @param playerId
	 * @param transactionDto
	 * @return Wallet  
	 */
	public Wallet getValidWalletData(Long playerId,TransactionDto transactionDto) {
		WalletDto walletDto =walletService.findByPlayerId(playerId);
		Wallet wallet = walletFacade.convertToWallet(walletDto);
		if(!wallet.getWalletId().equals(transactionDto.getWalletId())) {
			throw new WalletException (String.format(ErrorMessage.WALLET_PLAYER_MISMATCH , transactionDto.getWalletId(),playerId));			
		}
		return wallet;
	}
	
    /** Check if balance is sufficient to perform Debit transaction
	 * 
	 * @param transactionDto
	 * @param wallet
	 * @return boolean
	 * @throws :throw NoEnoughBalanceException if transaction amount is greater than available balance in wallet
	 */
	public boolean checkBalance(TransactionDto transactionDto, Wallet wallet) throws  NoEnoughBalanceException{
		BigDecimal transAmt= transactionDto.getTransactionAmount().abs();
		BigDecimal balance= wallet.getWalletBalance().abs();
		if (balance.compareTo(transAmt)<0) {
			throw new NoEnoughBalanceException (String.format(ErrorMessage.NO_ENOUGH_FUND_FOUND, balance,transAmt));	
		}
		return true;
	}

	/**
	 * Validate for unique callerSupplyTransId
	 * 
	 * @param callerSupplyTransId
	 * @return boolean
	 * @throws :throw NoUniqueCallerSupplyTransIdException if caller supplied external transaction id is already present
	 */
	public boolean validateUniqueCallerSupplyTransId(String callerSupplyTransId) throws NoUniqueCallerSupplyTransIdException{
		if(transactionRepository.findByCallerSupplyTransId(callerSupplyTransId).isPresent()) {
			throw new NoUniqueCallerSupplyTransIdException (String.format(ErrorMessage.NO_UNIQUE_TRANSACTION_REFERENCE_ID, callerSupplyTransId.toString() ));
		}
		return true;
	}
}

