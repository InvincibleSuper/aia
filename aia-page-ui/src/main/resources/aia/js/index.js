
import collections from "./utils/collection.js";

import {storeAiaData, storeChooseInfo} from "./utils/other-utils.js"

var pageType = null

var pageTypeIcon = {
    "template":"glyphicon glyphicon-list-alt",
    "api":"glyphicon glyphicon-tasks",
    "plugin":"glyphicon glyphicon-wrench"
}




var initUrl = {'template':'info/template','api':'info/api','plugin':'info/plugin'}
var allData = {}
var map = {

}

function startData(){
    for (let pageType in initUrl) {
        getAiaData(initUrl[pageType],pageType);
    }
    storeAiaData(allData);
}


/**
 * 初始化定时器
 */
var init;

function isScan(){
    var scan = isComplete()
    if (scan){
        clearInterval(init)
        $('.aia').show();
        $('.jumbotron').hide();
        startData()
        initNav()
        //将url上的历史重现
        var history = gainHistoryPage()
        if (history != null){
            selectItem(history.pageType,history.nodeId)
        }
    }else{
        $('.jumbotron').show();
    }
    return isScan;
}

function isComplete(){
    var res = false;
    $.ajax({
        url: 'info/complete',
        async:false,
        success: function (data){
            res = data;
        }
    });
    return res;
}

/**
 * 侧边栏模块
 */
function initNav(){
    $('.nav-sidebar>li').off('click').click(function (){
        $('.nav-sidebar>li').removeClass("active");
        $(this).addClass("active");
        var currentPageType = $(this).attr('name');
        if (pageType != currentPageType){
            pageType = currentPageType;
            initContent();
        }
    })
    $('.nav-sidebar>li[name=template]').click();
}

function getAiaData(url,currentPageType){
    if (allData[currentPageType] == null){
        $.ajax({
            url: url,
            async:false,
            success: function (data){
                allData[currentPageType] = data;
            }
        });

        map[currentPageType] = collections.newArrayList();
        for (let group in  allData[currentPageType]) {
            for (let info of allData[currentPageType][group]) {
               map[currentPageType].add(info);
            }
        }

    }
    return allData[currentPageType]
}


/**
 * 内容区域
 */

var treeDataParseFun = {
    "template":parseTemplateTreeData,
    "api":parseApiTreeData,
    "plugin":null
}

var treeDataCache = {}


function parseTreeData(){
    if (treeDataCache[pageType] != null){
        return treeDataCache[pageType]
    }
    var infos = map[pageType]
    var treeDatas = []
    var nodesMap = {}
    for (let i = 0; i < infos.size; i++) {
        let info = infos.get(i)
        if (nodesMap[info.group] == null){
            nodesMap[info.group] = [];
        }
        nodesMap[info.group].push(treeDataParseFun[pageType](info,i))
    }
    for (let group in nodesMap) {
        treeDatas.push({
            text:group,
            nodes:nodesMap[group]
        });
    }

    treeDataCache[pageType] = treeDatas
    return treeDatas;
}

function parseTemplateTreeData(templateInfo,index){
    var url = templateInfo.url
    return {
        text:getTemplateTreeItemHtml(url.method,url.url,templateInfo.name),
        type:url.method,
        url:url.url,
        tabName: templateInfo.name,
        pageType:pageType,
        index:index,
        unique:pageType+'_'+index
    }
}

function parseApiTreeData(apiInfo,index){
    return {
        text:getApiTreeItemHtml(apiInfo.url.method,apiInfo.url.url,apiInfo.definition),
        type:apiInfo.url.method,
        url:apiInfo.url.url,
        tabName: apiInfo.url.method+' '+apiInfo.url.url,
        pageType:pageType,
        index:index,
        unique:pageType+'_'+index
    }
}

function getApiTreeItemHtml(type,url,definition){
    var span = definition == null||definition =="" ? "":definition;
    var brIndex = span.indexOf('\n')
    span = brIndex==-1?span:span.substring(0,brIndex)
    return "<div class='content-tree-item content-tree-item-get'></div><div class='content-tree-item'><p>"+type+" "+url+"</p>"+span+"</div>"
}

function getTemplateTreeItemHtml(type,url,name){
    return "<div class='content-tree-item content-tree-item-get'></div><div class='content-tree-item'><p>"+name+"</p></div>"
}

