package com.algaworks.brewer.repository.filter;

import java.math.BigDecimal;
import java.util.List;

import com.algaworks.brewer.model.Estilo;
import com.algaworks.brewer.model.Origem;
import com.algaworks.brewer.model.Sabor;

public class CervejaFilter {
	
	private String sku;
	
	private String nome;
	
	private List<Estilo> estilos;
	
	private List<Sabor> sabores;
	
	private Origem origem;
	
	private BigDecimal valorDe;
	
	private BigDecimal valorAte;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Estilo> getEstilos() {
		return estilos;
	}

	public void setEstilos(List<Estilo> estilos) {
		this.estilos = estilos;
	}

	public List<Sabor> getSabores() {
		return sabores;
	}

	public void setSabores(List<Sabor> sabores) {
		this.sabores = sabores;
	}

	public Origem getOrigem() {
		return origem;
	}

	public void setOrigem(Origem origem) {
		this.origem = origem;
	}

	public BigDecimal getValorDe() {
		return valorDe;
	}

	public void setValorDe(BigDecimal valorDe) {
		this.valorDe = valorDe;
	}

	public BigDecimal getValorAte() {
		return valorAte;
	}

	public void setValorAte(BigDecimal valorAte) {
		this.valorAte = valorAte;
	}
	
}
