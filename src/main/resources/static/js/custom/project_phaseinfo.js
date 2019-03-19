/**
 * Created by ghl on 2018/3/1.
 */

function ReviewFiles(fid) {
    //$("#page-wrapper").load("ReviewProjectFiles?fid=" + fid);
    window.location.href = "ReviewProjectFiles?fid=" + fid;
}

$(document).ready(function () {

    $("#phaselist").val(1);
    $("#eauditUser").val(-1);
    $("#pauditUser").val(-1);
    $('#filetable').bootstrapTable({
        url: 'LoadProjectFiles',         //请求后台的URL（*）
        method: 'get',                      //请求方式（*）
        //toolbar: '#toolbar',                //工具按钮用哪个容器
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,                   //是否显示分页（*）
        sortable: true,                     //是否启用排序
        sortOrder: "asc",                   //排序方式
        queryParams: queryParams,//传递参数（*）
        sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1,                       //初始化加载第一页，默认第一页
        pageSize: 10,                       //每页的记录行数（*）
        pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
        search: false,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        strictSearch: false,
        showColumns: false,                  //是否显示所有的列
        showRefresh: false,                  //是否显示刷新按钮
        minimumCountColumns: 2,             //最少允许的列数
        clickToSelect: true,                //是否启用点击选中行
//            height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
        uniqueId: "id",                     //每一行的唯一标识，一般为主键列
        showToggle: false,                    //是否显示详细视图和列表视图的切换按钮
        cardView: false,                    //是否显示详细视图
        detailView: false,                   //是否显示父子表
        responseHandler: responseHandler,//请求数据成功后，渲染表格前的方法
        columns: [
            {
                field: 'check',
                checkbox: true
            },
            {
                field: 'id',
                title: '编号',
                visible: false
            },
            {
                field: 'fileName',
                title: '文件名称',
                sortable: true,
                formatter: function (value, row, index) {
                    // if (row.fileType == 1&&row.audit==1) {
                    //     //return "<a href='" + $("#basePath").val() + row.filePath + "' target='_blank' download='dd.pdf'>" + value + "</a>";
                    //
                    //     return "<a href='javascript:void(0)' target='_blank' onclick='ReviewFiles(" + row.id + ")'>" + value + "</a>"
                    //
                    // } else {
                    //     return value;
                    // }

                    return value;
                }

            }, {
                field: 'fileType',
                title: '文件类型',
                sortable: true,
                formatter: function (value, row, index) {
                    if (value == 1) {
                        return "电子";
                    } else {
                        return "纸质";
                    }
                }

            }, {
                field: 'fileSize',
                title: '文件大小',
                sortable: true,
                formatter: function (value, row, index) {

                    return value / 1000 + "KB";
                }
            },
            {
                field: 'classificlevelId',
                title: '密级',
                sortable: true,
                formatter: function (value, row, index) {
                    if (value == 1) {
                        return "公开";
                    } else if (value == 2) {
                        return "内部";
                    } else if (value == 3) {
                        return "秘密";
                    } else if (value == 4) {
                        return "机密";
                    } else {
                        return value;
                    }

                }
            },
            {
                field: 'audit',
                title: '文件状态',
                sortable: true,
                formatter: function (value, row, index) {
                    ////audit,1:正常，-1：上传审核未通过，-2：下载审核未通过，2：上传审核中，3：下载审核中 4：下载审核通过
                    if (value == 2) {
                        return "上传审核中";
                    } else if (value == 3) {
                        return "下载审核中";
                    }else if(value==4){
                        return "下载审核通过";
                    }
                    else if (value == -1) {
                        return "上传审核未通过";
                    } else if (value == -2) {
                        return "下载审核未通过";
                    }
                    else if (value == 1) {
                        return "正常";//可下载
                    } else {
                        return "";
                    }

                }
            },
            {
                field: 'creator',
                title: '上传者',
                sortable: true
            }, {
                field: 'zrr',
                title: '保管人',
                sortable: true
            }, {
                field: 'createTime',
                title: '上传时间',
                sortable: true
            }]
    })

//请求服务数据时所传参数
    function queryParams(params) {
        return {
            pageSize: params.limit, //每一页的数据行数，默认是上面设置的10(pageSize)
            pageIndex: params.offset / params.limit + 1, //当前页面,默认是上面设置的1(pageNumber)
            phaseId: $("#phaselist").val(), //这里是其他的参数，根据自己的需求定义，可以是多个
            attachFileId: $("#attachlist").val(),
            proId: $("#id").val()
        }
    }

//请求成功方法
    function responseHandler(result) {
        result = result.result;
        //如果没有错误则返回数据，渲染表格
        return {
            total: result[0].totalElements, //总页数,前面的key必须为"total"
            rows: result[0].content //行数据，前面的key要与之前设置的dataField的值一致.
        };
    };

    $("#paperClassicId").val("");
});

