<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">

<head>

    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <meta name="description" content=""/>
    <meta name="author" content=""/>

    <title>${proentity.proName}审核</title>

    <!-- jQuery -->
    <script src="static/js/jquery.min.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="static/js/bootstrap.min.js"></script>
    <script src="static/js/messenger.min.js"></script>
    <script src="static/js/messenger-theme-future.js"></script>


    <!-- MetisMenu CSS -->
    <link href="static/css/metisMenu.min.css" rel="stylesheet"/>

    <!-- Custom CSS -->
    <link href="static/css/sb-admin-2.css" rel="stylesheet"/>

    <!-- Morris Charts CSS -->
    <link href="static/css/morris.css" rel="stylesheet"/>


    <!-- Bootstrap Core CSS -->
    <link href="static/css/bootstrap.css" rel="stylesheet"/>
    <link href="static/css/messenger-theme-future.css" rel="stylesheet"/>
    <link href="static/css/messenger.css" rel="stylesheet"/>


    <!-- Custom Fonts -->
    <link href="static/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>


    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>

    <script src="static/js/html5shiv.js"></script>
    <script src="static/js/respond.min.js"></script>

    <link rel="stylesheet" href="static/css/bootstrap-table.min.css">
    <link rel="stylesheet" href="static/css/layer.css">
    <link rel="stylesheet" href="static/css/fileinput.css">
    <link rel="stylesheet" href="static/css/bootstrapValidator.min.css">
    <link rel="stylesheet" href="static/css/bootstrap-select.css">
    <link href="static/css/bootstrap-datetimepicker.min.css" rel="stylesheet" type="text/css">

    <script src="static/js/bootstrap-datetimepicker.js"></script>
    <script src="static/js/bootstrap-table.min.js"></script>
    <script src="static/js/bootstrap-table-zh-CN.min.js"></script>
    <script src="static/js/layer.js"></script>
    <script src="static/js/fileinput.js"></script>
    <script src="static/js/locales/zh.js"></script>
    <script src="static/js/jquery.form.js"></script>
    <script type="text/javascript" src="static/js/bootstrap-select.js"></script>
    <![endif]-->
    <style>
        .spc_span{
            padding: 0px 10px;
            width: 100px;
            display: block;
            line-height: 30px;
            margin-top: -5px;
            margin-right: -10px;
        }
        .disno{
            display: none;
        }
    </style>
</head>

<body>

