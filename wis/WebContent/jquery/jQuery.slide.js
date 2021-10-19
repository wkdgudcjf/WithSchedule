; (function ($) {

	// 슬라이드 쇼 플러그인
	// currentPosition: 0,		현재위치, 시작위치
	// slideWidth: 260,		슬라이드 가로 사이즈
	// slideHeight: 160,		슬라이드 세로 사이즈
	// slideId: 'slide',			내부 아이템 아이디
	// leftBtnId: 'left',			왼쪽 버튼 아이디
	// rigthBtnId: 'right'		오른쪽 버튼 아이디
	// 사용 법
	// $('#slidesContainer').slideShow({currentPosition:0, slideWidth: 260});
	//				==> {currentPosition:0, slideWidth: 260} 넣지 않을 경우 기본 값

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

	// slideShow 플러그인의 기본 옵션들이다.
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
