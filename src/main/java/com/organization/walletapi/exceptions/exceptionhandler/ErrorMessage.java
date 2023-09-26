package com.organization.walletapi.exceptions.exceptionhandler;

/**
 *
 * @author Geeta Saluja
 */
public class ErrorMessage {   
    public static final String NO_ENOUGH_FUND_FOUND = "Wallet has only %.2f balance. You can not perform debit transaction with amount %.2f";
    public static final String NO_UNIQUE_TRANSACTION_REFERENCE_ID = "Caller supply transaction id: %s, already Exist! Please use another caller supply transaction id";
    public static final String WALLET_PLAYER_MISMATCH = "Given wallet id %d is not associated with given player id %d";
    public static final String NO_WALLET_FOUND = "Wallet does not exist.";
    public static final String CALLER_SUPPLY_TRANSACTION_ID_NOTNULL = "Caller supplied Transaction Id should not be null";
    public static final String TRANSACTION_AMOUNT_NOTNULL = "Transaction Amount should not be null";
    public static final String TRANSACTION_DATE_NOTNULL = "Transaction Date should not be null";
    public static final String WALLET_ID_NOTNULL = "Wallet Id should not be null";
    public static final String NO_HANDLER_FOUND = "The URL you have reached is not in service at this time.";
}