//刷新表格数据,点击你的按钮调用这个方法就可以刷新
function refreshFileTable() {
    $('#filetable').bootstrapTable('refresh', {url: 'LoadProjectFiles'});
}

function DeleteAttachFile() {

    var selectefid = $("#filetable").bootstrapTable('getSelections');
    if (selectefid == null || selectefid.length <= 0) {
        layer.msg('请选择要删除的文件！');
        return;
    }

    var index = layer.confirm("确定要删除所选的文件吗？", function () {
        debugger;
        var fids = [];
        $.each(selectefid, function (i, item) {
            fids.push(item.id);
        })

        $.post("DeleteFilesByIds", {
            fids: fids.join(','),
            attachfid: $("#attachlist").val(),
            pid: $("#id").val(),
            phaseid: $("#phaselist").val()
        }, function (result) {
            result = JSON.parse(result)
            if (result.msg == 'success') {
                layer.msg('删除文件成功！');
                refreshFileTable();
            }
        });
    });
}

function OnPhaseItemChange() {

    /*
     1: 立项，【1_1:依据文件，1_2:建议书，1_3:任务书，1_4:其他文件】
     2：开题，【2_1:任务书，2_2:开题报告，2_3:其他文件】
     3：中期, 【3_1:研究报告，3_2:其他文件】
     4：结题，【4_1:研究报告，4_2:过程文件，4_3:其他文件】
     5：后期，【5_1:鉴定证书，5_2:申报书，5_3:评审结果】
     */
    var phaseId = $("#phaselist").val();
    var selHtml = "";

    $.post("GetFileTypeByPhaseId", {
        phaseId: phaseId
    }, function (result) {
        result = JSON.parse(result)
        if (result.msg == 'success') {
            $.each(result.data, function (i, item) {
                if (i == 0) {
                    //selHtml += "<option value='" + item.id + "' selected='selected'>" + item.fileTypeName + "</option>";
                    selHtml += "<option value='-1' selected='selected'>--全部文件--</option>";
                }
                //else {
                selHtml += "<option value='" + item.id + "'>" + item.fileTypeName + "</option>";
                // }
            })

            if(selHtml==""||selHtml.length<=0){
                selHtml += "<option value='-1' selected='selected'>--全部文件--</option>";
            }
            $("#attachlist").html("");
            $("#attachlist").append(selHtml);
            refreshFileTable();
        }
    });

    // switch (phaseId) {
    //     case "1":
    //         selHtml = "<option value='1_1' selected>依据文件</option><option value='1_2'>建议书</option><option value='1_3'>任务书</option><option value='1_4'>其他文件</option>";
    //         break;
    //     case "2":
    //         selHtml = "<option value='2_1' selected>任务书</option><option value='2_2'>开题报告</option><option value='2_3'>其他文件</option>";
    //         break;
    //     case "3":
    //         selHtml = "<option value='3_1' selected>研究报告</option><option value='3_2'>其他文件</option>";
    //         break;
    //     case "4":
    //         selHtml = "<option value='4_1' selected>研究报告</option><option value='4_2'>过程文件</option><option value='4_3'>其他文件</option>";
    //         break;
    //     case "5":
    //         selHtml = "<option value='5_1' selected>依据文件</option><option value='5_2'>建议书</option><option value='5_3'>任务书</option>";
    //         break;
    // }


}





modalContent='<form><div class="form-group" style="height: 300px;">' +
    '<input id="projectfile" name="projectfile" type="file" multiple></div></form>';


function AddPaperModalConent() {
    LoadZrr();
    $("#paperFileName").val("");
    $("#paperzrr").val("");
    $("#editFileId").val(0);
}

