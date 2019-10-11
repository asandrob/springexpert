package com.algaworks.brewer.service.exception;

public class SenhaObrigatoriaException extends RuntimeException {

	private static final long serialVersionUID = -5014180350864913853L;
	
	public SenhaObrigatoriaException(String mensagem) {
		super(mensagem);
	}
}
