$(function () {
    $('#log-out').click(function () {
        //清除session
        $.ajax({
            url:"/myo2o/local/logout",
            type:"post",
            async:false,
            cache:false,
            dataType:'json',
            success:function (data) {
                if(data.success){
                    var usertype = $('#log-out').attr("usertype");
                    //清除成功后退出到登录页面
                    window.location.href="/myo2o/local/login?usertype="+usertype;
                    return false;
                }
            },
            error:function (data,error) {
                alert(error);
            }
        });
    });
})