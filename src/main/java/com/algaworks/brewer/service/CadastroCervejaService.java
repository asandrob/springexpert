package com.algaworks.brewer.service;

import java.util.List;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.dto.CervejaDTO;
import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.repository.Cervejas;
import com.algaworks.brewer.repository.filter.CervejaFilter;
import com.algaworks.brewer.service.event.CervejaSalvaEvent;
import com.algaworks.brewer.service.exception.ImpossivelExcluirEntidadeException;
import com.algaworks.brewer.storage.FotoStorage;

@Service
public class CadastroCervejaService {
	
	@Autowired
	private Cervejas cervejas;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private FotoStorage fotoStorage;
	
	@Transactional
	public void salvar(Cerveja cerveja) {
		cervejas.save(cerveja);
		publisher.publishEvent(new CervejaSalvaEvent(cerveja));
	}
	
	@Transactional
	public void excluir(Cerveja cerveja) {
		try {
			String foto = cerveja.getFoto();
			cervejas.delete(cerveja);
			cervejas.flush();
			fotoStorage.remover(foto);
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidadeException("Impossível excluir esta cerveja, provavelmente já foi utilizada em alguma venda!");
		}
	}
	
	public Cerveja recuperar(Long codigo) {
		return cervejas.findOne(codigo);
	}
	public Page<Cerveja> filtrar(CervejaFilter filtro, Pageable pageable){
		return cervejas.filtrar(filtro, pageable);
	}
	
	public List<CervejaDTO> filtrar(String skuOuNome) {
		return cervejas.porSkuouNome(skuOuNome);
	}
}
