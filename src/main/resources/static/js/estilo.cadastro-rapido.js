var Brewer = Brewer || {};

Brewer.EstiloCadastroRapido = (function() {
	
	function EstiloCadastroRapido() {
		//pegar o modal pelo seu id
		//lá no html tem um elemento com id=modalCadastroRapidoEstilo
		this.modal = $('#modalCadastroRapidoEstilo');
		//pegar o botão Salvar, partindo do modal
		//para facilitar, usamos uma classe css de marcação js-modal-cadastro-estilo-salvar-btn
		this.botaoSalvar = this.modal.find('.js-modal-cadastro-estilo-salvar-btn');
		//pegar o form, também partindo do modal
		/* os seletores são:
		 * . para classe CSS
		 * # para ID
		 * sem nada, procura o elemento
		 * provavelmente existem mais, porém isso é tema para outro momento
		 */
		this.form = this.modal.find('form');
		this.inputNomeEstilo = this.modal.find('#nome');
		this.url = this.form.attr('action');
		this.containerMensagemErro = this.form.find('.js-mensagem-cadastro-rapido-estilo');
		/* para previnir que, ao apertar ENTER, o form seja submetido
		 * tem uma função preventDefault(), associado ao evento submit
		 * do form
		 */
		
	}
	
	EstiloCadastroRapido.prototype.iniciar = function() {
		this.form.on('submit', function(e) { e.preventDefault() });
		/* o modal do Bootstrap tem vários eventos, e podemos usar eles para fazer 
		 * umas coisas bem interessante, como um requisição ajax ao abrir um modal
		 * aqui usaremos o evento show para focar o input, pois o atributo  autofocus="autofocus"
		 * não funcionou, muito provavel que algum evento do layout esteja interferindo nesse comportamento 
		 */
		this.modal.on('shown.bs.modal', onModalShow.bind(this));
		/*
		 * quando fechamos clicando em cancelar, se houver algum valor digitado, ao reabrir o input já vem
		 * com o valor digitado, para corrigir isso, limpamos tudo ao fechar
		 */
		this.modal.on('hide.bs.modal', onModalClose.bind(this));
		this.botaoSalvar.on('click', onBotaoSalvarClick.bind(this));
		
		
		
	}
	
	function onModalShow(){
		this.inputNomeEstilo.focus();
	}

	function onModalClose(){
		this.inputNomeEstilo.val('');
		this.containerMensagemErro.addClass('hidden');
		this.form.find('.form-group').removeClass('has-error');
	}
	
	function onBotaoSalvarClick(){
		var nomeEstilo = this.inputNomeEstilo.val().trim();
		$.ajax({
			url: this.url,
			method: 'post',
			contentType: 'application/json',
			data: JSON.stringify({ nome: nomeEstilo }),
			error: onErroSalvandoEstilo.bind(this),
			success: onEstiloSalvo.bind(this),
		});
		
	}
	
	function onErroSalvandoEstilo(o){
		var mensagemErro = o.responseText;
		this.containerMensagemErro.removeClass('hidden');
		this.containerMensagemErro.html('<span>' + mensagemErro + '</span>');
		this.form.find('.form-group').addClass('has-error');
	}
	
	function onEstiloSalvo(estilo){
		var comboEstilo = $('#estilo');
		comboEstilo.append('<option value=' + estilo.codigo + '>' + estilo.nome + '</option>');
		comboEstilo.val(estilo.codigo);
		this.modal.modal('hide');
	}
	
	
	return EstiloCadastroRapido;
	
}());



$(function(){
	var cadastroEstiloRapido = new Brewer.EstiloCadastroRapido();
	cadastroEstiloRapido.iniciar();
});