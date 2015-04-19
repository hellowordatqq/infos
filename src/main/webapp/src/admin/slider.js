define(['require', 'plupload', 'jtable'], function (require) {
	var fileName = "";
	$('#sliderTableContainer').jtable({
		title: '轮播图列表',
		paging: false,	// 无分页
		sorting: false,	// 无排序
		actions: {
			listAction: '/admin/sliderList',
			deleteAction: '/admin/delSlider',
			updateAction: '/admin/addOrUpdateSlider',
			createAction: '/admin/addOrUpdateSlider'
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
				width: '20%'
			},
			originName: {
				title: '文件名',
				width: '20%',
				input: function(data) {
					return '<div id="filelist"></div><div id="container"><a id="originfile" href="javascript:;">选择文件</a><a id="uploadfile" href="javascript:;">上传文件</a></div><input type="hidden" name="originName" id="originName" /><br/><div id="console"></div>';
				}
			},
			destName: {
				title: '目标文件',
				type: 'hidden',
				list: false
			}
		},
		formCreated: function(event, data){
			uploader = new plupload.Uploader({
				runtimes: 'html5,flash,html4',
				browse_button: 'originfile',
				container: document.getElementById('container'),
				url: '/admin/slider/imageUpload',
				flash_swf_url: '/bower_components/plupload/js/Moxie.swf',
				multi_selection: false,
				
				filters : {
					max_file_size : '10mb',
					mime_types: [
						{title : "Image files", extensions : "jpg,gif,png"}
					]
				},
				
				init: {
					PostInit: function() {
						document.getElementById('filelist').innerHTML = '';

						document.getElementById('uploadfile').onclick = function() {
							uploader.start();
							return false;
						};
					},

					FilesAdded: function(up, files) {
						var MAX_UPLOAD_FILES = 1;
						plupload.each(files, function(file) {
							if(up.files.length > MAX_UPLOAD_FILES) {
								alert("You are allowed to add only " + MAX_UPLOAD_FILES + " files.");
								up.removeFile(file);
								return;
							}
							document.getElementById('filelist').innerHTML += '<div id="' + file.id + '">' + file.name + ' (' + plupload.formatSize(file.size) + ') <b></b></div>';
						});
					},

					UploadProgress: function(up, file) {
						document.getElementById(file.id).getElementsByTagName('b')[0].innerHTML = '<span>' + file.percent + "%</span>";
					},

					Error: function(up, err) {
						document.getElementById('console').innerHTML += "\nError #" + err.code + ": " + err.message;
					},
					
					FileUploaded: function(up, file, res){
						$('#originName').val(file.name);
						var res1 = res.response.replace('"{', '{').replace('}"', '}');
						var obj = JSON.parse(res1);
						$('#Edit-destName').val(obj.id);
					}
				}
			});
			uploader.init();
		}
	});
	
	// load first level category list from server
	$('#sliderTableContainer').jtable('load');
})