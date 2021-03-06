<link rel="stylesheet" href="static/css/bootstrap-table.min.css">
<link rel="stylesheet" href="static/css/layer.css">
<link href="static/css/bootstrap-datetimepicker.min.css" rel="stylesheet" type="text/css">

<script src="static/js/bootstrap-datetimepicker.js"></script>
<script src="static/js/bootstrap-table.min.js"></script>
<script src="static/js/bootstrap-table-zh-CN.min.js"></script>
<script src="static/js/layer.js"></script>
<script src="static/js/jquery.form.js"></script>
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">日志存储空间管理</h1>
    </div>
    <!-- /.col-lg-12 -->
</div>

<div class="content">

    <h4>当前存储空间阈值 ：<label>${storageinfo.totalAmount} <button id="btn_updatespace" type="button" class="btn btn-primary"
                                                                   data-toggle="modal"
                                                                  data-target="#updatespace">
    <span class="glyphicon glyphicon-alert" aria-hidden="true"></span>修改存储空间阈值</button></label></h4>
    <br>

    <h4>存储空间预警值 ：<label>${storageinfo.alertAmount} <button id="btn_notifyspace" type="button" class="btn btn-danger"
                                                            data-toggle="modal"
                                                            data-target="#notifyspace">
        <span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span>修改预警值</button></label></h4>
    <br>

    <h4>当前已使用的日志存储空间值： <label>${storageinfo.currentUsed}</label></h4>

    <br>

    <h4>日志保存时长： <input type="number" id="txtLogSaveTime" name="txtLogSaveTime" value="${storageinfo.logSaveTime!}"> 月<button id="btn_logSaveTime" onclick="UpdateLogSaveTime()" type="button" class="btn btn-primary" value="">修改</button>

        <h4>日志存储空间满时，将自动覆盖最早存储的日志数据</h4>
    <#---->
</div>
<#--<h5>空间占用明细如下表所示：</h5>-->
<#--<table width="100%" class="table table-striped table-bordered table-hover" id="table" data-toolbar="#toolbar">-->
    <#--<thead>-->
    <#--<tr>-->
    <#--&lt;#&ndash;<th data-field="id" data-checkbox="true"></th>&ndash;&gt;-->
        <#--&lt;#&ndash;<th>MYSQL数据库大小</th>&ndash;&gt;-->
        <#--&lt;#&ndash;<th>项目文件大小</th>&ndash;&gt;-->
        <#--&lt;#&ndash;<th>案例库文件大小</th>&ndash;&gt;-->
        <#--&lt;#&ndash;<th>资料库文件大小</th>&ndash;&gt;-->
        <#--&lt;#&ndash;<th>专家库文件大小</th>&ndash;&gt;-->
        <#--&lt;#&ndash;<th>公告文件大小</th>&ndash;&gt;-->
        <#--<th>日志空间大小</th>-->
    <#--</tr>-->
    <#--</thead>-->

    <#--<tr>-->
    <#--&lt;#&ndash;<th data-field="id" data-checkbox="true"></th>&ndash;&gt;-->
        <#--&lt;#&ndash;<td>${storageinfo.dbAmount}</td>&ndash;&gt;-->
        <#--&lt;#&ndash;<td>${storageinfo.projectAmount}</td>&ndash;&gt;-->
        <#--&lt;#&ndash;<td>${storageinfo.anliAmount}</td>&ndash;&gt;-->
        <#--&lt;#&ndash;<td>${storageinfo.ziliaoAmount}</td>&ndash;&gt;-->
        <#--&lt;#&ndash;<td>${storageinfo.expertAmount}</td>&ndash;&gt;-->
        <#--&lt;#&ndash;<td>${storageinfo.gongaoAmount}</td>&ndash;&gt;-->
        <#--<td>${storageinfo.logAmount}</td>-->
    <#--</tr>-->
    <#--<tbody>-->
    <#--</tbody>-->
<#--</table>-->


