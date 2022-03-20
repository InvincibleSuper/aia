filterHeaders = {
    "Content-Type":function (contentType){
        if (contentType == 'no-support'){
            alert('不支持的Content-type，无法调用')
        }
    }
}