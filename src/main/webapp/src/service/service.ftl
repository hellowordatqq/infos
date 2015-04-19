<#include "../header/header.ftl">
<link href="/src/service/service.css" rel="stylesheet" />
<#assign currentId = RequestParameters['id'] />
<div class="service-detail">
    <div class="pull-left service-nav">
        <ul class="nav bs-docs-sidenav">
            <#if model.basic.nav[model.type]??>
                <#list model.basic.nav[model.type] as service>
                    <#if service.id == currentId?number>
                        <li style="border-left: 5px solid ${service.color};background: #232837;">
                    <#else>
                        <li>
                    </#if>
                        <a href="/service/info?id=${service.id}&type=${model.type}">
                            <span class="service-icon" style="border: 2px solid ${service.color};"></span>
                            ${service.name}
                        </a>
                    </li>
                </#list>
            </#if>
        </ul>

    </div>
    
    <div class="pull-right service-main">
        <#if model.serviceIntro??>
            ${model.serviceIntro}
        </#if>
        <!-- 各项服务内容 -->
        <div id="service-title">
            <ul class="service-element" role="tablist">
                <#if model.details?? && model.details?size gt 0>
                    <#list model.details as detail>
                        <li><a class="${detail.part}" href="#${detail.part}">${detail.name}</a></li>
                    </#list>
                </#if>
            </ul>
        </div>
        <div class="service-each-element">
            <#if model.details?? && model.details?size gt 0>
                <#list model.details as detail>
                    <div class="service-one-part service-${detail_index}-part" id="${detail.part}">
                        <div class="element-part-head"></div>
                        <span class="service-ele-title">${detail.name}</span>
                        <span class="service-ele-line"></span>
                        
                        <div class="service-ele bs-docs-section">
                            ${detail.content}
                        </div>
                    </div>
                    <script language="JavaScript">

                        <#if model.basic.role?? && model.basic.role == "admin">
                            $('<a href="/service/edit?id=${currentId}&type=${model.type}&part=${detail.part}" class="service-ele-edit">编辑</a>').insertBefore('.service-${detail_index}-part .service-ele');
                        </#if>
                    </script>
                </#list>
            </#if>
            <#if model.contacts??>
                <div class="service-one-part service-interface">
                    <span class="service-ele-title">接口人</span>
                    <span class="service-ele-line"></span>
                    
                    <div class="service-ele bs-docs-section">
                        ${model.contacts}
                    </div>
                </div>
                <script language="JavaScript">

                    <#if model.basic.role?? && model.basic.role == "admin">
                        $('<a href="/service/edit?id=${currentId}&type=${model.type}&part=contacts" class="service-ele-edit">编辑</a>').insertBefore('.service-interface .service-ele');
                    </#if>
                </script>
            </#if>
        </div>
    </div>
</div>

<#include "../footer/footer.ftl">
<script>
    require(['service/service']);
</script>
</body>
</html>