var info = parent.chooseInfo
var isMultipart = false;
var excludeHeaders = {
    "Content-Type":processContentType
}
var paramNameList={}
initTemplate()
function initTemplate(){
    $(".name").text(info.name)
    var api = info.api;
    $('.method').text(api.url.method)
    $('.url').val(api.url.url)
    processHeaders(api.headers)
    processParam(api.params);

}

function processHeaders(headers){
    if (headers == null||Object.keys(headers).length == 0){
        $('.header-input').hide()
    }else{
        var headerBody = $('.header-input tbody');
        for (let headerName in headers) {
            var headerValue = headers[headerName] == null? "":headers[headerName]
            if (filterHeader(headerName,headerValue))continue;
            headerBody.append($('<tr>\n' +
                '                <td class="header-name is-required"><span class="header-name-text">'+headerName+' :</span></td>\n' +
                '                <td><input type="text" class="form-control" value="'+headerValue+'"></td>\n' +
                '            </tr>'))
        }
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
        for (let param of params) {
            var content = "";
            if (isMultipart){
                content = processMultipartParam(param)
            }else if (param.contentType == 'param'){
                content = processDefaultParam(param)
            }else if (param.contentType == 'json'){
                content = processJsonParam(param)
            }else if (param.contentType == 'url'){
                content = processMultipartParam(param)
            }
            paramBody.append(content)
        }
        processParamFinish(paramBody)
        paramBody.find("tr").slice(1).find("td").css("border","0px")
    }
}

function processDefaultParam(param){

    var prefix = param.prefix;
    prefix = prefix==null?"":prefix+"."
    var model = param.model
    if (model.model == null){
        paramNameList[prefix+model.name] = model.value;
    }else{
        for (let item of model.model.properties) {
            if (item.model == null){
                paramNameList[prefix+item.name] = item.value;
            }else {
                if (item.model.properties == null) {
                    item.model = model.model
                }
                for (let item1 of item.model.properties) {
                    if (item1.model == null){
                        paramNameList[prefix+item.name+"."+item1.name] = item1.value;
                    }
                }
            }
        }
    }

}

function processJsonParam(param){

}
function processMultipartParam(param){

}
function processParamFinish(body){
    var paramHtml = ''
    for (let key of Object.keys(paramNameList)) {
        paramHtml += '<tr>\n' +
            '                    <td class="param-name"><span class="param-name-text">'+key+' :</span><br/><span class="param-type">(param)</span></td>\n' +
            '                    <td class="param-value"><input type="text" class="form-control" value="'+paramNameList[key]+'"/></td>\n' +
            '                </tr>'
    }
    body.append($(paramHtml))
}
function processContentType(contentType){
    var type = contentType.substring(0,contentType.indexOf('/'));
    if (type.toLowerCase() == 'multipart'){
        isMultipart = true;
    }
}

