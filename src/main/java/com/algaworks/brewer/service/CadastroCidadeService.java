package com.algaworks.brewer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.model.Cidade;
import com.algaworks.brewer.repository.Cidades;
import com.algaworks.brewer.repository.filter.CidadeFilter;
import com.algaworks.brewer.service.exception.CidadeJaCadastradaException;

@Service
public class CadastroCidadeService {
	
	@Autowired
	private Cidades cidades;
	
	@Transactional
	public void salvar(Cidade cidade) {
		Optional<Cidade> cidadeExistente = cidades.findByEstadoAndNomeIgnoreCase(cidade.getEstado(), cidade.getNome());
		if (cidadeExistente.isPresent()) {
			throw new CidadeJaCadastradaException("Cidade " + cidade.getNome() + " já cadastrada");
		}
		cidades.save(cidade);
	}


	public Page<Cidade> filtrar(CidadeFilter filtro, Pageable pageable) {
		return cidades.filtrar(filtro, pageable);
	}

}
