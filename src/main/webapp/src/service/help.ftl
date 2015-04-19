<#include "../header/header.ftl">
<link href="/src/service/service.css" rel="stylesheet" />

<div class="service-help">
    <ol class="service-breadcrumb breadcrumb">
        <#if model.breadcrumb?size gt 0>
            <#list model.breadcrumb as bread>
                <li>
                    <#if bread.link??>
                        <a href="${bread.link}">${bread.text}</a>
                    <#else>
                        ${bread.text}
                    </#if>
                </li>
            </#list>
        </#if>
        <#if model.basic.role?? && model.basic.role == "admin">
            <#assign currentId = RequestParameters['id'] />
            <#assign q = RequestParameters['q'] />
            <a href="/help/edit?id=${currentId}&q=${model.q}" class="service-ele-edit service-help-edit">编辑</a>
        </#if>
    </ol>
    <div class="service-help-content">
        ${model.content}
    </div>
</div>

<#include "../footer/footer.ftl">
<script>
    require(['service/service']);
</script>
</body>
</html>