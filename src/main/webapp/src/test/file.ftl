<!DOCTYPE html>
<html>  
    <head>  
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">  
        <title>test file upload</title>  
    </head>  
      
    <body style="text-align:left">  
    	<#if message??>
    		<p>${message}</p>
    	</#if>
        <form id="loginform" name="loginform" method="POST" action="/uploadFileTest" enctype="multipart/form-data">  
            <table width="100%" border="0" cellspacing="0" cellpadding="0">  
                <tr>  
                    <td height="30" align="right">Choose File</td>  
                    <td align="left">  
                        <input name="imageFile" type="file"/>  
                    </td>  
                </tr>  
                <tr>  
                    <td align="center" colspan="2">  
                        <input type="submit" value="submit" name="submit" />  
                    </td>  
                </tr>  
            </table>  
        </form>  
    </body>  
</html>  