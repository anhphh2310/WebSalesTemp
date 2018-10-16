package tma.datraining.controller;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import tma.datraining.exception.BadRequestException;
import tma.datraining.exception.ForbiddentException;
import tma.datraining.exception.NotFoundDataException;
import tma.datraining.model.ResponseMsg;
import tma.datraining.util.LogUtil;;
@RestControllerAdvice
public class BaseController extends ResponseEntityExceptionHandler {

	private static final String NOT_FOUND_DATA = " NOT exist in the database.";
	private static final String BAD_REQUEST = "Wrong something in request.";
//	private static final String INTERNAL_EXCEPTION = "Internal server error.";
	private static final String FORBIDDEN = "have no permission.";
	
	private static final Logger LOG  = LoggerFactory.getLogger(BaseController.class);
	
	@ExceptionHandler(NotFoundDataException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ResponseMsg> notFoundData(NotFoundDataException ex, WebRequest re){
		LogUtil.error(LOG,NOT_FOUND_DATA);
		ResponseMsg res = new ResponseMsg(HttpStatus.NOT_FOUND,ex.getMessage() + NOT_FOUND_DATA,re.getDescription(false));
		return new ResponseEntity<>(res,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = { BadRequestException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ResponseMsg> badRequest(BadRequestException ex, WebRequest re){
		LogUtil.error(LOG,BAD_REQUEST);
		ResponseMsg res = new ResponseMsg(HttpStatus.BAD_REQUEST, BAD_REQUEST, re.getDescription(false));
		return new ResponseEntity<>(res,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = { ConstraintViolationException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ResponseMsg> constraintViolation(BadRequestException ex, WebRequest re){
		LogUtil.error(LOG,BAD_REQUEST);
		ResponseMsg res = new ResponseMsg(HttpStatus.BAD_REQUEST, BAD_REQUEST, re.getDescription(false));
		return new ResponseEntity<>(res,HttpStatus.BAD_REQUEST);
	}
//	@ExceptionHandler(value= {Exception.class})
//	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//	public ResponseEntity<ResponseMsg> unknowException(Exception ex, WebRequest re){
//		LOG.error(INTERNAL_EXCEPTION);
//		ResponseMsg res = new ResponseMsg(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_EXCEPTION , re.getDescription(false));
//		return new ResponseEntity<>(res,HttpStatus.INTERNAL_SERVER_ERROR);
//	}
	
	@ExceptionHandler(value= {ForbiddentException.class})
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<ResponseMsg> forbidden(ForbiddentException ex, WebRequest re){
		LogUtil.error(LOG,FORBIDDEN);
		ResponseMsg res = new ResponseMsg(HttpStatus.UNAUTHORIZED,ex.getMessage() + FORBIDDEN, re.getDescription(false));
		return new ResponseEntity<>(res,HttpStatus.UNAUTHORIZED);
	}
	
	
	
}
