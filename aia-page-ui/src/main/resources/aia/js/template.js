var info = parent.chooseInfo
var filterHeaders ;
var contentGenericReq;
var baseUrl = null;
var templateDoms = newArrayList();

function start(){
    parseChain(info)
    initChainDom()
    InitTemplateRelevanceChainDom();

    $('.response-type-option').click(resTypeClick)
    baseUrl = location.href.substring(0,location.href.lastIndexOf("/aia"))
}

function sendReq(){

    var templateDom = $(this).parent()
    var index = templateDom.val();
    for (let i = 0; i <= index; i++) {
        if (!templateDoms.get(i).sendReq()){
            scrollIntoView(templateDoms.get(i),$('html'));
            return;
        }
    }
    layer.msg('模板链调用完毕', {
        icon: 1,
        time: 1000
    },);
    updateTemplate()
}

function parseChain(info){
    var i = 0;
    for (; info != null; info = info.next) {
        var templateDom = templateDomInit(info,i);
        i++;
        $('.container').append(templateDom)
        templateDoms.add(templateDom)
    }
}



function addTemplate(templateDom,api){
    $.get('../info/templateGenerate',{api:api},function (data){
        if (data == null){
            alert('没有此api')
            return;
        }
        var index = getIndex(templateDom);
        var newTemplateDom = templateDomInit(data,index+1);
        templateDoms.addIndex(getIndex(newTemplateDom),newTemplateDom)
        for (let i = index+2; i < templateDoms.size; i++) {
            templateDoms.get(i).attr('id',i)
            templateDoms.get(i).val(i);
        }
        templateDom.after(newTemplateDom)
        addTemplateAfter(templateDom.template,data)
        initChainDom();
    })
}


function addTemplateAfter(template,addTemplate){
    addTemplate.next = template.next;
    template.next = addTemplate;
    updateTemplate()
}


function updateTemplate(){
    console.log(info)
    $.ajax({
        url:'../info/updateTemplate',
        type:'post',
        data:JSON.stringify(info),
        contentType:'application/json;utf-8'
    })

}






function getIndex(templateDom){
    return templateDom.val()*1;
}


function templateDomInit(template,index){
    var templateDom = $(templateHtml)
    templateDom.template = template;
    templateDom.attr('id',index)
    templateDom.val(index);
    templateDom.find(".name").text(templateDom.template.name)
    var url = templateDom.template.url;
    templateDom.find(".url-text").text(url.url)
    templateDom.find('.method').text(url.method)
    templateDom.find('.url').val(url.url)
    processHeaders(templateDom,templateDom.template.headers)
    processParam(templateDom,templateDom.template.params);
    templateDom.responseText = null
    templateDom.resType = 'TEXT'
    templateButtonInit(templateDom)

    templateDom.sendReq = function (){
        var templateDom = this
        var index = getIndex(templateDom)
        var template = this.template;
        for (let i = 0; i <= index; i++) {
            if (!templateDoms.get(i).find('.header-input form').valid())return false;
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
        contentGenericReq(ajaxContent, template.headers["Content-Type"],typeParamMap);
        ajaxContent.url = baseUrl + ajaxContent.url
        $.ajax(ajaxContent)
        return true;
    }


    templateDom.processRes = function (res){
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
            templateDom.find('.response-type-btn-text').text(templateDom.resType)
        }
        templateDom.responseText = res.responseText
        templateDom.processResType();
    }


    templateDom.processResType = function (){
        if (this.resType == 'TEXT'){
            this.find('.response-value').text(this.responseText);
        }else if (this.resType == 'JSON'){
            this.find('.response-value').jsonViewer(JSON.parse(this.responseText));
        }else if (this.resType == 'HTML'){
            this.find('.response-value').html(this.responseText)
        }
        this.find('.response-type-btn-text').text(this.resType)
    }
    return templateDom
}

function processHeaders(templateDom,headers){
    if (headers == null||Object.keys(headers).length == 0){
        templateDom.find('.header-input').hide()
    }else{
        var headerBody = templateDom.find('.header-input tbody');
        var rules = {}
        for (let headerName in headers) {
            var headerValue = headers[headerName] == null? "":headers[headerName]
            filterHeader(headerName,headerValue)
            headerBody.append($('<tr>\n' +
                '                <td class="header-name is-required"><span class="header-name-text">'+headerName+'</span></td>\n' +
                '                <td class="header-value"><input name="'+headerName+'" type="text" class="form-control" value="'+headerValue+'"></td>\n' +
                '            </tr>'))
            rules[headerName] = {required:true}
        }
        templateDom.find('.header-input form').validate({
            focusCleanup: true,
            rules:rules,
            errorClass: "error",
            errorPlacement:function (){}
        });
        headerBody.find("tr").slice(1).find("td").css("border","0px")
    }
}

function filterHeader(headerName,headerValue){
    for (let excludeHeader of Object.keys(filterHeaders)) {
        if (headerName.toLowerCase() == excludeHeader.toLowerCase()){
            filterHeaders[excludeHeader](headerValue);
            return true;
        }
    }
    return false;
}

