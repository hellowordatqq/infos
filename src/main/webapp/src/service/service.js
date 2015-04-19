define(function (require) {
	var flag;
	var element = $('.service-each-element .service-one-part').first();

	// 当前访问的链接中指定定位某tab项
	function getCurrentUrlTab() {
		var urlArr = location.href.split('#');
		for (var i = 1; i < urlArr.length; i++) {
			if ($('#'+ urlArr[i]).length > 0) {
				return urlArr[i];
			}
		}
        $('.service-element li').first().addClass('service-element-active');

		return null;
	}
	var urlTab = getCurrentUrlTab();
	if (urlTab) {
        $('.' + urlTab).addClass('service-element-active');
        $('.' + urlTab).siblings().removeClass('service-element-active');
        $('.' + urlTab).click();
	}
	// 选中右侧某项tab服务项
	$('.service-element li').on('click', function () {
		$(this).addClass('service-element-active');
		$(this).siblings().removeClass('service-element-active');
		var currentTab = $(this).children('a').attr('href');
		tabPostionOption($(currentTab));
        $(window).trigger("scroll");
	});

	// 最右侧tab宽度
	var totalWidth = $('.service-element').width();
	var liCount = $(".service-element li").length;
	var liWidth = (parseInt(totalWidth) - 3) / liCount;
	$(".service-element li").css('width', liWidth+'px');
	var newliWidth = $(".service-element li").width();
	var last = totalWidth - newliWidth * liCount;
	var lastWidth = newliWidth + last - 2;
	$(".service-element li").last().css('width', lastWidth+'px');

	// 下滑超过一定程度时tab标签选项始终悬浮在顶端
	$(window).on('scroll', function () {
		if ($(window).scrollTop() >= $("#service-title").offset().top) {
			if (!flag) {
				$(".service-element").css({ position: "fixed", top: 0, zIndex: 1001 });
				element.children('.element-part-head').css('display', 'block');
				flag = true;
			}
		} else {
			if (flag) {
				$(".service-element").css({ position: "static" });
				element.children('.element-part-head').css('display', 'none');
				flag = false;
			}
		}
	});
	// 点击tab时定位的内容上部被fix定位的tab内容盖住，此函数用于控制定位
	function tabPostionOption(o) {
		o.find('.element-part-head').css('display', 'block');
		o.siblings('.service-one-part').find('.element-part-head').css('display', 'none');
	}

})