function AddModalContent() {
    // if ($("#phaselist").val() == "-1") {
    //     $('.modal').map(function () {
    //         $(this).modal('hide');
    //     });
    //     $(".modal-backdrop").remove();
    //     layer.msg("请选择阶段!");
    //     return;
    // }
    //
    // if ($("#attachlist").val() == "-1") {
    //     $('.modal').map(function () {
    //         $(this).modal('hide');
    //     });
    //     $(".modal-backdrop").remove();
    //     layer.msg("请选择附件类型!");
    //     return;
    // }

    $("#savePic").removeAttr("disabled");

    $("#fileuploadModal .modal-body").html(modalContent);

    $('#projectfile').fileinput({//初始化上传文件框
        showUpload: false,
        showRemove: false,
        showBrowse: false,
        uploadAsync: true,
        uploadLabel: "上传",//设置上传按钮的汉字
        uploadClass: "btn btn-primary",//设置上传按钮样式
        showCaption: false,//是否显示标题
        language: "zh",//配置语言
        uploadUrl: "UpLoadProjectFiles",
        maxFileSize: 0,
        maxFileCount: -1, /*允许最大上传数，可以多个，当前设置单个*/
        enctype: 'multipart/form-data',
        allowedPreviewTypes: ['image', 'html', 'text', 'video', 'audio', 'flash', 'object', 'other'], //allowedFileTypes: ['image', 'video', 'flash'],
        allowedFileExtensions: ["jpg", "png", "jpeg", "gif", "pdf", "xls", "docx", "xlsx", "doc"], /*上传文件格式*/
        msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
        dropZoneTitle: "请通过拖拽文件放到这里",
        dropZoneClickTitle: "或者点击此区域添加文件",
        uploadExtraData: {"pid": $("#id").val(), "attachid": $("#attachlist").val(), "phaseid": $("#phaselist").val(),"eaudituser":$("#hidEaudiUser").val()},//这个是外带数据
        showBrowse: false,
        browseOnZoneClick: true,
        slugCallback: function (filename) {
            return filename.replace('(', '_').replace(']', '_');
        }
    });

   // $("#seldzfile").val("");

    //上传文件成功，回调函数
    $('#projectfile').on("fileuploaded", function (event, data, previewId, index) {
        if (data.response.success == "success") {
            layer.msg("文件上传成功!");
            //UpdateFilesClassId
            $.post("UpdateFilesClassId", {
                "fids": data.response.fid,
                "classid": $("#seldzfile").val(),
                "eaudituser":$("#eauditUser").val()
            }, function (result) {
                $("#eauditUser").val(-1);
                $("#seldzfile").val(-1);
            });
            $("#savePic").attr("disabled", "disabled");
            $("#savePic1").trigger("click");
            $('.modal').map(function () {
                $(this).modal('hide');
            });
            $(".modal-backdrop").remove();
            refreshFileTable();
        } else {
            layer.msg("文件上传失败，请稍后重试!");
        }

    });

}

$('#savePic').on('click', function () {// 提交图片信息 //
    //先上传文件，然后在回调函数提交表单
    if($("#seldzfile").val()==-1){
        layer.msg("请选择文件密级!");
        return;
    }

    if($("#eauditUser").val()==-1){
        layer.msg("请指定审核人员!");
        return;
    }

    $('#projectfile').fileinput('upload');

});


function EditAttachFile() {
    var selectefid = $("#filetable").bootstrapTable('getSelections');
    if (selectefid == null || selectefid.length <= 0) {
        layer.msg("请选择要编辑的文件!");
        // $('.modal').map(function () {
        //     $(this).modal('hide');
        // });
        $("#closeAddPaperModal").trigger("click");
        return;
    } else {

        $("#paperFileName").val(selectefid[0].fileName);
        $('#paperClassicId').val(selectefid[0].classificlevelId);
        $("#paperzrr").val(selectefid[0].zrr)
        $("#editFileId").val(selectefid[0].id);
        $("#btn_editfile1").trigger("click");

        // $.post("getlallusers", {}, function (result) {
        //
        //     result = JSON.parse(result);
        //     var htmlstr = "";
        //     $.each(result, function (i, item) {
        //         htmlstr += "<option value='" + item.id + "'>" + item.username + "</option>";
        //     })
        //
        //     if (htmlstr != null && htmlstr.length > 0) {
        //         //$("#proLeaders").innerHTML(htmlstr);
        //         debugger;
        //         $("#paperzrr").html("");
        //         $("#jingbanName").append("");
        //         $("#paperzrr").append(htmlstr);
        //         $("#jingbanName").append(htmlstr);
        //         $("#paperzrr option:contains('" + selectefid[0].zrr + "')").attr("selected", true);
        //     }
        // })


    }
}


