var info = parent.chooseInfo


function initApi(){
    $(".method").text(info.url.method);
    $(".url-text").text(info.url.url)
    $(".definition").html(info.definition == null || info.definition == "" ? "这个请求没有说明":info.definition.replaceAll('\n','<br/>'))
    initHeaders()
    initParams()
    initResultValue()
}

function initHeaders(){
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
}

function initParams(){
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
        parse(params,"",$('.param-table'))
    }
}

function parse(params,prevText,appendNode,flag){
    if (params==null)return "";
    var prefix = "";
    if (prevText.length != 0)prefix = prevText+"└"
    for (let i = 0; i < params.length; i++) {
        var name = prefix+params[i].name
        var contentType = params[i].contentType
        var type = params[i].type
        var definition = params[i].definition
        var value = params[i].value
        if (contentType == null)contentType = "/"
        if (value == null)value=""
        if (definition == null)definition = "无"
        var tr = $('<tr></tr>')
        tr.append($('<td></td>').html(name))
        if (flag == null)tr.append($('<td></td>').text(contentType));
        tr.append($('<td></td>').text(type))
        tr.append($('<td></td>').text(definition))
        appendNode.append(tr)
        var nextText = prevText+"&nbsp&nbsp"

        if (params[i].model != null){
            parse(params[i].model.properties,nextText,appendNode,flag)
        }else if (params[i].containerContent != null){
            var key = params[i].containerContent[0]
            key['name'] = 'key'
            var mapValue = params[i].containerContent[1]
            mapValue['name'] = 'value'
            parse([key],nextText,appendNode,flag)
            parse([mapValue],nextText,appendNode,flag)
        }else if (params[i].component != null){
            params[i].component['name'] = 'item'
            parse([params[i].component],nextText,appendNode,flag)
        }
    }
}

function initResultValue(){
    if (info.returnValue == null){
        $('.return-table').prev().html("")
        $('.return-table').html("<tr><td>无</td></tr>")
        $('.return-example').parent().hide()
    }else if (!info.returnValue.dataOfPage){
        $('.return-table').prev().html("")
        $('.return-table').html("<tr><td>返回的是页面数据</td></tr>")
        $('.return-example').parent().hide()
    }else{
        $('.return-table').html("")
        var params = [info.returnValue.returnModel]
        parse(params,"",$('.return-table'),false)
        $('.return-example').jsonViewer(JSON.parse(info.returnValue.example))
    }
}
initApi()