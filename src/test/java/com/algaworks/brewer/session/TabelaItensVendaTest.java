package com.algaworks.brewer.session;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.algaworks.brewer.model.Cerveja;

public class TabelaItensVendaTest {

	private TabelaItensVenda tabelaItensVenda;
	
	@Before
	public void setup() {
		this.tabelaItensVenda = new TabelaItensVenda("1");
	}
	
	@Test
	public void deveRetornarValorTotalSemItens() throws Exception {
		assertEquals(BigDecimal.ZERO, tabelaItensVenda.getValorTotal());
	}
	
	@Test
	public void deveRetornarValorTotalComUmItem() throws Exception {
		Cerveja cerveja = new Cerveja();
		BigDecimal valor = new BigDecimal("8.90");
		cerveja.setValor(valor);
		tabelaItensVenda.adicionaItem(cerveja, 1);
		assertEquals(valor, tabelaItensVenda.getValorTotal());
	}
	
	@Test
	public void deveRetornarValorTotalComVariosItens() throws Exception {
		Cerveja c1 = new Cerveja();
		c1.setCodigo(1L);
		BigDecimal v1 = new BigDecimal("8.9");
		c1.setValor(v1);
		Cerveja c2 = new Cerveja();
		c2.setCodigo(2l);
		BigDecimal v2 = new BigDecimal("4.999");
		c2.setValor(v2);
		
		tabelaItensVenda.adicionaItem(c1, 1);
		tabelaItensVenda.adicionaItem(c2, 2);
		
		
		assertEquals(new BigDecimal("18.90"), tabelaItensVenda.getValorTotal());
	}
	
	@Test
	public void deveManterTamanhoDaListaParaMesmasCervejas() throws Exception {
		Cerveja c1 = new Cerveja();
		c1.setCodigo(1L);
		c1.setValor(new BigDecimal("8.9"));
		
		tabelaItensVenda.adicionaItem(c1, 1);
		tabelaItensVenda.adicionaItem(c1, 1);
		
		assertEquals(1, tabelaItensVenda.getItens().size());
	}
	
	@Test
	public void deveAlteraQuantidadeDoItem() throws Exception {
		Cerveja c1 = new Cerveja();
		c1.setCodigo(1L);
		c1.setValor(new BigDecimal("8.9"));
		tabelaItensVenda.adicionaItem(c1, 1);
		
		tabelaItensVenda.alteraQuantidadeItem(c1, 3);
		
		assertEquals(new BigDecimal("26.70"), tabelaItensVenda.getValorTotal());
	}
	
	@Test
	public void deveExcluirItem() throws Exception {
		Cerveja c1 = new Cerveja();
		c1.setCodigo(1L);
		c1.setValor(new BigDecimal("8.9"));
		
		Cerveja c2 = new Cerveja();
		c2.setCodigo(2l);
		c2.setValor(new BigDecimal("4.999"));
		
		Cerveja c3 = new Cerveja();
		c3.setCodigo(2l);
		c3.setValor(new BigDecimal("5"));
		
		tabelaItensVenda.adicionaItem(c1, 1);
		tabelaItensVenda.adicionaItem(c2, 2);
		tabelaItensVenda.adicionaItem(c3, 2);
		
		assertEquals(new BigDecimal("28.90"), tabelaItensVenda.getValorTotal());
		assertEquals(2, tabelaItensVenda.getItens().size());
	}

}
