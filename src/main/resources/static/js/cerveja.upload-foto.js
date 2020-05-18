var Brewer = Brewer || {};

Brewer.UploadFoto = (function(){
	
	function UploadFoto(){
		this.inputNomeFoto = $('input[name=foto]');
		this.inputContentType = $('input[name=contentType]');
		this.novaFoto = $('input[name=novaFoto]');
		this.htmlFotoTemplate = $('#js-foto-cerveja').html();
		this.template = Handlebars.compile(this.htmlFotoTemplate);
		this.containerFoto = $('.js-container-foto'); 
		this.uploadDrop = $('#upload-drop');
	}
	
	UploadFoto.prototype.iniciar = function(){
		var settings = {
				type: 'json',
				filelimit: 1,
				allow: '*.(jpg|jpeg|png)',
				action: this.containerFoto.data('url-foto'),
				notallowed: 'Tipo de arquivo não permitido',
				complete: onUploadCompleto.bind(this),
				beforeSend: adicionarCsrfToken,
		}
		UIkit.uploadSelect($("#upload-select"), settings);
		UIkit.uploadDrop(this.uploadDrop, settings);
		this.uploadDrop.pastableTextarea().on('pasteImage', onColarImagem.bind(this));
		if (this.inputNomeFoto.val()) {
			renderizarFoto.call(this, {foto: this.inputNomeFoto.val(), contentType: this.inputContentType.val()})
		}
	}
	
	function onColarImagem(event, imagem){
		this.novaFoto.val('true');
		$.ajax({
			url: this.containerFoto.data('url-foto'),
			method: 'post',
			contentType: 'application/json',
			data: JSON.stringify({ dataUrl: imagem.dataURL, foto: '', contentType: '' }),
		    complete: (function(response) {
		    	renderizarFoto.call(this, {foto: response.responseJSON.foto, contentType: response.responseJSON.contentType})}).bind(this),
 		});
	}
	
	function onUploadCompleto(resposta) {
		this.novaFoto.val('true');
		renderizarFoto.call(this, resposta)
	}
	
	function renderizarFoto(resposta) {
		this.inputNomeFoto.val(resposta.foto);
		this.inputContentType.val(resposta.contentType);
		this.uploadDrop.addClass('hidden');
		var foto = '';
		if (this.novaFoto.val() == 'true') {
			foto = 'temp/';
		}
		foto += resposta.foto;
		var htmlFoto = this.template({foto: foto});
		this.containerFoto.append(htmlFoto);
		$('.js-remove-foto').on('click', onRemoverFoto.bind(this));
	}
	
	function onRemoverFoto(){
		$('.js-foto').remove();
		this.uploadDrop.removeClass('hidden');
		this.inputNomeFoto.val('');
		this.inputContentType.val('');
		this.novaFoto.val('false');
	}
	
	function adicionarCsrfToken(xhr) {
		var token = $('input[name=_csrf]').val();
		var header = $('input[name=_csrf_header]').val();
		xhr.setRequestHeader(header, token);
	}
	
	return UploadFoto;
})();

$(function(){
	var uploadFoto = new Brewer.UploadFoto();
	uploadFoto.iniciar();
});