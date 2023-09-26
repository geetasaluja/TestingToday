package com.organization.walletapi.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import com.organization.walletapi.domain.entities.Player;
import com.organization.walletapi.domain.entities.Transaction;
import com.organization.walletapi.domain.entities.Wallet;

/**
 *
 * @author Geeta Saluja
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class TransactionRepositoryTest {	
	 @Autowired
	 private TestEntityManager entityManager;
	 @Autowired
	 private TransactionRepository transactionRepository;	 
     Wallet wallet;
     Player player;
     Long walletId;
     Transaction transaction;
     String callerSupplyTransId;

	 @Before
     public void setup(){
		 callerSupplyTransId = UUID.randomUUID().toString();
		 player = new Player();
		 player.setPlayerName("test-1");
		 entityManager.persistAndFlush(player);
		 wallet = new Wallet();
		 wallet.setPlayer(player);
		 wallet.setWalletBalance(new BigDecimal(1000));
		 entityManager.persistAndFlush(wallet);
		 transaction = new Transaction("DEBIT", callerSupplyTransId, new BigDecimal(1000), wallet,LocalDateTime.now());
		 entityManager.persistAndFlush(transaction);
     }	 
	 @Test
     public void testFindByWallet_Success() {
        Set<Transaction> transactions = transactionRepository.findByWallet(wallet);
        assertTrue(transactions.size()== 1);
        assertThat(transactions.stream().findFirst().get().getWallet().getPlayer().getPlayerId(), is(equalTo(wallet.getPlayer().getPlayerId())));
        assertThat(transactions.stream().findFirst().get().getWallet().getWalletId(), is(equalTo(wallet.getWalletId())));
        assertThat(transactions.stream().findFirst().get().getTransactionId(), is(equalTo(transaction.getTransactionId())));
     }
	 @Test   
     public void testFindByCallerSupplyTransId_ReturnNoTransaction() {
		 String callerSupplyTransId1 = UUID.randomUUID().toString();
		 Optional<Transaction> testTransaction = transactionRepository.findByCallerSupplyTransId(callerSupplyTransId1);
		 assertTrue(!testTransaction.isPresent());
     }
	 @Test 
     public void testFindByCallerSupplyTransId_ReturnTransaction() {		
		 Optional<Transaction> testTransaction = transactionRepository.findByCallerSupplyTransId(transaction.getCallerSupplyTransId());
		 assertNotNull(testTransaction);
		 assertTrue(testTransaction.isPresent());
		 assertThat(testTransaction.get().getTransactionId(), is(equalTo(transaction.getTransactionId())));
		 assertThat(testTransaction.get().getCallerSupplyTransId(), is(equalTo(transaction.getCallerSupplyTransId())));
	 }
	 @Test 
     public void testDebitSave_Success() {		
		 Transaction newTransaction = new Transaction("DEBIT", callerSupplyTransId, new BigDecimal(500), wallet,LocalDateTime.now());
		 Transaction testTransaction = transactionRepository.save(newTransaction);
		 assertNotNull(testTransaction);
		 assertThat(testTransaction.getWallet().getWalletId(), is(equalTo(newTransaction.getWallet().getWalletId())));
		 assertThat(testTransaction.getCallerSupplyTransId(), is(equalTo(newTransaction.getCallerSupplyTransId())));
		 assertThat(testTransaction.getTransactionType(), is(equalTo("DEBIT")));
		 assertThat(testTransaction.getTransactionAmount(), is(equalTo(newTransaction.getTransactionAmount())));
	 }
	 @Test 
     public void testCreditSave_Success() {		
		 Transaction newTransaction = new Transaction("CREDIT", callerSupplyTransId, new BigDecimal(2000), wallet,LocalDateTime.now());
		 Transaction testTransaction = transactionRepository.save(newTransaction);
		 assertNotNull(testTransaction);
		 assertThat(testTransaction.getWallet().getWalletId(), is(equalTo(newTransaction.getWallet().getWalletId())));
		 assertThat(testTransaction.getCallerSupplyTransId(), is(equalTo(newTransaction.getCallerSupplyTransId())));
		 assertThat(testTransaction.getTransactionType(), is(equalTo("CREDIT")));
		 assertThat(testTransaction.getTransactionAmount(), is(equalTo(newTransaction.getTransactionAmount())));
     }
}
