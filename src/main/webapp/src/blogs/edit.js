define(['kindeditor', 'kindeditor-lang-CN', 'jquery-validation'], function () {
	var K = window.KindEditor;
	var editor = K.create('#service-edit',{
            uploadJson : '/editor/upload.do',
            allowFileManager : false,
            items : ['undo','redo','|','preview', 'print', 'cut', 'copy', 'paste', 'plainpaste', 'wordpaste', '|',
			         'justifyleft', 'justifycenter', 'justifyright', 'justifyfull', 'insertorderedlist', 'insertunorderedlist',
			         'indent', 'outdent', 'subscript', 'superscript', 'clearhtml', 'quickformat', 'selectall', '|',
			         'fullscreen', '/', 'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
			         'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image',
			         'flash', 'media', 'insertfile', 'table', 'hr', 'emoticons','pagebreak','anchor', 'link', 'unlink', '|', 'about'
			         ]
        });
    editor.html(content_string);
    
    $('#blog-form').validate({
    	errorClass : "warning",
    	onkeyup : false,
    	onblur : false,
    	submitHandler : function() {
    		editor.sync();
    		$.ajax({
    			cache: false,
    			type: "POST",
    			url: "/publish/save",
    			data: $('#blog-form').serialize(),
    			async: false,
    			error: function(request) {
    				alert("提交失败！");
    			},
    			success: function(data) {
    				var obj = eval(data);
    				if(obj.Result != "OK"){
    					alert(obj.Message);
    				}else{
    					window.location.href = obj.Redirect;
    				}
    			}
    		});
    	}
    });
})