<link href="static/css/bootstrap-datetimepicker.min.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="static/css/bootstrap-table.min.css">
<link rel="stylesheet" href="static/css/layer.css">
<link rel="stylesheet" href="static/css/fileinput.css">


<script src="static/js/bootstrap-datetimepicker.js"></script>
<script src="static/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<script src="static/js/bootstrap-table.min.js"></script>
<script src="static/js/bootstrap-table-zh-CN.min.js"></script>
<script src="static/js/layer.js"></script>
<script src="static/js/fileinput.js"></script>
<script src="static/js/locales/zh.js"></script>
<script src="static/js/jquery.form.js"></script>
<script src="static/js/formValidator.js"></script>

<style type="text/css">
    .dropdown-submenu {
        position: relative;
    }

    .dropdowmspc > a {
        text-align: left;

        border: 1px solid #ccc;
        color: #555;
        width: 65%;
    }

    .dropdown-submenu > .dropdown-menu {
        top: 0;
        left: 100%;
        margin-top: -6px;
        margin-left: -1px;
        -webkit-border-radius: 0 6px 6px 6px;
        -moz-border-radius: 0 6px 6px;
        border-radius: 0 6px 6px 6px;
    }

    .dropdown-submenu:hover > .dropdown-menu {
        display: block;
    }

    .dropdown-submenu > a:after {
        display: block;
        content: " ";
        float: right;
        width: 0;
        height: 0;
        border-color: transparent;
        border-style: solid;
        border-width: 5px 0 5px 5px;
        border-left-color: #ccc;
        margin-top: 5px;
        margin-right: -10px;
    }

    .dropdown-submenu:hover > a:after {
        border-left-color: #fff;
    }

    .dropdown-submenu.pull-left {
        float: none;
    }

    .dropdown-submenu.pull-left > .dropdown-menu {
        left: -100%;
        margin-left: 10px;
        -webkit-border-radius: 6px 0 6px 6px;
        -moz-border-radius: 6px 0 6px 6px;
        border-radius: 6px 0 6px 6px;
    }

    /*.form-horizontal .form-group {*/
    /*margin-right: -15;*/
    /*margin-left: -171px;*/
    /*}*/
</style>

