package com.organization.walletapi.exceptions.exceptionhandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.organization.walletapi.exceptions.NoEnoughBalanceException;
import com.organization.walletapi.exceptions.NoUniqueCallerSupplyTransIdException;
import com.organization.walletapi.exceptions.WalletException;

/**
 *
 * @author Geeta Saluja
 */
@ControllerAdvice
public class WalletResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
		
	@ExceptionHandler(WalletException.class)
	public final ResponseEntity<ExceptionDetail> handleWalletException(WalletException ex,
			WebRequest request) {
		List<String> errors = new ArrayList<String>();
		errors.add(ex.getMessage());
		ExceptionDetail exceptionDetail = new ExceptionDetail(LocalDateTime.now(), errors,HttpStatus.NOT_FOUND,HttpStatus.NOT_FOUND.value(),request.getDescription(false));
		return new ResponseEntity<>(exceptionDetail, HttpStatus.NOT_FOUND);
	}	
	@ExceptionHandler(NoUniqueCallerSupplyTransIdException.class)
	public final ResponseEntity<Object> handleNoUniqueTransactionRefIdException(NoUniqueCallerSupplyTransIdException ex,
			WebRequest request) {
		List<String> errors = new ArrayList<String>();
		errors.add(ex.getMessage());
		ExceptionDetail exceptionDetail = new ExceptionDetail(LocalDateTime.now(), errors,HttpStatus.CONFLICT,HttpStatus.CONFLICT.value(),request.getDescription(false));
		return new ResponseEntity<Object>(exceptionDetail, HttpStatus.CONFLICT);
	}	
	@ExceptionHandler(NoEnoughBalanceException.class)
	public final ResponseEntity<ExceptionDetail> handleNoEnoughBalanceException(NoEnoughBalanceException ex,
			WebRequest request) {
		List<String> errors = new ArrayList<String>();
		errors.add(ex.getMessage());
		ExceptionDetail exceptionDetail = new ExceptionDetail(LocalDateTime.now(), errors,HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value(),request.getDescription(false));
		return new ResponseEntity<>(exceptionDetail, HttpStatus.BAD_REQUEST);
	}
	@Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
        HttpHeaders headers, HttpStatus status, WebRequest request) {
		String locMessage[] =ex.getLocalizedMessage().split("default message", -2); 
		List<String> errors = new ArrayList<String>();
		for(int i=2;i<=locMessage.length;i=i+2) {
			String msg[]=locMessage[i].split("]]");
			String finalMsg = msg[0].replace("[", "");
			errors.add(finalMsg);
		}
		ExceptionDetail exceptionDetail = new ExceptionDetail(LocalDateTime.now(), errors,
				HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value(),request.getDescription(false));
        return new ResponseEntity<>(exceptionDetail, HttpStatus.BAD_REQUEST);
    } 
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        super.handleExceptionInternal(ex,body,headers,status, request);
        List<String> errors = new ArrayList<String>();
        errors.add(ex.getMessage());
        ExceptionDetail exceptionDetail = new ExceptionDetail(LocalDateTime.now(),errors ,
				HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value(),request.getDescription(false));
        return new ResponseEntity<>(exceptionDetail, HttpStatus.BAD_REQUEST);
    }
	@Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<String>();
        errors.add(ErrorMessage.NO_HANDLER_FOUND);
        ExceptionDetail exceptionDetail = new ExceptionDetail(LocalDateTime.now(), errors,HttpStatus.NOT_FOUND,HttpStatus.NOT_FOUND.value(),request.getDescription(false));
        return new ResponseEntity<Object>(exceptionDetail,HttpStatus.NOT_FOUND);
    }
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ExceptionDetail> handleAllException(Exception ex,WebRequest request) {
		List<String> errors = new ArrayList<String>();
		errors.add(ex.getMessage());
		ExceptionDetail exceptionDetail = new ExceptionDetail(LocalDateTime.now(), errors,HttpStatus.NOT_FOUND,HttpStatus.NOT_FOUND.value(),request.getDescription(false));
		return new ResponseEntity<>(exceptionDetail, HttpStatus.NOT_FOUND);
	}
}