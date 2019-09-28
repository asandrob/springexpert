(function($) {

	$.fn.fixedHeader = function(options) {
		var config = {
			topOffset : 0
		// bgColor: 'white'
		};
		if (options) {
			$.extend(config, options);
		}

		return this
				.each(function() {
					var o = $(this);
					var $win = $(window);
					var $head = $('thead.header', o);
					var isFixed = 0;
					var headTop;

					function recalcHeadTop() {
						headTop = $head.length && $head.offset().top - config.topOffset;
					}

					function processScroll() {
						recalcHeadTop()
						if ($('thead.header-copy').length) {
							$('thead.header-copy').width($('thead.header').width());
						}
						var scrollTop = $win.scrollTop();
						if (scrollTop >= headTop && !isFixed) {
							isFixed = 1;
						} else if (scrollTop <= headTop && isFixed) {
							isFixed = 0;
						}
						
						$('thead.header-copy', o).offset({left : $head.offset().left});
						if (isFixed){
							$('thead.header-copy', o).removeClass('hide');
						} else {
							$('thead.header-copy', o).addClass('hide');
						}
					}
					
					$win.on('scroll', processScroll);
					$win.on('resize', cloneHeader);
					$win.on('resize', processScroll);

					function cloneHeader() {
						var headerCloned = o.find('thead.header-copy');
						headerCloned.remove();
						$head.clone().removeClass('header').addClass('header-copy header-fixed').appendTo(o);
						var header_width = $head.width();
						o.find('thead.header-copy').width(header_width);
						o.find('thead.header > tr:first > th').each(
								function(i, h) {
									var w = $(h).width();
									o.find('thead.header-copy> tr > th:eq('	+ i + ')').width(w);
								});
					}

					cloneHeader();

					$head.css({
						margin : '0 auto',
						width : o.width(),
						'background-color' : config.bgColor
					});
					processScroll();
				});
	};

})(jQuery);