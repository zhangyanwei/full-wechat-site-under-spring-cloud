<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="Content-Language" content="zh-cn" />
    <title>ASKDOG | 经验分享社区</title>
</head>
<body>
<#setting number_format="##">

<nav>
    <ul>
        <#list pagedExperiences.page..pagedExperiences.total / pagedExperiences.size as i>
            <#if i == 0>
                <li><a href="page.html">${i+1}</a></li>
            <#else>
                <li><a href="page.html?page=${i+1}">${i+1}</a></li>
            </#if>
        </#list>
    </ul>
</nav>
<nav>
    <#list pagedExperiences.result as exp>
        <a href="http://www.askdog.com/exp/${exp.id}.html">${exp.subject}</a><br>
    </#list>
</nav>

</body>
</html>