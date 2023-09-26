package com.organization.walletapi.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
*
* @author Geeta Saluja
*/
@Entity
public class Wallet {
	
	@Id
	@GeneratedValue
    private Long walletId;
	private LocalDateTime walletDate;
    private BigDecimal walletBalance;
   
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playerId", unique=true,nullable = false)
    private Player player;
    
    @OneToMany(mappedBy = "wallet")
    @JsonManagedReference
    private Set<Transaction> transactions;
    
    // For concurrency control
    @Version
    private int version;    
    public Wallet() {}
    public Wallet(Long walletId, BigDecimal walletBalance) {
        this.walletId = walletId;
        this.walletBalance = walletBalance;
    }
    public Wallet(Long walletId, BigDecimal walletBalance, Player player) {
    	this(walletId, walletBalance);
        this.player = player;
    }
    public Wallet(Long walletId, BigDecimal walletBalance, Player player,LocalDateTime walletDate) {
    	this(walletId, walletBalance, player);
        this.walletDate = walletDate;
    }
	public Long getWalletId() {
		return walletId;
	}
	public void setWalletId(Long walletId) {
		this.walletId = walletId;
	}
	public LocalDateTime getWalletDate() {
		return walletDate;
	}
	public void setWalletDate(LocalDateTime walletDate) {
		this.walletDate = walletDate;
	}
	public BigDecimal getWalletBalance() {
		return walletBalance;
	}
	public void setWalletBalance(BigDecimal walletBalance) {
		this.walletBalance = walletBalance;
	}
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public Set<Transaction> getTransactions() {
		return transactions;
	}
	public void setTransactions(Set<Transaction> transactions) {
		this.transactions = transactions;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	@Override
    public int hashCode() {
        return 13;
    } 
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Wallet other = (Wallet) obj;
        return walletId != null && walletId.equals(other.getWalletId());
    }
}
