package com.organization.walletapi.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
import com.organization.walletapi.dto.WalletDto;
import com.organization.walletapi.facade.WalletFacade;
import com.organization.walletapi.exceptions.WalletException;
import com.organization.walletapi.exceptions.exceptionhandler.ErrorMessage;
import com.organization.walletapi.repository.WalletRepository;
import com.organization.walletapi.service.impl.WalletServiceImpl;

/**
 *
 * @author Geeta Saluja
 */
@RunWith(SpringRunner.class)
public class WalletServiceTest {
	@InjectMocks
	private WalletServiceImpl walletServiceImpl;
	@MockBean
    private WalletFacade walletFacade;
    @MockBean
    private WalletRepository walletRepository;
    
    Wallet wallet;
    WalletDto walletDto;
    Transaction transaction;
    Long playerId;
	Long walletId;
	Long transactionId;
    @Before
    public void setUp() {
    	MockitoAnnotations.initMocks(this);
    	playerId = 1l;
		walletId= 500l;
		transactionId = 10l;
    	wallet = new Wallet(walletId, new BigDecimal(1000), new Player(playerId,"player-test"));
    	walletDto =new WalletDto(walletId, playerId, "player-test",new BigDecimal(1000));
    }
    @Test
	public void testFindAll_Success() throws Exception{
    	 List<Wallet> wallets = Arrays.asList(wallet);
    	 List<WalletDto> walletsDto = Arrays.asList(walletDto);
    	 when(walletRepository.findAll()).thenReturn(wallets);
    	 when(walletFacade.domainToDtoWalletList(wallets)).thenReturn(walletsDto);    	 
    	 List<WalletDto> testWallet =  walletServiceImpl.findAll();
    	 assertNotNull(testWallet);
    	 assertTrue(testWallet.size() == 1);
    	 assertThat(testWallet, is(equalTo(walletsDto)));
	}
    @Test
	public void testFindByPlayerId_Success() throws Exception{
    	 when(walletRepository.findByPlayerId(playerId)).thenReturn(Optional.of(wallet));
    	 when(walletFacade.convertToWalletDto(wallet)).thenReturn(walletDto);    	 
    	 WalletDto testWallet =  walletServiceImpl.findByPlayerId(playerId);
    	 assertNotNull(testWallet);
    	 assertThat(testWallet, is(equalTo(walletDto)));
	}
    @Test
   	public void testFindByWalletId_Success() throws Exception{
       	 when(walletRepository.findByWalletId(walletId)).thenReturn(Optional.of(wallet));
       	 when(walletFacade.convertToWalletDto(wallet)).thenReturn(walletDto);    	 
       	 WalletDto testWallet =  walletServiceImpl.findByWalletId(walletId);
       	 assertNotNull(testWallet);
       	 assertThat(testWallet, is(equalTo(walletDto)));
   	}
    @Test
	public void testUpdateWalletById_Success() throws Exception{
    	 BigDecimal currentBalance = new BigDecimal (5000);
    	 when(walletRepository.findByWalletId(walletId)).thenReturn(Optional.of(wallet));
    	 wallet.setWalletBalance(currentBalance);
    	 when(walletRepository.save(wallet)).thenReturn(wallet);
    	 Wallet testWallet = walletServiceImpl.updateWalletById(currentBalance, walletId);
    	 assertNotNull(testWallet);
    	 assertThat(testWallet.getWalletBalance(), is(equalTo(wallet.getWalletBalance())));
	}
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    @Test
	public void testWalletNotFoundByWalletId() throws Exception,WalletException{
    	 String errorMessage = String.format(ErrorMessage.NO_WALLET_FOUND);
    	 when(walletRepository.findByWalletId(walletId)).thenReturn(Optional.empty()); 
    	 exceptionRule.expect(WalletException.class);
    	 exceptionRule.expectMessage(errorMessage);
    	 walletServiceImpl.findByWalletId(walletId);
    }
    @Test
	public void testWalletNotFoundByPlayerId() throws Exception,WalletException{
    	 String errorMessage = String.format(ErrorMessage.NO_WALLET_FOUND);
    	 when(walletRepository.findByPlayerId(playerId)).thenReturn(Optional.empty()); 
    	exceptionRule.expect(WalletException.class);
    	exceptionRule.expectMessage(errorMessage);
    	walletServiceImpl.findByPlayerId(playerId);
	}
}

	


