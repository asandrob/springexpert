package com.algaworks.brewer.repository.helper.vendas;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import org.springframework.util.StringUtils;

import com.algaworks.brewer.model.TipoPessoa;
import com.algaworks.brewer.model.Venda;
import com.algaworks.brewer.repository.filter.VendaFilter;
import com.algaworks.brewer.repository.paginacao.PaginacaoUtil;

public class VendasImpl implements VendasQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;

	@Override
	public Page<Venda> filtrar(VendaFilter filtro, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Venda> criteriaQuery = builder.createQuery(Venda.class);
		Root<Venda> vendaRoot = criteriaQuery.from(Venda.class);
		vendaRoot.fetch("cliente");
		Predicate[] predicates = criarCriterios(filtro, builder, vendaRoot);
		criteriaQuery.where(predicates);
		paginacaoUtil.preparaOrdenacao(pageable, criteriaQuery, builder, vendaRoot);
		TypedQuery<Venda> query = manager.createQuery(criteriaQuery);
		paginacaoUtil.preparaPaginacao(pageable, query);
		return new PageImpl<Venda>(query.getResultList(), pageable, total(filtro));		
	}
	
	@Override
	public Venda buscarComItens(Long codigo) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Venda> criteriaQuery = builder.createQuery(Venda.class);
		Root<Venda> vendaRoot = criteriaQuery.from(Venda.class);
		vendaRoot.fetch("itens");
		criteriaQuery.where(builder.equal(vendaRoot.get("codigo"), codigo));
		TypedQuery<Venda> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult();
	}
	
	private Predicate[] criarCriterios(VendaFilter filtros, CriteriaBuilder builder, Root<Venda> vendaRoot) {
		List<Predicate> predicatesList = new ArrayList<>();
		if (filtros.getCodigo() != null) {
			if (filtros.getCodigo() != 0L) {
				predicatesList.add(builder.equal(vendaRoot.get("codigo"), filtros.getCodigo()));
			}
		}
		if (filtros.getStatus() != null) {
			predicatesList.add(builder.equal(vendaRoot.get("status"), filtros.getStatus()));
		}
		if (filtros.getDesde() !=  null) {
			LocalDateTime desde = LocalDateTime.of(filtros.getDesde(), LocalTime.of(0, 0));
			predicatesList.add(builder.greaterThanOrEqualTo(vendaRoot.get("dataCriacao"), desde));
		}
		if (filtros.getAte() !=  null) {
			LocalDateTime ate = LocalDateTime.of(filtros.getAte(), LocalTime.of(23, 59));
			predicatesList.add(builder.lessThanOrEqualTo(vendaRoot.get("dataCriacao"), ate));
		}
		if (filtros.getValorMinimo() !=  null) {
			if (filtros.getValorMinimo().compareTo(BigDecimal.ZERO) != 0) {
				predicatesList.add(builder.greaterThanOrEqualTo(vendaRoot.get("valorTotal"), filtros.getValorMinimo()));
			}
		}
		if (filtros.getValorMaximo() !=  null) {
			if (filtros.getValorMaximo().compareTo(BigDecimal.ZERO) != 0) {
				predicatesList.add(builder.lessThanOrEqualTo(vendaRoot.get("valorTotal"), filtros.getValorMaximo()));
			}
		}
		if (!StringUtils.isEmpty(filtros.getNomeCliente())) {
			predicatesList.add(builder.like(vendaRoot.get("cliente").get("nome"), "%" + filtros.getNomeCliente() + "%"));
		}
		if (!StringUtils.isEmpty(filtros.getCpfOuCnpjCliente())) {
			predicatesList.add(builder.like(vendaRoot.get("cliente").get("cpfOuCnpj"), TipoPessoa.removerFormatacao(filtros.getCpfOuCnpjCliente()) + "%"));
		}
		return predicatesList.toArray(new Predicate[0]);
	}
	
	private Long total(VendaFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Venda> root = criteria.from(Venda.class);
		Predicate[] predicates = criarCriterios(filtro, builder, root);
		criteria.where(predicates);
		criteria.select(builder.count(root));
		return manager.createQuery(criteria).getSingleResult();
	}

}
