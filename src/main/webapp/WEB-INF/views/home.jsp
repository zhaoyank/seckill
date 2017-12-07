<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>秒杀列表</title>
    <link rel="stylesheet" href="/static/css/bootstrap.min.css">
</head>
<body>
    <div class="container">


        <div class="page-header">
            <div>
                <h3 class="block">抢购列表</h3>
                <button id="loginLink" class="btn btn-info" ${sessionScope.curr_acccount == null ? "" : "disabled"}>登录</button>
                <h4 id="account">用户:${sessionScope.curr_account.accountName}</h4>
            </div>
        </div>
        <div class="panel panel-default">
            <div class="panel-heading">
                <a href="/product/new" class="btn btn-success">添加商品</a>
            </div>

            <div class="panel panel-body">
                <c:forEach items="${productList}" var="product">
                    <div class="row">
                        <div class="col-md-3">
                            <img src="http://ozpfz7pbc.bkt.clouddn.com/${product.image}?imageView2/1/w/200/h/200" alt="">
                        </div>
                        <div class="col-md-9">
                            <h4><a href="/product/${product.id}">${product.productName}</a></h4>
                            <h3>秒杀价: ￥ ${product.price}</h3>
                            <h3>开始时间: <fmt:formatDate value="${product.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></h3>
                        </div>
                    </div>
                </c:forEach>
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
    <!-- Bootstrap 3.3.6 -->
    <script src="/static/js/bootstrap.min.js"></script>
    <script src="/static/layer/layer.js"></script>
    <script>
        $(function () {
            var account = ${sessionScope.curr_account};


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