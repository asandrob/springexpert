package com.algaworks.brewer.repository.helper.cliente;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.algaworks.brewer.model.Cliente;
import com.algaworks.brewer.repository.filter.ClienteFilter;
import com.algaworks.brewer.repository.paginacao.PaginacaoUtil;

public class ClientesImpl implements ClientesQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;
	
	@Override
	public Page<Cliente> filtrar(ClienteFilter filtro, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Cliente> criteriaQuery = builder.createQuery(Cliente.class);
		Root<Cliente> clienteRoot = criteriaQuery.from(Cliente.class);
		
		clienteRoot.fetch("endereco").fetch("cidade", JoinType.LEFT).fetch("estado", JoinType.LEFT);
		
		Predicate[] predicates = criarCriterios(filtro, builder, clienteRoot);
		criteriaQuery.where(predicates);
		paginacaoUtil.preparaOrdenacao(pageable, criteriaQuery, builder, clienteRoot);
		TypedQuery<Cliente> query = manager.createQuery(criteriaQuery);
		paginacaoUtil.preparaPaginacao(pageable, query);
		return new PageImpl<Cliente>(query.getResultList(), pageable, total(filtro));
	}

	private Predicate[] criarCriterios(ClienteFilter filtros, CriteriaBuilder builder, Root<Cliente> clienteRoot) {
		List<Predicate> predicatesList = new ArrayList<>();
		if (!StringUtils.isEmpty(filtros.getNome())) {
			predicatesList.add(builder.like(clienteRoot.get("nome"), "%" + filtros.getNome() + "%"));
		}
		if (!StringUtils.isEmpty(filtros.getCpfOuCnpj())) {
			predicatesList.add(builder.like(clienteRoot.get("cpfOuCnpj"), filtros.getCpfOuCnpj() + "%"));
		}
		return predicatesList.toArray(new Predicate[0]);
	}
	
	private Long total(ClienteFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Cliente> root = criteria.from(Cliente.class);
		Predicate[] predicates = criarCriterios(filtro, builder, root);
		criteria.where(predicates);
		criteria.select(builder.count(root));
		return manager.createQuery(criteria).getSingleResult();
	}

}
