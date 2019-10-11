package com.algaworks.brewer.service.exception;

public class EmailUsuarioJaCadastradoException extends RuntimeException {

	private static final long serialVersionUID = 6028873599670605169L;

	public EmailUsuarioJaCadastradoException(String mensagem) {
		super(mensagem);
	}
}