<div class="content">

    <div class="panel-body">
        <form role="form" id="updateprojectForm" name="updateprojectForm">
            <div class="panel-group" id="accordion">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne">基本信息</a>
                        </h4>
                    </div>
                    <div id="collapseOne" class="panel-collapse collapse in">
                        <div class="panel-body">

                            <input type="hidden" id="id" name="id" value="${proentity.id}">

                            <div class="form-group col-md-6">
                                <label>项目名称</label>
                                <input class="form-control" id="proName" name="proName"
                                       value="${proentity.proName}">
                            </div>
                            <div class="form-group col-md-6">
                                <label>项目密级</label>
                                <select class="form-control" id="classificlevelId" name="classificlevelId">
                                <#--<option value="-1">请选择</option>-->
                               <#if (levelId >= 4)> <option value="4">机密</option></#if>
                   <#if (levelId >= 3)> <option value="3">秘密</option></#if>
                   <#if (levelId >= 2)> <option value="2">内部</option></#if>
                   <#if (levelId >= 1)>  <option value="1">公开</option></#if>
                                </select>
                            </div>

                            <div class="form-group col-md-6">
                                <label>是否立项</label>
                                <select class="form-control" id="proPhase" name="proPhase">
                                <#--<option value="-1">请输入</option>-->
                                    <option value="0">立项中</option>
                                    <option value="1">在研中</option>
                                    <option value="2">已验收</option>
                                    <option value="3">待验收</option>
                                    <option value="4">未立项</option>
                                </select>
                            </div>

                            <div class="form-group col-md-6">
                                <label>项目编号</label>
                                <input class="form-control" id="proNo" name="proNo" value="${proentity.proNo}">
                            </div>


                            <div class="form-group col-md-6">
                                <label>项目来源</label>
                                <input class="form-control" id="proFrom" name="proFrom"
                                       value="${proentity.proFrom}">
                            </div>
                            <div class="form-group col-md-6">
                                <label>项目负责人</label>
                                <select class="form-control" id="proLeaders" onchange="FilterUsers(1)"  name="proLeaders">
                                </select>
                            </div>
                            <div class="form-group col-md-6">
                                <label>主要完成人</label>
                                <select class="selectpicker form-control" onchange="FilterUsers(3)" multiple id="proCompleteors"
                                        title='选择参与者 (必选、可多选)' name="proCompleteors">
                                </select>
                            </div>

                            <div class="form-group col-md-6">
                                <label>项目参与者</label>
                                <select name="proJoiners" id="proJoiners" onchange="FilterUsers(2)" multiple
                                        class="selectpicker form-control"
                                        title='选择参与者(非必选)'>
                                </select>
                            </div>

                            <div class="form-group col-md-6">
                                <label>乘研单位</label>
                                <input class="form-control" id="mainDepartment" name="mainDepartment"
                                       value="${proentity.mainDepartment!}">
                            </div>

                            <div class="form-group col-md-6">

                                <label>研究周期</label>
                            <#--<div class="row">-->
                                <table width="100%">
                                    <tr width="100%">
                                        <td width="40%">
                                            <input class="form-control" width="100%" AUTOCOMPLETE="off" placeholder="请输入开始时间" id="yanjiuZhouQi"
                                                   name="yanjiuZhouQi" value="${proentity.yanjiuZhouQi!}"></td>

                                        <td width="20%">
                                            <span style="margin-left: 40%"> 至</span>
                                        </td>
                                        <td width="40%">
                                            <input class="form-control" width="100%" value="${proentity.yanjiuZhouQi1!}" AUTOCOMPLETE="off" placeholder="请输入结束时间" id="yanjiuZhouQi1"
                                                   name="yanjiuZhouQi1">
                                        </td>
                                    </tr>
                                </table>
                            <#--</div>-->

                            </div>

                            <div class="form-group col-md-6">
                                <label>总经费</label>
                                <div class="input-group">
                                    <input class="form-control" id="totalFee" type="number" name="totalFee"
                                           value="${proentity.totalFee!}">
                                    <span class="input-group-addon">万元</span>
                                </div>
                            </div>

                            <div class="form-group col-md-12">
                                <label>项目研究内容</label>
                                <textarea class="form-control" rows="3" id="proResearchcontent"
                                          name="proResearchcontent"></textarea>
                            </div>


                            <div class="form-group col-md-12">
                                <label>项目方向</label>
                                <textarea class="form-control" rows="3" id="yanjiuFangXiang"
                                          name="yanjiuFangXiang"></textarea>
                            </div>


                            <div class="form-group col-md-12">
                                <label>项目备注</label>
                                <textarea class="form-control" rows="3" id="proRemark"
                                          name="proRemark"></textarea>
                            </div>

                        </div>
                    </div>
                </div>

            <#--<div class="panel panel-default">-->
            <#--<div class="panel-heading">-->
            <#--<h4 class="panel-title">-->
            <#--<a data-toggle="collapse" data-parent="#accordion" href="#collapseTwo">报奖信息</a>-->
            <#--</h4>-->
            <#--</div>-->
            <#--<div id="collapseTwo" class="panel-collapse collapse">-->
            <#--<div class="panel-body">-->


            <#--</div>-->
            <#--</div>-->
            <#--</div>-->

                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a data-toggle="collapse" data-parent="#accordion" href="#collapseThree">阶段时间设置</a>
                        </h4>
                    </div>
                    <div id="collapseThree" class="panel-collapse collapse">
                        <div class="panel-body">
                            <div class="form-group col-md-6">
                                <label for="dtp_input2">立项时间</label>

                                <input class="form-control" AUTOCOMPLETE="off" placeholder="请输入立项时间" value="${proentity.createPhasetime!}"
                                       id="createPhasetime"
                                       name="createPhasetime">

                            <#--<input class="form-control" id="createPhasetime" name="createPhasetime">-->
                            </div>
                            <div class="form-group col-md-6">
                                <label>立项提前通知时间</label>

                                <div class="input-group">
                                    <input class="form-control" id="cpAlertdays" placeholder="请输入立项通知时间" type="number" name="cpAlertdays"
                                           value="${proentity.cpAlertdays!}">
                                    <span class="input-group-addon">天</span>
                                </div>
                            </div>
                            <div class="form-group col-md-6">
                                <label for="dtp_input3">开题时间</label>
                                <input class="form-control" AUTOCOMPLETE="off" placeholder="请输入开题时间" value="${proentity.openPhasetime!}"
                                       id="openPhasetime"
                                       name="openPhasetime">
                            </div>

                            <div class="form-group col-md-6">
                                <label>开题提前通知时间</label>
                                <div class="input-group">
                                    <input class="form-control" id="opAlertdays" placeholder="请输入开题通知时间" type="number" name="opAlertdays"
                                           value="${proentity.opAlertdays!}">
                                    <span class="input-group-addon">天</span>
                                </div>
                            </div>


                            <div class="form-group col-md-6">
                                <label for="dtp_input4">中期检查时间</label>
                                <input class="form-control" AUTOCOMPLETE="off" placeholder="请输入中期检查时间"
                                       value="${proentity.midcheckPhasetime!}"
                                       id="midcheckPhasetime"
                                       name="midcheckPhasetime">

                            <#--<input class="form-control" id="createPhasetime" name="createPhasetime">-->
                            </div>
                            <div class="form-group col-md-6">
                                <label>中期检查提前通知时间</label>
                                <div class="input-group">
                                    <input class="form-control" id="mpAlertdays" placeholder="请输入中期检查通知时间" type="number" name="mpAlertdays"
                                           value="${proentity.mpAlertdays!}">
                                    <span class="input-group-addon">天</span>
                                </div>
                            </div>

                            <div class="form-group col-md-6">
                                <label for="dtp_input4">结题时间</label>

                                <input class="form-control" AUTOCOMPLETE="off" placeholder="请输入结题时间" value="${proentity.closePhasetime!}"
                                       id="closePhasetime"
                                       name="closePhasetime">
                            <#--<input class="form-control" id="createPhasetime" name="createPhasetime">-->
                            </div>
                            <div class="form-group col-md-6">
                                <label>结题提前通知时间</label>
                                <div class="input-group">
                                    <input class="form-control" id="closepAlertdays" placeholder="请输入结题通知时间" type="number" name="closepAlertdays"
                                           value="${proentity.closepAlertdays!}">
                                    <span class="input-group-addon">天</span>
                                </div>
                            </div>

                            <div class="form-group col-md-6">
                                <label for="dtp_input4">验收时间</label>
                                <input class="form-control" AUTOCOMPLETE="off" placeholder="请输入验收时间" value="${proentity.endPhasetime!}"
                                       id="endPhasetime"
                                       name="endPhasetime">
                            <#--<input class="form-control" id="createPhasetime" name="createPhasetime">-->
                            </div>
                            <div class="form-group col-md-6">
                                <label>验收管理提前通知时间 </label>
                                <div class="input-group">
                                    <input class="form-control" id="epAlertdays"  type="number" name="epAlertdays"
                                           value="${proentity.epAlertdays!}">
                                    <span class="input-group-addon">天</span>
                                </div>
                            </div>
                            <div class="form-group col-md-6">
                                <label>是否报奖</label>
                                <select class="form-control" id="isreportReward" name="isreportReward">
                                <#--<option value="-1">请选择</option>-->
                                    <option value="0">否</option>
                                    <option value="1">是</option>
                                </select>
                            </div>

                            <div class="form-group col-md-6">
                                <label>报奖渠道</label>
                                <select class="form-control" id="reportChannel" name="reportChannel">
                                <#--<option value="-1">请选择</option>-->
                                    <option value="0">院内</option>
                                    <option value="1">集团</option>
                                    <option value="2">地方/行业/学会</option>
                                    <option value="3">国防</option>
                                    <option value="4">国家</option>
                                </select>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
            <div class="form-group col-md-12" style="margin-left: 40%">

                <script language="JavaScript" type="text/javascript">
                    $('input').attr("readonly", "readonly")
                    $('select').attr("disabled", "disabled")
                    $('textarea').attr("readonly", "readonly")
                    $('#proJoiners').attr("disabled", "disabled")
                </script>
            <#--<@shiro.hasPermission name="project:save">-->
            <#--<script language="JavaScript" type="text/javascript">-->
            <#--if($("#hidowner").val()=="1") {-->
            <#--$('input').removeAttr("readonly")-->
            <#--$('select').removeAttr("disabled")-->
            <#--$('textarea').removeAttr("readonly")-->
            <#--$('#proJoiners').removeAttr("disabled")-->
            <#--}-->
            <#--</script>-->

            <#--</@shiro.hasPermission>-->
            <#--<a class="btn btn-primary" onclick="UdpateProjectForm()" id="btnupdatepro">-->
            <#--<i class="fa fa-save"></i> 修改-->
            <#--</a>-->

            <#--<button class="btn btn-danger" type="reset">-->
            <#--<span class="glyphicon glyphicon-refresh" aria-hidden="true">重置</span>-->
            <#--</button>-->

            </div>
        </form>
    </div>
