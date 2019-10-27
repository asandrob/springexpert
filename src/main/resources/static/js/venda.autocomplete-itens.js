Brewer = Brewer || {};

Brewer.Autocomplete = (function() {

	function Autocomplete() {
		this.skuOuNomeInput = $('.js-sku-nome-cerveja-input');
		this.htmlTemplateAutocomplete = $('#template-autocomplete-cerveja').html();
		this.template = Handlebars.compile(this.htmlTemplateAutocomplete);
		this.emitter = $({});
		this.on = this.emitter.on.bind(this.emitter);
	}

	Autocomplete.prototype.iniciar = function() {
		var options = {
			url : function(skuOuNome) {
				return this.skuOuNomeInput.data('url') + '?skuOuNome=' + skuOuNome;
			}.bind(this),
			getValue : 'nome',
			minCharNumber : 3,
			requestDelay : 300,
			adjustWidth: false,
			ajaxSettings : {
				contentType : 'application/json'
			},
			template : {
				type : 'custom',
				method : template.bind(this),
			},
			list : {
				onChooseEvent : onItemSelecionado.bind(this)
			}
		};

		function template(nome, cerveja) {
			cerveja.valorFormatado = Brewer.formatarMoeda(cerveja.valor);
			return this.template(cerveja);
		}

		function onItemSelecionado() {
			this.emitter.trigger('item-selecionado', this.skuOuNomeInput.getSelectedItemData());
			this.skuOuNomeInput.val('');
			this.skuOuNomeInput.focus();
		}

		this.skuOuNomeInput.easyAutocomplete(options);
	}

	return Autocomplete;

}());
