<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <meta name="description" content=""/>
    <meta name="author" content=""/>

    <title>知识共享管理平台</title>

    <!-- Bootstrap Core CSS -->
    <link href="static/css/bootstrap.min.css" rel="stylesheet"/>

    <!-- MetisMenu CSS -->
    <link href="static/css/metisMenu.min.css" rel="stylesheet"/>

    <!-- Custom CSS -->
    <link href="static/css/sb-admin-2.css" rel="stylesheet"/>

    <!-- Custom Fonts -->

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="static/js/html5shiv.js"></script>
    <script src="static/js/respond.min.js"></script>
    <![endif]-->

</head>

<body style="background-image: url('static/img/loginbg.jpg')">

<div class="container">
    <div class="row">

        <input id="txtDn" name="txtDn" type="text">
        <input id="txtCardNo" name="txtCardNo" type="text">
    </div>
</div>

    <!-- jQuery -->
    <script src="static/js/jquery.min.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="static/js/bootstrap.min.js"></script>

    <!-- Metis Menu Plugin JavaScript -->
    <script src="static/js/metisMenu.min.js"></script>

    <!-- Custom Theme JavaScript -->
    <script src="static/js/sb-admin-2.js"></script>

    <script type="text/javascript">


        function UniformLogin() {
            $.post("UniformLogin",{},function(result){
                result = JSON.parse(result);
                if(result && (result.msg == 'success'||result.msg[0] == 'success')){
                    alert("dname="+result.dname);
                    alert("cardno="+result.cardno);
                    window.location.href = "index";
                }else {
                    $(".alert-error").removeAttr("style");
                    $("strong").text(result.msg)
                }
            });
        }

        UniformLogin();
        
    </script>

</div>
</body>

</html>
