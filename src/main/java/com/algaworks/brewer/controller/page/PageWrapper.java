package com.algaworks.brewer.controller.page;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

public class PageWrapper<T> {
	
	private Page<T> page;
	private UriComponentsBuilder uriBuilder;
	
	public PageWrapper(Page<T> page, HttpServletRequest httpServletRequest) {
		this.page = page;
		String httpUrl = httpServletRequest.getRequestURL().append(
				httpServletRequest.getQueryString() != null ? "?" + httpServletRequest.getQueryString() : "")
				.toString().replaceAll("\\+", "%20");
		this.uriBuilder = ServletUriComponentsBuilder.fromHttpUrl(httpUrl);
	}
	
	public List<T> getConteudo(){
		return this.page.getContent();
	}
	
	public boolean isVazia() {
		return this.page.getContent().isEmpty();
	}
	
	public int getAtual() {
		return this.page.getNumber();
	}
	
	public boolean isPrimeira() {
		return this.page.isFirst();
	}
	
	public boolean isUltima() {
		return this.page.isLast();
	}
	
	public int getTotal() {
		return this.page.getTotalPages();
	}
	
	public String urlParaPagina(int pagina) {
		return uriBuilder.replaceQueryParam("page", pagina).build(true).encode().toUriString();
	}
	
	public String urlOrdenada(String propriedade) {
		UriComponentsBuilder uriBuilderOrder = UriComponentsBuilder.fromHttpUrl(uriBuilder.build(true).encode().toUriString());
		String valorSort = String.format("%s,%s", propriedade, inverterDirecao(propriedade)); 
		return uriBuilderOrder.replaceQueryParam("sort", valorSort).build(true).encode().toUriString();
	}

	public String inverterDirecao(String propriedade) {
		String direcaoPadrao = "asc";
		Order ordem = page.getSort() != null ? page.getSort().getOrderFor(propriedade) : null;
		if (ordem != null) {
			direcaoPadrao = Sort.Direction.ASC.equals(ordem.getDirection()) ? "desc" : "asc";
		}
		return direcaoPadrao;
	}
	
	public boolean descendente(String propriedade) {
		return page.getSort().getOrderFor(propriedade).isDescending();
	}
	
	public boolean ordenada(String propriedade) {
		Order ordem = page.getSort() != null ? page.getSort().getOrderFor(propriedade) : null;
		if (ordem == null) {
			return false;
		}
		return true;
	}

}
