function ProjectValid() {

    if (!$("#proName").val()) {
        layer.msg("项目名称不能为空");
        return false;
    }


    if (!$("#classificlevelId").val() || $("#classificlevelId").val() == -1) {
        layer.msg("密级不能为空");
        return false;
    }

    if (!$("#proNo").val()) {
        layer.msg("项目编号不能为空");
        return false;
    }

    if (!$("#proFrom").val()) {
        layer.msg("项目来源不能为空");
        return false;
    }

    if (!$("#proLeaders").val() || $("#proLeaders").val() == -1) {
        layer.msg("项目负责人不能为空");
        return false;
    }

    if (!$("#proJoiners").val() || $("#proJoiners").val() == -1) {
        layer.msg("项目参与人不能为空");
        return false;
    }


    if (!$("#proJoiners").val()) {
        layer.msg("项目参与者不能为空");
        return false;
    }

    if (!$("#mainDepartment").val()) {
        layer.msg("乘研单位不能为空");
        return false;
    }

    if (!$("#createPhasetime").val()) {
        layer.msg("立项时间不能为空");
        return false;
    }

    if (!$("#cpAlertdays").val()) {
        layer.msg("立项提前通知时间不能为空");
        return false;
    }

    if (!$("#openPhasetime").val()) {
        layer.msg("开题时间不能为空");
        return false;
    }

    if (!$("#opAlertdays").val()) {
        layer.msg("开题提前通知时间不能为空");
        return false;
    }

    if (!$("#midcheckPhasetime").val()) {
        layer.msg("中期检查时间不能为空");
        return false;
    }

    if (!$("#mpAlertdays").val()) {
        layer.msg("中期检查提前通知时间不能为空");
        return false;
    }

    if (!$("#closePhasetime").val()) {
        layer.msg("结题时间不能为空");
        return false;
    }

    if (!$("#closepAlertdays").val()) {
        layer.msg("结题提前通知时间不能为空");
        return false;
    }
    if (!$("#endPhasetime").val()) {
        layer.msg("验收时间不能为空");
        return false;
    }

    if (!$("#epAlertdays").val()) {
        layer.msg("验收提前通知时间不能为空");
        return false;
    }

    if (!$("#totalFee").val()) {
        layer.msg("总经费不能为空");
        return false;
    }

    if (!$("#isreportReward").val() || $("#isreportReward").val() == -1) {
        layer.msg("是否报奖不能为空");
        return false;
    }

    if (!$("#reportChannel").val() || $("#reportChannel").val() == -1) {
        layer.msg("报奖渠道不能为空");
        return false;
    }

    if (!$("#proPhase").val() || $("#proPhase").val() == -1) {
        layer.msg("是否立项不能为空");
        return false;
    }



    // if(!$("#proauditUser").val()||$("#proauditUser").val()==-1){
    //     layer.msg("请选择此项目的审核人员！");
    //     return false;
    // }

    return true;
}