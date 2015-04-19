define(['jmenu'], function () {
	function setCurrent(cls) {
		$(cls).children('a').css('color', '#2DA1DC').end()
			.siblings('li').children('a').css('color', '#D5D6D8');
	}
	function getParameterByName(name) {
	    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
	    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
	        results = regex.exec(location.search);
	    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
	}
	if (location.href === location.origin) {
		setCurrent('.nav-index');
	}
	// 根据当前页面给导航加active样式
	if (location.href.indexOf('publish') !== -1) {
		setCurrent('#nav_publish');
	}else if (location.href.indexOf('admin') !== -1) {
		setCurrent('#nav_admin');
	}else if (location.href.indexOf('/blogs/root') !== -1) {
		var categoryId = getParameterByName('id');
		setCurrent('#nav_category_' + categoryId);
	}else if (location.href.indexOf('blogList') !== -1) {
		var categoryId = getParameterByName('rootCategory');
		setCurrent('#nav_category_' + categoryId);
	}else if(location.href.indexOf('/blogs/view') !== -1) {
		setCurrent('#nav_category_' + curRootCategory);
	}
	else {
		setCurrent('.nav-index');
	}
	
	// jmenu设置
	$("#jMenu").jMenu({
        openClick : false,
        ulWidth : '8em',
         TimeBeforeOpening : 100,
        TimeBeforeClosing : 11,
        animatedText : false,
        paddingLeft: 1,
        effects : {
            effectSpeedOpen : 150,
            effectSpeedClose : 150,
            effectTypeOpen : 'slide',
            effectTypeClose : 'slide',
            effectOpen : 'swing',
            effectClose : 'swing'
        }

    });
})