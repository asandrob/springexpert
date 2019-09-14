$(function(){
	
	//pegar o modal pelo seu id
	//lá no html tem um elemento com id=modalCadastroRapidoEstilo
	var modal = $('#modalCadastroRapidoEstilo');
	//pegar o botão Salvar, partindo do modal
	//para facilitar, usamos uma classe css de marcação js-modal-cadastro-estilo-salvar-btn
	var botaoSalvar = modal.find('.js-modal-cadastro-estilo-salvar-btn');
	//pegar o form, também partindo do modal
	/* os seletores são:
	 * . para classe CSS
	 * # para ID
	 * sem nada, procura o elemento
	 * provavelmente existem mais, porém isso é tema para outro momento
	 */
	var form = modal.find('form');
	var inputNomeEstilo = modal.find('#nome');
	var url = form.attr('action');
	var containerMensagemErro = form.find('.js-mensagem-cadastro-rapido-estilo');
	/* para previnir que, ao apertar ENTER, o form seja submetido
	 * tem uma função preventDefault(), associado ao evento submit
	 * do form
	 */
	form.on('submit', function(e) { e.preventDefault() });
	/* o modal do Bootstrap tem vários eventos, e podemos usar eles para fazer 
	 * umas coisas bem interessante, como um requisição ajax ao abrir um modal
	 * aqui usaremos o evento show para focar o input, pois o atributo  autofocus="autofocus"
	 * não funcionou, muito provavel que algum evento do layout esteja interferindo nesse comportamento 
	 */
	modal.on('shown.bs.modal', onModalShow);
	function onModalShow(){
		inputNomeEstilo.focus();
	}
	/*
	 * quando fechamos clicando em cancelar, se houver algum valor digitado, ao reabrir o input já vem
	 * com o valor digitado, para corrigir isso, limpamos tudo ao fechar
	 */
	modal.on('hide.bs.modal', onModalClose);
	function onModalClose(){
		inputNomeEstilo.val('');
		containerMensagemErro.addClass('hidden');
		form.find('.form-group').removeClass('has-error');
	}
	
	botaoSalvar.on('click', onBotaoSalvarClick);
	function onBotaoSalvarClick(){
		var nomeEstilo = inputNomeEstilo.val().trim();
		$.ajax({
			url: url,
			method: 'post',
			contentType: 'application/json',
			data: JSON.stringify({ nome: nomeEstilo }),
			error: onErroSalvandoEstilo,
			success: onEstiloSalvo,
		});
		
	}
	
	function onErroSalvandoEstilo(o){
		var mensagemErro = o.responseText;
		containerMensagemErro.removeClass('hidden');
		containerMensagemErro.html('<span>' + mensagemErro + '</span>');
		form.find('.form-group').addClass('has-error');
	}
	
	function onEstiloSalvo(estilo){
		var comboEstilo = $('#estilo');
		comboEstilo.append('<option value=' + estilo.codigo + '>' + estilo.nome + '</option>');
		comboEstilo.val(estilo.codigo);
		modal.modal('hide');
	}
});