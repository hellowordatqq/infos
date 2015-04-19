define(['slider'], function () {
    // 各服务项hover效果
	$('.service-content li').mouseover(function () {
        $(this).removeClass('service-content-unhover');
        $(this).addClass('service-content-hover');
    }).mouseout(function () {
        $(this).removeClass('service-content-hover');
        $(this).addClass('service-content-unhover');
    });

    // banner鼠标悬浮停留时出现左右点击按钮
    $(".estart-banner").on({
        mouseenter: function () {
            $(".left-focus-arrow").stop().animate({ left: 0 }, "linear");
            $(".right-focus-arrow").stop().animate({ right: 0 }, "linear");
        },
        mouseleave: function () {
            $(".left-focus-arrow").stop().animate({ left: "-35px" }, "linear");
            $(".right-focus-arrow").stop().animate({ right: "-35px" }, "linear");
        }
    });
    // 工具列表左右点击slider
    if (typeof($("#tool-scroll").scrollPic) !== 'undefined') {
        $("#tool-scroll").scrollPic({
            leftbtn : "em.lf",
            rightbtn : "em.rg",
            distance:238,
            runtime:300,
            auto:false,
            picsize:5
        });
    }
    // 基础服务列表左右点击slider
    if (typeof($("#basic-scroll").scrollPic) !== 'undefined') {
        $("#basic-scroll").scrollPic({
            leftbtn : "em.lf",
            rightbtn : "em.rg",
            distance:238,
            runtime:300,
            auto:false,
            picsize:5
        });
    }
    // banner 显示第一幅图
    $('.banner-content li').first().addClass('active');
    $('.carousel-indicators li').first().addClass('active');
})