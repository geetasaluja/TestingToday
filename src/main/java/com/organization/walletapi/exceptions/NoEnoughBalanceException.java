package com.organization.walletapi.exceptions;

/**
 *
 * @author Geeta Saluja
 */
public class NoEnoughBalanceException extends Exception {
  	private static final long serialVersionUID = -4525672573468965052L;	    
    public NoEnoughBalanceException(String message) {
        super(message);
    }
}
