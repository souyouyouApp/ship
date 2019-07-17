<style>
    .kv-file-zoom {
        display: none
    }

</style>
<div class="panel-body">
    <div class="row">
        <form class="form-inline">
            <div class="form-group">
                <label>选择阶段:</label>
                <select class="form-control" style="width: 150px;" id="phaselist"
                        name="phaselist" onchange="OnPhaseItemChange()">
                    <option value="-1" selected="selected">-全部阶段---</option>
                     <#list phases as phase>

                 <option value="${phase.id!}">${phase.phaseName!}</option>

                     </#list>

                </select>
            </div>
            <div class="form-group" style="margin-left: 50px;">
                <label>选择附件类型:</label>
                <select class="form-control" style="width: 150px;" id="attachlist"
                        name="attachlist" onchange="refreshFileTable()">
                    <option value="-1" selected="selected">--全部文件--</option>
                    <#list phasesfiletype as pfiletype>
                        <option value="${pfiletype.id!}">${pfiletype.fileTypeName!}</option>
                    </#list>
                </select>
            </div>
        <#--<div class="form-group" style="margin-left: 50px;">-->
        <#--<button id="btn_search" type="button" class="btn btn-primary"-->
        <#--onclick="refreshFileTable()">-->
        <#--<span class="glyphicon glyphicon-search" aria-hidden="true"></span>查询-->
        <#--</button>-->
        <#--</div>-->
        </form>
    </div>
    <div class="row">
        <div class="table-responsive" style="margin-top: 20px;">

            <div id="filetabletoolbar" class="btn-group">
        <@shiro.hasPermission name="project:uploadattach">
            <button id="btn_addele" type="button" class="btn btn-primary"
                    data-toggle="modal" data-target="#fileuploadModal"
                    onclick="AddModalContent()">
                <span class="glyphicon glyphicon-upload" aria-hidden="true"></span>上传电子附件
            </button>
        </@shiro.hasPermission>
        <@shiro.hasPermission name="project:addpaper">
            <button id="btn_addpaper" type="button" class="btn btn-primary"
                    style="margin-left: 20px;" data-toggle="modal"
                    data-target="#addPaperModal" onclick="AddPaperModalConent()">
                <span class="glyphicon glyphicon-book" aria-hidden="true"></span>添加纸质附件
            </button>
        </@shiro.hasPermission>
        <#--<@shiro.hasPermission name="project:editfile">-->
            <#--<button id="btn_editfile" type="button" class="btn btn-primary"-->
                    <#--style="margin-left: 20px;" onclick="EditAttachFile()">-->
                <#--<span class="glyphicon glyphicon-edit" aria-hidden="true"></span>编辑文件信息-->
            <#--</button>-->
        <#--</@shiro.hasPermission>-->
                <#--<button id="btn_editfile1" data-toggle="modal"-->
                        <#--data-target="#addPaperModal" type="button" class="btn btn-primary"-->
                        <#--style="margin-left: 20px; display: none">-->
                    <#--<span class="glyphicon glyphicon-edit" aria-hidden="true"></span>-->
                <#--</button>-->
        <@shiro.hasPermission name="project:downfile">
            <button id="btn_downloadfile" type="button" class="btn btn-primary"
                    style="margin-left: 20px;" onclick="DownLoadAttachFile()">
                <span class="glyphicon glyphicon-download" aria-hidden="true"></span>下载文件
            </button>
        </@shiro.hasPermission>
                <button id="btn_reqdownloadfile" type="button" class="btn btn-primary"
                        style="margin-left: 20px;"  data-toggle="modal"
                        data-target="#reqdown">
                    <span class="glyphicon glyphicon-registration-mark" aria-hidden="true"></span>提交下载请求
                </button>
        <@shiro.hasPermission name="project:deleteattach">
            <button id="btn_delete" type="button" class="btn btn-danger"
                    style="margin-left: 20px;" onclick="DeleteAttachFile()">
                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>删除附件
            </button>
        </@shiro.hasPermission>
            </div>
            <table width="100%" class="table table-striped table-bordered table-hover text-nowrap"
                   id="filetable" data-toolbar="#filetabletoolbar" style="margin-top:20px;">
            </table>
        </div>
    </div>
</div>

<div class="modal fade" id="reqdown" role="dialog" aria-labelledby="reqdownlabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="reqdownModalLabel">选择下载审核人</h4>
            </div>
            <div class="modal-body">
                <div class="row">

                    <form role="form" id="reqdownForm" name="reqdownForm">

                        <div class="form-group col-md-12">
                            <label>选择下载审核人：</label>
                            <select id="selreqDown" class="form-control">
                                <#--<#list users as user>-->
                                    <#--<option value="${user.username}">${user.username}</option>-->
                                <#--</#list>-->
                                    <option value="-1">请选择审核人员</option>
                                                <#foreach user in auditUsersByLevel>
                                <option value="${user.id?c}">${user.username!}</option>
                                                </#foreach>
                            </select>
                        </div>
                    </form>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                </button>
                <button type="button" class="btn btn-primary" data-dismiss="modal" onclick="ReqDownLoadAttachFile()" id="ReqDownLoadAttachFile">提交</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>


