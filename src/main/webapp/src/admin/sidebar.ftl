	<!-- admin console sidebar -->
	<#assign sidebars = [{"name" : "分类管理", "link" : "/admin/category"}, {"name" : "权限管理", "link" : "/admin/member"}, {"name" : "轮播图设置", "link" : "/admin/slider"}, {"name" : "友情链接设置", "link" : "/admin/link"}]>
	<div class="pull-left service-nav">
        <ul class="nav bs-docs-sidenav">
            <#list sidebars as service>
                <#if service.link == model.basic.tag>
                    <li style="border-left: 5px solid;background: #232837;">
                <#else>
                    <li>
                </#if>
                    <a href="${service.link}">
                        ${service.name}
                    </a>
                </li>
            </#list>
        </ul>

    </div>