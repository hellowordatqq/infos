<#include "../header/header.ftl">
<link href="/src/service/service.css" rel="stylesheet" />

<link href="/bower_components/jquery-ui/jquery-ui.min.css" rel="stylesheet" type="text/css" />
<link href="/bower_components/jtable/themes/lightcolor/blue/jtable.css" rel="stylesheet" type="text/css" />
<link href="/bower_components/uploadify/uploadify.css" rel="stylesheet" type="text/css" />

<script src="/bower_components/jquery-ui/jquery-ui.min.js" type="text/javascript"></script>
<script src="/bower_components/jtable/jquery.jtable.js" type="text/javascript"></script>
<script src="/bower_components/uploadify/jquery.uploadify.min.js" type="text/javascript"></script>

<div class="service-detail">
	<#include "sidebar.ftl">
	    
    <div class="pull-right service-main">
    	<div id="sliderTableContainer"></div>
    </div>
</div>

<#include "../footer/footer.ftl">
<script type="text/javascript">
	var fileName = "";
    $(document).ready(function(){
		$('#sliderTableContainer').jtable({
			title: '轮播图列表',
			paging: false,	// 无分页
			sorting: false,	// 无排序
			actions: {
				listAction: '/admin/sliderList',
				deleteAction: '/admin/delSlider',
				updateAction: '/admin/updateSlider',
				createAction: '/admin/addSlider'
			},
			fields: {
				id : {
					key: true,
					create: false,
					edit: false,
					list: false
				},
				idx: {
					title: '序号',
					width: '20%',
					create: false
				},
				originName: {
					title: '文件名',
					width: '20%',
					edit: false,
					input: function(data) {
						return '<div id="fileUpload" name="originName"></div>';
					}
				},
				destName: {
					title: '目标文件',
					list: false,
					edit: false,
					create: false
				}
			},
			formCreated: function(event, data){
				fileName = "";
				$('#fileUpload').uploadify({
					height: 12,
					swf: '/bower_components/uploadify/uploadify.swf',
					uploader: '/admin/addSlider',
					removeCompleted: false,
					'onUploadComplete' : function(file) {
						fileName = file.name;
					},
					width: 120
				});
			},
			formSubmitting: function(event, data) {
				$('#fileUpload').html('<input type="text" id="fileUpload" name="originName" value="' + fileName + '">');
				return data.form.validationEngine('validate');
			}
		});
		
		// load first level category list from server
		$('#sliderTableContainer').jtable('load');
    });
</script>
</body>
</html>