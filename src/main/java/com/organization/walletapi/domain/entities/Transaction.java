package com.organization.walletapi.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 *
 * @author Geeta Saluja
 */
@Entity
public class Transaction {	
	@Id
	@GeneratedValue
    private Long transactionId;
    private LocalDateTime transactionDate;
    private BigDecimal transactionAmount;
    private String transactionType;
    
    // At DB end unique external caller supplied transaction id
    @Column(unique = true)
    private String callerSupplyTransId;
    
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "walletId", nullable=false)
    @JsonBackReference
    private Wallet wallet;
    
    // For concurrency control
    @Version
    private int version;    
    public Transaction() {}
    
    public Transaction(String transactionType, String callerSupplyTransId, BigDecimal transactionAmount, Wallet wallet,LocalDateTime transactionDate) {
    	this.callerSupplyTransId = callerSupplyTransId;
    	this.transactionType = transactionType;
    	this.transactionAmount = transactionAmount;
    	this.wallet = wallet;
    	this.transactionDate = transactionDate;
    }
    public Transaction(Long transactionId,String transactionType, String callerSupplyTransId, BigDecimal transactionAmount, Wallet wallet,LocalDateTime transactionDate) {
    	this(transactionType, callerSupplyTransId,transactionAmount,wallet,transactionDate);
    	this.transactionId = transactionId;
    }
	public Long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}
	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}
	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getCallerSupplyTransId() {
		return callerSupplyTransId;
	}
	public void setCallerSupplyTransId(String callerSupplyTransId) {
		this.callerSupplyTransId = callerSupplyTransId;
	}
	public Wallet getWallet() {
		return wallet;
	}
	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
}
