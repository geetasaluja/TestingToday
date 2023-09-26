package com.organization.walletapi.exceptions;

/**
 *
 * @author Geeta Saluja
 */
public class WalletException extends RuntimeException {   
	private static final long serialVersionUID = 7770117257837456360L;
	public WalletException(String message) {
        super(message);
    }
}
