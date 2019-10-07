package com.algaworks.brewer.repository.helper.cidade;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.algaworks.brewer.model.Cidade;
import com.algaworks.brewer.repository.filter.CidadeFilter;
import com.algaworks.brewer.repository.paginacao.PaginacaoUtil;

public class CidadesImpl implements CidadesQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;

	@Override
	public Page<Cidade> filtrar(CidadeFilter filtro, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Cidade> criteriaQuery = builder.createQuery(Cidade.class);
		Root<Cidade> cidadeRoot = criteriaQuery.from(Cidade.class);
		
		cidadeRoot.fetch("estado");
		
		Predicate[] predicates = criarCriterios(filtro, builder, cidadeRoot);
		criteriaQuery.where(predicates);
		paginacaoUtil.preparaOrdenacao(pageable, criteriaQuery, builder, cidadeRoot);
		TypedQuery<Cidade> query = manager.createQuery(criteriaQuery);
		paginacaoUtil.preparaPaginacao(pageable, query);
		return new PageImpl<Cidade>(query.getResultList(), pageable, total(filtro));
	}

	private Predicate[] criarCriterios(CidadeFilter filtros, CriteriaBuilder builder, Root<Cidade> cidadeRoot) {
		List<Predicate> predicatesList = new ArrayList<>();
		if (!StringUtils.isEmpty(filtros.getNome())) {
			predicatesList.add(builder.like(cidadeRoot.get("nome"), "%" + filtros.getNome() + "%"));
		}
		if (filtros.getEstado() != null) {
			predicatesList.add(builder.equal(cidadeRoot.get("estado"), filtros.getEstado()));
		}
		return predicatesList.toArray(new Predicate[0]);
	}
	
	private Long total(CidadeFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Cidade> root = criteria.from(Cidade.class);
		Predicate[] predicates = criarCriterios(filtro, builder, root);
		criteria.where(predicates);
		criteria.select(builder.count(root));
		return manager.createQuery(criteria).getSingleResult();
	}

}
