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

$(function() {
	var maskMoney = new Brewer.MaskMoney();
	maskMoney.ativar();
	$('.js-tooltip').tooltip();
	var maskPhoneNumber = new Brewer.MaskPhoneNumber();
	maskPhoneNumber.ativar();
});