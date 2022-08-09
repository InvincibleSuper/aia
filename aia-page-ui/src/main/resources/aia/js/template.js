
import tpUtils from './template/template-dom-function.js';
import filterHeader from './template/header-filter.js';
import collections from './utils/collection.js';
import {gainAiaDataByType, gainChooseInfo} from './utils/other-utils.js';



var info
var baseUrl = null;
var templateDoms = collections.newArrayList();
var templateHtml = $('.samples #template-sample').html();
var templateChainItemHtml = $('.samples #template-chain-sample').html();

(function start(){
    info = gainChooseInfo();
    parseChain(info)
    initChainDom()
    initTemplateRelevanceChainDom();

    $('.response-type-option').click(resTypeClick)
    baseUrl = location.href.substring(0,location.href.lastIndexOf("/aia"))
})()

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
        var index = templateDom.getIndex();
        var newTemplateDom = templateDomInit(data,index+1);
        templateDoms.addIndex(newTemplateDom.getIndex(),newTemplateDom)
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
    if (template == null){
        info = addTemplate;
    }else{
        addTemplate.next = template.next;
        template.next = addTemplate;
    }
    updateTemplate()
}

function minusTemplate(template,minusTemplate){
    if (template == null){
        info = minusTemplate.next;
    }else{
        template.next = minusTemplate.next;
    }
    updateTemplate()
}



function updateTemplate(){
    $.ajax({
        url:'../info/updateTemplate',
        type:'post',
        data:JSON.stringify(info),
        contentType:'application/json;utf-8'
    })

}









function templateDomInit(template,index){
    var templateDom = $(templateHtml)
    templateDom.context = templateDoms
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
    Object.assign(templateDom,tpUtils)
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
        if (templateDom.getIndex() != 0){
            var index = templateDom.getIndex();
            minusTemplate(templateDoms.get(index-1).template,templateDom.template)
            templateDoms.remove(index)
            templateDom.remove()
            for (let i = index; i < templateDoms.size; i++) {
                templateDoms.get(i).val(i)
                templateDoms.get(i).attr('id',i)
            }
            initChainDom()

        }else{
            layer.msg('不能删除第一个模板', {
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

    var apiData = gainAiaDataByType("api")
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

function initTemplateRelevanceChainDom(){
    $(document).scroll(function(){
        //当前滚动的距离
        var current = document.documentElement.scrollTop;
        //页面的总高度
        var offsetHeight  = document.documentElement.offsetHeight
        //页面的可视高度
        var clientHeight = document.documentElement.clientHeight
        //offsetHeight - clientHeight得出的就是滚动条最大滚动的长度。
        //跟当前滚动距离相比较，相等则直接高亮最后的锚点
        var index = 0;
        if (current == offsetHeight - clientHeight){
            index = templateDoms.size-1
        }else{
            var min = offsetHeight;
            for (let i = 0; i < templateDoms.size; i++) {
                var diff = Math.abs(templateDoms.get(i).offset().top - current);
                if (min >= diff){
                    min = diff;
                    index = i;
                }
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

