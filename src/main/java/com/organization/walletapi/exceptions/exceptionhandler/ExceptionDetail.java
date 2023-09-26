package com.organization.walletapi.exceptions.exceptionhandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;

/**
 *
 * @author Geeta Saluja
 */
public class ExceptionDetail {
	private LocalDateTime timestamp;
	private HttpStatus status;
	private int statusCode;
	List<String> errors = new ArrayList<>();
	private String details;
	public ExceptionDetail(LocalDateTime timestamp,List<String> errors, HttpStatus status,int statusCode,String details ) {
		this.timestamp = timestamp;
		this.errors = errors;
		this.status = status;
		this.statusCode = statusCode;
		this.details = details;
	}
	public List<String> getErrors() {
		return errors;
	}
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public String getDetails() {
		return details;
	}
	public HttpStatus getStatus() {
		return status;
	}
	public int getStatusCode() {
		return statusCode;
	}
}
