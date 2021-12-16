var info = parent.chooseInfo
$(".method").text(info.url.method);
$(".url-text").text(info.url.url)
$(".definition").text(info.definition == null || info.definition == "" ? "这个请求没有说明":info.definition)
if (Object.keys(info.headers).length == 0) {
    $('.header-table').prev().html("")
    $('.header-table').html("<tr><td>无</td></tr>")
}else{
    $('.header-table').html("")
    for (let headersKey in info.headers) {
        var value = info.headers[headersKey] == null ? "无":info.headers[headersKey]
        $('.header-table').append('<tr><td>'+headersKey+'</td><td>'+value+'</td></tr>')
    }
}

if (Object.keys(info.params).length == 0) {
    $('.param-table').prev().html("")
    $('.param-table').html("<tr><td>无</td></tr>")
}else{
    $('.param-table').html("")
    var params = []
    for (let param of info.params) {
        params.push(param.model)
        params[params.length-1]['contentType'] = param.contentType
    }
    $('.param-table').html(parse(params,""))
}

function parse(params,prevText){
    if (params==null)return "";
    var prefix = "";
    if (prevText.length != 0)prefix = prevText+"└"
    var res = "";
    for (let i = 0; i < params.length; i++) {
        var name = prefix+params[i].name
        var contentType = params[i].contentType
        var value = params[i].value
        var definition = params[i].definition
        if (contentType == null)contentType = "/"
        if (value == null)value =""
        if (definition == null)definition = "无"

        res +='<tr><td>'+name+'</td><td>'+contentType+'</td><td>'+value+'</td><td>'+definition+'</td></tr>'
        if (params[i].model != null){
            res += parse(params[i].model.properties,prevText+"  ")
        }
    }
    return res;
}