<#include "../header/header.ftl">
<link href="/src/service/service.css" rel="stylesheet" />
<link href="/src/blogs/blog.css" rel="stylesheet" />
<link href="/bower_components/font-awesome/font-awesome.min.css" rel="stylesheet" />

<div class="service-detail">
	<#include "sidebar.ftl">
	    
    <div class="pull-right service-main">
    	<div id="blogsContainer"style="text-align:left;">
    		<#list model.blogs as blog>
	    		<div class="row">
					<div class="col-lg-10">
						<!--blog title-->
						<h3><a href="/blogs/view?id=${blog.id}">${blog.title}</a></h3>
						<p class="lead"><i class="fa fa-user"></i> ${blog.author} 发布于 ${blog.addTime} </p>
						<hr>
					</div>
				</div>
			</#list>
    	</div>
    	<div id="page-selection"></div>
    </div>
</div>

<#include "../footer/footer.ftl">
</body>
<script>
	var pageNum = ${model.pageNum?number};
	var totalPage = ${model.totalPage?number};
	var rootCategory = ${RequestParameters.rootCategory};
	var childCategory = ${RequestParameters.childCategory};
	if(pageNum > totalPage) {
		pageNum = totalPage;
	}
	require(['blogs/list']);
	
</script>
</html>