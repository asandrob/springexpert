package com.algaworks.brewer.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.algaworks.brewer.model.Usuario;
import com.algaworks.brewer.repository.Usuarios;

@Service
public class AppUserDetailsService implements UserDetailsService {

	@Autowired
	private Usuarios usuarios;
	
	private UsuarioSistema usuarioSistema;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> usuarioOptional = usuarios.porEmailAtivo(username);
		Usuario usuario = usuarioOptional.orElseThrow(() -> new UsernameNotFoundException("Usuário e/ou senha incorretos"));
		UsuarioSistema usuarioSistema = new UsuarioSistema(usuario, getPermissoes(usuario));
		return usuarioSistema;
	}

	private Collection<? extends GrantedAuthority> getPermissoes(Usuario usuario) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		List<String> permissoes = usuarios.permissoes(usuario);
		permissoes.forEach(permissao -> authorities.add(new SimpleGrantedAuthority(permissao.toUpperCase())));
		return authorities;
	}
	
	@Bean
	public UsuarioSistema UsuarioSistema() {
		return this.usuarioSistema;
	}

}
