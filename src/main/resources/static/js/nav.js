/**
 * Created by souyouyou on 2018/2/7.
 */
function tab(val) {
    if (val == 1) {
        $("#page-wrapper").load("ProjectList")
    }
    if (val == 2) {
        $("#page-wrapper").load("dataList")
    }
    if (val == 3) {
        $("#page-wrapper").load("anliList")
    }
    if (val == 4) {
        $("#page-wrapper").load("expertList")
    }
    if (val == 5) {
        $("#page-wrapper").load("announceList")
    }
    if (val == 6) {
        $("#page-wrapper").load("msgGroupList")
    }
    if (val == 7) {
        $("#page-wrapper").load("outBoxList")
    }
    if (val == 8) {
        $("#page-wrapper").load("draftList")
    }
    if (val == 9) {
        $("#page-wrapper").load("memberList")
    }
    if (val == 10) {
        $("#page-wrapper").load("roleList")
        
    }
    if (val == 11) {
        $("#page-wrapper").load("permissionList")
    }
    if (val == 12) {
        $("#page-wrapper").load("CreateProject")
    }
    if (val == 13) {
        $("#page-wrapper").load("KeywordList")
    }
    if (val == 16) {
        $("#page-wrapper").load("logList")
    }

    if (val == 17) {
        $("#page-wrapper").load("GetStorageInfo")
    }

    //法律法规库
    if (val == 18) {
        $("#page-wrapper").load("lowList")
    }


}


function menu(type) {
    $("#page-wrapper").load("menuList/"+type)
}

function review(type) {
    $("#page-wrapper").load("reviewList/"+type)
}

$(document).ready(
    function () {
        $("#page-wrapper").load("main")
    }
)


function qryAuditUser(obj,id) {
    var optionAudit = '<option value="-1">请选择审核人员</option>';
    $.ajax({
        type: "post",
        url: "getAuditByClassify",
        data: {classify: obj.value},
        async: false,
        success: function (result) {
            result = JSON.parse(result);
            if (result && result.length > 0) {
                for (let i = 0; i < result.length; i++) {
                    optionAudit += '<option value="'+result[i].id+'">'+result[i].username+'</option>'
                }

                $('#'+id).html(optionAudit);

            }
        }
    });
}

function qryDownAuditUser(fileId) {
    var optionAudit = '<option value="-1">请选择审核人员</option>';
    $.ajax({
        type: "post",
        url: "getDownAuditByClassify",
        data: {fileId: fileId},
        async: false,
        success: function (result) {
            debugger
            result = JSON.parse(result);
            if (result && result.length > 0) {
                for (let i = 0; i < result.length; i++) {
                    optionAudit += '<option value="'+result[i].id+'">'+result[i].username+'</option>'
                }

            }
        }
    });
    return optionAudit;
}