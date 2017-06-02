<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Error</title>
</head>
<body>
<h1>错误</h1>
<#if Session["SPRING_SECURITY_LAST_EXCEPTION"]??>
    <div>Error: ${Session["SPRING_SECURITY_LAST_EXCEPTION"].message}</div>
<#else>
    <div>Error: ${error!""}</div>
    <div>Message: ${message!""}</div>
    <div>Status: ${status!""}</div>
    <div>Path: ${path!""}</div>
</#if>
</body>
</html>