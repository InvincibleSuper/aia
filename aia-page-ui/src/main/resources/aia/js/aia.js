var pageType = null

var pageTypeIcon = {
    "template":"glyphicon glyphicon-list-alt",
    "api":"glyphicon glyphicon-tasks",
    "plugin":"glyphicon glyphicon-wrench"
}

var allData = {}

/**
 * 初始化
 */
var init;
function aiaInit(){
    $('.aia').hide();
    $('.jumbotron').hide();
    init = setInterval(isScan(),500)

}
function isScan(){
    var scan = scanFlag()
    if (scan){
        clearInterval(init)
        $('.aia').show();
        $('.jumbotron').hide();
        initNav()
        $('.nav-sidebar>li[name=template]').click();
    }else{
        $('.jumbotron').show();
    }
    return isScan;
}

function scanFlag(){
    var res = false;
    $.ajax({
        url: 'info/scan',
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
    $('.nav-sidebar>li').click(function (){
        $('.nav-sidebar>li').removeClass("active");
        $(this).addClass("active");
        var currentPageType = $(this).attr('name');
        if (allData[currentPageType] == null){
            $.ajax({
                url: $(this).attr('url'),
                async:false,
                success: function (data){
                    allData[currentPageType] = data;
                }
            });
        }
        if (pageType != currentPageType){
            pageType = currentPageType;
            initContent();
        }

    })
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

function parseTemplateTreeData(){
    if (treeDataCache[pageType] != null){
        return treeDataCache[pageType]
    }
    var templateInfos = allData[pageType]
    var treeDatas = []
    let count = 0;
    for (let group in templateInfos) {
        var nodes = []
        for (let templateInfo of templateInfos[group]) {
            var url = templateInfo.url
            nodes.push({
                text:getTemplateTreeItemHtml(url.method,url.url,templateInfo.name),
                type:url.method,
                url:url.url,
                unique:pageType+"_"+count++,
                data:templateInfo,
                tabName: templateInfo.name
            })
        }
        treeDatas.push({
            text:group,
            nodes:nodes
        });
    }
    treeDataCache[pageType] = treeDatas
    return treeDatas;
}

function parseApiTreeData(){
    if (treeDataCache[pageType] != null){
        return treeDataCache[pageType]
    }
    var apiInfos = allData[pageType]
    var treeDatas = []
    let count = 0;
    for (let group in apiInfos) {
        var nodes = []
        for (let apiInfo of apiInfos[group]) {
            nodes.push({
                text:getApiTreeItemHtml(apiInfo.url.method,apiInfo.url.url,apiInfo.definition),
                type:apiInfo.url.method,
                url:apiInfo.url.url,
                unique:pageType+"_"+count++,
                data:apiInfo,
                tabName: apiInfo.url.method+' '+apiInfo.url.url
            })
        }
        treeDatas.push({
            text:group,
            nodes:nodes
        });
    }
    treeDataCache[pageType] = treeDatas
    return treeDatas;
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
    var treeData = treeDataParseFun[pageType]();
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
}


function openInvoke(data){
    $('.invoke-content-items>.invoke-content-item').hide();
    $('.invoke-tabs>.nav-tabs>li').removeClass("active");
    if ($('.invoke-content-item[unique="'+data.unique+'"]').length == 0){
        chooseInfo = data.data
        $('.invoke-content-items')
            .append(getFrameHtml(data.unique,data.url));
        $('.invoke-tabs>.nav-tabs')
            .append(getTabHtml(data.unique,data.tabName));
    }

    $('.invoke-tabs>.nav-tabs>li[unique="'+data.unique+'"]').addClass("active");
    $('.invoke-content-items>.invoke-content-item[unique="'+data.unique+'"]').show();
    invokeInit()
}


/**
 * 执行区域模块
 */

var chooseInfo = null;

function invokeInit(){
    $('.invoke>.invoke-tabs>.nav-tabs>li').click(function (){
        $('.invoke>.invoke-tabs>.nav-tabs>li').removeClass("active");
        $(this).addClass("active");
        var unique = $(this).attr('unique')
        $('.invoke-content-items>.invoke-content-item').hide();
        $('.invoke-content-items>.invoke-content-item[unique="'+unique+'"]').show();
    })

    $('.invoke>.invoke-tabs>.nav-tabs>li').hover(function (){
        $('.invoke .tab-close').hide();
        $(this).find('.tab-close').css('display','block');
    },function (){
        $('.invoke .tab-close').hide();
    })
    $(".invoke .tab-close").click(function (){
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

function getTabHtml(unique,tabName){
    var tabHtml = '<li class="active" unique="'+unique+'" ><a href="#"><span class="'+pageTypeIcon[pageType]+'"></span> '+tabName+' <span class="glyphicon glyphicon-remove tab-close"></span></a></li>'
    return tabHtml;
}


function searchData(chooseInfo){

}
aiaInit();