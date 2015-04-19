<#include "../header/header.ftl">
<link href="/src/service/service.css" rel="stylesheet" />
<div class="linkpath-breadcrumb">
    <ol class="breadcrumb">
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
    </ol>
</div>
<form action="/backup/submit" class="service-edit-form form-horizontal container" role="form" method="POST" id="edit-service">
    <textarea id="service-edit" name="content">
        
    </textarea>
    <button type="submit" class="btn btn-primary">提交</button>
    <button type="reset" class="btn btn-default">取消</button>
</form>
<#assign part = RequestParameters['part'] />

<script language="JavaScript">
    $('#edit-service').append('<input type="hidden" id="part" name="part" value="${part}" />');
</script>
<#include "../footer/footer.ftl">
<script charset="utf-8" src="/kindeditor/kindeditor.js"></script>
<script charset="utf-8" src="/kindeditor/lang/zh_CN.js"></script>
<script>
    require(['service/service']);
    KindEditor.ready(function(K) {
        var editor = K.create('#service-edit',{
            uploadJson : '/editor/upload.do',
            allowFileManager : false
        });
        editor.html("${model.content?js_string}");
    });
</script>
</body>
</html>