<script type="text/javascript" src="static/js/ckeditor/ckeditor.js"></script>
<#--<script type="text/javascript" src="static/js/ckeditor/ckeditor_msg.js"></script>-->
<div class="content">


    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">新建案例</h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>


    <div class="row">
        <ul id="myTab" class="nav nav-tabs">
            <li><a href="#sfpl" id="sfplA" data-toggle="tab">司法判例</a></li>
            <li><a href="#glzc" id="glzcA" data-toggle="tab">管理支撑</a></li>
            <li><a href="#hxhz" id="hxhzA" data-toggle="tab">横向合作</a></li>
            <li><a href="#zlfw" id="zlfwA" data-toggle="tab">专利服务</a></li>
        </ul>


        <div id="myTabContent" class="tab-content">
            <div class="tab-pane" id="sfpl">
                <input type="hidden" id="tabType"/>
                <div class="panel-body" style="margin-top: 30px">
                    <form class="form-horizontal" role="form" id="sfplForm">
                        <input type="hidden" id="sfpl-id"/>
                        <div class="form-group">
                            <label for="sfpl-title" class="col-sm-1 control-label">标题</label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" id="sfpl-title" name="sfpl-title" placeholder="请输入标题"
                                       value="${info.title!}">
                            </div>
                        </div>


                        <div class="form-group">
                            <label for="sfpl-classificlevelId" class="col-sm-1 control-label">密级</label>
                            <div class="col-sm-6">
                                <select name="sfpl-classificlevelId" id="sfpl-classificlevelId" class="form-control">
                                    <option value="-1">请选择密级</option>
                                    <#if (levelId >= 4)> <option value="4">机密</option></#if>
                                    <#if (levelId >= 3)> <option value="3">秘密</option></#if>
                                    <#if (levelId >= 2)> <option value="2">内部</option></#if>
                                    <#if (levelId >= 1)>  <option value="1">公开</option></#if>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="nationality" class="col-sm-1 control-label">国别</label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" id="nationality" name="nationality" placeholder="请输入国别" value="${info.nationality!}">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="caseName" class="col-sm-1 control-label">案例名称</label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" id="caseName" name="caseName"
                                       placeholder="请输入案例名称"
                                       value="${info.caseName!}">
                            </div>
                        </div>


                        <div class="form-group">
                            <label for="closeTime" class="col-sm-1 control-label">结案时间</label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" id="closeTime" name="closeTime"
                                       placeholder="请输入结案时间"
                                       value="${info.closeTime!}">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="finalAppeal" class="col-sm-1 control-label">终审法院</label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" id="finalAppeal" name="finalAppeal"
                                       placeholder="请输入终审法院"
                                       value="${info.finalAppeal!}">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="ziliaoContent" class="col-sm-1 control-label">上传附件</label>

                            <div class="col-sm-6">
                                <a href="javascript:void(0)" class="btn btn-warning" onclick="electronicFile('sfpl')"
                                   data-toggle="modal" data-target="#fileuploadModal">电子</a>
                                <a href="javascript:void(0)" class="btn btn-primary" onclick="paperFile('sfpl')"
                                   data-toggle="modal" data-target="#paperuploadModal">纸质</a>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-1 control-label">附件列表</label>
                            <div class="col-sm-6" id="sfpl-attachment">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="sfpl-ckeditor" class="col-sm-1 control-label">摘要</label>
                            <div class="col-sm-6">
                            <textarea id="sfpl-ckeditor" name="sfpl-ckeditor" class="form-control" rows="5"
                                      cols="38"></textarea>
                                <script type="text/javascript">CKEDITOR.replace('sfpl-ckeditor')</script>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-6">
                                <@shiro.lacksPermission name="anli:save">
                                    <script language="JavaScript" type="text/javascript">
                                        $('input').attr("readonly", "readonly")
                                        $('select').attr("disabled", "disabled")
                                        $('textarea').attr("readonly", "readonly")
                                        $("[onclick='electronicFile()']").attr('disabled', 'disabled')
                                        $("[onclick='paperFile()']").attr('disabled', 'disabled');
                                        $('#dLabel').attr('data-toggle', '');
                                    </script>

                                </@shiro.lacksPermission>

                                <@shiro.hasPermission name="anli:save">
                                    <a href="javascript:void(0)" class="btn btn-success" onclick="createAnli('sfpl')">保存</a>
                                </@shiro.hasPermission>

                                <a href="javascript:void(0)" class="btn btn-primary" onclick="back()">返回</a>
                            </div>
                        </div>

                    </form>
                </div>
            </div>


            <div class="tab-pane fade" id="glzc">
                <div class="panel-body" style="margin-top: 30px">
                    <form class="form-horizontal" role="form" id="glzcForm">
                        <input type="hidden" id="glzc-id"/>
                        <div class="form-group">
                            <label for="glzc-title" class="col-sm-1 control-label">标题</label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" id="glzc-title" name="glzc-title" placeholder="请输入标题"
                                       value="${info.title!}">
                            </div>
                        </div>


                        <div class="form-group">
                            <label for="glzc-classificlevelId" class="col-sm-1 control-label">密级</label>
                            <div class="col-sm-6">
                                <select name="glzc-classificlevelId" id="glzc-classificlevelId" class="form-control">
                                    <option value="-1">请选择密级</option>
                                    <#if (levelId >= 4)> <option value="4">机密</option></#if>
                                    <#if (levelId >= 3)> <option value="3">秘密</option></#if>
                                    <#if (levelId >= 2)> <option value="2">内部</option></#if>
                                    <#if (levelId >= 1)>  <option value="1">公开</option></#if>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="projectName" class="col-sm-1 control-label">项目名称</label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" id="projectName" name="projectName" placeholder="请输入项目名称" value="${info.projectName!}">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="projectSource" class="col-sm-1 control-label">项目来源</label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" id="projectSource" name="projectSource" placeholder="请输入项目来源" value="${info.projectSource!}">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="principal" class="col-sm-1 control-label">主要负责人</label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" id="principal" name="principal" placeholder="请输入主要负责人" value="${info.principal!}">
                            </div>
                        </div>


                        <div class="form-group">
                            <label for="participant" class="col-sm-1 control-label">参与人</label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" id="participant" name="participant" placeholder="请输入参与人" value="${info.participant!}">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="judicialStatus" class="col-sm-1 control-label">状态</label>
                            <div class="col-sm-6">
                                <select class="form-control" id="judicialStatus" name="judicialStatus">
                                    <option value="一审">一审</option>
                                    <option value="二审">二审</option>
                                    <option value="再审">再审</option>
                                    <option value="其他">其他</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="ziliaoContent" class="col-sm-1 control-label">上传附件</label>

                            <div class="col-sm-6">
                                <a href="javascript:void(0)" class="btn btn-warning" onclick="electronicFile('glzc')"
                                   data-toggle="modal" data-target="#fileuploadModal">电子</a>
                                <a href="javascript:void(0)" class="btn btn-primary" onclick="paperFile('glzc')"
                                   data-toggle="modal" data-target="#paperuploadModal">纸质</a>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="ziliaoContent" class="col-sm-1 control-label">附件列表</label>
                            <div class="col-sm-6" id="glzc-attachment">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="glzc-ckeditor" class="col-sm-1 control-label">摘要</label>
                            <div class="col-sm-6">
                            <textarea id="glzc-ckeditor" name="glzc-ckeditor" class="form-control" rows="5"
                                      cols="38"></textarea>
                                <script type="text/javascript">CKEDITOR.replace('glzc-ckeditor')</script>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-6">
                                <@shiro.lacksPermission name="anli:save">
                                    <script language="JavaScript" type="text/javascript">
                                        $('input').attr("readonly", "readonly")
                                        $('select').attr("disabled", "disabled")
                                        $('textarea').attr("readonly", "readonly")
                                        $("[onclick='electronicFile()']").attr('disabled', 'disabled')
                                        $("[onclick='paperFile()']").attr('disabled', 'disabled');
                                        $('#dLabel').attr('data-toggle', '');
                                    </script>

                                </@shiro.lacksPermission>

                                <@shiro.hasPermission name="anli:save">
                                    <a href="javascript:void(0)" class="btn btn-success" onclick="createAnli('glzc')">保存</a>
                                </@shiro.hasPermission>

                                <a href="javascript:void(0)" class="btn btn-primary" onclick="back()">返回</a>
                            </div>
                        </div>

                    </form>
                </div>
            </div>

            <div class="tab-pane fade" id="hxhz">
                <div class="panel-body" style="margin-top: 30px">
                    <form class="form-horizontal" role="form" id="hxhzForm">
                        <input type="hidden" id="hxhz-id"/>
                        <div class="form-group">
                            <label for="hxhz-title" class="col-sm-1 control-label">标题</label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" id="hxhz-title" name="hxhz-title" placeholder="请输入标题"
                                       value="${info.title!}">
                            </div>
                        </div>


                        <div class="form-group">
                            <label for="hxhz-classificlevelId" class="col-sm-1 control-label">密级</label>
                            <div class="col-sm-6">
                                <select name="hxhz-classificlevelId" id="hxhz-classificlevelId" class="form-control">
                                    <option value="-1">请选择密级</option>
                                    <#if (levelId >= 4)> <option value="4">机密</option></#if>
                                    <#if (levelId >= 3)> <option value="3">秘密</option></#if>
                                    <#if (levelId >= 2)> <option value="2">内部</option></#if>
                                    <#if (levelId >= 1)>  <option value="1">公开</option></#if>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="ziliaoContent" class="col-sm-1 control-label">上传附件</label>

                            <div class="col-sm-6">
                                <a href="javascript:void(0)" class="btn btn-warning" onclick="electronicFile('hxhz')"
                                   data-toggle="modal" data-target="#fileuploadModal">电子</a>
                                <a href="javascript:void(0)" class="btn btn-primary" onclick="paperFile('hxhz')"
                                   data-toggle="modal" data-target="#paperuploadModal">纸质</a>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="ziliaoContent" class="col-sm-1 control-label">附件列表</label>
                            <div class="col-sm-6" id="hxhz-attachment">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="hxhz-ckeditor" class="col-sm-1 control-label">摘要</label>
                            <div class="col-sm-6">
                            <textarea id="hxhz-ckeditor" name="hxhz-ckeditor" class="form-control" rows="5"
                                      cols="38"></textarea>
                                <script type="text/javascript">CKEDITOR.replace('hxhz-ckeditor')</script>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-6">
                                <@shiro.lacksPermission name="anli:save">
                                    <script language="JavaScript" type="text/javascript">
                                        $('input').attr("readonly", "readonly")
                                        $('select').attr("disabled", "disabled")
                                        $('textarea').attr("readonly", "readonly")
                                        $("[onclick='electronicFile()']").attr('disabled', 'disabled')
                                        $("[onclick='paperFile()']").attr('disabled', 'disabled');
                                        $('#dLabel').attr('data-toggle', '');
                                    </script>

                                </@shiro.lacksPermission>

                                <@shiro.hasPermission name="anli:save">
                                    <a href="javascript:void(0)" class="btn btn-success" onclick="createAnli('hxhz')">保存</a>
                                </@shiro.hasPermission>

                                <a href="javascript:void(0)" class="btn btn-primary" onclick="back()">返回</a>
                            </div>
                        </div>

                    </form>
                </div>
            </div>

            <div class="tab-pane fade" id="zlfw">
                <div class="panel-body" style="margin-top: 30px">
                    <form class="form-horizontal" role="form" id="zlfwForm">
                        <input type="hidden" id="zlfw-id"/>
                        <div class="form-group">
                            <label for="zlfw-title" class="col-sm-1 control-label">标题</label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" id="zlfw-title" name="zlfw-title" placeholder="请输入标题"
                                       value="${info.title!}">
                            </div>
                        </div>


                        <div class="form-group">
                            <label for="zlfw-classificlevelId" class="col-sm-1 control-label">密级</label>
                            <div class="col-sm-6">
                                <select name="zlfw-classificlevelId" id="zlfw-classificlevelId" class="form-control">
                                    <option value="-1">请选择密级</option>
                                    <#if (levelId >= 4)> <option value="4">机密</option></#if>
                                    <#if (levelId >= 3)> <option value="3">秘密</option></#if>
                                    <#if (levelId >= 2)> <option value="2">内部</option></#if>
                                    <#if (levelId >= 1)>  <option value="1">公开</option></#if>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="ziliaoContent" class="col-sm-1 control-label">上传附件</label>

                            <div class="col-sm-6">
                                <a href="javascript:void(0)" class="btn btn-warning" onclick="electronicFile('zlfw')"
                                   data-toggle="modal" data-target="#fileuploadModal">电子</a>
                                <a href="javascript:void(0)" class="btn btn-primary" onclick="paperFile('zlfw')"
                                   data-toggle="modal" data-target="#paperuploadModal">纸质</a>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="zlfw-attachment" class="col-sm-1 control-label">附件列表</label>
                            <div class="col-sm-6" id="zlfw-attachment">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="zlfw-ckeditor" class="col-sm-1 control-label">摘要</label>
                            <div class="col-sm-6">
                            <textarea id="zlfw-ckeditor" name="zlfw-ckeditor" class="form-control" rows="5"
                                      cols="38"></textarea>
                                <script type="text/javascript">CKEDITOR.replace('zlfw-ckeditor')</script>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-6">
                                <@shiro.lacksPermission name="anli:save">
                                    <script language="JavaScript" type="text/javascript">
                                        $('input').attr("readonly", "readonly")
                                        $('select').attr("disabled", "disabled")
                                        $('textarea').attr("readonly", "readonly")
                                        $("[onclick='electronicFile()']").attr('disabled', 'disabled')
                                        $("[onclick='paperFile()']").attr('disabled', 'disabled');
                                        $('#dLabel').attr('data-toggle', '');
                                    </script>

                                </@shiro.lacksPermission>

                                <@shiro.hasPermission name="anli:save">
                                    <a href="javascript:void(0)" class="btn btn-success" onclick="createAnli('zlfw')">保存</a>
                                </@shiro.hasPermission>

                                <a href="javascript:void(0)" class="btn btn-primary" onclick="back()">返回</a>
                            </div>
                        </div>

                    </form>
                </div>
            </div>
        </div>



    </div>


