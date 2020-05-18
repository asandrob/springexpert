package com.algaworks.brewer.service.event;

import org.springframework.util.StringUtils;

import com.algaworks.brewer.model.Cerveja;

public class CervejaSalvaEvent {
	
	private Cerveja cerveja;

	public Cerveja getCerveja() {
		return cerveja;
	}

	public CervejaSalvaEvent(Cerveja cerveja) {
		this.cerveja = cerveja;
	}
	
	public boolean temFoto() {
		return !StringUtils.isEmpty(cerveja.getFoto());
	}
	
	public boolean isNovafoto() {
		return cerveja.isNovaFoto();
	}
	
}
