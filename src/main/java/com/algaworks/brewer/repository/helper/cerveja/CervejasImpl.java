package com.algaworks.brewer.repository.helper.cerveja;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.model.Estilo;
import com.algaworks.brewer.model.Sabor;
import com.algaworks.brewer.repository.filter.CervejaFilter;
import com.algaworks.brewer.repository.paginacao.PaginacaoUtil;

public class CervejasImpl implements CervejasQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;
	
	@Override
	public Page<Cerveja> filtrar(CervejaFilter filtro, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Cerveja> criteriaQuery = builder.createQuery(Cerveja.class);
		Root<Cerveja> cervejaRoot = criteriaQuery.from(Cerveja.class);
		cervejaRoot.fetch("estilo");
		Predicate[] predicates = criarCriterios(filtro, builder, cervejaRoot);
		criteriaQuery.where(predicates);
		paginacaoUtil.preparaOrdenacao(pageable, criteriaQuery, builder, cervejaRoot);
		TypedQuery<Cerveja> query = manager.createQuery(criteriaQuery);
		paginacaoUtil.preparaPaginacao(pageable, query);
		return new PageImpl<Cerveja>(query.getResultList(), pageable, total(filtro));		
	}

	private Predicate[] criarCriterios(CervejaFilter filtros, CriteriaBuilder builder, Root<Cerveja> cervejaRoot) {
		List<Predicate> predicatesList = new ArrayList<>();
		if (!StringUtils.isEmpty(filtros.getSku())) {
			predicatesList.add(builder.equal(cervejaRoot.get("sku"), filtros.getSku()));
		}
		if (!StringUtils.isEmpty(filtros.getNome())) {
			predicatesList.add(builder.like(cervejaRoot.get("nome"), "%" + filtros.getNome() + "%"));
		}
		if (filtros.getEstilos() !=  null) {
			if (!filtros.getEstilos().isEmpty()) {
				Expression<Estilo> estilosExpression = cervejaRoot.get("estilo");
				predicatesList.add(estilosExpression.in(filtros.getEstilos()));
			}
		}
		if (filtros.getSabores() !=  null) {
			if (!filtros.getSabores().isEmpty()) {
				Expression<Sabor> estilosExpression = cervejaRoot.get("sabor");
				predicatesList.add(estilosExpression.in(filtros.getSabores()));
			}
		}
		if (filtros.getOrigem() != null) {
			predicatesList.add(builder.equal(cervejaRoot.get("origem"), filtros.getOrigem()));
		}
		if (filtros.getValorDe() !=  null) {
			if (filtros.getValorDe().compareTo(BigDecimal.ZERO) != 0) {
				predicatesList.add(builder.ge(cervejaRoot.get("valor"), filtros.getValorDe()));
			}
		}
		if (filtros.getValorAte() !=  null) {
			if (filtros.getValorAte().compareTo(BigDecimal.ZERO) != 0) {
				predicatesList.add(builder.le(cervejaRoot.get("valor"), filtros.getValorAte()));
			}
		}
		return predicatesList.toArray(new Predicate[0]);
	}
	
	private Long total(CervejaFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Cerveja> root = criteria.from(Cerveja.class);
		Predicate[] predicates = criarCriterios(filtro, builder, root);
		criteria.where(predicates);
		criteria.select(builder.count(root));
		return manager.createQuery(criteria).getSingleResult();
	}

}
