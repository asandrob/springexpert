package com.algaworks.brewer.model.validation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;
import org.springframework.util.StringUtils;

import com.algaworks.brewer.model.Cliente;

public class ClienteGroupSequenceProvider implements DefaultGroupSequenceProvider<Cliente> {

	@Override
	public List<Class<?>> getValidationGroups(Cliente cliente) {
		List<Class<?>> grupos = new ArrayList<>();
		grupos.add(Cliente.class);
		if (isTipoPessoaSelecionado(cliente)) {
			grupos.add(cliente.getTipoPessoa().getGrupo());
		}
		return grupos;
	}

	private boolean isTipoPessoaSelecionado(Cliente cliente) {
		return cliente != null &&
				cliente.getTipoPessoa() != null &&
				!StringUtils.isEmpty(cliente.getCpfOuCnpj());
	}

}