<div class="modal fade" id="updatespace" role="dialog" aria-labelledby="updatespacelabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="yishouModalLabel">修改阈值</h4>
            </div>
            <div class="modal-body">
                <div class="row">

                    <form role="form" id="yishouForm" name="yishouForm">
                        <div class="form-group col-md-12">
                            <label>当前阈值</label>
                            <label>${storageinfo.totalAmount}</label>
                        </div>

                        <div class="form-group col-md-12">
                            <label>选择单位：</label>
                            <select id="seldanwei" class="form-control">
                                <option value="MB">MB</option>
                                <option value="GB">GB</option>
                                <option VALUE="TB">TB</option>
                                <option VALUE="PB">PB</option>
                            </select>
                        </div>
                        <div class="form-group col-md-12">
                            <label>请输入新阈值:</label>
                            <input class="form-control" type="number"
                                   id="newspace"
                                   name="newspace">

                        </div>
                    </form>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                </button>
                <button type="button" class="btn btn-primary" data-dismiss="modal" onclick="UpdateSpaceAmount()" id="UpdateSpaceAmount">修改</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>


<div class="modal fade" id="notifyspace" role="dialog" aria-labelledby="notifyspacelabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="alertModalLabel">修改预警</h4>
            </div>
            <div class="modal-body">
                <div class="row">

                    <form role="form" id="alertForm" name="alertForm">
                        <div class="form-group col-md-12">
                            <label>当前预警值</label>
                            <label>${storageinfo.alertAmount}</label>
                        </div>

                        <div class="form-group col-md-12">
                            <label>选择单位：</label>
                            <select id="seldalertanwei" class="form-control">
                                <option value="MB">MB</option>
                                <option value="GB">GB</option>
                                <option VALUE="TB">TB</option>
                                <option VALUE="PB">PB</option>
                            </select>
                        </div>
                        <div class="form-group col-md-12">
                            <label>请输入新预警值:</label>
                            <input class="form-control" type="number"
                                   id="newalert"
                                   name="newalert">

                        </div>
                    </form>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                </button>
                <button type="button" class="btn btn-primary" data-dismiss="modal" onclick="UpdateAlertAmount()" id="UpdateAlertAmount">修改</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<!-- Page-Level Demo Scripts - Tables - Use for reference -->
<script type="text/javascript">



    $(document).ready(function () {


    })

    function UpdateLogSaveTime() {

        if (!$("#txtLogSaveTime").val()) {
            layer.alert("请输入日志保存时长");
            return;
        }
        if (!($("#txtLogSaveTime").val() >= 0 && $("#txtLogSaveTime").val() <= 12)) {

            layer.alert("请输入小于12的数字");
            return;
        }

        $.ajax({
            type: "post",
            url: "UpdateLogSaveTime",
            data: {saveTime: $("#txtLogSaveTime").val()},
            success: function (result) {
                result = JSON.parse(result);
                if (result.msg == "success") {
                    layer.alert("修改成功！");
                } else {
                    layer.alert("修改失败,请稍后重试！")
                }
            }
        });

    }
    function UpdateAlertAmount() {
        $.ajax({
            type: "post",
            url: "UpdateAlertAmount",
            data: {amount:$("#newalert").val(),danwei:$("#seldalertanwei").val()},
            async: false,
            success: function (result) {
                result=JSON.parse(result);
                if (result.msg == "success") {
                    layer.alert("修改成功！");
                    $('.modal').map(function () {
                        $(this).modal('hide');
                    });
                    $(".modal-backdrop").remove();
                    $("#page-wrapper").load("GetStorageInfo");
                } else {
                    layer.alert("修改失败,请稍后重试！")
                }
            }
        });

    }
    function UpdateSpaceAmount() {

        $.ajax({
            type: "post",
            url: "UpdateSpaceAmount",
            data: {amount:$("#newspace").val(),danwei:$("#seldanwei").val()},
            async: false,
            success: function (result) {
                result=JSON.parse(result);
                if (result.msg == "success") {
                    layer.alert("修改成功！");
                    $('.modal').map(function () {
                        $(this).modal('hide');
                    });
                    $(".modal-backdrop").remove();
                    $("#page-wrapper").load("GetStorageInfo");
                } else {
                    layer.alert("修改失败,请稍后重试！")
                }
            }
        });


    }

</script>
