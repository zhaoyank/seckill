<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>新增秒杀商品</title>
    <link rel="stylesheet" href="/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="/static/datetimepicker/css/bootstrap-datetimepicker.min.css">
    <link rel="stylesheet" href="/static/simditor/styles/simditor.css">
</head>
<body>
    <div class="container">
        <form method="post" enctype="multipart/form-data">
            <legend>新增抢购商品</legend>

            <div class="form-group">
                <label>商品名称</label>
                <input type="text" name="productName" class="form-control">
            </div>
            <div class="form-group">
                <label>副标题</label>
                <input type="text" name="productTitle" class="form-control">
            </div>
            <div class="form-group">
                <label>抢购数量</label>
                <input type="text" name="productInventory" class="form-control">
            </div>
            <div class="form-group">
                <label>图片</label>
                <input type="file" name="file" class="form-control">
            </div>
            <div class="form-group">
                <label>商品价格</label>
                <input type="text" name="price" class="form-control">
            </div>
            <div class="form-group">
                <label>商品市场价</label>
                <input type="text" name="marketPrice" class="form-control">
            </div>
            <div class="form-group">
                <label>开始抢购时间</label>
                <input type="text" name="sTime" id="startTime" class="form-control">
            </div>
            <div class="form-group">
                <label>结束抢购时间</label>
                <input type="text" name="eTime" id="endTime" class="form-control">
            </div>
            <div class="from-group">
                <label>商品详情</label>
                <textarea class="form-control" name="productDesc" id="editor"></textarea>
            </div>

            <button class="btn btn-primary">保存</button>
        </form>
    </div>

    <script src="/static/js/jquery-2.2.3.min.js"></script>
    <script src="/static/plugins/datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
    <script src="/static/plugins/datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
    <script src="/static/plugins/moment/moment.js"></script>
    <script type="text/javascript" src="/static/simditor/scripts/module.js"></script>
    <script type="text/javascript" src="/static/simditor/scripts/hotkeys.js"></script>
    <script type="text/javascript" src="/static/simditor/scripts/uploader.js"></script>
    <script type="text/javascript" src="/static/simditor/scripts/simditor.js"></script>
    <script>
        $(function () {

            var editor = new Simditor({
                textarea: $('#editor')
            });

            <!-- 时间框 -->
            var picker = $('#startTime').datetimepicker({
                format: "yyyy-mm-dd hh:ii",
                language: "zh-CN",
                autoclose: true,
                todayHighlight: true,
                startDate:"now()"
            });

            var timepicker = $('#endTime').datetimepicker({
                format: "yyyy-mm-dd hh:ii",
                language: "zh-CN",
                autoclose: true,
                todayHighlight: true,
                startDate:"now()"
            });
        });
    </script>
</body>
</html>