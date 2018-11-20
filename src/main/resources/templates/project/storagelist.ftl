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
        <h1 class="page-header">存储空间管理</h1>
    </div>
    <!-- /.col-lg-12 -->
</div>

<div class="content">

    <h4>当前存储空间阈值 ：<label>${storageinfo.totalAmount}GB</label></h4> <button id="btn_updatespace" type="button" class="btn btn-primary"
                                                                   data-toggle="modal"
                                                                  data-target="#updatespace">
    <span class="glyphicon glyphicon-alert" aria-hidden="true"></span>修改存储空间阈值</button>
    <br>
    <br>

    <h4>当前已使用的存储空间值： <label>${storageinfo.currentUsed}GB</label></h4>

    <br>



</div>
<h5>空间占用明细如下表所示：</h5>
<table width="100%" class="table table-striped table-bordered table-hover" id="table" data-toolbar="#toolbar">
    <thead>
    <tr>
    <#--<th data-field="id" data-checkbox="true"></th>-->
        <th>MYSQL数据库大小</th>
        <th>项目文件大小</th>
        <th>案例库文件大小</th>
        <th>资料库文件大小</th>
        <th>专家库文件大小</th>
        <th>公告文件大小</th>
    </tr>
    </thead>

    <tr>
    <#--<th data-field="id" data-checkbox="true"></th>-->
        <td>${storageinfo.dbAmount}GB</td>
        <td>${storageinfo.projectAmount}GB</td>
        <td>${storageinfo.anliAmount}GB</td>
        <td>${storageinfo.ziliaoAmount}GB</td>
        <td>${storageinfo.expertAmount}GB</td>
        <td>${storageinfo.gongaoAmount}GB</td>
    </tr>
    <tbody>
    </tbody>
</table>


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
                        <input type="hidden" id="editYishouId" name="editYishouId" value="0">
                        <div class="form-group col-md-12">
                            <label>当前阈值</label>
                            <label>${storageinfo.totalAmount}GB</label>
                        </div>

                        <div class="form-group col-md-12">
                            <label>请输入新阈值</label>
                            <input class="form-control" type="text"
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

<!-- Page-Level Demo Scripts - Tables - Use for reference -->
<script type="text/javascript">



    $(document).ready(function () {


    })

    function UpdateSpaceAmount() {

        $.ajax({
            type: "post",
            url: "UpdateSpaceAmount",
            data: {amount:$("#newspace").val()},
            async: false,
            success: function (result) {
                if (result == "ok") {
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
