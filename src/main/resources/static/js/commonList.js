/**
 * Created by souyouyou on 2018/3/30.
 */
function checkMenu(type){
    $.ajax({
        type: "post",
        url: "findMenuByTypeAndParentId",
        data: {type: type, parentId: 0},
        async: false,
        success: function (result) {
            result = JSON.parse(result);

            if (result.result.length == 0){
                layer.msg("请联系系统管理员创建菜单后,再进行相关操作")
                return;
                // $("#page-wrapper").load("menuList/"+type);
            }

        }
    });
}