</div>

<script language="JavaScript" type="text/javascript">



    function LoadUsers() {
        $.post("getlallusers", {}, function (result) {

            result = JSON.parse(result);
            //var htmlstr = "<option value='-1'>请选择</option>";
            var htmlstr="";
            $.each(result, function (i, item) {
                // htmlstr += "<option value='" + item.id + "'>" + item.username + "</option>";
                htmlstr += "<option value='" + item.username + "'>" + item.username + "</option>";
            })

            if (htmlstr != null && htmlstr.length > 0) {
                //$("#proLeaders").innerHTML(htmlstr);
                debugger;
                $("#proLeaders").append(htmlstr);
                $("#proLeaders").val('${proentity.proLeaders!}');
                $("#proJoiners").append(htmlstr);
                $("#proCompleteors").append(htmlstr);
                $('#proJoiners').selectpicker();
                $('#proCompleteors').selectpicker();


                var proJoiners = "${proentity.proJoiners!}";
                var proCompleteors = "${proentity.proCompleteors!}";

                alert(proJoiners);

                $('#proJoiners').selectpicker("val", proJoiners.split(","));
                $('#proJoiners').selectpicker("refresh");

                $('#proCompleteors').selectpicker("val", proCompleteors.split(","));
                $('#proCompleteors').selectpicker("refresh");

            }
        })
    }


    function InitData() {

        $("#isreportReward").val("${proentity.isreportReward!}");
        $("#reportChannel").val("${proentity.reportChannel!}");
        $("#proPhase").val("${proentity.proPhase!}");
        $("#classificlevelId").val("${proentity.classificlevelId!}");

        $("#proResearchcontent").val("${proentity.proResearchcontent!}");
        $("#proRemark").val("${proentity.proRemark!}");
        $("#yanjiuFangXiang").val("${proentity.yanjiuFangXiang!}");
    }

    function InitDateBCrt() {

        $('#yanjiuZhouQi').datetimepicker({
            minView: "month", format: "yyyy-mm-dd", autoclose:true,todayBtn:true
        });
        $('#yanjiuZhouQi1').datetimepicker({
            minView: "month", format: "yyyy-mm-dd", autoclose:true,todayBtn:true
        });
        $('#createPhasetime').datetimepicker({
            minView: "month", format: "yyyy-mm-dd", autoclose:true,todayBtn:true
        });
        $('#createPhasetime').click(function () {
            $('#createPhasetime').datetimepicker('show');
        });

        $('#openPhasetime').datetimepicker({
            minView: "month", format: "yyyy-mm-dd", autoclose:true,todayBtn: true
        });

        $('#openPhasetime').click(function () {
            $('#openPhasetime').datetimepicker('show');
        });

        $('#midcheckPhasetime').datetimepicker({
            minView: "month", format: "yyyy-mm-dd", autoclose:true,todayBtn: true
        });

        $('#midcheckPhasetime').click(function () {
            $('#midcheckPhasetime').datetimepicker('show');
        });

        $('#closePhasetime').datetimepicker({
            minView: "month", format: "yyyy-mm-dd", autoclose:true,todayBtn: true
        });

        $('#closePhasetime').click(function () {
            $('#closePhasetime').datetimepicker('show');
        });

        $('#endPhasetime').datetimepicker({
            minView: "month", format: "yyyy-mm-dd", autoclose:true,todayBtn: true
        });

        $('#endPhasetime').click(function () {
            $('#endPhasetime').datetimepicker('show');
        });

    }

    $(document).ready(function () {

        LoadUsers();
        InitData();
        InitDateBCrt();
        // if($("#hidowner").val()=="1") {
        //     $("#btnupdatepro").show();
        // }else
        // {
        //     $("#btnupdatepro").hide();
        // }

    })
    </script>

</body>
</html>


