package com.organization.walletapi.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.ArgumentMatchers.any;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.organization.walletapi.domain.entities.Transaction;
import com.organization.walletapi.domain.entities.Wallet;
import com.organization.walletapi.dto.OutputDto;
import com.organization.walletapi.dto.TransactionDto;
import com.organization.walletapi.dto.TransactionSetDto;
import com.organization.walletapi.dto.WalletDto;
import com.organization.walletapi.exceptions.NoEnoughBalanceException;
import com.organization.walletapi.exceptions.NoUniqueCallerSupplyTransIdException;
import com.organization.walletapi.exceptions.WalletException;
import com.organization.walletapi.exceptions.exceptionhandler.ErrorMessage;
import com.organization.walletapi.service.TransactionService;

/**
 *
 * @author Geeta Saluja
 */
@RunWith(SpringRunner.class)
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	private TransactionService transactionService; 
	
	Long playerId;
	Long walletId;
	Long transactionId;
	String callerSupplyTransId;	
	WalletDto walletDto;
	Wallet wallet;
	TransactionDto transactionDto;
	OutputDto outputDto;
	Transaction transaction;
	TransactionSetDto transactionSetDto;
	private static final String pathUrl = "/transactions";
	
	@Before
	public void setup() {
		playerId = 1l;
		walletId= 500l;
		transactionId = 10l;
		callerSupplyTransId = UUID.randomUUID().toString();
		transactionDto = new TransactionDto (callerSupplyTransId, walletId, new BigDecimal(200),"DEBIT",LocalDateTime.now());
		outputDto = new OutputDto (callerSupplyTransId, walletId,playerId,"player-test", new BigDecimal (2000),new BigDecimal (1800),LocalDateTime.now());
		walletDto = new WalletDto(walletId, playerId, "player-test",new BigDecimal(2000));
		wallet = new Wallet(walletId, new BigDecimal(2000));
		transaction = new Transaction(transactionId,"DEBIT", callerSupplyTransId, new BigDecimal(200), wallet,LocalDateTime.now());
		Set<TransactionDto> transactions = new HashSet<>(Arrays.asList(transactionDto));
		transactionSetDto = new TransactionSetDto(walletId, playerId, "player-test",transactions);
	}
	@Test
	public void testGetTransactoinByPlayerId_Success() throws Exception{
		when(transactionService.getTransactionByPlayerId(playerId)).thenReturn(transactionSetDto);		
		mockMvc.perform(get(pathUrl + "/history/{playerId}", 1)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())				
				.andExpect(jsonPath("$.playerName", is("player-test")))
				.andExpect(jsonPath("$.walletId", is(500)))
				.andExpect(jsonPath("$.playerId", is(1)))
				.andExpect(jsonPath("$.transactions",  hasSize(1)))
				.andExpect(jsonPath("$.transactions.[0].callerSupplyTransId", is(callerSupplyTransId.toString())))
				.andExpect(jsonPath("$.transactions.[0].transactionAmount", is(200)))
				.andExpect(jsonPath("$.transactions.[0].transactionType", is("DEBIT")));
	}
	@Test
	public void testDebit_Success() throws Exception{
		when(transactionService.registerTransaction(any(Long.class),refEq(transactionDto))).thenReturn(outputDto);		
		mockMvc.perform(post(pathUrl + "/debit/{playerId}", 1)
				.content(objectMapper.writeValueAsBytes(transactionDto))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())				
				.andExpect(jsonPath("$.playerName", is("player-test")))
				.andExpect(jsonPath("$.walletId", is(500)))
				.andExpect(jsonPath("$.playerId", is(1)))
				.andExpect(jsonPath("$.callerSupplyTransId", is(callerSupplyTransId.toString())))
				.andExpect(jsonPath("$.oldBalance", is(2000)))
				.andExpect(jsonPath("$.newBalance", is(1800)));		
	}
	@Test
	public void testCredit_Success() throws Exception{
		callerSupplyTransId = UUID.randomUUID().toString();
		transactionDto.setCallerSupplyTransId(callerSupplyTransId);
		transactionDto.setTransactionType("CREDIT");
		outputDto.setCallerSupplyTransId(callerSupplyTransId);
		outputDto.setNewBalance(new BigDecimal(2200));
		when(transactionService.registerTransaction(any(Long.class),refEq(transactionDto))).thenReturn(outputDto);
		mockMvc.perform(post(pathUrl + "/credit/{playerId}", 1)
				.content(objectMapper.writeValueAsBytes(transactionDto))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())				
				.andExpect(jsonPath("$.playerName", is("player-test")))
				.andExpect(jsonPath("$.walletId", is(500)))
				.andExpect(jsonPath("$.playerId", is(1)))
				.andExpect(jsonPath("$.callerSupplyTransId", is(callerSupplyTransId.toString())))
				.andExpect(jsonPath("$.oldBalance", is(2000)))
				.andExpect(jsonPath("$.newBalance", is(2200)));		
	}	
	@Test
	public void testDebit_ThrowNoEnoughBalanceException() throws Exception {
		transactionDto.setTransactionAmount(new BigDecimal(4000));
		String errorMessage = String.format(ErrorMessage.NO_ENOUGH_FUND_FOUND, wallet.getWalletBalance(),transactionDto.getTransactionAmount());
		when(transactionService.registerTransaction(any(Long.class),refEq(transactionDto))).thenThrow(new NoEnoughBalanceException(errorMessage));
		mockMvc.perform(post(pathUrl + "/debit/{playerId}", 1)
				.content(objectMapper.writeValueAsBytes(transactionDto))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[0]", is(errorMessage)))
				.andExpect(jsonPath("$.statusCode", is(400)))
				.andExpect(jsonPath("$.details", is("uri=/transactions/debit/1")));
	}
	@Test 
	public void testDebit_ThrowNoUniqueCallerSupplyTransIdException() throws Exception {
		String errorMessage = String.format(ErrorMessage.NO_UNIQUE_TRANSACTION_REFERENCE_ID, transactionDto.getCallerSupplyTransId());
		when(transactionService.registerTransaction(any(Long.class),refEq(transactionDto))).thenThrow(new NoUniqueCallerSupplyTransIdException(errorMessage));
		mockMvc.perform(post(pathUrl + "/debit/{playerId}", 1)
				.content(objectMapper.writeValueAsBytes(transactionDto))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.errors[0]", is(errorMessage)))
				.andExpect(jsonPath("$.statusCode", is(409)))
				.andExpect(jsonPath("$.details", is("uri=/transactions/debit/1")));
	}
	@Test 
	public void testCredit_ThrowNoUniqueCallerSupplyTransIdException() throws Exception {
		transactionDto.setTransactionType("CREDIT");
		String errorMessage = String.format(ErrorMessage.NO_UNIQUE_TRANSACTION_REFERENCE_ID, transactionDto.getCallerSupplyTransId());
		when(transactionService.registerTransaction(any(Long.class),refEq(transactionDto))).thenThrow(new NoUniqueCallerSupplyTransIdException(errorMessage));
		mockMvc.perform(post(pathUrl + "/credit/{playerId}", 1)
				.content(objectMapper.writeValueAsBytes(transactionDto))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.errors[0]", is(errorMessage)))
				.andExpect(jsonPath("$.statusCode", is(409)))
				.andExpect(jsonPath("$.details", is("uri=/transactions/credit/1")));
	}
	@Test
	public void testDebit_ThrowWalletException() throws Exception {
		transactionDto.setWalletId(501L);
		playerId=2L;
		String errorMessage = String.format(ErrorMessage.WALLET_PLAYER_MISMATCH, transactionDto.getWalletId(),playerId);
		when(transactionService.registerTransaction(any(Long.class),refEq(transactionDto))).thenThrow(new WalletException(errorMessage));
		mockMvc.perform(post(pathUrl + "/debit/{playerId}", playerId)
				.content(objectMapper.writeValueAsBytes(transactionDto))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.errors[0]", is(errorMessage)))
				.andExpect(jsonPath("$.statusCode", is(404)))
				.andExpect(jsonPath("$.details", is("uri=/transactions/debit/2")));
	}
}
