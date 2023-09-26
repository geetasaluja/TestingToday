package com.organization.walletapi.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.organization.walletapi.domain.entities.Wallet;
import com.organization.walletapi.dto.WalletDto;
import com.organization.walletapi.facade.WalletFacade;
import com.organization.walletapi.exceptions.WalletException;
import com.organization.walletapi.exceptions.exceptionhandler.ErrorMessage;
import com.organization.walletapi.repository.WalletRepository;
import com.organization.walletapi.service.WalletService;

/**
 *
 * @author Geeta Saluja
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WalletServiceImpl implements WalletService {
	
	@Autowired
    private WalletRepository walletRepository;	
	@Autowired
    private WalletFacade walletFacade;	
	@Override
    public List<WalletDto> findAll()  {
		return walletFacade.domainToDtoWalletList(walletRepository.findAll());
    }
	
    @Override
    public WalletDto findByPlayerId(Long playerId)  {
    	Optional<Wallet> wallet =  walletRepository.findByPlayerId(playerId);
    	checkWallet(wallet);
    	return walletFacade.convertToWalletDto( wallet.get());
    }
    
    /**
	 * Update wallet balance with new current balance to wallet table
	 * 
	 * @param currentBalance
	 * @param walletId
	 * @return Wallet
	 */
    @Override
    public Wallet updateWalletById(BigDecimal currentBalance, Long walletId) {
    	Optional<Wallet> wallet =  walletRepository.findByWalletId(walletId);
    	checkWallet(wallet);
       	wallet.get().setWalletBalance(currentBalance);
    	return walletRepository.save(wallet.get());
   	}
   
    @Override
    public WalletDto findByWalletId(Long walletId)  {
   	Optional<Wallet> wallet =  walletRepository.findByWalletId(walletId);
    	checkWallet(wallet);
    	return walletFacade.convertToWalletDto( wallet.get());
    }
    
    /**
	 * Check if wallet is not present throw WalletException
	 * 
	 * @param wallet
	 * @return boolean
     */
    public boolean checkWallet(Optional<Wallet> wallet) {
    	if(wallet.isPresent()) {
    		return true;
    	}
    	throw new WalletException (String.format(ErrorMessage.NO_WALLET_FOUND));
	}
}

