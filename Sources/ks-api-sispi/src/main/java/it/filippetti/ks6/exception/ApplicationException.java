/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks6.exception;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * 
 * @author marco.mazzocchetti
 */
public abstract class ApplicationException extends Exception {

	private static final long serialVersionUID = 1L;
	private List<String> details;

	public ApplicationException(Throwable cause) {
		super(cause);
		this.details = Collections.emptyList();
	}

	public ApplicationException(String message) {
		super(message);
		this.details = Collections.emptyList();
	}

	public ApplicationException(String message, String details) {
		super(message);
		this.details = Collections.unmodifiableList(Lists.newArrayList(details));
	}

	public ApplicationException(String message, List<String> details) {
		super(message);
		this.details = Collections.unmodifiableList(details);
	}

	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
		this.details = Collections.emptyList();
	}

	public ApplicationException(String message, String details, Throwable cause) {
		super(message, cause);
		this.details = Collections.unmodifiableList(Lists.newArrayList(details));
	}

	public ApplicationException(String message, List<String> details, Throwable cause) {
		super(message, cause);
		this.details = Collections.unmodifiableList(details);
	}

	public List<String> getDetails() {
		return details;
	}
}