function processParam(templateDom,params){
    if (params == null||Object.keys(params).length == 0){
        templateDom.find('.param-input').hide()
    }else{
        var paramBody =  templateDom.find('.param-input tbody')
        for (let paramName in params) {
            var content = "";
            var param = params[paramName]
            param['name'] = paramName
            if (param.type == 'file'){
                content = processFileParam(param)
            }else if (param.type == 'param'){
                content = processDefaultParam(param)
            }else if (param.type == 'json'){
                content = processJsonParam(param)
            }else if (param.type == 'url'){
                content = processDefaultParam(param)
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
function processFileParam(param){
    return '<tr >\n' +
        '<td class="param-name"><span class="param-name-text">'+param.name+'</span><br/><span class="param-type">('+param.type+')</span></td>\n' +
        '<td class="param-value"><input type="file"  class="form-control" /></td>\n' +
        '</tr>'
}




function resTypeClick(){
    var resType = $(this).text();
    var templateDom = $(this).parent().parent().parent().parent().parent()
    var index = templateDom.val();
    templateDoms.get(index).resType = resType;
    templateDoms.get(index).processResType()
}

function chainOperationInit(){
    $('.template-chain-item').hover(function (){
        chooseChaining($(this))
    },function (){
        cancelChooseChaining();
    })


    $('.template-chain-item').click(function (){
        var index = $(this).attr('value')
        scrollIntoView(templateDoms.get(index),$('html'));
    })
}

function chooseChaining(choose){
    choose.find('.template-flow1').addClass('template-flow1-hover')
    choose.find('.template-flow2').addClass('template-flow2-hover')
    choose.find('.template-flow3').addClass('template-flow3-hover')
}

function cancelChooseChaining(){
    $('.template-flow1').removeClass('template-flow1-hover')
    $('.template-flow2').removeClass('template-flow2-hover')
    $('.template-flow3').removeClass('template-flow3-hover')
}

function activeChooseChaining(choose){
    choose.find('.template-flow1').addClass('template-flow1-active')
    choose.find('.template-flow2').addClass('template-flow2-active')
    choose.find('.template-flow3').addClass('template-flow3-active')
}
function cancelActiveChooseChaining(){
    $('.template-flow1').removeClass('template-flow1-active')
    $('.template-flow2').removeClass('template-flow2-active')
    $('.template-flow3').removeClass('template-flow3-active')
}

function initChainDom(){
    var templateChain = $('.template-chain')
    templateChain.html('')
    for (let i = 0; i < templateDoms.size; i++) {
        var templateChainItem = $(templateChainItemHtml)
        templateChain.append(templateChainItem)
        templateChainItem.attr('value',i)
        templateChainItem.find('.template-flow-label').text(i+1)
    }
    chainOperationInit()
    activeChooseChaining($('.template-chain-item[value=0]'))
}

function templateButtonInit(templateDom){
    templateDom.find('.template-plus').click(function (){
        layer.open({
            type: 1,
            skin: 'layui-layer-rim', //加上边框
            area: ['420px', '240px'], //宽高
            title:'选择一个api生成',
            content:$('#api-tree-sample').html(),
            success:function (){
                apiTreeInit(templateDom)
            }
        });
    })
    templateDom.find('.template-minus').click(function (){
        if (templateDoms.size > 1){
            var index = getIndex(templateDom)
            templateDoms.remove(index)
            templateDom.remove()
            for (let i = index; i < templateDoms.size; i++) {
                templateDoms.get(i).val(i)
                templateDoms.get(i).attr('id',i)
            }
            initChainDom()
        }else{
            layer.msg('不能删除最后一个模板', {
                icon: 2,
                time: 1000
            },);
        }
    })
    templateDom.find('.send-request').click(sendReq)
}

function apiTreeInit(templateDom){
    var tree = layui.tree;
    var layer = layui.layer

    var apiData = parent.getAiaData('info/api','api')
    var treeData = [];
    for (let group in apiData) {
        var item = {
            title:group,
            type:0,
            spread:true
        }
        var children = []
        for (let api of apiData[group]) {
            children.push({
                title:api.url.method+' '+api.url.url,
                type:1
            })
        }
        item.children = children;
        treeData.push(item);
    }


    //渲染
    var inst1 = tree.render({
        elem: '.api-tree',
        id:"apiTree",
        showCheckbox:false,
        accordion:true,
        data: treeData,
        click:function (obj){
            if (obj.data.type == 1){
                addTemplate(templateDom,obj.data.title);
                layer.close(layer.index);
            }
        }
    });

}

function InitTemplateRelevanceChainDom(){
    $(document).scroll(function(){
        var current = $(document).scrollTop();
        var height = $(window).height();
        var windows = new Array(templateDoms.size+1)
        for (let i = 0; i < templateDoms.size; i++) {
            windows[i] = templateDoms.get(i).offset().top
        }
        windows[templateDoms.size] = $(document).height();
        var max = 0,index = 0;
        for (let i = 0; i < windows.length-1; i++) {
            if (windows[i+1] < current)continue;
            if (windows[i] > current+height)break;
            var minOccupy = windows[i+1] > current+height?current+height : windows[i+1]
            var c = windows[i] <= current ? minOccupy - current : minOccupy - windows[i];
            if (c > max){
                max = c;
                index = i;
            }
        }
        cancelActiveChooseChaining();
        activeChooseChaining($('.template-chain-item[value='+index+']'))
        $('.template-chain-item[value='+index+']')[0].scrollIntoView();
    });
}
function scrollIntoView(into,container){
    container.animate({
        scrollTop: parseInt(into.offset().top) + "px"
    }, {
        duration: 400,
        easing: "linear"
    });
    return false;
}

var templateHtml = $('.samples #template-sample').html();

var templateChainItemHtml = $('.samples #template-chain-sample').html();