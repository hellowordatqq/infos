	<div class="pull-left service-nav">
        <ul class="nav bs-docs-sidenav">
            <#list model.categorys as category>
                <#if category.id == model.childCategory>
                    <li style="border-left: 5px solid;background: #232837;">
                <#else>
                    <li>
                </#if>
                    <a href="/blogList?rootCategory=${model.rootCategory}&childCategory=${category.id}">
                        ${category.name}
                    </a>
                </li>
            </#list>
        </ul>

    </div>