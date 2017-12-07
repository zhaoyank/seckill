<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>秒杀</title>
    <link rel="stylesheet" href="/static/css/bootstrap.min.css">
    <link href="https://cdn.bootcss.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <style>
        .btn-lg {
            margin-top: 35px;
            width: 400px;
        }
        .clock {
            margin-top: 40px;
            font-size: 24px;
        }

    </style>
</head>
<body>
    <div class="container">
        <div class="page-header">
            <div>
                <h3 class="block">秒杀商品</h3>
                <button class="btn btn-info" id="loginLink" ${sessionScope.curr_acccount == null ? "" : "disabled"}>登录</button>
                <h4 id="account">用户:${sessionScope.curr_account.accountName}</h4>
            </div>
        </div>
        <%--<div class="page-header">
            <h4>${product.productName}</h4>
            <small>${product.productTitle}</small>
        </div>--%>
        <div class="row">
            <div class="col-md-4">
                <img src="http://ozpfz7pbc.bkt.clouddn.com/${product.image}?imageView2/1/w/300/h/300" alt="">
            </div>
            <div class="col-md-8">
                <h4>${product.productName}</h4>
                <p>${product.productTitle}</p>
                <h3 class="text-danger">抢购价：￥${product.price} <small style="text-decoration:line-through">市场价：￥ ${product.marketPrice}</small></h3>
                <h4 class="text-danger">总数量: ${product.productInventory}</h4>
                <c:choose>
                    <c:when test="${product.productInventory == 0}">
                        <button class="btn btn-default btn-lg" disabled>已售罄</button>
                    </c:when>
                    <c:when test="${product.end}">
                        <button class="btn btn-default btn-lg" disabled>已结束</button>
                    </c:when>
                    <c:when test="${product.start and not product.end}">
                        <button id="secKillBtn" class="btn btn-lg btn-danger">立即抢购</button>
                    </c:when>
                    <c:otherwise>
                        <button id="secKillBtn" class="btn btn-lg btn-danger" disabled>等待抢购</button>
                        <div class="clock">距离抢购时间：<span id="clock">xx时xx分</span></div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <div class="row">
            <div class="col-md-offset-4 col-md-8">
                <div>
                    ${product.productDesc}
                </div>
            </div>
        </div>
    </div>

    <!-- 模态框 -->
    <div class="modal fade" id="loginModal">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="modal-title">登录</h4>
                </div>
                <div class="modal-body">
                    <form id="loginForm" action="/login" method="post">
                        <div class="form-group">
                            <label>账号</label>
                            <input type="text" class="form-control remindTime" name="accountName">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal" id="resetBtn">取消</button>
                    <button type="button" class="btn btn-primary" id="loginBtn">登录</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->


    <script src="/static/js/jquery-2.2.3.min.js"></script>
    <script src="/static/moment/moment.js"></script>
    <script src="/static/js/jquery.countdown.min.js"></script>
    <script src="/static/layer/layer.js"></script>
    <!-- Bootstrap 3.3.6 -->
    <script src="/static/js/bootstrap.min.js"></script>
    <script>
        $(function () {
            $("#clock").countdown(${product.startTimeToMil},function(event) {
                $(this).html(event.strftime('%D 天 %H小时%M分钟%S秒'));
            }).on("finish.countdown",function () {
                $("#secKillBtn").text("立即抢购").removeAttr("disabled");
                $("#clock").countdown(${product.endTimeToMil},function(event) {
                    $(this).html(event.strftime('%D 天 %H小时%M分钟%S秒'));
                }).on("finish.countdown",function () {
                    $("#secKillBtn").text("活动结束").attr("disabled", "disabled");
                });
            });

            //抢购
            $("#secKillBtn").click(function () {
                $.get("/product/seckill/${product.id}").done(function (resp) {
                    if(resp.state == "success") {
                        layer.alert("抢购成功");
                    } else {
                        layer.alert(resp.message);
                    }
                }).error(function () {
                    layer.msg("服务器异常");
                });
            });

            $("#loginLink").click(function () {
                $("#loginModal").modal({
                   show : true,
                   backdrop : "static"
                });
            });
            $("#loginBtn").click(function () {
                $.post("/login",$("#loginForm").serialize()).done(function (resp) {
                    if(resp.state == "success") {
                        layer.msg("登录成功");
                        $("#loginLink").attr("disabled","disabled");
                        $("#account").text(resp.data.accountName);
                    } else {
                        layer.msg(resp.message);
                    }
                }).error(function () {
                    layer.msg("系统异常");
                });
            });

        });
    </script>
</body>
</html>