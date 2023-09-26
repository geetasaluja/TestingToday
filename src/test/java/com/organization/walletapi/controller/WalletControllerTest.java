package com.organization.walletapi.controller;

import static org.hamcrest.Matchers.is;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.organization.walletapi.dto.WalletDto;
import com.organization.walletapi.exceptions.WalletException;
import com.organization.walletapi.exceptions.exceptionhandler.ErrorMessage;
import com.organization.walletapi.service.WalletService;

/**
 *
 * @author Geeta Saluja
 */
@RunWith(SpringRunner.class)
@WebMvcTest(WalletController.class)
public class WalletControllerTest {
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private WalletService walletService;
	
	Long playerId;
	Long walletId;
	WalletDto walletDto;
	private static final String pathUrl = "/wallets";
	
	@Before
	public void setup() {
		playerId = 1l;
		walletId= 500l;
		walletDto = new WalletDto(walletId, playerId, "player-test",new BigDecimal(2000));
	}	
	@Test
	public void testGetWallets_Success() throws Exception{
		List<WalletDto> allWallets = Arrays.asList(walletDto);
		when(walletService.findAll()).thenReturn(allWallets);
		mockMvc.perform(get(pathUrl + "/all")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].walletId", is(500)))
				.andExpect(jsonPath("$[0].playerId", is(1)))
				.andExpect(jsonPath("$[0].playerName", is("player-test")))
				.andExpect(jsonPath("$[0].walletBalance", is(2000)));
	}
	@Test
	public void testGetWalletBalanceByPlayerId_Success() throws Exception{
		when(walletService.findByPlayerId(playerId)).thenReturn(walletDto);
		
		mockMvc.perform(get(pathUrl + "/balance/{playerId}", 1)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.playerId", is(1)))
				.andExpect(jsonPath("$.playerName", is("player-test")))
				.andExpect(jsonPath("$.walletId", is(500)))
				.andExpect(jsonPath("$.walletBalance", is(2000)));
	}
	@Test
	public void testGetWalletByPlayerId_Success() throws Exception{
		when(walletService.findByPlayerId(playerId)).thenReturn(walletDto);
		mockMvc.perform(get(pathUrl + "/player/{playerId}", 1)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.playerId", is(1)))
				.andExpect(jsonPath("$.playerName", is("player-test")))
				.andExpect(jsonPath("$.walletId", is(500)))
				.andExpect(jsonPath("$.walletBalance", is(2000)));
	}	
	@Test
	public void testGetWalletByWalletId_Success() throws Exception{
		when(walletService.findByWalletId(walletId)).thenReturn(walletDto);
		mockMvc.perform(get(pathUrl + "/{walletId}", 500)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.walletId", is(500)))
				.andExpect(jsonPath("$.walletBalance", is(2000)))
				.andExpect(jsonPath("$.playerName", is("player-test")))
				.andExpect(jsonPath("$.playerId", is(1)));
	}
	@Test
	public void testWalletNotFound_GivenPlayerId() throws Exception {
		playerId=2L;
		String errorMessage = String.format(ErrorMessage.NO_WALLET_FOUND);
		when(walletService.findByPlayerId(playerId)).thenThrow(new WalletException(errorMessage));
		mockMvc.perform(get(pathUrl + "/player/{playerId}", 2)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.errors[0]", is(errorMessage)))
				.andExpect(jsonPath("$.statusCode", is(404)))
				.andExpect(jsonPath("$.details", is("uri=/wallets/player/2")));
	}
	@Test
	public void testWalletNotFound_GivenWalletId() throws Exception {
		walletId=200L;
		String errorMessage = String.format(ErrorMessage.NO_WALLET_FOUND);
		when(walletService.findByWalletId(walletId)).thenThrow(new WalletException(errorMessage));
		mockMvc.perform(get(pathUrl + "/{walletId}", walletId)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.errors[0]", is(errorMessage)))
				.andExpect(jsonPath("$.statusCode", is(404)))
				.andExpect(jsonPath("$.details", is("uri=/wallets/200")));
	}
}