<#include "../keyword/AddKeyword.ftl">


</div>


<!-- 上传电子附件 -->
<div class="modal fade" id="fileuploadModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <input type="hidden" id="classificlevelId"/>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">电子附件</h4>
            </div>
            <div class="modal-body">
                <form id="fileForm" action="uploadAatachment" enctype="multipart/form-data" method="post">
                    <input type="hidden" name="category" value="AL"/>
                    <input type="hidden" name="fileType" value="1"/>
                    <input id="fileClassify" name="fileClassify" type="hidden" value="2"/>
                    <div class="form-group">
                        <select name="classificlevel" id="classificlevel" class="form-control">
                            <option value="-1">请选择密级</option>
                            <#if (levelId >= 4)> <option value="4">机密</option></#if>
                            <#if (levelId >= 3)> <option value="3">秘密</option></#if>
                            <#if (levelId >= 2)> <option value="2">内部</option></#if>
                            <#if (levelId >= 1)>  <option value="1">公开</option></#if>
                        </select>
                    </div>
                    <div class="form-group">
                        <select name="auditUser" id="auditUser" class="form-control">
                            <option value="-1">请选择审核人员</option>
                            <#foreach user in auditUsers>
                                <option value="${user.id?c}">${user.username!}</option>
                            </#foreach>
                        </select>
                    </div>
                    <div class="form-group" style="height: 300px">
                        <input id="dataFile" name="dataFile" onchange="selectFile()" type="file" multiple/>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="saveFile">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>


