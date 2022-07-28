
import contentGenericReq from './generic-req.js';
export default {

    sendReq :function  (){
        var templateDom = this
        var index = this.getIndex()
        var template = this.template;
        for (let i = 0; i <= index; i++) {
            if (!templateDom.find('.header-input form').valid())return false;
        }

        var ajaxContent = {
            templateDom:templateDom,
            url:templateDom.find('.url').val(),
            type:template.url.method,
            async:false,
            complete:templateDom.processRes
        }
        var headerValues = templateDom.find('.header-value')
        var headers = {}
        for (let headerValueHtml of headerValues) {
            var headerValue = $(headerValueHtml)
            var input = headerValue.find('.form-control')
            headers[input.attr('name')] = input.val()
            template.headers[input.attr('name')] = input.val()
        }
        ajaxContent['headers'] = headers;
        var paramValues = templateDom.find('.param-value')
        var typeParamMap = {}
        for (let paramValueHtml of paramValues) {
            var paramValue = $(paramValueHtml)
            var input = paramValue.find('.form-control')
            var inputType = input.attr('type'),name = paramValue.prev().find('.param-name-text').text();
            var param = {};
            if (inputType == 'file'){
                param.val = input[0].files
            }else{
                param.val = input.val()
                template.params[name].value = param.val
            }
            param.metadata = template.params[name]
            if (typeParamMap[param.metadata.type] == null){
                typeParamMap[param.metadata.type] = []
            }
            typeParamMap[param.metadata.type].push(param)
        }
        processParamValue(templateDom,typeParamMap)
        contentGenericReq(ajaxContent, template.headers["Content-Type"],typeParamMap);
        // ajaxContent.url = baseUrl + ajaxContent.url
        $.ajax(ajaxContent)
        return true;
    },


    processRes:function (res){
        var templateDom = this.templateDom
        var responseStatus = templateDom.find('.response-status');
        responseStatus.show();
        if (res.status == 200){
            responseStatus.removeClass('alert-danger')
            responseStatus.addClass('alert-success')
            responseStatus.text('请求成功')
        }else{
            responseStatus.removeClass('alert-success')
            responseStatus.addClass('alert-danger')
            responseStatus.text('请求失败')
        }
        var contentType = res.getResponseHeader('content-type')
        if (contentType!=null && contentType.substring(contentType.indexOf('/')+1).toLowerCase() == 'json'){
            templateDom.resType = 'JSON'
            templateDom.responseValue = JSON.parse(res.responseText)
            templateDom.find('.response-type-btn-text').text(templateDom.resType)
        }
        templateDom.responseText = res.responseText
        templateDom.processResType();
    },

    processResType: function (){
        if (this.resType == 'TEXT'){
            this.find('.response-value').text(this.responseText);
        }else if (this.resType == 'JSON'){
            this.find('.response-value').jsonViewer(JSON.parse(this.responseText));
        }else if (this.resType == 'HTML'){
            this.find('.response-value').html(this.responseText)
        }
        this.find('.response-type-btn-text').text(this.resType)
    },
    getIndex : function (){
        return this.val()*1;
    }

}

/**
 * 处理请求参数内的表达式
 * 例子：
 * ${#parent.user.name}上一级模板返回值得user属性的name属性的值
 * ${#2.name} 标号为2的模板返回值的name属性
 * ${#parent}上级模板的全部返回值
 * @param templateDom
 * @param typeParamMap
 */
function processParamValue(templateDom,typeParamMap){

    for (let typeParamMapKey in typeParamMap) {
        for (let itemKey in typeParamMap[typeParamMapKey]) {
            var item = typeParamMap[typeParamMapKey][itemKey].val
            if (typeof item !== "string")continue
            var s = ""

            while(true){
                var l = item.indexOf('${'),r = item.indexOf('}',l)
                if (l < r && l != -1){
                    var pattern = item.substring(l+2,r)
                    s += item.substring(0,l)
                    item = item.substring(r+1,item.length)
                    if (pattern == null || pattern.length == 0)continue
                    var patternItems = pattern.split('.')
                    var replaceValue = processPattern(templateDom,patternItems)
                    if (typeParamMapKey == 'json'){
                        replaceValue = JSON.stringify(replaceValue)
                    }
                    s += replaceValue
                }else{
                    s += item
                    break;
                }
            }
            typeParamMap[typeParamMapKey][itemKey].val = s
        }
        for (let item of typeParamMap[typeParamMapKey]) {

        }
    }
}


function processPattern(templateDom,patternItems){

    try {
        var index = 0;
        for (let i = 0; i < patternItems.length; i++) {
            var patternItem = patternItems[i]
            if (patternItem.startsWith("#")){
                var expression = patternItem.substring(1)
                if (expression === 'parent'){
                    templateDom = templateDom.context.get(templateDom.getIndex()-1)
                }else{
                    templateDom = templateDom.context.get(parseInt(expression))
                }
                index = i+1
            }else{
                break;
            }
        }

        var v = templateDom.responseValue
        for (let i = index; i < patternItems.length; i++) {
            var patternItem = patternItems[i]
            if (patternItem.indexOf('[') != -1){
                var arrPatternItems = patternItem.split('[');
                v = v[arrPatternItems[0]]
                for (let j = 1; j < arrPatternItems.length; j++) {
                    v = v[parseInt(arrPatternItems[j].substring(0,arrPatternItems[j].length -1))]
                }
            }else{
                v = v[patternItems[i]]
            }

        }
        if (v == undefined || v == null){
            v = ""
        }
        return v;
    }catch (e){
        console.error(e)
    }
    return ""



}
