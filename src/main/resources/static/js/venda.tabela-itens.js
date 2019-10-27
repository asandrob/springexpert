Brewer.TabelaItens = (function() {

	function TabelaItens(autocomplete) {
		this.autocomplete = autocomplete;
		this.tabelaCervejasContainer = $('.js-tabela-cervejas-container');
		this.uuid = $('#uuid').val();
		this.emitter = $({});
		this.on = this.emitter.on.bind(this.emitter);
	}

	TabelaItens.prototype.iniciar = function() {
		this.autocomplete.on('item-selecionado', onItemSelecionado.bind(this))
		bindQuantidade.call(this);
		bindTabelaItens.call(this);
	}
	
	TabelaItens.prototype.valorTotal = function() {
		return this.tabelaCervejasContainer.data('valor');
	}

	function onItemSelecionado(event, item) {
		var resposta = $.ajax({
			url: 'item',
			method: 'post',
			data: {
				codigoCerveja: item.codigo,
				uuid: this.uuid
			}
		});
		resposta.done(onItemAtualizadoServidor.bind(this));
	}
	
	function onItemAtualizadoServidor(html) {
		this.tabelaCervejasContainer.html(html);
		bindQuantidade.call(this);
		
		var tabelaItens = bindTabelaItens.call(this)
		
		var valorTotal = tabelaItens.data('valor-total');
		this.emitter.trigger('tabela-itens-atualizada', valorTotal);
	}
	
	function onExclusaoClick(evento) {
		var codigoCerveja = $(evento.target).data('codigo-cerveja');
		var resposta = $.ajax({
			url: 'item/' + this.uuid + '/' + codigoCerveja,
			method: 'delete',
			data: {
				codigoCerveja: codigoCerveja
			}
		});
		resposta.done(onItemAtualizadoServidor.bind(this));
	}
	
	function onDoubleClick(evento) {
		$(this).toggleClass('solicitando-exclusao');
	}
	
	function onQuantidadeAlterada(evento) {
		var input = $(evento.target);
		var quantidade = input.val();
		if (quantidade <=0) {
			input.val(1);
			quantidade = 1;
		}
		var codigoCerveja = input.data('codigo-cerveja');
		var resposta = $.ajax({
			url: 'item/' + codigoCerveja,
			method: 'put',
			data: {
				quantidade: quantidade,
				uuid: this.uuid
			}
		});
		resposta.done(onItemAtualizadoServidor.bind(this));
	}
	
	function bindQuantidade() {
		var quantidadeItemInput = $('.js-tabela-cerveja-quantidade-item');
		quantidadeItemInput.on('change', onQuantidadeAlterada.bind(this));
		quantidadeItemInput.maskMoney({ precision: 0, thousands: '' });
	}
	
	function bindTabelaItens() {
		var tabelaItens = $('.js-tabela-item'); 
		tabelaItens.on('dblclick', onDoubleClick);
		$('.js-btn-excluir').on('click', onExclusaoClick.bind(this));
		return tabelaItens;
	}
	
	return TabelaItens;

}());

