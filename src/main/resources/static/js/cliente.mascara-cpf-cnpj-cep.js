var Brewer = Brewer || {};

Brewer.MascaraCpfCnpj = (function() {

	function MascaraCpfCnpj() {
		this.radioTipoPessoa = $('.js-radio-tipo-pessoa');
		this.labelCpfCnpj = $('[for=cpfCnpj]');
		this.inputCpfCnpj = $('#cpfCnpj');
	}

	MascaraCpfCnpj.prototype.iniciar = function() {
		this.radioTipoPessoa.on('change', onTipoPessoaAlterado.bind(this));
		var tipoPessoaSelecionada = this.radioTipoPessoa.filter(':checked')[0];
		
		if (tipoPessoaSelecionada) {
			aplicarMascara.call(this, $(tipoPessoaSelecionada));
		}
	}

	function onTipoPessoaAlterado(evento) {
		var tipoPessoaSelecionada = $(evento.currentTarget);
		aplicarMascara.call(this, tipoPessoaSelecionada);
	}

	function aplicarMascara(tipoPessoaSelecionada) {
		this.inputCpfCnpj.removeAttr('disabled');
		this.labelCpfCnpj.text(tipoPessoaSelecionada.data('documento'));
		this.inputCpfCnpj.mask(tipoPessoaSelecionada.data('mascara'));
	}

	return MascaraCpfCnpj;

}());

Brewer.MascaraCEP = (function() {

	function MascaraCEP() {
		this.inputCEP = $('.js-cep');
	}

	MascaraCEP.prototype.iniciar = function() {
		this.inputCEP.mask('00.000-000');
	}

	return MascaraCEP;

}());

$(function() {
	var mascaraCpfCnpj = new Brewer.MascaraCpfCnpj();
	var mascaraCEP = new Brewer.MascaraCEP();
	mascaraCpfCnpj.iniciar();
	mascaraCEP.iniciar();
});