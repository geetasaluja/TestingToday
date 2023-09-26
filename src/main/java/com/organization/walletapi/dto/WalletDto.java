package com.organization.walletapi.dto;

import java.math.BigDecimal;

/**
 *
 * @author Geeta Saluja
 */
public class WalletDto extends BaseDto{

	private BigDecimal walletBalance;
   	public WalletDto() {}	
	public WalletDto(Long walletId, Long playerId,String playerName,BigDecimal walletBalance) {
		super(walletId, playerId,playerName);
		this.walletBalance = walletBalance;
	}
	public BigDecimal getWalletBalance() {
		return walletBalance;
	}
	public void setWalletBalance(BigDecimal walletBalance) {
		this.walletBalance = walletBalance;
	}
}
