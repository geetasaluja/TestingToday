package com.organization.walletapi;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.DisabledIf;
import org.springframework.test.context.junit4.SpringRunner;
import com.organization.walletapi.dto.TransactionDto;
import com.organization.walletapi.exceptions.exceptionhandler.ErrorMessage;

/**
 *
 * @author Geeta Saluja
 */
@DisabledIf("failed")
@RunWith(SpringRunner.class)
@SpringBootTest(classes=WalletapiApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
public class WalletapiApplicationIntegrationTests {
	
	@LocalServerPort
	private int port;
	
	@Autowired
	TestRestTemplate restTemplate;
	HttpHeaders headers = new HttpHeaders();
	
	@Test
	public void testGetWalletByWalletId_Success() throws Exception{
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);		
		String expected = "{walletId:5001,walletBalance:2000,playerId:101}";	
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/wallets/5001"),HttpMethod.GET,entity,String.class);
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}
	@Test
	public void testGetWalletBalanceByPlayerId_Success() throws Exception{
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);		
		String expected = "{playerId:101,walletBalance:2000}";	
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/wallets/player/101"),HttpMethod.GET,entity,String.class);
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}
	@Test
	public void testGetTransactoinByPlayerId_Success() throws Exception{
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);		
		String expected = "{playerId:103,walletId:5003,transactions:[{transactionType:DEBIT,transactionAmount:100.00}]}";	
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/transactions/history/103"),HttpMethod.GET,entity,String.class);
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}	
	@Test
	public void testCreditByPlayerId_Success() throws Exception{
		TransactionDto transactonDto = new TransactionDto(UUID.randomUUID().toString(), 5002l, new BigDecimal(200),"CREDIT",LocalDateTime.now());
		HttpEntity<TransactionDto> entity = new HttpEntity<TransactionDto>(transactonDto, headers);		
		String expected = "{playerId:102,walletId:5002,oldBalance:2800,newBalance:3000}";	
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/transactions/credit/102"),HttpMethod.POST,entity,String.class);
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}
	@Test
	public void testDebitByPlayerId_Success() throws Exception{
		TransactionDto transactonDto = new TransactionDto(UUID.randomUUID().toString(), 5002l, new BigDecimal(200),"DEBIT",LocalDateTime.now());
		HttpEntity<TransactionDto> entity = new HttpEntity<TransactionDto>(transactonDto, headers);		
		String expected = "{playerId:102,walletId:5002,oldBalance:3000,newBalance:2800}";	
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/transactions/debit/102"),HttpMethod.POST,entity,String.class);
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}
	@Test
	public void testDebitByPlayerId_Failure_NoUniqueCallerSupplyTransIdException() throws Exception{
		String errorMessage = String.format(ErrorMessage.NO_UNIQUE_TRANSACTION_REFERENCE_ID, "testing-purpose-dummy-data");
		TransactionDto transactonDto = new TransactionDto("testing-purpose-dummy-data", 5003l, new BigDecimal(100),"DEBIT",LocalDateTime.now());
		HttpEntity<TransactionDto> entity = new HttpEntity<TransactionDto>(transactonDto, headers);	
		ResponseEntity<String> response =restTemplate.exchange(createURLWithPort("/transactions/debit/103"),HttpMethod.POST,entity,String.class);
		assertThat(response.getBody(),containsString(errorMessage));
		assertThat(response.getBody(),containsString("409"));
		assertThat(response.getBody(),containsString("CONFLICT"));
	}
	@Test
	public void testDebitByPlayerId_Failure_NoEnoughBalanceException() throws Exception{
		String errorMessage = String.format(ErrorMessage.NO_ENOUGH_FUND_FOUND, 2000.00,8000.00);
		TransactionDto transactonDto = new TransactionDto(UUID.randomUUID().toString(), 5001l, new BigDecimal(8000),"DEBIT",LocalDateTime.now());
		HttpEntity<TransactionDto> entity = new HttpEntity<TransactionDto>(transactonDto, headers);	
		ResponseEntity<String> response =restTemplate.exchange(createURLWithPort("/transactions/debit/101"),HttpMethod.POST,entity,String.class);
		assertThat(response.getBody(),containsString(errorMessage));
		assertThat(response.getBody(),containsString("400"));
		assertThat(response.getBody(),containsString("BAD_REQUEST"));
	}
	@Test
	public void testCreditByPlayerId_Failure_WalletPlayerMismatch() throws Exception{
		String errorMessage = String.format(ErrorMessage.WALLET_PLAYER_MISMATCH, 5001l,102l);
		TransactionDto transactonDto = new TransactionDto(UUID.randomUUID().toString(), 5001l, new BigDecimal(8000),"CREDIT",LocalDateTime.now());
		HttpEntity<TransactionDto> entity = new HttpEntity<TransactionDto>(transactonDto, headers);	
		ResponseEntity<String> response =restTemplate.exchange(createURLWithPort("/transactions/credit/102"),HttpMethod.POST,entity,String.class);
		assertThat(response.getBody(),containsString(errorMessage));
		assertThat(response.getBody(),containsString("404"));
		assertThat(response.getBody(),containsString("NOT_FOUND"));
	}
	private String createURLWithPort(String uri) {
		return "http://localhost:"+port+uri;
	}
}
