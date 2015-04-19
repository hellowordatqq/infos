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
<form action="/service/submit" class="service-edit-form form-horizontal container" role="form" method="POST" id="edit-service">
    <textarea id="service-edit" name="content">
        
    </textarea>
    <button type="submit" class="btn btn-primary">提交</button>
    <button type="reset" class="btn btn-default">取消</button>
</form>
<script language="JavaScript">
    function getVal(key){
        var url = location.href.split("?");
        if(url[1]==null)return null;
        var field = url[1].split("&");
        for(i=0;i<field.length;i++){
            var val = field[i].split("="); 
            if(val[0]==key){
                return val[1];
            }
        }
        return null;
    }
    
    $('#edit-service').append('<input type="hidden" id="id" name="id" value="'+ getVal("id") +'"/>');
    $('#edit-service').append('<input type="hidden" id="type" name="type" value="'+ getVal("type") +'"/>');
    $('#edit-service').append('<input type="hidden" id="part" name="part" value="'+ getVal("part") +'"/>');
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