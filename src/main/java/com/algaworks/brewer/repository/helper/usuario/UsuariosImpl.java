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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.algaworks.brewer.model.Grupo;
import com.algaworks.brewer.model.Permissao;
import com.algaworks.brewer.model.Usuario;

public class UsuariosImpl implements UsuariosQueries {

	@PersistenceContext
	private EntityManager manager;
	
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

}
