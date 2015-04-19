<#include "../header/header.ftl">
<link href="/src/service/service.css" rel="stylesheet" />

<link href="/bower_components/jquery-ui/jquery-ui.min.css" rel="stylesheet" type="text/css" />
<link href="/bower_components/jtable/themes/lightcolor/blue/jtable.css" rel="stylesheet" type="text/css" />
<link href="/bower_components/uploadify/uploadify.css" rel="stylesheet" type="text/css" />

<div class="service-detail">
	<#include "sidebar.ftl">
	    
    <div class="pull-right service-main">
    	<div id="sliderTableContainer"></div>
    </div>
</div>

<#include "../footer/footer.ftl">
<script>
    require(['admin/slider']);
</script>
</body>
</html>