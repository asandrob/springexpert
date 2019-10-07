package com.algaworks.brewer.service;

import java.util.Optional;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.model.Cliente;
import com.algaworks.brewer.repository.Clientes;
import com.algaworks.brewer.repository.filter.ClienteFilter;
import com.algaworks.brewer.service.exception.CpfCnpjClienteJaCadastradoException;

@Service
public class CadastroClienteService {

	@Autowired
	private Clientes clientes;
	
	@Transactional
	public void salvar(Cliente cliente) {
		Optional<Cliente> clienteExistente = clientes.findByCpfOuCnpj(cliente.getCpfOuCnpjSemFormatacao());
		if (clienteExistente.isPresent()) {
			throw new CpfCnpjClienteJaCadastradoException(cliente.getTipoPessoa().getDocumento() + " já cadastrado");
		}
		try {
			clientes.save(cliente);
		} catch (PersistenceException e) {
			throw new CpfCnpjClienteJaCadastradoException("Erro ao tentar cadastrar este " + cliente.getTipoPessoa().getDocumento(), e);
		}
	}

	public Page<Cliente> filtrar(ClienteFilter filtro, Pageable pageable) {
		return clientes.filtrar(filtro, pageable);
	}
	
}
