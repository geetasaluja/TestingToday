package com.organization.walletapi.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import com.organization.walletapi.domain.entities.Player;
import com.organization.walletapi.domain.entities.Transaction;
import com.organization.walletapi.domain.entities.Wallet;
import com.organization.walletapi.dto.OutputDto;
import com.organization.walletapi.dto.TransactionDto;
import com.organization.walletapi.dto.TransactionSetDto;
import com.organization.walletapi.dto.WalletDto;
import com.organization.walletapi.facade.TransactionFacade;
import com.organization.walletapi.facade.WalletFacade;
import com.organization.walletapi.exceptions.NoEnoughBalanceException;
import com.organization.walletapi.exceptions.NoUniqueCallerSupplyTransIdException;
import com.organization.walletapi.exceptions.WalletException;
import com.organization.walletapi.exceptions.exceptionhandler.ErrorMessage;
import com.organization.walletapi.repository.TransactionRepository;
import com.organization.walletapi.repository.WalletRepository;
import com.organization.walletapi.service.impl.TransactionServiceImpl;
import com.organization.walletapi.service.impl.WalletServiceImpl;

/**
 *
 * @author Geeta Saluja
 */
@RunWith(SpringRunner.class)
public class TransactionServiceTest {

	@InjectMocks
	private WalletServiceImpl walletServiceImpl;
	
	@InjectMocks
	private TransactionServiceImpl transactionServiceImpl;
	
	@MockBean
    private WalletService walletService;
	@MockBean
    private WalletFacade walletFacade;
	@MockBean
    private TransactionFacade transactionFacade;

    @MockBean
    private WalletRepository walletRepository;
    @MockBean
    private TransactionRepository transactionRepository;
    
    Wallet wallet;
    Wallet updatedWallet;
    WalletDto walletDto;
    Transaction transaction;
    Long playerId;
	Long walletId;
	Long transactionId;
	String callerSupplyTransId;
	TransactionSetDto transactionSetDto;
	TransactionDto transactionDto;
	BigDecimal transAmt;
	BigDecimal previousBalance;
	BigDecimal currentBalance;
	OutputDto outputDto;
    
