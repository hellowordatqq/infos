<#include "../header/header.ftl">
<link href="/src/home/home.css" rel="stylesheet" />
<link href="/src/home/default.css" rel="stylesheet" />
<link href="/src/home/newhome.css" rel="stylesheet" />
<link href="/src/blogs/blog.css" rel="stylesheet" />
<link href="/bower_components/font-awesome/font-awesome.min.css" rel="stylesheet" />

<div id="container" style="margin-top: 30px">
	<div id="content" class="clearfix">
		<div id="main" class="col620 clearfix" role="main">
			<div class="item-wrap clearfix">
				<#if model.blogs?size gt 0>
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
				<#else>
					<div class="row">
						<div class="col-lg-10">
							没有符合条件的结果
						</div>
					</div>
				</#if>
			</div>
			
			<div id="page-selection"></div>
		</div>
		
		<div id="sidebar" class="widget-area col300" role="complementary">
			<div id="social-media" class="clearfix"></div>
			<aside id="search-3" class="widget widget_search">
				<form id="searchform" class="searchform" method="post" action="/search" role="search">
					<div class="form-group">
				        <div class="col-sm-8" style="padding-right: 0px;">
				            <input type="text" class="form-control required" id="s" name="s" value="${model.s}" placeholder="请输入关键词" style="padding-right: 0px; padding-left: 5px;"/>
				        </div>
				    </div>
					<button type="submit" class="btn btn-primary" id="submitBtn">查询</button>
				</form>
			</aside>
			<!--user infos -->
			<aside id="userinfos" class="widget widget_recent_entries">
				<h2 class="widget-title">${model.basic.userName}</h2>
			</aside>
		</div>
	</div>
	
</div>
	
<#include "../footer/footer.ftl">
<script>
	var pageNum = ${model.pageNum?number};
	var totalPage = ${model.totalPage?number};
	if(pageNum > totalPage) {
		pageNum = totalPage;
	}
	require(['home/search']);
	
</script>
</body>
</html>
