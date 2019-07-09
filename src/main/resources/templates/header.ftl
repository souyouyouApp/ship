
<nav class="navbar navbar-default navbar-static-top" role="navigation"
     style="margin-bottom: 0; background-image: url('static/img/banner.jpg')">
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="index.html"><span style="color: white; font-weight: bold">知识共享管理平台</span><span style="margin-left: 10px;color: red;">机密&nbsp;★ 版本 V1.0</span></a>
    </div>
    <!-- /.navbar-header -->

    <ul class="nav navbar-top-links navbar-right">
        <li class="dropdown" style="display: inline;float: left;">
            <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                <i class="fa fa-bell fa-fw"></i> <i class="fa fa-caret-down"></i>
            </a>
            <ul class="dropdown-menu dropdown-alerts">


            </ul>
            <!-- /.dropdown-user -->
        </li>
        <!-- /.dropdown -->

        <li class="dropdown" style="display: inline;float: left;">
            <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                <i class="fa fa-user fa-fw"></i> <i class="fa fa-caret-down"></i>
            </a>
            <ul class="dropdown-menu dropdown-user">
                <li><a href="#"><i class="fa fa-user fa-fw"></i> <@shiro.principal property="realName"/></a>
                </li>
                <li><a href="#" onclick='$("#page-wrapper").load("userInfo?uId=<@shiro.principal property="id"/>")'><i class="fa fa-gear fa-fw"></i> 设置</a>
                </li>
                <li class="divider"></li>
                <li><a href="sign_out"><i class="fa fa-sign-out fa-fw"></i> 退出</a>
                </li>
            </ul>
            <!-- /.dropdown-user -->
        </li>
    </ul>
    <!-- /.navbar-top-links -->
    <#include "left.ftl">
    <!-- /.navbar-static-side -->
</nav>
<!-- /.modal -->
<link rel="stylesheet" href="static/css/bootstrap-table.min.css">
<script src="static/js/bootstrap-table.min.js"></script>
<script src="static/js/bootstrap-table-zh-CN.min.js"></script>
<script src="static/js/layer.js"></script>
<script type="text/javascript">
    function getNotify(){

        $.post("auditCnt", {}, function (result) {
            res = JSON.parse(result);
            if (res.totalCnt > 0){
                $('#totalCnt').text(res.totalCnt)
            }
            if (res.proCnt > 0){
                $('#proCnt').text(res.proCnt)
            }
            if (res.dataCnt > 0){
                $('#dataCnt').text(res.dataCnt)
            }
            if (res.anliCnt > 0){
                $('#anliCnt').text(res.anliCnt)
            }
            if (res.ggCnt > 0){
                $('#ggCnt').text(res.ggCnt)
            }
            if (res.flCnt > 0){
                $('#flCnt').text(res.flCnt)
            }
            console.log(result)
        })
        $.post("getNotify", {}, function (result) {

            result = JSON.parse(result);

            if (result.msg == "success" && result.result.length > 0) {
                Messenger().hideAll()
                $.each(result.result,function (index,val) {
                    msg = Messenger({parentLocations: ['.messagener']}).post({
                        maxMessages: 20,
                        hideAfter: 30,
                        message: '<span style="display: none">'+this.id+'</span>'+this.content,
                        showCloseButton: true

                    })
                })
                $('.messenger-close').click(function(){
                    msgId = $(this).parent().find('span')[0].innerText
                    $.post("updateNotify",{id:msgId},function (result) {

                    })
                })
            }
        })
    }
//         $.post("getNotify", {}, function (result) {
//
//             result = JSON.parse(result);
//
//             if (result.msg == "success") {
//
//                 var liStr = "";
//                 if (result.result.length > 0 ){
//                     $('.fa-bell').css('color','red');
//                     $('.fa-bell').text(result.result.length)
//                     $.each( result.result, function( index, val ) {
// //                        <span class="pull-right text-muted small">12 minutes ago</span>
//                         if(index == 0){
//                             liStr += '<div id="'+this.id+'" ><li> <div style="display: inline" onclick="notifyDetail(&quot;'+this.fileName+'&quot;,'+this.fileClassify+',&quot;'+this.content+'&quot;)"> <i class="fa fa-twitter fa-fw"></i><span>'+this.content+'<span> </div>&nbsp;&nbsp;<div style="display: inline;color: red;" onclick="delNotify('+this.id+')">X</div></li></div>'
//                         }else {
//                             liStr += '<div id="'+this.id+'" ><li class="divider"></li> <li> <div style="display: inline" onclick="notifyDetail(&quot;'+this.fileName+'&quot;,'+this.fileClassify+',&quot;'+this.content+'&quot;)"> <i class="fa fa-twitter fa-fw"></i><span>'+this.content+'<span> </div>&nbsp;&nbsp;<div style="display: inline;color: red;" onclick="delNotify('+this.id+')">X</div></li></div>'
//                         }
//
//                     } );
//                     $('.dropdown-alerts').html(liStr);
//                 }else {
//                     liStr += '<li> <a href="#"> <div> <i class="fa fa-twitter fa-fw"></i><b>暂无通知</b> </div></a></li>'
//
//                     $('.dropdown-alerts').html(liStr)
//                     $('.fa-bell').removeAttr("style")
//                     $('.fa-bell').text('')
//                 }
//             }
//         });
    $(document).ready(function(){
         // setInterval(getNotify, 5000);
        getNotify();
    })


    function delNotify(id) {

        layer.confirm('你确定删除该消息？', {
            btn: ['确定','取消'] //按钮
        }, function(){
            $.post("delNotify", {nId:id}, function (result) {
                result = JSON.parse(result);
                if (result.msg == 'success') {
                    layer.msg('删除成功')
                    getNotify()
                }
            });
        });


    }
     function notifyDetail(fileName,fileClassify,content) {

        /**
         * 1 项目文件
         * 2 案例库文件
         * 3 资料库文件
         * 4 专家库
         * 5 公告库
         **/
        var clssify = '';
        switch (fileClassify){
            case 1:
                clssify = '项目文件';
//              $("#page-wrapper").load("ProjectDetail?pid=" + data);
                break;
            case 2:
                clssify = '案例文件';
//              $("#page-wrapper").load("createAnliPage?aid=" + data);
                break;
            case 3:
                clssify = '资料文件';
//              $("#page-wrapper").load("createData?zid=" + data);
                break;
            case 5:
                clssify = '公告文件';
//              $("#page-wrapper").load("createAnnoucePage?aid=" + data);
                break;
        }

        layer.alert(content)
        
    }

</script>