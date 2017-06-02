<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>惠券 | 认证</title>
    <link href="lib/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <script src="lib/jquery/jquery.min.js"></script>
    <script src="lib/bootstrap/js/bootstrap.min.js"></script>

    <style type="text/css">
        .form-control {
            border: none;
            box-shadow: none;
            border-bottom: 1px solid #ccc;
            border-radius: 0px;
            outline: 0;
            padding: 6px 4px;
            background: transparent;
        }

        .form-control:focus,
        .form-control:active,
        .form-control:hover,
        .has-success .form-control:focus,
        .has-success .form-control:hover {
            border-color: #f70;
            box-shadow: none;
        }

        .btn {
            border: none;
            outline: none !important;
            box-shadow: 0px 1px 2px #bbb;
            padding: 7px 12px;
        }

        .btn-primary {
            color: #fff;
            background-color: #f70;
        }

        .btn-primary:hover {
            color: #fff;
            background-color: #f90;
            border: none;
        }

        .btn-primary:active, .btn-primary:focus {
            color: #fff;
            background-color: #f70 !important;
            border: none;
        }

        body {
            background: #000;
            font-family: Helvetica, Tahoma, Arial, STXihei, "华文细黑", "Microsoft YaHei", "微软雅黑", SimSun, "宋体", Heiti, "黑体", sans-serif;
        }

        #container {
            text-align: center;
            position: absolute;
            top: 280px;
            width: 100%;
        }

        #bg {
            background: #000;
            opacity: 0.5;
            width: 100%;
            height: 319px;
            position: absolute;
            box-shadow: 0px 2px 15px #000;
        }

        #login {
            display: inline-block;
            width: 450px;
            padding: 20px;
            background: #eee;
            text-align: left;
            position: relative;
            border-top: 4px solid #f70;
        }
    </style>
</head>

<body>
<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->


<img src="http://vod-bucket-resources.oss-cn-hangzhou.aliyuncs.com/bg2.jpg" style="position: absolute; height: 100%; width: 100%; top: 0px;" />

<div style="position: relative; top: 90px;
    font-size: 50px;
    color: #fff;
    width: 768px;
    margin: 0 auto;
    border-bottom: 1px solid #f70;
    z-index: 10;">
    <span style="display: inline-block; padding: 2px; background: #f70;">惠券</span> 您身边的移动互联网营销专家
</div>

<div style="position: relative; top: 100px;
    font-size: 20px;
    color: #fff;
    width: 768px;
    margin: 0 auto;
    text-align: right;
    z-index: 10;">覆盖主流目标群体，移动互联精准投放
</div>

<div id="container">

    <div id="bg"></div>

    <div id="login">

        <div style="color: #f70; font-size: 30px;margin-bottom:20px;"><img
                src="http://coupon.askdog.com/style/images/logo.png"/> 用户登录
        </div>
        <form class="form-horizontal" role="form" action="login" method="post">
            <div class="form-group">
                <div class="col-sm-12">
                    <input type="text" class="form-control" name="username" id="username" placeholder="用户名/手机号/邮箱地址">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-12">
                    <input type="password" class="form-control" name="password" id="password" placeholder="密码">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-9">
                    <div class="checkbox">
                        <label>
                            <input type="checkbox" name="remember_me"> 自动登录
                        </label>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-12">
                    <button type="submit" class="btn btn-default btn-primary" style="width: 100%;">登录</button>
                    <#if Session["SPRING_SECURITY_LAST_EXCEPTION"]??>
                        <span style="position: absolute; color: #f55;;">${Session["SPRING_SECURITY_LAST_EXCEPTION"].message}</span>
                    </#if>
                </div>
            </div>
        </form>
    </div>
</div>

</body>
</html>