var info = parent.chooseInfo
var isMultipart = false;
var excludeHeaders = {
    "Content-Type":processContentType
}
initTemplate()
function initTemplate(){
    $(".name").text(info.name)
    var url = info.url;
    $(".url-text").text(url.url)
    $('.method').text(url.method)
    $('.url').val(url.url)
    processHeaders(info.headers)
    processParam(info.params);
    $('.send-request').click(sendReq)
    $('.response-type-option').click(resTypeClick)
}

function processHeaders(headers){
    if (headers == null||Object.keys(headers).length == 0){
        $('.header-input').hide()
    }else{
        var headerBody = $('.header-input tbody');
        var rules = {}
        for (let headerName in headers) {
            var headerValue = headers[headerName] == null? "":headers[headerName]
            if (filterHeader(headerName,headerValue))continue;
            headerBody.append($('<tr>\n' +
                '                <td class="header-name is-required"><span class="header-name-text">'+headerName+'</span></td>\n' +
                '                <td class="header-value"><input name="'+headerName+'" type="text" class="form-control" value="'+headerValue+'"></td>\n' +
                '            </tr>'))
            rules[headerName] = {required:true}
        }
        $('.header-input form').validate({
            focusCleanup: true,
            rules:rules,
            errorClass: "error",
            errorPlacement:function (){}
        });
        headerBody.find("tr").slice(1).find("td").css("border","0px")
    }
}

function filterHeader(headerName,headerValue){
    for (let excludeHeader of Object.keys(excludeHeaders)) {
        if (headerName.toLowerCase() == excludeHeader.toLowerCase()){
            excludeHeaders[excludeHeaders](headerValue);
            return true;
        }
    }
    return false;
}

function processParam(params){
    if (params == null||Object.keys(params).length == 0){
        $('.param-input').hide()
    }else{
        var paramBody =  $('.param-input tbody')
        for (let paramName in params) {
            var content = "";
            var param = params[paramName]
            param['name'] = paramName
            if (isMultipart){
                content = processMultipartParam(param)
            }else if (param.type == 'param'){
                content = processDefaultParam(param)
            }else if (param.type == 'json'){
                content = processJsonParam(param)
            }else if (param.type == 'url'){
                content = processMultipartParam(param)
            }
            paramBody.append(content)
        }
        paramBody.find("tr").slice(1).find("td").css("border","0px")
    }
}

function processDefaultParam(param){
    return '<tr >\n' +
        '<td class="param-name"><span class="param-name-text">'+param.name+'</span><br/><span class="param-type">('+param.type+')</span></td>\n' +
        '<td class="param-value"><input type="text" class="form-control" value="'+param.value+'"/></td>\n' +
        '</tr>'
}

function processJsonParam(param){
    return '<tr >\n' +
        '<td class="param-name"><span class="param-name-text">'+param.name+'</span><br/><span class="param-type">('+param.type+')</span></td>\n' +
        '<td class="param-value"><textarea  class="form-control" >'+param.value+'</textarea></td>\n' +
        '</tr>'
}
function processMultipartParam(param){

}

function processContentType(contentType){
    var type = contentType.substring(0,contentType.indexOf('/'));
    if (type.toLowerCase() == 'multipart'){
        isMultipart = true;
    }
}


function sendReq() {
    if (!$('.header-input form').valid())return;
    var ajaxContent = {
        url:$('.url').val(),
        type:info.url.method,
        async:false,
        complete:res
    }
    var headerValues = $('.header-value')
    var headers = {}
    for (let headerValueHtml of headerValues) {
        var headerValue = $(headerValueHtml)
        var input = headerValue.find('.form-control')
        headers[input.attr('name')] = input.val()
    }
    ajaxContent['headers'] = headers;
    var paramValues = $('.param-value')
    var params = {}
    var jsonData = null
    for (let paramValueHtml of paramValues) {
        var paramValue = $(paramValueHtml)
        var input = paramValue.find('.form-control')
        if (input[0].tagName == 'TEXTAREA'){
            jsonData = input.val()
        }else{
            params[paramValue.prev().find('.param-name-text').text()] = input.val()
        }
    }

    if (isMultipart){
        ajaxContent['processData'] = false;
        ajaxContent['Content-Type'] = false
        var formData = new FormData();
        for (let paramName in params) {
            formData.append(paramName,params[paramName])
        }
        ajaxContent['data'] = formData
    }else if (jsonData != null){
        ajaxContent['processData'] = false;
        ajaxContent['headers']['Content-Type'] = 'application/json;charset=utf-8'
        if (Object.keys(params).length != 0){
            var urlParam = ''
            for (let paramName in params) {
                urlParam+= "&"+paramName+"="+params[paramName]
            }
            urlParam = urlParam.substring(1)
            ajaxContent['url'] += '?'+urlParam
        }
        ajaxContent['data'] = jsonData
    }else{
        ajaxContent['data'] = params
    }

    $.ajax(ajaxContent)

}
var responseText = ''
var resType = 'TEXT'
function res(res){
    $('.response-status').show();
    if (res.status == 200){
        $('.response-status').removeClass('alert-danger')
        $('.response-status').addClass('alert-success')
        $('.response-status').text('请求成功')
    }else{
        $('.response-status').removeClass('alert-success')
        $('.response-status').addClass('alert-danger')
        $('.response-status').text('请求失败')
    }
    var contentType = res.getResponseHeader('content-type')
    if (contentType!=null && contentType.substring(contentType.indexOf('/')+1).toLowerCase() == 'json'){
        resType = 'JSON'
        $('.response-type-btn-text').text(resType)
    }
    responseText = res.responseText
    processRes();
}


function processRes(){
    if (resType == 'TEXT'){
        $('.response-value').text(responseText);
    }else if (resType == 'JSON'){
        $('.response-value').jsonViewer(JSON.parse(responseText));
    }else if (resType == 'HTML'){
        $('.response-value').html(responseText)
    }
    $('.response-type-btn-text').text(resType)
}

function resTypeClick(){
    resType = $(this).text();
    processRes()
}

