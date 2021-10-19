; (function ($) {

	// �����̵� �� �÷�����
	// currentPosition: 0,		������ġ, ������ġ
	// slideWidth: 260,		�����̵� ���� ������
	// slideHeight: 160,		�����̵� ���� ������
	// slideId: 'slide',			���� ������ ���̵�
	// leftBtnId: 'left',			���� ��ư ���̵�
	// rigthBtnId: 'right'		������ ��ư ���̵�
	// ��� ��
	// $('#slidesContainer').slideShow({currentPosition:0, slideWidth: 260});
	//				==> {currentPosition:0, slideWidth: 260} ���� ���� ��� �⺻ ��

	$.fn.slideShow = function(options) {
	  
	  var opts = jQuery.extend({}, jQuery.fn.slideShow.defaults, options);
		 
	  return this.each(function() {		
	
		var slides = $('.'+opts.slideId);
		var numberOfSlides = slides.length;
		var $leftBtn = $('#'+opts.leftBtnId);
		var $rigthBtn = $('#'+opts.rigthBtnId);
		
		slides.wrapAll('<div id="'+opts.slideShowId+'"></div>').css({'width' : opts.slideWidth, 'height': opts.slideHeight });

		var $sliderInner = $('#'+opts.slideShowId);
		$sliderInner.css('width', opts.slideWidth * numberOfSlides);
		
		if( opts.slideWay == 'left' )
		{
			slides.css({'float':'left'});
			$sliderInner.animate({
				'marginLeft' : opts.slideWidth*(-opts.currentPosition)				
			});
		}
		else
		{
			$sliderInner.animate({				
				'marginTop' : opts.slideHeight*(-opts.currentPosition)
			});
		}

		$leftBtn
			.hide()
			.click(function(){
				Slide('pre');
			});

		$rigthBtn.click(function(){
			Slide('next');
		});

		function Slide(preNext) 
		{
			if( preNext == 'pre' )
			{
				opts.currentPosition = opts.currentPosition-1;
				$rigthBtn.show();

				if( opts.currentPosition <= 0 )
				{
					opts.currentPosition = 0;
					$leftBtn.hide();
				}
			}
			else
			{
				opts.currentPosition = opts.currentPosition+1;
				$leftBtn.show();

				if( opts.currentPosition >= numberOfSlides -1 )
				{
					opts.currentPosition = numberOfSlides -1;
					$rigthBtn.hide();
				}
			}

			if( opts.slideWay == 'left' )
			{
				$sliderInner.animate({
					'marginLeft' : opts.slideWidth*(-opts.currentPosition)				
				});
			}
			else
			{
				$sliderInner.animate({				
					'marginTop' : opts.slideHeight*(-opts.currentPosition)
				});
			}
		}
	  });
	};

	// slideShow �÷������� �⺻ �ɼǵ��̴�.
	jQuery.fn.slideShow.defaults = {
		currentPosition: 0,	
		slideWidth: 260,
		slideHeight: 160,
		slideId: 'slide',
		leftBtnId: 'left',
		rigthBtnId: 'right',
		slideShowId: 'slideInner',
		slideWay: 'left'
	}; 
}) (jQuery);
