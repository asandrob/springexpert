Brewer = Brewer || {};

Brewer.DialogoExcluir = (function() {

	function DialogoExcluir() {
		this.exclusaoBtn = $('.js-exclusao-btn');
	}

	DialogoExcluir.prototype.iniciar = function() {
		this.exclusaoBtn.on('click', onExcluirClicado.bind(this));
		if (window.location.search.indexOf('excluido') > -1) {
			Swal.fire('Pronto', 'Excluído com sucesso!', 'success');
		}
	}

	function onExcluirClicado(evento) {
		evento.preventDefault();
		var botaoClicado = $(evento.currentTarget);
		var url = botaoClicado.data('url');
		var objeto = botaoClicado.data('objeto');
		
		Swal.fire({
			title : 'Tem certeza?',
			text : 'Excluir "' + objeto + '"?',
			icon : 'warning',
			showCancelButton: true,
			width: '50%',
			confirmButtonColor: '#DD6B55',
			confirmButtonText: 'Sim, exclua agora!',
			cancelButtonText: 'Cancelar',
		}).then((resposta) => { 
			if (resposta.value)
				onExcluirConfirmado.call(this, url);
		}
		);
			
	}

	function onExcluirConfirmado(url) {
		$.ajax({
			url: url,
			method: 'delete',
			success: onExcluidoSucesso.bind(this),
			error: onErroExcluir.bind(this),
		});
	}

	function onExcluidoSucesso() {
		var urlAtual = window.location.href;
		var separador = urlAtual.indexOf('?') > -1 ? '&' : '?';
		var novaUrl = urlAtual.indexOf('excluido') > -1 ? urlAtual : urlAtual + separador + 'excluido';
		window.location = novaUrl;
	}
	
	function onErroExcluir(e) {
		//Swal.fire('Oops', e.responseText, 'error');
		Swal.fire({
			title: 'Oops!',
			html: e.responseText,
			icon: 'error',
			allowOutsideClick: false,
			showConfirmButton: false,
			timer: 3000,
			timerProgressBar: true,
			heightAuto: false,
		});
	}
	
	return DialogoExcluir;

}());

$(function() {
	var dialogo = new Brewer.DialogoExcluir();
	dialogo.iniciar();
});