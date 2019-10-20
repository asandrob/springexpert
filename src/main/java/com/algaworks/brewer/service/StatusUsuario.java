package com.algaworks.brewer.service;

import com.algaworks.brewer.repository.Usuarios;

public enum StatusUsuario {
	
	ATIVAR {
		@Override
		public void executar(Long[] codigos, Usuarios usuarios) {
			usuarios.findByCodigoIn(codigos).forEach(usuario -> usuario.setAtivo(true));
		}
	},
	DESATIVAR {
		@Override
		public void executar(Long[] codigos, Usuarios usuarios) {
			usuarios.findByCodigoIn(codigos).forEach(usuario -> usuario.setAtivo(false));			
		}
	};
	
	public abstract void executar(Long[] codigos, Usuarios usuarios);
}