function initContent(){
    var treeData = parseTreeData();
    $('.content-tree').treeview({
        "data":treeData,
        "expandIcon":"glyphicon glyphicon-chevron-down",
        "collapseIcon":"glyphicon glyphicon-chevron-up",
        "borderColor":"white",
        onNodeSelected: function(event, data) {

            if (data.unique != null){
                openInvoke(data)
            }
        }
    })


    //将url上的历史重现
    var history = gainHistoryPage()
    if (history != null && history.pageType == pageType){
        selectItem(history.pageType,history.nodeId)
    }

}


function openInvoke(data){
    $('.invoke-content-items>.invoke-content-item').hide();
    $('.invoke-tabs>.nav-tabs>li').removeClass("active");
    if ($('.invoke-content-item[unique="'+data.unique+'"]').length == 0){
        storeChooseInfo(map[data.pageType].get(data.index))
        $('.invoke-content-items')
            .append(getFrameHtml(data.unique,data.url));
        $('.invoke-tabs>.nav-tabs')
            .append(getTabHtml(data.unique,data.tabName,data.nodeId));
    }

    storeNowPage(data.pageType,data.nodeId);
    $('.invoke-tabs>.nav-tabs>li[unique="'+data.unique+'"]').addClass("active");
    $('.invoke-content-items>.invoke-content-item[unique="'+data.unique+'"]').show();

    invokeInit()
}

function storeNowPage(pageType,nodeId){
    var url = window.location.href
    window.location.href = url.substring(0,url.indexOf('#')) + '#'+pageType+':'+nodeId
}

function gainHistoryPage(){
    var url = window.location.href
    var parseStr = url.substring(url.lastIndexOf('#')+1)
    var parseArr = parseStr.split(':')
    if (parseArr.length != 2)return null;
    return {
        pageType:parseArr[0],
        nodeId:parseInt(parseArr[1])
    }
}

function selectItem(pageType,nodeId){
    $('li[name='+pageType+']').click()
    $('.content-tree').treeview('selectNode',nodeId,{silent:true})
}

/**
 * 执行区域模块
 */
function invokeInit(){
    $('.invoke>.invoke-tabs>.nav-tabs>li').off('click').click(function (){
        $('.invoke>.invoke-tabs>.nav-tabs>li').removeClass("active");
        $(this).addClass("active");
        var unique = $(this).attr('unique')
        $('.invoke-content-items>.invoke-content-item').hide();
        $('.invoke-content-items>.invoke-content-item[unique="'+unique+'"]').show();
        var type = unique.substring(0,unique.indexOf('_'))
        var nodeId = parseInt($(this).attr('nodeid'))
        selectItem(type,nodeId)
    })

    $('.invoke>.invoke-tabs>.nav-tabs>li').off('hover').hover(function (){
        $('.invoke .tab-close').hide();
        $(this).find('.tab-close').css('display','block');
    },function (){
        $('.invoke .tab-close').hide();
    })
    $(".invoke .tab-close").off('click').click(function (){
        var chooseTab = $(this).parent().parent();
        var unique = chooseTab.attr('unique')
        if (chooseTab.attr('class').indexOf("active") != -1){
            if (chooseTab.next().length != 0){
                openInvoke({unique:chooseTab.next().attr('unique')});
            }else if (chooseTab.prev().length != 0){
                openInvoke({unique:chooseTab.prev().attr('unique')});
            }
        }

        $('.invoke-content-items>.invoke-content-item[unique="'+unique+'"]').remove();
        $('.invoke-tabs>.nav-tabs>li[unique="'+unique+'"]').remove();
    })
}


function getFrameHtml(unique,url){
    var frameHtml ='<iframe class="embed-responsive-item invoke-content-item" src="page/'+pageType+'.html" unique="'+unique+'"></iframe>';
    return frameHtml;
}

function getTabHtml(unique,tabName,nodeId){
    var tabHtml = '<li class="active" unique="'+unique+'" nodeid="'+nodeId+'"><a href="#" onclick="return false">' +
        '<span class="'+pageTypeIcon[pageType]+'"></span> '+tabName+' <span class="glyphicon glyphicon-remove tab-close"></span></a></li>'
    return tabHtml;
}


(function aiaInit(){
    $('.aia').hide();
    $('.jumbotron').hide();
    init = setInterval(isScan(),500)
})();

