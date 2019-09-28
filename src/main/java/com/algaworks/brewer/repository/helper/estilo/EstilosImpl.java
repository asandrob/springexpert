package com.algaworks.brewer.repository.helper.estilo;

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

import com.algaworks.brewer.model.Estilo;
import com.algaworks.brewer.repository.filter.EstiloFilter;
import com.algaworks.brewer.repository.paginacao.PaginacaoUtil;

public class EstilosImpl implements EstilosQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;
	
	@Override
	public Page<Estilo> filtrar(EstiloFilter filtro, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Estilo> criteriaQuery = builder.createQuery(Estilo.class);
		Root<Estilo> estiloRoot = criteriaQuery.from(Estilo.class);
		Predicate[] predicates = criarCriterios(filtro, builder, estiloRoot);
		criteriaQuery.where(predicates);
		TypedQuery<Estilo> query = manager.createQuery(criteriaQuery);
		
		paginacaoUtil.preparaPaginacao(pageable, builder, criteriaQuery, estiloRoot, query);
		
		return new PageImpl<Estilo>(query.getResultList(), pageable, total(filtro));		
	}
	
	private Predicate[] criarCriterios(EstiloFilter filtros, CriteriaBuilder builder, Root<Estilo> estiloRoot) {
		List<Predicate> predicatesList = new ArrayList<>();
		if (!StringUtils.isEmpty(filtros.getNome())) {
			predicatesList.add(builder.like(estiloRoot.get("nome"), "%" + filtros.getNome() + "%"));
		}
		return predicatesList.toArray(new Predicate[0]);
	}
	
	private Long total(EstiloFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Estilo> root = criteria.from(Estilo.class);
		Predicate[] predicates = criarCriterios(filtro, builder, root);
		criteria.where(predicates);
		criteria.select(builder.count(root));
		return manager.createQuery(criteria).getSingleResult();
	}

}