function ReqDownLoadAttachFile() {
    var selectedObjs = $("#filetable").bootstrapTable('getSelections');
    var ids = [];
    if (selectedObjs == null || selectedObjs.length <= 0) {
        layer.msg("请选择要提交下载请求的文件!");
        return;
    }
    if(!$("#selreqDown").val()){
        layer.msg("请选择下载请求审核人!");
        return;
    }
    debugger;
    var downloadok = [], downloadno = [];

    for (var i = 0; i < selectedObjs.length; i++) {
        if (selectedObjs[i].audit != 1) {
            downloadno.push(selectedObjs[i].id);
        } else {
            downloadok.push(selectedObjs[i].id);
        }
    }

    if (downloadno != null && downloadno.length > 0) {
        layer.msg("请选择【正常】状态的文件提交申请，已通过下载请求的文件不需要重复提交！");
        return;
    }

    $.ajax({
        type: "post",
        url: "ReqFileDownLoad",
        data: {fileId: downloadok.join(","), type: 'DOWNLOAD',daudituser:$("#selreqDown").val()},
        async: false,
        success: function (result) {
            result = JSON.parse(result);
            if (result.auditResult == 0) {
                layer.alert("下载请求已提交审核,请稍后！")

                refreshFileTable();
            } else {
                layer.alert("提交审核失败,请稍后重试！")
            }
        }
    });


}

function DownLoadAttachFile() {
    //audit,1:可下载，-1：上传审核未通过，-2：下载审核未通过，2：上传审核中，3：下载审核中 4：下载审核通过
    var selectedObjs = $("#filetable").bootstrapTable('getSelections');
    var ids = [];
    if (selectedObjs == null || selectedObjs.length <= 0) {
        layer.msg("请选择要下载的文件!");
        return;
    }
    var downloadno = [];

    for (i = 0; i < selectedObjs.length; i++) {
        if (selectedObjs[i].audit != 4) {
            downloadno.push(selectedObjs[i].id);
        }
    }
    //debugger;
    if (downloadno != null && downloadno.length > 0) {
        layer.msg("请选择下载审核通过的文件进行下载！");
        return;
    }

    var form = $("<form></form>").attr("action", "BatchDownProjectFiles").attr("method", "post");
    $.each(selectedObjs, function () {
        form.append($("<input/>").attr("type", "hidden").attr("name", "ids").attr("value", this.id));
    });
    form.append($("<input/>").attr("type", "hidden").attr("name", "proid").attr("value", $("#id").val()));
    form.appendTo('body')
    form.submit().remove();
}

function UploadPaper() {

    if (!$("#paperClassicId").val()) {
        layer.msg("请选择文件密级！");

        return;
    }

    if($("#pauditUser").val()==-1) {
        layer.msg("请指定审核人员！");

        return;
    }

    // if($("#hidProClassLevel").val()<$("#paperClassicId").val()){
    //     layer.msg("文件密级需小于或低于项目的密级！");
    //
    //     return ;
    // }
    if (!$("#paperFileName").val()) {
        layer.msg("请输入文件名称！");

        return;
    }
    if (!$("#paperzrr").val()) {
        layer.msg("请选择责任人！");

        return;
    }
    if (!$("#filingNum").val()) {
        layer.msg("请输入文件归档号！");

        return;
    }

    //find("option:selected").text(),
    $.post("UploadPaper",
        {
            paperFileName: $("#paperFileName").val(),
            paperClassicId: $("#paperClassicId").val(),
            zrr: $("#paperzrr").val(),
            paperproid: $("#paperproid").val(),
            filingNum: $("#filingNum").val(),
            attachid: $("#attachlist").val(),
            pauditUser:$("#pauditUser").val(),
            editFileId: $("#editFileId").val(),
            phaseid: $("#phaselist").val()
        }
        , function (result) {
            result = JSON.parse(result)
            if (result.msg == 'success') {
                layer.msg('保存成功！');
                $("#filetable").bootstrapTable('refresh')
            } else {
                layer.msg('保存失败！');
            }
            $('.modal').map(function () {
                $(this).modal('hide');
            });
            $(".modal-backdrop").remove();
            $("#paperFileName").val('');
            $('#paperClassicId').val('');
            $("#pauditUser").val(-1),
            //$('#paperClassicId option:first').attr("selected", true);
            $('#paperzrr option:first').attr("selected", true);
        });
}

function LoadZrr() {
    $.post("getlallusers", {}, function (result) {

        result = JSON.parse(result);
        var htmlstr = "";
        $.each(result, function (i, item) {
            htmlstr += "<option value='" + item.id + "'>" + item.username + "</option>";
        })

        if (htmlstr != null && htmlstr.length > 0) {
            //$("#proLeaders").innerHTML(htmlstr);
            $("#paperzrr").html("");
            $("#jingbanName").html("");
            $("#paperzrr").append(htmlstr);
            $("#jingbanName").append(htmlstr);
        }
    })
}



