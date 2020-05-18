package com.algaworks.brewer.repository.helper.usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.algaworks.brewer.model.Grupo;
import com.algaworks.brewer.model.Permissao;
import com.algaworks.brewer.model.Usuario;
import com.algaworks.brewer.model.UsuarioGrupo;
import com.algaworks.brewer.repository.filter.UsuarioFilter;
import com.algaworks.brewer.repository.paginacao.PaginacaoUtil;

public class UsuariosImpl implements UsuariosQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;
	
	@Override
	public Optional<Usuario> porEmailAtivo(String email) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Usuario> criteriaQuery = builder.createQuery(Usuario.class);
		Root<Usuario> usuarioRoot = criteriaQuery.from(Usuario.class);
		List<Predicate> predicatesList = new ArrayList<>();
		predicatesList.add(builder.equal(builder.lower(usuarioRoot.get("email")), email.toLowerCase()));
		predicatesList.add(builder.equal(usuarioRoot.get("ativo"), true));
		criteriaQuery.where(predicatesList.toArray(new Predicate[0]));
		TypedQuery<Usuario> query = manager.createQuery(criteriaQuery);
		return query.getResultList().stream().findFirst();
	}

	@Override
	public List<String> permissoes(Usuario usuario) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<String> criteriaQuery = builder.createQuery(String.class);
		Root<Usuario> usuarioRoot = criteriaQuery.from(Usuario.class);
		Join<Usuario, Grupo> usuarioGrupo = usuarioRoot.join("grupos"); 
		Join<Grupo, Permissao> grupoPermissao = usuarioGrupo.join("permissoes");
		criteriaQuery.select(grupoPermissao.get("nome")).distinct(true);
		criteriaQuery.where(builder.equal(usuarioRoot, usuario));
		TypedQuery<String> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
		/* o equivalente em JPQL
		manager.createQuery(
			"select p.nome from Usuario u inner join u.grupos g inner join g.permissoes p where u = :usuario", String.class)
			.setParameter("", usuario)
			.getResultList();
		 */
	}

	@Override
	public Page<Usuario> filtrar(UsuarioFilter filtro, Pageable pageable) {
		/* isto me ajudou
		 * https://stackoverflow.com/questions/35148276/jpa-criteriabuilder-for-join-in-subquery
		 */		
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Usuario> criteriaQuery = builder.createQuery(Usuario.class);
		Root<Usuario> usuarioRoot = criteriaQuery.from(Usuario.class);
		usuarioRoot.fetch("grupos", JoinType.LEFT);
		paginacaoUtil.preparaOrdenacao(pageable, criteriaQuery, builder, usuarioRoot);
		criteriaQuery.where(criarCriterios(filtro, builder, usuarioRoot, criteriaQuery)).distinct(true);
		TypedQuery<Usuario> query = manager.createQuery(criteriaQuery);
		paginacaoUtil.preparaPaginacao(pageable, query);
		return new PageImpl<Usuario>(query.getResultList(), pageable, total(filtro));
	}
	
	@Override
	public Usuario buscarComGrupo(Long codigo) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Usuario> criteriaQuery = builder.createQuery(Usuario.class);
		Root<Usuario> usuarioRoot = criteriaQuery.from(Usuario.class);
		usuarioRoot.fetch("grupos");
		criteriaQuery.where(builder.equal(usuarioRoot.get("codigo"), codigo));
		TypedQuery<Usuario> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult();
	}

	private Predicate[] criarCriterios(UsuarioFilter filtros, CriteriaBuilder builder, Root<Usuario> usuarioRoot, CriteriaQuery<?> criteriaQuery) {
		List<Predicate> predicatesList = new ArrayList<>();
		if (!StringUtils.isEmpty(filtros.getNome())) {
			predicatesList.add(builder.like(usuarioRoot.get("nome"), "%" + filtros.getNome() + "%"));
		}
		if (!StringUtils.isEmpty(filtros.getEmail())) {
			predicatesList.add(builder.like(usuarioRoot.get("email"), "%" + filtros.getEmail() + "%"));
		}
		if (filtros.getGrupos() != null && !filtros.getGrupos().isEmpty()) {
			for (Long codigoGrupo : filtros.getGrupos().stream().mapToLong(g ->  g.getCodigo()).toArray()) {
				Subquery<Long> sub = criteriaQuery.subquery(Long.class);
				Root<UsuarioGrupo> subRoot = sub.from(UsuarioGrupo.class);
				sub.select((subRoot.get("id").get("usuario").get("codigo")));
				sub.where(builder.equal(subRoot.get("id").get("grupo").get("codigo"), codigoGrupo));
				predicatesList.add(usuarioRoot.in(sub));
			}
		}
		return predicatesList.toArray(new Predicate[0]);
	}
	
	private Long total(UsuarioFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Usuario> root = criteria.from(Usuario.class);
		Predicate[] predicates = criarCriterios(filtro, builder, root, criteria);
		criteria.where(predicates);
		criteria.select(builder.count(root));
		return manager.createQuery(criteria).getSingleResult();
	}

}
