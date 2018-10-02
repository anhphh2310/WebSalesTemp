package tma.datraining.controller;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import tma.datraining.exception.NotFoundDataException;
import tma.datraining.model.ResponseMsg;

@RestControllerAdvice
public class BaseController {

	private static final String NOT_FOUND_DATA = "Not exist in the database.";
	
	
//	private static final Logger LOG  = LoggerFactory.getLogger(BaseController.class);
	
	@ExceptionHandler(NotFoundDataException.class)
	public ResponseEntity<ResponseMsg> notFoundData(NotFoundDataException ex, WebRequest re){
		Timestamp time = new Timestamp(System.currentTimeMillis());
		ResponseMsg res = new ResponseMsg(time,HttpStatus.NOT_FOUND.value(),NOT_FOUND_DATA,re.getDescription(false));
		return new ResponseEntity<>(res,HttpStatus.NOT_FOUND);
	}
	
	
}
