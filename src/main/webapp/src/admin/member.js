define(['require', 'jtable'], function (require) {
	$('#memberTableContainer').jtable({
		title: '用户列表',
		paging: false,	// 无分页
		sorting: false,	// 无排序
		actions: {
			listAction: '/admin/memberList',
			deleteAction: '/admin/delMember',
			updateAction: '/admin/updateMember',
			createAction: '/admin/addMember'
		},
		fields: {
			id : {
				key: true,
				create: false,
				edit: false,
				list: false
			},
			realName: {
				title: '姓名',
				width: '20%',
				create: false,
				edit: false
			},
			userName: {
				title: '用户名',
				width: '20%',
				edit: false
			},
			uid: {
				title: '集团id',
				list: false,
				edit: false,
				create: false
			},
			roleGroup: {
				title: '用户权限',
				width: '20%',
				options: {'0' : '只读权限', '1' : '发布权限', '2' : '管理员权限'}
			},
			isDel : {
				title : '是否删除',
				list: false,
				edit: false,
				create: false
			},
			addTime: {
				title: '添加时间',
				list: false,
				edit : false,
				create: false
			},
			updateTime: {
				title: '更新时间',
				list: false,
				edit: false,
				create: false
			}
		}
	});
	
	// load first level category list from server
	$('#memberTableContainer').jtable('load');
})