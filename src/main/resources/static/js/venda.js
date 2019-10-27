Brewer.Venda = (function(){
	
	function Venda(tabelaItensVenda) {
		this.tabelaItensVenda = tabelaItensVenda
		this.valorTotalBox = $('.js-valor-total-box');
		this.valorFreteInput = $('#valorFrete');
		this.valorDescontoInput = $('#valorDesconto');
		this.footerBoxValor = $('.js-footer-box');
		this.valorTotalItens = tabelaItensVenda.valorTotal();
		this.valorFrete = this.valorFreteInput.data('valor');
		this.valorDesconto = this.valorDescontoInput.data('valor');
	}
	
	Venda.prototype.iniciar = function() {
		this.tabelaItensVenda.on('tabela-itens-atualizada', onTabelaItensAtualizada.bind(this));
		this.valorFreteInput.on('keyup', onValorFreteAlterado.bind(this));
		this.valorDescontoInput.on('keyup', onValorDescontoAlterado.bind(this));
		this.tabelaItensVenda.on('tabela-itens-atualizada', onValoresAlterados.bind(this));
		this.valorFreteInput.on('keyup', onValoresAlterados.bind(this));
		this.valorDescontoInput.on('keyup', onValoresAlterados.bind(this));
		onValoresAlterados.call(this);
	}
	
	function onValorFreteAlterado(evento){
		this.valorFrete = Brewer.recuperarValor($(evento.target).val());
	}
	
	function onValorDescontoAlterado(evento){
		this.valorDesconto = Brewer.recuperarValor($(evento.target).val());
	}
	
	function onTabelaItensAtualizada(evento, valorTotalItens) {
		this.valorTotalItens = valorTotalItens == null ? 0 : valorTotalItens;
	}
	
	function onValoresAlterados() {
//		console.log("vlr total", this.valorTotalItens);
//		console.log("vlr tot numeral", numeral(this.valorTotalItens).value())
//		console.log("vlr frete", this.valorFrete);
//		console.log("vlr frete numeral", numeral(this.valorFrete).value());
//		console.log("vlr desc", this.valorDesconto);
//		console.log("vlr desc numeral", numeral(this.valorDesconto).value());
		var valor = numeral(this.valorTotalItens) + numeral(this.valorFrete) - numeral(this.valorDesconto);
//		console.log('vlr tot', valor);
		this.valorTotalBox.html(Brewer.formatarMoeda(valor));
		this.footerBoxValor.toggleClass('footer-box-alert', valor < 0);
		if (valor < 0) {
			this.footerBoxValor.css('background','red');
		} else {
			this.footerBoxValor.css('background', '');
		}
	}
	
	return Venda;
	
}())





$(function() {
	var autocomplete = new Brewer.Autocomplete();
	autocomplete.iniciar();
	var tabelaItens = new Brewer.TabelaItens(autocomplete);
	tabelaItens.iniciar();
	var venda = new Brewer.Venda(tabelaItens);
	venda.iniciar();
	
})
