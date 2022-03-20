contentGenericReq = function (ajaxContent,contentType,typeParamMap) {
    processUrl(ajaxContent,typeParamMap)
    contentTypeFunMap[contentType](ajaxContent,typeParamMap)
}
var contentTypeFunMap = {
    "multipart/form-data":function (ajaxContent,typeParamMap){
        ajaxContent['processData'] = false;
        ajaxContent['contentType'] = false
        var formData = new FormData();
        for (let paramType in typeParamMap) {
            for (let param of typeParamMap[paramType]) {
                var paramMetadata = param.metadata
                if (paramType == 'file' && !paramMetadata.array){
                    formData.append(paramMetadata.name,param.val[0])
                }else{
                    formData.append(paramMetadata.name,param.val)
                }
            }
        }
        ajaxContent['data'] = formData
    },
    "application/json":function (ajaxContent,typeParamMap){

        ajaxContent['processData'] = false;
        ajaxContent['headers']['Content-Type'] = 'application/json;charset=utf-8'
        var jsonData = typeParamMap['json'] != null ? typeParamMap.json[0].val:null;
        ajaxContent['data'] = jsonData
        processParamAddUrl(ajaxContent,typeParamMap)
    },
    "application/x-www-form-urlencoded":function (ajaxContent,typeParamMap){
        if (ajaxContent.type == 'GET'){
            processParamAddUrl(ajaxContent,typeParamMap)
        }else if (typeParamMap.param != null){
            var data = {}
            for (let param of typeParamMap.param) {
                data[param.metadata.name] = param.val
            }
            ajaxContent['data'] = data
        }
    },
    "application/json1":function (ajaxContent,typeParamMap){},
    "application/json1":function (ajaxContent,typeParamMap){},
    "application/json1":function (ajaxContent,typeParamMap){},
    "application/json1":function (ajaxContent,typeParamMap){},
}


function processUrl(ajaxContent,typeParamMap){
    if (typeParamMap.url == null)return;
    var url = ajaxContent.url
    for (let param of typeParamMap.url) {
        url = url.replace('{'+param.metadata.name+'}',param.val)
    }
    delete typeParamMap['url'];
    ajaxContent.url = url;
}

function processParamAddUrl(ajaxContent,typeParamMap){
    if (typeParamMap.param == null)return;
    var urlParam = ''
    for (let param of typeParamMap.param) {
        urlParam+= "&"+param.metadata.name+"="+param.val
    }
    urlParam = urlParam.substring(1)
    delete typeParamMap['param'];
    ajaxContent.url = ajaxContent.url + '?' + urlParam
}



