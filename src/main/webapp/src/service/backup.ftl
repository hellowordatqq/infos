<#include "../header/header.ftl">
<link href="/src/service/service.css" rel="stylesheet" />
<div class="service-detail">
    <div class="container backup-main">
        <!-- 各项服务内容 -->
        <div id="service-title">
            <ul class="service-element" role="tablist">
                <#if model.details?? && model.details?size gt 0>
                    <#list model.details as detail>
                        <li class="${detail.name}"><a href="#${detail.name}">${detail.name}</a></li>
                    </#list>
                </#if>
            </ul>
        </div>
        <div class="service-each-element">
            <#if model.details?? && model.details?size gt 0>
                <#list model.details as detail>
                    <div class="service-one-part service-${detail_index}-part" id="${detail.name}">
                        <div class="element-part-head"></div>
                        <span class="service-ele-title">${detail.name}</span>
                        <span class="backup-ele-line service-ele-line"></span>
                        
                        <div class="service-ele bs-docs-section">
                            ${detail.content}
                        </div>
                    </div>
                    <script language="JavaScript">
                        <#if model.basic.role?? && model.basic.role == "admin">
                            $('<a href="/backup/edit?part=${detail.part}" class="service-ele-edit">编辑</a>').insertBefore('.service-${detail_index}-part .service-ele');
                        </#if>
                    </script>
                </#list>
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