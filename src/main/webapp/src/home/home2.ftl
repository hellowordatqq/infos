<#include "../header/header.ftl">
<link href="/src/home/home.css" rel="stylesheet" />
<link href="/src/home/newhome.css" rel="stylesheet" />

<#if model.banner?? && model.banner?size gt 0>
	<div class="banner-container carousel slide" id="estart-banner-carousel" data-ride="carousel">
		<div class="estart-banner">
			<ol class="carousel-indicators">
				<#list model.banner as ban>
					<li data-target="#estart-banner-carousel" data-slide-to="${ban_index}"></li>
				</#list>
			</ol>
			<ul class="banner-content carousel-inner" role="listbox">
				<#list model.banner as ban>
					<li class="item">
                        <img class="slider-img" src="/onesfile/sliders/${ban}" style="margin:auto;" />
                    </li>
				</#list>
			</ul>
			<a class="focus-arrow left-focus-arrow" href="#estart-banner-carousel" data-slide="prev"></a>
			<a class="focus-arrow right-focus-arrow" href="#estart-banner-carousel"  data-slide="next"></a>
		</div>
	</div>
</#if>

<div id="container" style="margin-top: 30px">
	<div id="content" class="clearfix">
		<div id="main" class="col620 clearfix" role="main">
			<div class="item-wrap clearfix">
				<#if model.newBlogs?size gt 0>
					<table width="100%" cellspacing="0" cellpadding="0">
						<tbody>
							<#assign closed=true />
							<#list model.newBlogs as rootEntry>
								<#if rootEntry_index % 2 == 0>
									<tr>
									<#assign closed=false />
								</#if>
								
								<td style="width: 50%; vertical-align: top;">
									<div class="TodayNews">
										<table border="0" cellpadding="0" cellspacing="0">
											<tr>
												<td>
													<img src="/src/img/main/icon/zuixinxinxi_1.jpg" height="35" width="67" border="0" />
												</td>
												<td width="105" background="/src/img/main/icon/zuixinxinxi_2.jpg">
													<a href="/blogs/root?id=${rootEntry.getKey().id}" style="font-size:18px;">${rootEntry.getKey().name}</a>
												</td>
												<td>
													<img src="/src/img/main/icon/zuixinxinxi_3.jpg" width="193" height="35" border="0" />
												</td>
											</tr>
										</table>
										<div class="clear"></div>
										<ul>
											<#list rootEntry.getValue() as childEntry>
												<#list childEntry.getValue() as latestBlog>
													<#if latestBlog.fresh>
														<li class="today">
													<#else>
														<li>
													</#if>
													
													<span class="date">${latestBlog.formatedAddTime}</span>
													<a href="/blogs/view?id=${latestBlog.id}">
														<abbr title="${latestBlog.title}">
															<#if latestBlog.title?length gt 10>
																${latestBlog.title?substring(0,8)}...
															<#else>
																${latestBlog.title}
															</#if>
															
															<#if latestBlog.fresh>
																<span class="blog_new"></span>
															</#if>
														</abbr>
													</a>
													
													</li>
												</#list>
											</#list>
										</ul>
									</div>
								</td>
								
								<#if rootEntry_index % 2 == 1>
									</tr>
									<#assign closed=true />
								</#if>								
							</#list>
							
							<#if closed == false>
								</tr>
							</#if>
						</tbody>
					</table>
				</#if>
			</div>
		</div>
		
		<div id="sidebar" class="widget-area col300" role="complementary">
			<div id="social-media" class="clearfix"></div>
			<aside id="search-3" class="widget widget_search">
				<form id="searchform" class="searchform" method="post" action="/search" role="search">
					<div class="form-group">
				        <div class="col-sm-8" style="padding-right: 0px;">
				            <input type="text" class="form-control required" id="s" name="s" placeholder="请输入关键词" style="padding-right: 0px; padding-left: 5px;"/>
				        </div>
				    </div>
					<button type="submit" class="btn btn-primary" id="submitBtn">查询</button>
				</form>
			</aside>
			<!--user infos -->
			<aside id="userinfos" class="widget widget_recent_entries">
				<h2 class="widget-title">${model.basic.userName}</h2>
				<ul>
					<!-- TODO LIST FOR CURRENT USER -->
				</ul>
			</aside>
			<!-- links -->
			<aside id="links" class="widget widget_recent_entries">
				<h2 class="widget-title">友情链接</h2>
				<ul>
					<#if model.links?? && model.links?size gt 0>
						<#list model.links as link>
							<!--for access db, data result will have a id = 0 data even links table emtpy -->
							<#if link.link??>
								<li><a target="_blank" href="${link.link}">${link.name}</a></li>
							</#if>
						</#list>
					</#if>
				</ul>
			</aside>
		</div>
	</div>
</div>
	
<#include "../footer/footer.ftl">
<script>
    require(['home/home']);
</script>
</body>
</html>
