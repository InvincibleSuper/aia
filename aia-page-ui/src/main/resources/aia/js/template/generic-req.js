var contentGenericReq = function (ajaxContent,contentType,typeParamMap) {
    processUrl(ajaxContent,typeParamMap)
    if ( contentTypeFunMap[contentType] == null){
        contentTypeFunMap['other'](ajaxContent,typeParamMap,contentType)
    }else{
        contentTypeFunMap[contentType](ajaxContent,typeParamMap)
    }
}
export default contentGenericReq;
var contentTypeFunMap = {
    "multipart/form-data":function (ajaxContent,typeParamMap){
        ajaxContent['processData'] = false;
        ajaxContent['contentType'] = false
        delete ajaxContent['headers']['Content-Type']
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
    "other":function (ajaxContent,typeParamMap,contentType){
        ajaxContent['headers']['Content-Type'] = contentType
        if (typeParamMap.param != null && typeParamMap.json==null && typeParamMap.file == null){
            var data = {}
            for (let param of typeParamMap.param) {
                data[param.metadata.name] = param.val
            }
            ajaxContent['data'] = data
        }else{
            processParamAddUrl(ajaxContent,typeParamMap)
            if (typeParamMap.json != null){
                ajaxContent['data'] = typeParamMap.json[0].val
            }else if (typeParamMap.file != null){
                //暂时不处理，多种选择：多部分、二进制流
            }
        }

    }
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



