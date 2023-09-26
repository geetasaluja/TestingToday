package com.organization.walletapi.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.organization.walletapi.dto.WalletDto;
import com.organization.walletapi.service.WalletService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * @author Geeta Saluja
 */
@RestController
@Api(tags="Wallet Management")
@RequestMapping(value = "/wallets", produces = MediaType.APPLICATION_JSON_VALUE)
public class WalletController {	
	Logger logger = LoggerFactory.getLogger(this.getClass()); 
	
	@Autowired
	private WalletService walletService;	
	
    @ApiOperation(value = "Get all wallets")
    @GetMapping(value="/all")
    public ResponseEntity<List<WalletDto>> getWallets(){	   
	   logger.debug("Called WalletController.getWallets");		   	   
	   List<WalletDto> walletDtos = walletService.findAll();
	   return new ResponseEntity<List<WalletDto>>(walletDtos, HttpStatus.OK);
   } 
    
   @ApiOperation(value = "Get Wallet per Player Id")
   @GetMapping(value="/player/{playerId}")
   public ResponseEntity<WalletDto> getWalletByPlayerId(@PathVariable("playerId") Long playerId){	   
	   logger.debug("Called WalletController.getWalletByPlayerId");
	   WalletDto walletDto = walletService.findByPlayerId(playerId);
	   return new ResponseEntity<WalletDto>(walletDto, HttpStatus.OK);
   } 
   
   @ApiOperation(value = "Get Current Wallet Balance per Player Id")
   @GetMapping(value="/balance/{playerId}")
   public ResponseEntity<WalletDto> getWalletBalanceByPlayerId(@PathVariable("playerId") Long playerId){	   
	   logger.debug("Called WalletController.getWalletBalanceByPlayerId");
	   WalletDto walletDto = walletService.findByPlayerId(playerId);
	   return new ResponseEntity<WalletDto>(walletDto, HttpStatus.OK);
   } 
   
   @ApiOperation(value = "Get Wallet by Wallet Id")
   @GetMapping(value="/{walletId}")
   public ResponseEntity<WalletDto> getWalletByWalletId(@PathVariable("walletId") Long walletId){	   
	   logger.debug("Called WalletController.getWalletByWalletId");
	   WalletDto walletDto = walletService.findByWalletId(walletId);
	   return new ResponseEntity<WalletDto>(walletDto, HttpStatus.OK);
   } 
}
