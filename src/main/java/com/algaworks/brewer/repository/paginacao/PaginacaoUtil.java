package com.algaworks.brewer.repository.paginacao;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PaginacaoUtil {
	
	public void preparaPaginacao(Pageable pageable,
			 TypedQuery<?> query) {
		int registrosPorPagina = pageable.getPageSize();
		int paginaAtual = pageable.getPageNumber();
		int primeiroRegistro= registrosPorPagina * paginaAtual;
		query.setFirstResult(primeiroRegistro);
		query.setMaxResults(registrosPorPagina);
	}
	
	public void preparaOrdenacao(Pageable pageable, CriteriaQuery<?> criteriaQuery,
			CriteriaBuilder builder, Root<?> root) {
		Sort sort = pageable.getSort();
		if (sort != null) {
			Sort.Order order = sort.iterator().next();
			String property = order.getProperty();
			String[] properties = property.split("\\.");
			Path<?> path = null;
			for (String p : properties) {
				if (path == null) {
					path = root.get(p);
				} else {
					path = path.get(p);
				}
			}
			criteriaQuery.orderBy(order.isAscending() ? builder.asc(path) : builder.desc(path));
		}
		
	}
}