<!-- Modal 上传电子文件-->
<div class="modal fade" id="fileuploadModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">上传文件</h4>


            </div>

            <div class="modal-body">
            </div>

            <div class="modal-footer">

                <form>

                    <div class="form-group col-md-6">
                        <select class="form-control" id="seldzfile" name="seldzfile" onchange="LoadAuditUsers(this)">
                            <option value="-1">--请指选择密级--</option>
                        <#if (proClassLevel >= 4)> <option value="4">机密</option></#if>
                   <#if (proClassLevel >= 3)> <option value="3">秘密</option></#if>
                   <#if (proClassLevel >= 2)> <option value="2">内部</option></#if>
                   <#if (proClassLevel >= 1)>  <option value="1">公开</option></#if>
                        </select>
                    </div>

                    <div class="form-group col-md-6">
                        <select name="eauditUser" id="eauditUser" class="form-control" onchange="SetEauditUser()">
                            <option value="-1">--请指定审核人员--</option>
        <#foreach user in auditUsers>
            <option value="${user.id?c}">${user.username!}</option>
        </#foreach>
                        </select>
                    </div>

                </form>
                <button type="button" class="btn btn-default" onclick="RefreshFilelist()" data-dismiss="modal">关闭
                </button>
                <button type="button" class="btn btn-primary" id="savePic">保存</button>
                <button type="button" class="btn btn-primary" data-dismiss="modal" style="display: none" id="savePic1">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->

<!-- Modal 添加纸质文件-->
<div class="modal fade" id="addPaperModal" role="dialog" aria-labelledby="paperModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" id="closeAddPaperModal" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="paperModalLabel">文件信息</h4>
            </div>
            <div class="modal-body">
                <div class="row">

                    <form role="form" id="paperForm" name="paperForm">
                        <input type="hidden" id="paperproid" name="paperproid" value="${proentity.id}">
                        <input type="hidden" id="editFileId" name="editFileId" value="0">
                        <div class="form-group col-md-12">
                            <label>文件名称</label>
                            <input class="form-control" id="paperFileName" name="paperFileName">
                        </div>
                        <div class="form-group col-md-12">
                            <label>密级</label>
                            <select class="form-control" id="paperClassicId" name="paperClassicId" onchange="FilterUsersForPaper(this)">
                              <#if (proClassLevel >= 4)> <option value="4">机密</option></#if>
                   <#if (proClassLevel >= 3)> <option value="3">秘密</option></#if>
                   <#if (proClassLevel >= 2)> <option value="2">内部</option></#if>
                   <#if (proClassLevel >= 1)>  <option value="1">公开</option></#if>
                            </select>
                        </div>
                        <div class="form-group col-md-12">
                            <label>文件归档号</label>
                            <input class="form-control" id="filingNum" name="filingNum" placeholder="请输入文件归档号"/>
                        </div>

                        <div class="form-group col-md-12">
                            <label>保管人</label>
                            <select class="form-control" id="paperzrr" name="paperzrr">
                                 <#list users as user>
                                     <option value="${user.username}">${user.username}</option>
                                 </#list>
                            </select>
                        </div>
                        <div class="form-group col-md-12">
                            <label>指定审核人员</label>
                            <select name="pauditUser" id="pauditUser" class="form-control">
                                <option value="-1" selected>请选择</option>

                            </select>
                        </div>

                    </form>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" onclick="RefreshFilelist()" data-dismiss="modal">关闭
                </button>
                <button type="button" class="btn btn-primary" onclick="UploadPaper()" id="savePaper">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->
<input type="hidden" id="hidEaudiUser" name="hidEaudiUser" value="-1">
<input type="hidden" id="hidProClassLevel" name="hidProClassLevel" value="${proClassLevel}">
<script language="JavaScript">
    function SetEauditUser() {

        $("#hidEaudiUser").val($("#eauditUser").val());

    }
    if($("#hidowner").val()=="1") {
        $("#btn_addele").show();
        $("#btn_addpaper").show();
        $("#btn_editfile").show();
        $("#btn_delete").show();
    }else
    {
        $("#btn_addele").hide();
        $("#btn_addpaper").hide();
        $("#btn_editfile").hide();
        $("#btn_delete").hide();
    }
    $("#phaselist").removeAttr("disabled")
    $("#attachlist").removeAttr("disabled")
    $("#selreqDown").removeAttr("disabled");

    function LoadAuditUsers(obj) {

        //alert($(obj).val());

        $.post("getAuditByClassifyForProject", {cl: $(obj).val()}, function (result) {

            result = JSON.parse(result);
            var htmlstr = "";
            $.each(result, function (i, item) {
                // htmlstr += "<option value='" + item.id + "'>" + item.username + "</option>";
                htmlstr += "<option value='" + item.id + "'>" + item.username + "</option>";
            })

            if (htmlstr != null && htmlstr.length > 0) {

                $("#eauditUser").html("")
                $("#eauditUser").append(htmlstr);
            }
        });

    }

    function FilterUsersForPaper(obj) {

        $.post("getAuditByClassifyForProject", {cl: $(obj).val()}, function (result) {

            result = JSON.parse(result);
            var htmlstr = "";
            $.each(result, function (i, item) {
                // htmlstr += "<option value='" + item.id + "'>" + item.username + "</option>";
                htmlstr += "<option value='" + item.id + "'>" + item.username + "</option>";
            })

            if (htmlstr != null && htmlstr.length > 0) {
                $("#pauditUser").html("")
                $("#pauditUser").append(htmlstr);
            }
        });


        $.post("getusersByClassLevel", {cl: $(obj).val()}, function (result) {

            result = JSON.parse(result);
            var htmlstr = "";
            $.each(result, function (i, item) {
                // htmlstr += "<option value='" + item.id + "'>" + item.username + "</option>";
                htmlstr += "<option value='" + item.realName + "'>" + item.realName + "</option>";
            })

            if (htmlstr != null && htmlstr.length > 0) {
                //$("#proLeaders").innerHTML(htmlstr);
                $("#paperzrr").html("")
                $("#paperzrr").append(htmlstr);
            }
        })

    }

    </script>