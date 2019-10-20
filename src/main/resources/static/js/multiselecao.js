Brewer = Brewer || {};

Brewer.Multiselecao = (function(){
	
	function Multiselecao() {
		this.acoesBtn = $('#acoesBtn');
		this.statusAnchor = $('.js-status-a');
		this.selecaoCheckBox = $('.js-selecao');
		this.selecaoTodosCheckBox = $('.js-selecao-todos');
		
	}
	
	Multiselecao.prototype.iniciar = function()	{
		this.statusAnchor.on('click', onStatusAnchorClicado.bind(this));
		this.selecaoTodosCheckBox.on('ifToggled', onSelecaoTodosCheckBoxClicado.bind(this));
		this.selecaoCheckBox.on('ifToggled', onSelecaoCheckBoxClicado.bind(this));
	}
	
	function onStatusAnchorClicado(event) {
		var anchorClicado = $(event.currentTarget);
		var url = anchorClicado.data('url');
		var status = anchorClicado.data('status');
		var disabled = anchorClicado.hasClass('disabled');
		var checkBoxSelecionados = this.selecaoCheckBox.filter(':checked');
		var codigos = $.map(checkBoxSelecionados, function(codigo){
			return $(codigo).data('codigo');
		})
		if (codigos.length > 0 && !disabled) {
			$.ajax({
				url: url,
				method: 'put',
				data: {
					codigos: codigos,
					status: status
				},
				success: function() {
					window.location.reload();
				}
			});
		}
	}
	
	function onSelecaoTodosCheckBoxClicado(event) {
		var status = event.currentTarget.checked;
		this.selecaoCheckBox.prop('checked', status);
		this.selecaoCheckBox.iCheck('update');
		statusAcoesBtn.call(this, status);
	}
	
	function onSelecaoCheckBoxClicado() {
		var selecaoCheckBoxMarcados = this.selecaoCheckBox.filter(':checked');
		var todosMarcados = selecaoCheckBoxMarcados.length == this.selecaoCheckBox.length;
		this.selecaoTodosCheckBox.prop('checked', todosMarcados);
		this.selecaoTodosCheckBox.iCheck('update');
		statusAcoesBtn.call(this, selecaoCheckBoxMarcados.length);
	}
	
	function statusAcoesBtn(ativar) {
		ativar ? this.acoesBtn.removeClass('disabled') : this.acoesBtn.addClass('disabled'); 
	}
	
	return Multiselecao;
	
}());

$(function(){
	var multiSelecao = new Brewer.Multiselecao();
	multiSelecao.iniciar();
});