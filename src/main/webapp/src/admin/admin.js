define(function (require) {
	$('#categoryTableContainer').jtable({
		title: '一级分类列表',
		paging: false,	// 无分页
		sorting: false,	// 无排序
		actions: {
			listAction: '/admin/rootCategoryList',
			deleteAction: '/admin/delRootCategory',
			updateAction: '/admin/updateRootCategory',
			createAction: '/admin/addRootCategory'
		},
		fields: {
			id : {
				key: true,
				create: false,
				edit: false,
				list: false
			},
			name: {
				title: '分类名称',
				width: '20%'
			},
			icon: {
				title: 'icon',
				width: '20%'
			},
			level: {
				title: 'level',
				list: false,
				type: 'hidden',
				defaultValue : '1',
				eidt: false
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
						// clear last child category table if needs.
						$('#childCategoryTableContainer').empty();
						
						// load a jtable
						$('#childCategoryTableContainer').jtable({
							title: '二级分类列表',
							paging: false,	// 无分页
							sorting: false,	// 无排序
							actions: {
								listAction: '/admin/childCategoryList?parentCategory=' + categoryData.record.id,
								deleteAction: '/admin/delChildCategory',
								updateAction: '/admin/updateChildCategory',
								createAction: '/admin/addChildCategory'
							},
							fields: {
								id : {
									key: true,
									create: false,
									edit: false,
									list: false
								},
								name: {
									title: '分类名称',
									width: '20%'
								},
								icon: {
									title: 'icon',
									width: '20%'
								},
								level: {
									title: 'level',
									list: false,
									type: 'hidden',
									defaultValue : '2',
									eidt: false
								},
								parentCategory: {
									title: '父分类',
									list: false,
									edit: false,
									type: 'hidden',
									defaultValue: categoryData.record.id
								},
								addTime: {
									title: '添加时间',
									list: false,
									edit: false,
									create: false
								},
								updateTime: {
									title: '更新时间',
									list : false,
									edit : false,
									create: false
								},
								updateuser: {
									title: '更新人',
									list: false,
									edit: false,
									create: false
								}
							}
						});
						
						// load first level category list from server
						$('#childCategoryTableContainer').jtable('load');
					});
					// return image to show on the first level category table row
					return $img;
				}
			},
			parentCategory: {
				title: '父分类',
				list: false,
				edit: false,
				type: 'hidden',
				defaultValue: '0'
			},
			addTime: {
				title: '添加时间',
				list: false,
				edit: false,
				create: false
			},
			updateTime: {
				title: '更新时间',
				list : false,
				edit : false,
				create: false
			},
			updateuser: {
				title: '更新人',
				list: false,
				edit: false,
				create: false
			}
		}
	});
	
	// load first level category list from server
	$('#categoryTableContainer').jtable('load');
	
})