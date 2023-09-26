package com.organization.walletapi.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import com.organization.walletapi.domain.entities.Player;
import com.organization.walletapi.domain.entities.Wallet;

/**
 * 
 * @author Geeta Saluja
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class WalletRepositoryTest {
	 @Autowired
	 private TestEntityManager entityManager;
	 @Autowired
	 private WalletRepository walletRepository; 	 
	 Wallet wallet1;
	 Player player;
	 Long walletId;

	 @Before
     public void setup(){
		 player = new Player();
		 player.setPlayerName("test-1");
		 entityManager.persistAndFlush(player);
		 wallet1 = new Wallet();
		 wallet1.setPlayer(player);
		 wallet1.setWalletBalance(new BigDecimal(1000));
		 entityManager.persist(wallet1);
		 entityManager.flush();
    }
	 
	@Test
    public void testFindAll_Success() {
        List<Wallet> testWallet = walletRepository.findAll();
        assertNotNull(testWallet);
        assertTrue(!testWallet.isEmpty());
        assertTrue(testWallet.size()>0);
        assertTrue(testWallet.get(0).getWalletId().equals(wallet1.getWalletId()));
        assertThat(testWallet.get(0).getWalletId(), is(equalTo(wallet1.getWalletId())));
    }
	@Test
    public void testWalletByPlayerId_Success() {
		Optional<Wallet> testWallet = walletRepository.findByPlayerId(player.getPlayerId());
        assertNotNull(testWallet);
        assertTrue(testWallet.isPresent());
        assertThat(testWallet.get().getPlayer().getPlayerId(), is(equalTo(player.getPlayerId())));
    }
	@Test
    public void testWalletByPlayerId_ReturnNoWallet() {
		Optional<Wallet> testWallet = walletRepository.findByPlayerId(100L);
		assertTrue(!testWallet.isPresent());
    }
	@Test
    public void testWalletByWalletId_Success() {
		Optional<Wallet> testWallet = walletRepository.findByWalletId(wallet1.getWalletId());
        assertNotNull(testWallet);
        assertTrue(testWallet.isPresent());
        assertThat(testWallet.get().getWalletId(), is(equalTo(wallet1.getWalletId())));
        assertThat(testWallet.get().getWalletBalance(), is(equalTo(wallet1.getWalletBalance())));
        assertThat(testWallet.get().getPlayer().getPlayerId(), is(equalTo(wallet1.getPlayer().getPlayerId())));
    }
	@Test
    public void testWalletByWalletId_ReturnNoWallet() {
		Optional<Wallet> testWallet = walletRepository.findByWalletId(100L);
		assertTrue(!testWallet.isPresent());
    }
	@Test
    public void testUpdateWalletBalance_Success() {
		Optional<Wallet> testWallet = walletRepository.findByWalletId(wallet1.getWalletId());
		assertNotNull(testWallet);
		assertTrue(testWallet.isPresent());
		Wallet newWallet = testWallet.get();
		newWallet.setWalletBalance(new BigDecimal(2000));
		Wallet saveWallet = walletRepository.save(newWallet);
		assertNotNull(saveWallet);
        assertThat((saveWallet.getWalletBalance()), is(equalTo(new BigDecimal(2000) )));
    }
}
