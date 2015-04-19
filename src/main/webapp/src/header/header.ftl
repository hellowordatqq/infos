<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>Ones123</title>
    <link href="/src/common/css/common.css" rel="stylesheet" />
    <link href="/src/common/css/base.css" rel="stylesheet" />
    <link href="/bower_components/bootstrap/dist/css/bootstrap.css" rel="stylesheet" />
    <link href="/bower_components/jmenu/jmenu.css" rel="stylesheet" />
    <!--[if lt IE 9]>
      <script src="/src/common/js/html5.js"></script>
      <script src="/src/common/js/html5shiv.min.js"></script>
      <script src="/src/common/js/respond.min.js"></script>
      <script src="/src/common/js/ie8Indexof.js"></script>
    <![endif]-->
</head>
<body>
<div class="header">
    <div class="estart-top">
        <div class="top-bg">
            <a class="estart-logo" href="/"></a>
        </div>
    </div>
    <div class="estart-top">
        <div class="top-bg">
            <ul id="jMenu" class="estart-navlist" >
                <li class="nav-index" id="home"><a class="estart-navlist-link" href="/" data-log="{'target':'view-nav-service','id':'home'}">首页</a></li>
                <!-- category nav bars -->
                <#if model?? && model.basic??>
	                <#if model.basic.nav?size gt 0>
		                <#list model.basic.nav as rootEntry>
		                	<li id="nav_category_${rootEntry.getKey().id}">
		                		<a class="estart-navlist-link" href="/blogs/root?id=${rootEntry.getKey().id}">${rootEntry.getKey().name}</a>
		                		<#if rootEntry.getValue()?size gt 0>
		                			<ul>
		                			<#list rootEntry.getValue() as childEntry>
		                				<li><a href="/blogList?rootCategory=${rootEntry.getKey().id}&childCategory=${childEntry.id}">${childEntry.name}</a></li>
		                			</#list>
		                			</ul>
		                		</#if>
		                	</li>
		                </#list>
		            </#if>
		            <!-- end of category nav bars -->
	                <#if model.basic.roleLevel gt 0>
	                	<li class="nav-backup" id="nav_publish"><a class="estart-navlist-link" href="/publish" data-log="{'target':'view-nav-service','id':'publish'}">新增发布</a></li>
	                </#if>
	                <#if model.basic.roleLevel gt 1>
	                	<li class="nav-backup" id="nav_admin"><a class="estart-navlist-link" href="/admin" data-log="{'target':'view-nav-service','id':'admin'}">系统管理</a></li>
	                </#if>
	            </#if>
            </ul>
        </div>
    </div>
</div>