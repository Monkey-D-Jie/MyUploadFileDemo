function uploadpic() {
    $("body").mLoading({
        text: "正在上传,请稍后...",//加载文字，默认值：加载中...
        icon: "",//加载图标，默认值：一个小型的base64的gif图片
        html: false,//设置加载内容是否是html格式，默认值是false
        content: "",//忽略icon和text的值，直接在加载框中显示此值
        mask: true//是否显示遮罩效果，默认显示
    });
    $("body").mLoading("show");
    var formData = new FormData();
    formData.append("file", document.getElementById("file").files[0]);
    $.ajax({
        contentType: "multipart/form-data",//必须要
        // url:"/citys/slide/file.do",
        url: " /handleFile/uploadFile",
        type: "POST",
        data: formData,
        contentType: false,
        processData: false,
        success: function (data) {
            $("body").mLoading("hide");//隐藏loading组件
            showPic(data, ".myimg");
            layer.msg("上传任务已经开启...完成后会通知您的噢(#^.^#)");
        },
        error: function () {
            alert("上传失败");
            $("body").mLoading("hide");//隐藏loading组件
        }

    });

}

//多图片上传
function batchuploadpic() {
    $("body").mLoading({
        text: "正在上传,请稍后...",//加载文字，默认值：加载中...
        icon: "",//加载图标，默认值：一个小型的base64的gif图片
        html: false,//设置加载内容是否是html格式，默认值是false
        content: "",//忽略icon和text的值，直接在加载框中显示此值
        mask: true//是否显示遮罩效果，默认显示
    });
    var formData = new FormData();
    var filesObj = document.getElementsByName("files");
    for (var i = 0; i < filesObj.length; i++) {
        if (filesObj[i].files[0] == null) {
            $(".img_info" + (i + 1)).html("  *请选择图片");
            $(".img_info" + (i + 1)).css({color: "red"});
            $("body").mLoading("hide");//隐藏loading组件
            return;
        } else {
            $(".img_info" + (i + 1)).html("");
        }
        formData.append("files", filesObj[i].files[0]);
    }
    $.ajax({
        contentType: "multipart/form-data",//必须要
        // url: "/citys/slide/batchfile.do",
        url: " /handleFile/batchUploadFile",
        type: "POST",
        data: formData,
        /**
         *必须false才会自动加上正确的Content-Type
         */
        contentType: false,
        /**
         * 必须false才会避开jQuery对 formdata 的默认处理
         * XMLHttpRequest会对 formdata 进行正确的处理
         */
        processData: false,
        success: function (data) {
            if (data != "" && data != null) {
                var ids = data.split(",");
                for (var i = 0; i < ids.length; i++) {
                    showPic(ids[i], (".myimg" + (i + 1)));
                }
            }
            $("body").mLoading("hide");//隐藏loading组件
            layer.msg("上传任务已经开启...完成后会通知您的噢(#^.^#)");
        },
        error: function () {
            alert("上传失败");
            $("body").mLoading("hide");//隐藏loading组件
        }
    });
}

//展示头像
function showPic(fileid, img_class) {
    $(img_class).attr("src", "/handleFile/getfile?fileid=" + fileid + "&mas=" + Math.random());
}