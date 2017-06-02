<#setting number_format="##">
<#assign original_url = "http://www.askdog.com/#/exp/${exp.id}">
<#assign countdown = 30>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="Content-Language" content="zh-cn" />
    <title>ASKDOG | ${exp.subject}</title>
</head>
<body>
<script type="text/javascript">
    var regexp=/\.(?!askdog).+\..+/ig;
    var where = document.referrer;
    if(regexp.test(where)) {
        window.location.href="${original_url}";
    }
</script>

<main>
    <header>
        <h4>${exp.subject}</h4>
    </header>
    <article>
        <#if exp.content.type == "TEXT">
            ${exp.content.content}
        <#else>
            ${exp.subject}
        </#if>
    </article>
</main>

<div style="margin-top: 20px">
    <a href="${original_url}">查看原始页面(<b id="timer">${countdown}</b>秒之后将自动跳转到原始页面)</a>
</div>

<script type="text/javascript">
    var counter = ${countdown};
    var timer = setInterval(function() {
        document.getElementById("timer").innerText = counter--;
        if (counter < 0) {
            clearInterval(timer);
            window.location.href="${original_url}";
        }
    }, 1000)
</script>

</body>
</html>