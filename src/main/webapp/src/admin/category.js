define(['require', 'jtable'], function (require) {
	var $table = $('#categoryTableContainer');
	
	$table.jtable({
		title: '一级分类列表',
		paging: false,	// 无分页
		sorting: false,	// 无排序
		actions: {
			listAction: '/admin/rootCategoryList',
			deleteAction: '/admin/delCategory',
			updateAction: '/admin/updateCategory',
			createAction: '/admin/addCategory'
		},
		fields: {
			id : {
				key: true,
				create: false,
				edit: false,
				list: false
			},
			// child table
			Child: {
				title: '',
				width: '5%',
				sorting: false,
				edit: false,
				create: false,
				display: function(categoryData) {
					// create an image that will be used to open child table
					var $img = $('<img src="/src/admin/icon/note.png" title="子分类编辑" />');
					// Open child table when user clicks the image
					$img.click(function(){
						$table.jtable('openChildTable', $img.closest('tr'), {
									title: categoryData.record.name + ' - 子分类',
									actions: {
										listAction: '/admin/childCategoryList?parentCategory=' + categoryData.record.id,
										deleteAction: '/admin/delCategory',
										updateAction: '/admin/updateCategory',
										createAction: '/admin/addCategory'
									},
									fields: {
										parentCategory: {
											type: 'hidden',
											defaultValue: categoryData.record.id
										},
										id: {
											key: true,
											create: false,
											edit: false,
											list: false
										},
										name: {
											title: '分类名称',
											width: '20%'
										},
										level: {
											title: 'level',
											list: false,
											type: 'hidden',
											defaultValue : '2',
											edit: false
										}
									}
								}, function(data) {
									// opened handler
									data.childTable.jtable('load');
								});
					});
					// return image to show on the first level category table row
					return $img;
				}
			},
			name: {
				title: '分类名称',
				width: '20%'
			},
			level: {
				title: 'level',
				list: false,
				type: 'hidden',
				edit: false,
				defaultValue : '1'
			},
			parentCategory: {
				title: '父分类',
				list: false,
				edit: false,
				type: 'hidden',
				defaultValue: '0'
			}
		}
	});
	
	// load first level category list from server
	$('#categoryTableContainer').jtable('load');
	
})