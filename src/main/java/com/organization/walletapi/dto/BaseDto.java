package com.organization.walletapi.dto;

/**
 *
 * @author Geeta Saluja
 */
public class BaseDto {
	private Long playerId;
    private String playerName;
    private Long walletId;
   	public BaseDto() { }    
	public BaseDto(Long walletId, Long playerId,String playerName) {
		this.playerId = playerId;		
		this.walletId = walletId;
		this.playerName = playerName;
	}
	public Long getWalletId() {
		return walletId;
	}
	public void setWalletId(Long walletId) {
		this.walletId = walletId;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
  	public Long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}
}
