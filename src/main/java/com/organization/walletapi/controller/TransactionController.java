package com.organization.walletapi.controller;

import java.util.UUID;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.organization.walletapi.dto.OutputDto;
import com.organization.walletapi.dto.TransactionDto;
import com.organization.walletapi.dto.TransactionSetDto;
import com.organization.walletapi.exceptions.NoEnoughBalanceException;
import com.organization.walletapi.exceptions.NoUniqueCallerSupplyTransIdException;
import com.organization.walletapi.service.TransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * @author Geeta Saluja
 */
@RestController
@Api(tags="Transaction Management")
@RequestMapping(value = "/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController {	
   Logger logger = LoggerFactory.getLogger(this.getClass());
   
   @Autowired
   private TransactionService transactionService;   
   
   @ApiOperation(value = "Get Transaction History per Player Id")
   @GetMapping(value="/history/{playerId}")
   public ResponseEntity<TransactionSetDto> getTransactoinByPlayerId(@PathVariable("playerId") Long playerId) {	   
	   logger.debug("Called TransactionController.getTransactoinByPlayerId");
	   TransactionSetDto transactions =transactionService.getTransactionByPlayerId(playerId);
	   return new ResponseEntity<TransactionSetDto>(transactions, HttpStatus.OK);
   }  
   
   @ApiOperation(value = "Create External Caller Supplied Transaction Id")
   @GetMapping(value="/external/transactionId")
   public String createExternalTransactionId() {
	   logger.debug("Called TransactionController.createExternalTransactionId");
	   UUID extTransactionId = UUID.randomUUID();
	   return extTransactionId.toString();
   }
   
   @ApiOperation(value = "Registration for Debit Transaction")
   @PostMapping(value="/debit/{playerId}")
   public ResponseEntity<OutputDto> debitByPlayerId(@PathVariable("playerId") Long playerId, @Valid @RequestBody TransactionDto transactionDto) 
		   throws NoUniqueCallerSupplyTransIdException, NoEnoughBalanceException {	
	   logger.debug("Called TransactionController.debitByPlayerId");
	   transactionDto.setTransactionType("DEBIT");
	   OutputDto outputDto = transactionService.registerTransaction(playerId,transactionDto);
	   return new ResponseEntity<OutputDto>(outputDto, HttpStatus.CREATED);
   }   
   
   @ApiOperation(value = "Registration for Credit Transaction")
   @PostMapping(value="/credit/{playerId}")
   public ResponseEntity<OutputDto> creditByPlayerId(@PathVariable("playerId") Long playerId,@Valid @RequestBody TransactionDto transactionDto) 
		   throws NoUniqueCallerSupplyTransIdException,NoEnoughBalanceException{	   
	   logger.debug("Called TransactionController.creditByPlayerId");
	   transactionDto.setTransactionType("CREDIT");
	   OutputDto outputDto = transactionService.registerTransaction(playerId,transactionDto);
	   return new ResponseEntity<OutputDto>(outputDto, HttpStatus.CREATED);
   }
}
