/**
 * @file 工具与资源滚动插件
 */ 
define(['jquery'], function(){
　　$.fn.scrollPic = function(options){
        var $this = $(this),_this = this,isover = true,tt;
        var opts = $.extend({
            leftbtn:".left",
            rightbtn:".right",
            autotime : 3000, //自动播放毫秒
            runtime : 300, //运动开始带结束时需时间
            distance : 226, //每次移动距离
            picsize : 4,
            auto: false
        },options); 
        if($this.length==0) return false;
        var _objul = $this.find("ul"),
            _size = _objul.find("li").size();
        var initEvent = function(){
            _objul.width(opts.distance*_size + 35);
            if(_size > opts.picsize){
                $this.find(opts.leftbtn).bind("click",rightMoveEvent);
                $this.find(opts.rightbtn).bind("click",leftMoveEvent);
                if(opts.auto){
                    autoMoveEvent();
                    $this.hover(stopMoveEvent,autoMoveEvent);
                }
            }else{
                $this.find(opts.leftbtn).parent().addClass("lf-end rg-end");
            }
        };
        var leftMoveEvent = function(){
            if(isover){
                isover = false;
                var objfirst = $this.find("li:first"),
                      objlast = $this.find("li:last");
                objfirst.animate({'margin-left': '-'+opts.distance+'px'},opts.runtime,function(){
                  $(this).insertAfter(objlast).removeAttr("style");
                });
                window.setTimeout(function(){isover=true},opts.runtime);
            }
        }
        var rightMoveEvent = function(){
            if(isover){
                 isover = false;
                var objfirst = $this.find("li:first"),
                      objlast = $this.find("li:last");
                objfirst.before(objlast.css({'margin-left': '-'+opts.distance+'px'}));
                objlast.animate({"margin-left":0},opts.runtime,function(){
                  $(this).removeAttr("style");
                });
                window.setTimeout(function(){isover=true},opts.runtime);
            }
        };
        var autoMoveEvent = function(){
            tt= window.setInterval(leftMoveEvent,opts.autotime);
        };
        var stopMoveEvent = function(){
            if(tt) window.clearInterval(tt);
        };
        initEvent();
    };
})
