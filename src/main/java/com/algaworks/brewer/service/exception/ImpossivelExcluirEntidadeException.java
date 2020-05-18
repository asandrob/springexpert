package com.algaworks.brewer.service.exception;

public class ImpossivelExcluirEntidadeException extends RuntimeException {
	
	private static final long serialVersionUID = -3923052442890328070L;
	
	public ImpossivelExcluirEntidadeException(String mensagem) {
		super(mensagem);
	}
	
}
