/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks6.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.google.common.collect.Lists;
import it.filippetti.ks6.dto.ErrorDTO;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 
 * @author marco.mazzocchetti
 */
@ControllerAdvice
public class EndpointExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(EndpointExceptionHandler.class);

	/**
	 * Unexpected authentication errors
	 * 
	 * @param exception
	 * @param request
	 * @return
	 */
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<?> handleAuthorizationException(AuthenticationException exception, WebRequest request) {

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDTO(HttpStatus.UNAUTHORIZED.value(),
				HttpStatus.UNAUTHORIZED.getReasonPhrase(), Lists.newArrayList(exception.getMessage())));
	}

	/**
	 * Authorization errors
	 * 
	 * @param exception
	 * @param request
	 * @return
	 */
	@ExceptionHandler(AuthorizationException.class)
	public ResponseEntity<?> handleAuthorizationException(AuthorizationException exception, WebRequest request) {

		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(new ErrorDTO(HttpStatus.FORBIDDEN.value(), exception.getMessage(), exception.getDetails()));
	}

	/**
	 * Not found errors
	 * 
	 * @param exception
	 * @param request
	 * @return
	 */
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<?> handleNotFoundException(NotFoundException exception, WebRequest request) {

		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ErrorDTO(HttpStatus.NOT_FOUND.value(), exception.getMessage(), exception.getDetails()));
	}

	/**
	 * Validation errors
	 * 
	 * @param exception
	 * @param request
	 * @return
	 */
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ErrorDTO> handleValidationException(ValidationException exception, WebRequest request) {

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorDTO(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), exception.getDetails()));
	}

	/**
	 * Business errors
	 * 
	 * @param exception
	 * @param request
	 * @return
	 */
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<?> handleBusinessException(BusinessException exception, WebRequest request) {

		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(new ErrorDTO(HttpStatus.UNPROCESSABLE_ENTITY.value(), exception.getBusinessError().code(),
						exception.getMessage(), exception.getDetails()));
	}

	/**
	 * Operation errors (wrapped exceptions)
	 * 
	 * @param exception
	 * @param request
	 * @return
	 */
	@ExceptionHandler(OperationException.class)
	public ResponseEntity<?> handleOperationException(Throwable exception, WebRequest request) {

		return handleAllOtherExceptions(exception.getCause(), request);
	}

	/**
	 * Other errors
	 * 
	 * @param exception
	 * @param request
	 * @return
	 */
	@ExceptionHandler(Throwable.class)
	public ResponseEntity<?> handleAllOtherExceptions(Throwable exception, WebRequest request) {

		log.error(exception.getMessage(), exception);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
						HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
						Lists.newArrayList(exception.getMessage())));
	}

	/**
	 * Internal errors
	 * 
	 * @param exception
	 * @param body
	 * @param headers
	 * @param status
	 * @param request
	 * @return
	 */
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception exception, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		Throwable cause;
		String details;

		cause = exception;
		while (cause.getCause() != null) {
			cause = cause.getCause();
		}
		if (cause instanceof JsonProcessingException) {
			details = getDetails((JsonProcessingException) cause);
		} else {
			details = cause.getMessage();
		}

		return ResponseEntity.status(status.value())
				.body(new ErrorDTO(status.value(), status.getReasonPhrase(), Lists.newArrayList(details)));
	}

	private String getDetails(JsonProcessingException cause) {

		StringBuilder detailsBuilder;

		detailsBuilder = new StringBuilder();
		if (cause instanceof JsonMappingException) {
			detailsBuilder.append(getPath(((JsonMappingException) cause).getPath()));
		}
		if (cause instanceof UnrecognizedPropertyException) {
			detailsBuilder.append("unrecognized property");
		} else if (cause instanceof MismatchedInputException) {
			detailsBuilder.append("unexpected value");
		} else if (cause instanceof InvalidFormatException) {
			detailsBuilder.append("invalid value");
		} else if (cause instanceof InvalidTypeIdException) {
			detailsBuilder.append("type: invalid value");
		} else if (cause.getOriginalMessage().contains("missing property 'type'")) {
			detailsBuilder.append("type: missing value");
		} else {
			detailsBuilder.append(cause.getOriginalMessage());
		}
		return detailsBuilder.toString();
	}

	private String getPath(List<JsonMappingException.Reference> references) {

		StringBuilder sb;

		sb = new StringBuilder();
		for (JsonMappingException.Reference reference : references) {
			if (reference.getFieldName() != null) {
				sb.append(".");
				sb.append(reference.getFieldName());
			} else {
				sb.append("[");
				sb.append(reference.getIndex());
				sb.append("]");
			}
		}
		if (sb.length() > 0 && sb.charAt(0) == '.') {
			sb.deleteCharAt(0);
		}
		sb.append(": ");
		return sb.toString();
	}
}
