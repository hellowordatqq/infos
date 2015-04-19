<#include "../header/header.ftl">
<link href="/src/blogs/blog.css" rel="stylesheet" />
<link href="/bower_components/font-awesome/font-awesome.min.css" rel="stylesheet" />
<div class="container">
	<!-- breadcrumb -->
	<div class="blog-breadcrumb">
		<#if model.breadcrumb??>
			<ol class="breadcrumb">
				<#list model.breadcrumb as item>
					<li><a href="${item.link}">${item.name}</a></li>
				</#list>
			</ol>
		</#if>
	</div>
	
	<div class="row">
		<div class="col-lg-10">
			<!--blog title-->
			<h1>${model.title}</h1>
			<p class="lead"><i class="fa fa-user"></i> ${model.author} 发布于 ${model.addTime} </p>
			<#if model.basic.userName == model.author || model.basic.roleLevel gt 1>
				<#assign currentBlogId = RequestParameters['id'] />
				<p><a href="/publish/edit?id=${currentBlogId}">编辑</a> <span> </span> <a href="/publish/delete?id=${currentBlogId}">删除</a></p>
			</#if>
			<hr>
			
			<!--blog content-->
			<div>
				${model.content}
			</div>
		</div>
	</div>
</div>
<script>
	var curRootCategory = ${model.rootCategory};
</script>
<#include "../footer/footer.ftl">
</body>
</html>