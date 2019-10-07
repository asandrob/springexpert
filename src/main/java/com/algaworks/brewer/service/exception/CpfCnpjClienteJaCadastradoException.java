package com.algaworks.brewer.service.exception;

public class CpfCnpjClienteJaCadastradoException extends RuntimeException {

	private static final long serialVersionUID = -5301018262300221302L;
	
	public CpfCnpjClienteJaCadastradoException(String message) {
		super(message);
	}
	
	public CpfCnpjClienteJaCadastradoException(String message, Exception e) {
		super(message, e);
	}
}
