Brewer = Brewer || {};

Brewer.PesquisaRapidaCliente = (function(){
	
	function PesquisaRapidaCliente() {
		this.pesquisaRapidaClientesModal = $('#pesquisaRapidaClientes');
		this.inputNomeCliente = $('#nome');
		this.pesquisarBtn = $('.js-pesquisa-rapida-clientes-btn');
		this.mensagemErro = $('.js-mensagem-erro');
		this.form = this.pesquisaRapidaClientesModal.find('form');
		this.imgLoading = $('.js-img-loading');
		this.containerTabelaPesquisaRapidaClientes = $('#containerTabelaPesquisaRapidaClientes');
		this.htmlTabelaPesquisaCliente = $('#tabela-pesquisa-rapida-cliente').html();
		this.template = Handlebars.compile(this.htmlTabelaPesquisaCliente);
	}
	
	PesquisaRapidaCliente.prototype.iniciar = function(){
		this.pesquisarBtn.on('click', onPesquisarBtnClicado.bind(this));
		this.pesquisaRapidaClientesModal.on('hide.bs.modal', onModalClose.bind(this));
		this.pesquisaRapidaClientesModal.on('shown.bs.modal', onModalShow.bind(this));
	}
	
	function onPesquisarBtnClicado(evento) {
		evento.preventDefault();
		$.ajax({
			url: this.pesquisaRapidaClientesModal.find('form').attr('action'),
			method: 'get',
			contentType: 'application/json',
			data: {
				nome: this.inputNomeCliente.val()
			},
			success: onPesquisaConcluida.bind(this),
			error: onErroPesquisa.bind(this),
			beforeSend: iniciarRequisicao.bind(this)
		});
	}
	
	function onPesquisaConcluida(resultado) {
		this.imgLoading.hide();
		this.inputNomeCliente.focus();
		var html = this.template(resultado);
		this.containerTabelaPesquisaRapidaClientes.html(html);
		
		var tabelaClientePesquisaRapida = new Brewer.TabelaClientePesquisaRapida(this.pesquisaRapidaClientesModal);
		tabelaClientePesquisaRapida.iniciar();
	}
	
	function onErroPesquisa() {
		this.mensagemErro.removeClass('hidden');
		this.form.find('.form-group').addClass('has-error');
		this.inputNomeCliente.focus();
		this.imgLoading.hide();
	}
	
	function iniciarRequisicao() {
		this.mensagemErro.addClass('hidden');
		this.form.find('.form-group').removeClass('has-error');
		this.imgLoading.show();
	}
	
	function onModalShow(){
		this.containerTabelaPesquisaRapidaClientes.html('');
		this.inputNomeCliente.focus();
	}

	function onModalClose() {
		this.inputNomeCliente.val('');
		this.mensagemErro.addClass('hidden');
		this.form.find('.form-group').removeClass('has-error');
		this.containerTabelaPesquisaRapidaClientes.html('');
	}
	
	return PesquisaRapidaCliente;
	
}());

Brewer.TabelaClientePesquisaRapida = (function() {
	
	function TabelaClientePesquisaRapida(modal) {
		this.cliente = $('.js-cliente-pesquisa-rapida');
		this.modalCliente = modal;
	}
	
	TabelaClientePesquisaRapida.prototype.iniciar = function() {
		this.cliente.on('click', onClienteSelecionado.bind(this));
	}
	
	function onClienteSelecionado(evento) {
		var clienteSelecionado = $(evento.currentTarget);
		this.modalCliente.modal('hide');
		$('#nomeCliente').val(clienteSelecionado.data('nome'));
		$('#codigoCliente').val(clienteSelecionado.data('codigo'));
	}
	
	return TabelaClientePesquisaRapida;
}());

$(function(){
	var pesquisaRapidaCliente = new Brewer.PesquisaRapidaCliente();
	pesquisaRapidaCliente.iniciar();
});