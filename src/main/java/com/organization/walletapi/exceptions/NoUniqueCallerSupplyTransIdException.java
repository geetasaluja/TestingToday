package com.organization.walletapi.exceptions;

/**
 *
 * @author Geeta Saluja
 */
public class NoUniqueCallerSupplyTransIdException extends Exception {   
	private static final long serialVersionUID = 2068857457473037202L;
	public NoUniqueCallerSupplyTransIdException(String message) {
        super(message);
    }
}