    @Before
    public void setUp() {
    	MockitoAnnotations.initMocks(this);
    	playerId = 1l;
		walletId= 500l;
		transactionId = 10l;
		callerSupplyTransId = UUID.randomUUID().toString();
    	wallet = new Wallet(walletId, new BigDecimal(1000), new Player(playerId,"player-test"));
    	updatedWallet = new Wallet(walletId, new BigDecimal(800), new Player(playerId,"player-test"));
    	walletDto =new WalletDto(walletId, playerId, "player-test",new BigDecimal(1000));
    	transaction = new Transaction(transactionId,"DEBIT", callerSupplyTransId, new BigDecimal(200), wallet,LocalDateTime.now());
    	transactionDto = new TransactionDto (callerSupplyTransId, walletId, new BigDecimal(200),"DEBIT",LocalDateTime.now());
    	Set<TransactionDto> transactionsDto = new HashSet<>(Arrays.asList(transactionDto));
		transactionSetDto = new TransactionSetDto(walletId, playerId, "player-test",transactionsDto);
		previousBalance= wallet.getWalletBalance().abs();
    }
    @Test
   	public void testGetTransactionByPlayerId_Success() throws Exception{
    	 Set<Transaction> transactions = new HashSet<>(Arrays.asList(transaction));
    	 when(walletService.findByPlayerId(playerId)).thenReturn(walletDto);
       	 when(walletFacade.convertToWallet(walletDto)).thenReturn(wallet);
       	 when(transactionRepository.findByWallet(wallet)).thenReturn(transactions); 
       	 when(transactionFacade.domainToDtoTransactionSet(transactions,wallet)).thenReturn(transactionSetDto); 
       	 TransactionSetDto testTransactionSetDto =  transactionServiceImpl.getTransactionByPlayerId(playerId);
       	 assertNotNull(testTransactionSetDto);
       	 assertThat(testTransactionSetDto.getTransactions().size(), is(equalTo(transactionSetDto.getTransactions().size())));
         assertThat(testTransactionSetDto.getWalletId(), is(equalTo(transactionSetDto.getWalletId())));
       	 assertThat(testTransactionSetDto.getPlayerId(), is(equalTo(transactionSetDto.getPlayerId())));
       	 assertThat(testTransactionSetDto.getTransactions().stream().findFirst().get().getCallerSupplyTransId(), is(equalTo(transactionSetDto.getTransactions().stream().findFirst().get().getCallerSupplyTransId())));
   	}
    @Test
   	public void testRegisterTransaction_DebitSuccess() throws Exception{
    	 BigDecimal currentBalance = new BigDecimal(800);
    	 callerSupplyTransId=UUID.randomUUID().toString();
    	 Transaction updatedTransaction = new Transaction(20L,"DEBIT", callerSupplyTransId, new BigDecimal(200), updatedWallet,LocalDateTime.now());
    	 outputDto = new OutputDto (callerSupplyTransId, walletId,playerId,"player-test", new BigDecimal (1000),currentBalance,LocalDateTime.now());
    	 when(walletService.findByPlayerId(playerId)).thenReturn(walletDto);
       	 when(walletFacade.convertToWallet(walletDto)).thenReturn(wallet);
         when(walletService.updateWalletById(currentBalance, wallet.getWalletId())).thenReturn(updatedWallet); 
         when(transactionFacade.convertToTransactionDomain(transactionDto)).thenReturn(transaction);
         when(transactionRepository.save(transaction)).thenReturn(updatedTransaction);
         when(transactionFacade.convertToOutputDto(updatedTransaction, updatedWallet,previousBalance)).thenReturn(outputDto);
         OutputDto testOutput=transactionServiceImpl.registerTransaction(playerId,transactionDto);
         assertNotNull(testOutput);
    	 assertThat(testOutput.getNewBalance(), is(equalTo(outputDto.getNewBalance())));
    	 assertThat(testOutput.getOldBalance(), is(equalTo(outputDto.getOldBalance())));
    	 assertThat(testOutput.getWalletId(), is(equalTo(outputDto.getWalletId())));
    	 assertThat(testOutput.getPlayerId(), is(equalTo(outputDto.getPlayerId())));
   	}
    @Test
   	public void testRegisterTransaction_CreditSuccess() throws Exception{
    	 BigDecimal currentBalance = new BigDecimal(1200);
    	 callerSupplyTransId=UUID.randomUUID().toString();
    	 Transaction updatedTransaction = new Transaction(20L,"CREDIT", callerSupplyTransId, new BigDecimal(200), updatedWallet,LocalDateTime.now());
    	 outputDto = new OutputDto (callerSupplyTransId, walletId,playerId,"player-test", new BigDecimal (1000),currentBalance,LocalDateTime.now());
    	 updatedWallet.setWalletBalance(currentBalance);
    	 transaction.setTransactionType("CREDIT");
    	 transactionDto.setTransactionType("CREDIT");
    	 when(walletService.findByPlayerId(playerId)).thenReturn(walletDto);
       	 when(walletFacade.convertToWallet(walletDto)).thenReturn(wallet);
         when(walletService.updateWalletById(currentBalance, wallet.getWalletId())).thenReturn(updatedWallet); 
         when(transactionFacade.convertToTransactionDomain(transactionDto)).thenReturn(transaction);
         when(transactionRepository.save(transaction)).thenReturn(updatedTransaction);
         when(transactionFacade.convertToOutputDto(updatedTransaction, updatedWallet,previousBalance)).thenReturn(outputDto);
         OutputDto testOutput=transactionServiceImpl.registerTransaction(playerId,transactionDto);
         assertNotNull(testOutput);
    	 assertThat(testOutput.getNewBalance(), is(equalTo(outputDto.getNewBalance())));
    	 assertThat(testOutput.getOldBalance(), is(equalTo(outputDto.getOldBalance())));
    	 assertThat(testOutput.getWalletId(), is(equalTo(outputDto.getWalletId())));
    	 assertThat(testOutput.getPlayerId(), is(equalTo(outputDto.getPlayerId())));
   	}
    
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    
    @Test
   	public void testRegisterTransaction_WalletPlayerMisMatchException() throws Exception{
    	 transactionDto.setWalletId(502L);
    	 String errorMessage = String.format(String.format(ErrorMessage.WALLET_PLAYER_MISMATCH , transactionDto.getWalletId(),playerId));
    	 when(walletService.findByPlayerId(playerId)).thenReturn(walletDto);
       	 when(walletFacade.convertToWallet(walletDto)).thenReturn(wallet);
    	 exceptionRule.expect(WalletException.class);
    	 exceptionRule.expectMessage(errorMessage);
       	 transactionServiceImpl.registerTransaction(playerId,transactionDto);
   	}
    @Test
   	public void testRegisterTransaction_NoUniqueCallerSupplyTransIdException() throws Exception{
    	 String errorMessage = String.format(ErrorMessage.NO_UNIQUE_TRANSACTION_REFERENCE_ID, transactionDto.getCallerSupplyTransId());
    	 when(walletService.findByPlayerId(playerId)).thenReturn(walletDto);
       	 when(walletFacade.convertToWallet(walletDto)).thenReturn(wallet);
       	 when(transactionRepository.findByCallerSupplyTransId(transactionDto.getCallerSupplyTransId())).thenReturn(Optional.of(transaction)); 
    	 exceptionRule.expect(NoUniqueCallerSupplyTransIdException.class);
    	 exceptionRule.expectMessage(errorMessage);
       	 transactionServiceImpl.registerTransaction(playerId,transactionDto);
   	}
    @Test
   	public void testRegisterTransaction_DebitFailure_NoEnoughBalanceException() throws Exception{
    	 BigDecimal transAmt= transactionDto.getTransactionAmount().abs();
		 BigDecimal balance= new BigDecimal(100);
    	 wallet.setWalletBalance(balance);
    	 when(walletService.findByPlayerId(playerId)).thenReturn(walletDto);
       	 when(walletFacade.convertToWallet(walletDto)).thenReturn(wallet);
       	 String errorMessage = String.format(ErrorMessage.NO_ENOUGH_FUND_FOUND, balance,transAmt);
       	 exceptionRule.expect(NoEnoughBalanceException.class);
       	 exceptionRule.expectMessage(errorMessage);
       	 transactionServiceImpl.registerTransaction(playerId,transactionDto);
   	}
    @Test
	public void testNoUniqueCallerSupplyTransIdException() throws NoUniqueCallerSupplyTransIdException{
    	 String errorMessage = String.format(ErrorMessage.NO_UNIQUE_TRANSACTION_REFERENCE_ID, transactionDto.getCallerSupplyTransId());
    	 when(transactionRepository.findByCallerSupplyTransId(callerSupplyTransId)).thenReturn(Optional.of(transaction)); 
    	 exceptionRule.expect(NoUniqueCallerSupplyTransIdException.class);
    	 exceptionRule.expectMessage(errorMessage);
    	 transactionServiceImpl.validateUniqueCallerSupplyTransId(callerSupplyTransId);
	}
    @Test
   	public void testNoEnoughBalanceException() throws NoEnoughBalanceException{
    	 BigDecimal transAmt= transactionDto.getTransactionAmount().abs();
		 BigDecimal balance= new BigDecimal(100);
		 wallet.setWalletBalance(balance);
       	 String errorMessage = String.format(ErrorMessage.NO_ENOUGH_FUND_FOUND, balance,transAmt);
       	 exceptionRule.expect(NoEnoughBalanceException.class);
       	 exceptionRule.expectMessage(errorMessage);
       	 transactionServiceImpl.checkBalance(transactionDto, wallet);
   	}
}
