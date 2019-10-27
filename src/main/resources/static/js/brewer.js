var Brewer = Brewer || {};

Brewer.MaskMoney = (function(){
	
	function MaskMoney(){
		this.decimal = $('.js-decimal');
		this.plain = $('.js-plain');
	}
	
	MaskMoney.prototype.ativar = function(){
		this.decimal.maskMoney({ decimal: ',', thousands: '.', allowZero: true });
		this.plain.maskMoney({ precision: 0, thousands: '.', allowZero: true });
	}
	
	return MaskMoney;
	
}());

Brewer.MaskPhoneNumber = (function(){
	
	function MaskPhoneNumber(){
		this.inputPhoneNumber = $('.js-phone-number');
	}
	
	MaskPhoneNumber.prototype.ativar = function(){
		var maskBehavior = function(val) {
			return val.replace(/\D/g, '').length === 11 ? '(00) 00000-0000'	: '(00) 0000-00009';
		};
		var options = {
			onKeyPress : function(val, e, field, options) {
				field.mask(maskBehavior.apply({}, arguments), options);
			}
		};
		this.inputPhoneNumber.mask(maskBehavior, options);
	}
	
	return MaskPhoneNumber;
	
}());

Brewer.MaskDate = (function() {

	function MaskDate() {
		this.inputDate = $('.js-date');
	}

	MaskDate.prototype.ativar = function() {
		this.inputDate.mask('00/00/0000');
		this.inputDate.datepicker({
			orientation: 'botton',
			language: 'pt-BR',
			todayHighlight: true,
			autoclose: true,
		});
	}

	return MaskDate;

}());

Brewer.Icheck = (function() {

	function Icheck() {
		this.checkBox = $(':checkbox');
	}

	Icheck.prototype.ativar = function() {
		var options = {
			checkboxClass: 'icheckbox_flat-blue',
			radioClass   : 'iradio_flat-blue'
		};
		this.checkBox.iCheck(options);
	}

	return Icheck;

}());

Brewer.Security = (function() {
	
	function Security() {
		this.token = $('input[name=_csrf]').val();
		this.header = $('input[name=_csrf_header]').val();
	}
	
	Security.prototype.ativar = function() {
		$(document).ajaxSend(function(event, jqxhr, settings) {
			jqxhr.setRequestHeader(this.header, this.token);
		}.bind(this));
	}
	
	return Security;
	
}());

numeral.language('pt-br');

Brewer.formatarMoeda = function(valor) {
	return numeral(valor).format('0,0.00');
}

Brewer.recuperarValor = function(valorFormatado) {
	return numeral().unformat(valorFormatado);
}

$(function() {
	$('.js-tooltip').tooltip();
	var maskMoney = new Brewer.MaskMoney();
	maskMoney.ativar();
	var maskPhoneNumber = new Brewer.MaskPhoneNumber();
	maskPhoneNumber.ativar();
	var maskDate = new Brewer.MaskDate();
	maskDate.ativar();
	var checkBox = new Brewer.Icheck();
	checkBox.ativar();
	var security = new Brewer.Security();
	security.ativar();
});