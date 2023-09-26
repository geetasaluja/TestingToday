package com.organization.walletapi.domain.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author Geeta Saluja
 */
@Entity
public class Player {	
	@Id
	@GeneratedValue
    private Long playerId;
    private String playerName;
    public Player() {}  
    public Player(Long playerId, String playerName) {
    	this.playerId = playerId;
        this.playerName = playerName;
    }
    public Long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}   
}
