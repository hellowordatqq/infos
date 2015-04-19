define(['require', 'jtable'], function (require) {
	$('#linkTableContainer').jtable({
		title: '友情链接列表',
		paging: false,	// 无分页
		sorting: false,	// 无排序
		actions: {
			listAction: '/admin/linkList',
			deleteAction: '/admin/delLink',
			updateAction: '/admin/addOrUpdateLink',
			createAction: '/admin/addOrUpdateLink'
		},
		fields: {
			id : {
				key: true,
				create: false,
				edit: false,
				list: false
			},
			name: {
				title: '名称',
				width: '20%'
			},
			link: {
				title: '链接',
				width: '40%'
			}
		}
	});
	
	// load first level category list from server
	$('#linkTableContainer').jtable('load');
})