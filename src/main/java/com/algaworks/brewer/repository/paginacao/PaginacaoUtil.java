package com.algaworks.brewer.repository.paginacao;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PaginacaoUtil {
	
	public void preparaPaginacao(Pageable pageable,
			CriteriaBuilder builder, CriteriaQuery<?> criteriaQuery,
			Root<?> root, TypedQuery<?> query) {
	int registrosPorPagina = pageable.getPageSize();
	int paginaAtual = pageable.getPageNumber();
	int primeiroRegistro= registrosPorPagina * paginaAtual;
	Sort sort = pageable.getSort();
	if (sort != null) {
		Sort.Order order = sort.iterator().next();
		String property = order.getProperty();
		criteriaQuery.orderBy(order.isAscending() ? builder.asc(root.get(property)) : builder.desc(root.get(property)));
	}
	query.setFirstResult(primeiroRegistro);
	query.setMaxResults(registrosPorPagina);
}
}