<!-- 更新纸质附件信息 -->
<div class="modal fade" id="paperuploadModal" role="dialog" aria-labelledby="myPaperModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myPaperModalLabel">纸质附件</h4>
            </div>
            <div class="paper-body">
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="savePaperFile">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->
<script type="text/javascript">


    function addInput(id, fileName, type) {
        var viewDiv = '';
        if (type == 1) {
            viewDiv = '<a href="getFile?mid=' + id + '" download="' + fileName + '"><span class="fa fa-download" style="margin-left: 6px"></span></a>';
        } else {
            viewDiv = '<a href="javascript:void(0)" onclick="viewFile(' + id + ')"><span class="fa fa-eye" style="margin-left: 6px"></span></a>';
        }

        var childInput = '<div><input style="margin-top: 5px;" type="text" name="mid" data-value="' + id + '" value="' + fileName + '" class="btn btn-default" readonly><span class="glyphicon glyphicon-remove" style="color: red;margin-left: 6px" onclick="removeInput(this)"></span>' + viewDiv + '</div>'

        var type = $('#myTab').find("li.active a")[0].id;
        type = type.substr(0,type.length-1)
        debugger
        $("#"+type+"-attachment").append(childInput)
        // $("#attachment").append(childInput)

    }

    function removeInput(obj) {

        obj.parentElement.remove();

    }


    var paperContent = '<div class="panel-body"><div class="row"><div class="col-lg-6"><form role="form"id="paperForm"><div class="form-group"><label for="select">密级</label><select id="classificlevel"name="classificlevel"class="form-control"><option value="-1">请选择密级</option><#if (levelId >= 4)> <option value="4">机密</option></#if><#if (levelId >= 3)> <option value="3">秘密</option></#if><#if (levelId >= 2)> <option value="2">内部</option></#if><#if (levelId >= 1)>  <option value="1">公开</option></#if>r</select></div><div class="form-group"><label for="select">密级</label><select name="auditUser" id="auditUser1" class="form-control"><option value="-1">请指定审核人员</option><#foreach user in auditUsers><option value="${user.id?c}">${user.username!}</option></#foreach></select></div><div class="form-group"><label>文件归档号</label><input class="form-control"name="filingNum"placeholder="请输入文件归档号"/></div><div class="form-group"><label>责任人</label><input type="hidden"name="fileType"value="0"/><input type="hidden"name="fileClassify"value="2"/><input type="hidden"name="category"value="AL"/><input type="hidden"name="creator"value=<@shiro.principal property="username"/>><input class="form-control"name="zrr"placeholder="请输入责任人"/></div></form></div></div></div>'
    function paperFile() {

        $("#savePaperFile").removeAttr("disabled");

        $(".paper-body").html(paperContent);


        $('#paperForm').bootstrapValidator({
            message: 'This value is not valid',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                fileName: {
                    validators: {
                        notEmpty: {
                            message: '文件名不能为空'
                        }
                    }
                },
                zrr: {
                    validators: {
                        notEmpty: {
                            message: '责任人不能为空'
                        }
                    }
                },
                filingNum: {
                    validators: {
                        notEmpty: {
                            message: '文件归档号不能为空'
                        }
                    }
                },
                classificlevel: {
                    message: '请选择文件密级',
                    validators: {
                        message: '请选择文件密级',
                        callback: {
                            callback: function (value, validator) {
                                if (value == -1) {
                                    return false;
                                } else {
                                    return true;
                                }
                            }
                        }
                    }
                },
                auditUser: {
                    message: '请选择审核人员',
                    validators: {
                        message: '请选择审核人员',
                        callback: {
                            callback: function (value, validator) {
                                if (value == -1) {
                                    return false;
                                } else {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    function back() {
        $("#page-wrapper").load("anliList");
    }

    function selectFile() {
        var obj = document.getElementById("dataFile");
        var len = obj.files.length;
        for (var i = 0; i < len; i++) {
            temp = obj.files[i].name;

            $("input[name='mid']").each(function () {
                if (temp == this.value) {
                    layer.msg("请勿重复上传同一文件,或者修改名称后重新上传");
                    $("#saveFile").attr("disabled", "disabled");
                    return;
                }

            });

        }
    }

    var modalContent = '';

    // $("#classificlevel").change(function () {
    //     var selectVal = $("#classificlevel").find("option:selected").val();
    //
    //     $("#classificlevelId").val(selectVal);
    //
    // })
    function electronicFile(type) {

        $("#saveFile").removeAttr("disabled");
        $('#fileuploadModal').modal('show')


        $('#dataFile').fileinput({//初始化上传文件框
            showUpload: false,
            showRemove: false,
            uploadAsync:false,
            uploadLabel: "上传",//设置上传按钮的汉字
            uploadClass: "btn btn-primary",//设置上传按钮样式
            showCaption: false,//是否显示标题
            language: "zh",//配置语言
            uploadUrl: "uploadAatachment",
            maxFileSize: 0,
            maxFileCount: -1, /*允许最大上传数，可以多个，当前设置单个*/
            enctype: 'multipart/form-data',
            allowedPreviewTypes: ['image', 'html', 'text', 'video', 'audio', 'flash', 'object', 'other'], //allowedFileTypes: ['image', 'video', 'flash'],
            allowedFileExtensions: ["jpg", "png", "jpeg", "gif", "docx", "pdf", "xls", "xlsx", "doc", "txt"], /*上传文件格式*/
            msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
            dropZoneTitle: "请通过拖拽文件放到这里",
            dropZoneClickTitle: "或者点击此区域添加文件",
            uploadExtraData: {
                'category': 'AL',
                "fileType": '1',
                'classificlevel': $("#"+type+"classificlevel").value,
                'fileClassify': 2,
                'auditUser':$('#auditUser').value,
            },//这个是外带数据
            showBrowse: false,
            browseOnZoneClick: true,
            slugCallback: function (filename) {
                return filename.replace('(', '_').replace(']', '_');
            }
        });

    };

    $('#saveFile').on('click', function () {


        var selectVal = $("#classificlevel").find("option:selected").val();

        if (selectVal == -1) {
            layer.msg("请选择密级");
            return;
        }

        var tabId = $('#myTab').find("li.active a")[0].id;
        var infoLevel = $("#"+tabId.substr(0,tabId.length-1)+"-classificlevelId").find("option:selected").val();
        var fileLevel = $("#classificlevel").find("option:selected").val();

        if (fileLevel > infoLevel){
            layer.msg("文件密级不可大于案例密级");
            return;
        }

        var auditUser = $("#auditUser").find("option:selected").val();

        if (auditUser == -1) {
            layer.msg("请选择审核人员");
            return;
        }

        var options = {
            url: 'uploadAatachment',   //同action
            type: 'post',
            beforeSend: function (xhr) {//请求之前

            },
            success: function (data) {
                data = JSON.parse(data)
                if (data.msg == "success") {

                    var mid = data.mid;
                    var fileName = data.fileName;

                    var viewDiv = '审核中';
                    var childInput = '<div><input style="margin-bottom: 5px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;" type="text" name="mid" data-value="' + mid + '" value="' + fileName + '" class="btn btn-default" readonly><span class="glyphicon glyphicon-remove" style="color: red;    margin-left: 6px" onclick="removeInput(this)"></span>' + viewDiv + '</div>';
                    var type = $('#myTab').find("li.active a")[0].id;
                    type = type.substr(0,type.length-1);
                    debugger
                    $("#"+type+"-attachment").append(childInput)
                    layer.msg("文件上传成功!");
//
                    $("#saveFile").attr("disabled", "disabled");
                    $('.modal').map(function () {
                        $(this).modal('hide');
                    });
                    $(".modal-backdrop").remove();
                } else {
                    layer.msg("文件上传失败，请稍后重试!");
                }

            },
            error: function (xhr, status, msg) {
                layer.msg('玩命加载中..');

            }
        };
        $("#fileForm").ajaxSubmit(options);

    });

    $('#savePaperFile').on('click', function () {

        var bootstrapValidator = $("#paperForm").data('bootstrapValidator');
        bootstrapValidator.validate()
        if (!bootstrapValidator.isValid())
            return;

        var selectVal = $("#classificlevel").find("option:selected").val();

        if (selectVal == -1) {
            layer.msg("请选择密级");
            return;
        }


        var tabId = $('#myTab').find("li.active a")[0].id;
        var infoLevel = $("#"+tabId.substr(0,tabId.length-1)+"-classificlevelId").find("option:selected").val();
        var fileLevel = $("#classificlevel").find("option:selected").val();


        if (fileLevel > infoLevel){
            layer.msg("文件密级不可大于案例密级");
            return;
        }

        $.post('uploadAatachment', $("#paperForm").serialize(), function (data) {
            data = JSON.parse(data)

            if (data.msg == 'success') {
                layer.msg("附件信息保存成功!");

                addInput(data.mid, data.fileName, 0);
                $("#savePaperFile").attr("disabled", "disabled");

                $('.modal').map(function () {
                    $(this).modal('hide');
                });
                $(".modal-backdrop").remove();
            }

        })
    })


</script>

<script type="text/javascript">
    var button = '<a id="dLabel" role="button" data-toggle="dropdown" class="btn btn-white" href="javascript:;"><span id="select-title">选择分类</span> <span class="caret"></span></a>';

    var html = '';

    var subHtml = addSubMenu(1, 0);

    if (subHtml) {
        html = button + subHtml;
        $("#dropDown").html(html);
    }


    function addSubMenu(type, parentId) {
        var subHtml = [];
        $.ajax({
            type: "post",
            url: "findMenuByTypeAndParentId",
            data: {type: type, parentId: parentId},
            async: false,
            success: function (result) {
                result = JSON.parse(result).result;
                if (result && result.length > 0) {
                    subHtml.push('<ul class="dropdown-menu multi-level" role="menu" aria-labelledby="dropdownMenu">');

                    $.each(result, function () {

                        subHtml.push('<li class="dropdown-submenu">');
                        subHtml.push('<a href="javascript:void(0);" data-title="' + this.typeName + '" data-content="' + this.id + '">' + this.typeName + '</a>');
                        subHtml.push(addSubMenu(type, this.id));
                        subHtml.push('</li>');

                    })

                    subHtml.push('</ul>');

                }
            }
        });

        return subHtml.join('');

    }


    $('.dropdown li a').click(function () {

        title = $(this).attr("data-title");
        id = $(this).attr("data-content");
        $("#select-title").text(title);
        $("#typeId").val(id)
    })
</script>
<script type="text/javascript">

    function createAnli(type) {

        debugger
        //类别动态赋值
        var as = $('#anli').find('a');
        var typeId;
        for(var i =1 ;i<as.length;i++){
            var typeName = $(as[i]).find('span')[0].innerText;
            var href = as[i].href;

            if (typeName == '司法判例' && type == 'sfpl'){
               typeId = href.split(',')[1].substring(0,href.split(',')[1].length-1);
            } else if (typeName == '管理支撑' && type == 'glzc') {
                typeId = href.split(',')[1].substring(0,href.split(',')[1].length-1);
            } else if (typeName == '横向合作' && type == 'hxhz') {
                typeId = href.split(',')[1].substring(0,href.split(',')[1].length-1);
            } else if (typeName == '专利服务' && type == 'zlfw') {
                typeId = href.split(',')[1].substring(0,href.split(',')[1].length-1);
            }
        }
        var selectVal = $("#classificlevelId").find("option:selected").val();

        if (selectVal == -1) {
            layer.msg("请选择密级");
            return;
        }

        var data = CKEDITOR.instances[type+'-ckeditor'].getData();


        if (data == '') {
            layer.msg('请填写案例摘要');
            return false;
        }

        if (typeId == '' || typeId == undefined) {
            layer.msg('请选择案例类别');
            return false;
        }

        var bootstrapValidator = $("#"+type+"Form").data('bootstrapValidator');
        bootstrapValidator.validate();

        if (!bootstrapValidator.isValid()) {
            return;
        }

        var mids = [];
        $("input[name='mid']").each(function () {
            mids.push(this.dataset.value)
        });

        var params = {
            title: $("#"+type+"-title").val(),
            content: data,
            classificlevelId: $("#"+type+"-classificlevelId").val(),
            typeId: typeId,
            mids: mids,
            id: $("#"+type+"-id").val(),
            type: 'AL',
            nationality:$('#nationality').val(),
            caseName:$('#caseName').val(),
            closeTime:$('#closeTime').val(),
            finalAppeal:$('#finalAppeal').val(),
            projectName:$('#projectName').val(),
            projectSource:$('#projectSource').val(),
            principal:$('#principal').val(),
            participant:$('#participant').val(),
            judicialStatus:$('judicialStatus').val(),
            type:type
        }


        $.post('createAnli', params, function (result) {


            result = JSON.parse(result);

            if (result.msg == 'success') {
                layer.msg("案例信息保存成功!");
                $("#page-wrapper").load("anliList");
            } else {
                layer.msg(result.msg);
            }
        });

    }


</script>

<script type="text/javascript">

    function qryFileAuitResult(fileId,fileName,mid,auditUser) {

        $.ajax({
            type: "post",
            url: "getFileAuditResult",
            data: {fileId: fileId,type:'DOWNLOAD',auditUser:auditUser},
            async: false,
            success: function (result) {
                var paperContent = '<div class="panel-body"><div class="row"><div class="col-lg-6"><form role="form"><div class="form-group"><label for="select">审核人员</label><select name="auditUser" id="auditUser1" class="form-control"><option value="-1">请选择审核人员</option><#foreach user in auditUsers><option value="${user.id?c}">${user.username!}</option></#foreach></select></div></form></div></div></div>'


                result = JSON.parse(result);
                if (result.auditResult == 0){
                    layer.alert("下载请求已提交审核,请稍后再试。")
                }else if(result.auditResult == -1){
                    layer.alert("下载请求被拒绝,请联系审核人员。")
                }else if (result.auditResult == 2){

                    layer.open({
                        type: 1,
                        title:'文件审核',
                        area: ['300px', '220px'],
                        btn: ['确定','关闭'],
                        content: paperContent,
                        yes:function () {

                            var auditUser = $("#auditUser1").find("option:selected").val();

                            if (auditUser == -1) {
                                layer.msg("请选择审核人员");
                                return;
                            }

                            layer.closeAll()
                            qryFileAuitResult(fileId,fileName,mid,auditUser)
                        }
                    });
                }else {
                    // window.location.href=";
                    var link = document.createElement('a');
                    link.href="getFile?mid="+mid;
                    link.download = fileName;
                    var e = document.createEvent('MouseEvents');
                    e.initEvent('click', true, true);
                    link.dispatchEvent(e);

                }
            }
        });
    }

    debugger
    var classificlevelId = '${info.classificlevelId!}';

    var type = '${info.type}';

    $('#'+type+"-id").val('${info.id}')

    if (classificlevelId != "") {
        $("#"+type+"-classificlevelId").val(classificlevelId);
    }

    CKEDITOR.instances[type+'-ckeditor'].setData('${info.content!}');
    <#--$("#typeId").val('${info.typeId!}');-->

    <#--if ('${info.typeId!}' != '') {-->
    <#--    $.ajax({-->
    <#--        type: "post",-->
    <#--        url: "findMenuById",-->
    <#--        data: {mid: '${info.typeId!}'},-->
    <#--        async: false,-->
    <#--        success: function (data) {-->
    <#--            data = JSON.parse(data)-->
    <#--            $("#select-title").text(data.typeName);-->
    <#--            <@shiro.lacksPermission name="anli:save">-->
    <#--                $('#dropDown').html('<input type="text" class="form-control" readonly value="'+data.typeName+'"/>')-->
    <#--            </@shiro.lacksPermission>-->
    <#--        }-->
    <#--    });-->
    <#--}-->


    if ('${info.id!}' != '') {
        var finalResult;
        $.ajax({
            type: "post",
            url: "findFileByZid",
            data: {zid: '${info.id!}', type: 'AL'},
            async: false,
            success: function (data) {
                debugger

                data = JSON.parse(data)
                $.each(data, function () {
                    var fileName = this.fileName;
                    var fileId = this.id;
                    $.ajax({
                        type: "post",
                        url: "findModuleFileByFileCode",
                        data: {fileCode: this.fileCode},
                        async: false,
                        success: function (result) {
                            result = JSON.parse(result);
                            debugger
                            var fileType = result.fileType;
                            var mid = result.id;
                            var viewDiv = '<@shiro.hasPermission name="anli:delete"><span class="glyphicon glyphicon-remove" style="color: red;margin-left: 6px" onclick="removeInput(this)"></span></@shiro.hasPermission>';
                            $.ajax({
                                type: "post",
                                url: "getFileAuditResult",
                                data: {fileId: fileId,type:'UPLOAD'},
                                async: false,
                                success: function (result) {
                                    debugger

                                    result = JSON.parse(result);
                                    finalResult = result.finalResult
                                    if (result.auditResult == -1) {
                                        viewDiv = '<label style="margin-left: 20px;color: red;">审核未通过</label>'
                                    }else if(result.auditResult == 0){
                                        viewDiv = '<label style="margin-left: 20px;color: grey;">审核中</label>'
                                    }else {

                                        if (fileType == 1) {
                                            debugger
                                            // viewDiv += '<a href="getFile?mid=' + result.id + '" download="' + fileName + '"><span class="fa fa-download" style="margin-left: 6px"></span></a>';
                                            viewDiv += '<a href="javascript:void(0)" onclick="qryFileAuitResult('+fileId+',\''+fileName+'\',\''+mid+'\',\''+""+'\')"><span class="fa fa-download" style="margin-left: 6px"></span></a>';                                        } else {
                                            viewDiv += '<a href="javascript:void(0)" onclick="viewFile(' + mid + ')"><span class="fa fa-eye" style="margin-left: 6px"></span></a>';
                                        }

                                    }



                                }
                            });

                            var childInput = '<div><input style="margin-top: 5px;"  type="text" name="mid" data-value="' + result.id + '" value="' + fileName + '" class="btn btn-default" readonly>' + viewDiv + '</div>'
                            if (finalResult != -1){
                                var type = '${info.type}';
                                debugger
                                $("#"+type+"-attachment").append(childInput)
                                // $("#attachment").append(childInput);
                            }
                        }
                    });
                })

            }
        });
    }


    $(document).ready(function () {
        var type = '${info.type}'
        $('#'+type+'A').tab('show');
    })

</script>