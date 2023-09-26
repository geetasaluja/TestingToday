package com.organization.walletapi.service;

import java.math.BigDecimal;
import java.util.List;
import com.organization.walletapi.domain.entities.Wallet;
import com.organization.walletapi.dto.WalletDto;
import com.organization.walletapi.exceptions.WalletException;

/**
 *
 * @author Geeta Saluja
 */
public interface WalletService {
	public List<WalletDto> findAll();
	public WalletDto findByPlayerId(Long playerId);
	public WalletDto findByWalletId(Long walletId);
	public Wallet updateWalletById(BigDecimal currentBalance, Long walletId) throws WalletException;
}
