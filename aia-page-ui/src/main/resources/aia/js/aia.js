var pageType = "template"

var pageTypeIcon = {
    "template":"glyphicon glyphicon-list-alt",
    "api":"glyphicon glyphicon-tasks",
    "plugin":"glyphicon glyphicon-wrench"
}

var allData = {}


/**
 * 侧边栏模块
 */
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


/**
 * 内容区域
 */

var treeDataParseFun = {
    "template":null,
    "api":parseApiTreeData,
    "plugin":null
}

var treeDataCache = {}


function parseApiTreeData(){
    if (treeDataCache[pageType] != null){
        return treeDataCache[pageType]
    }
    var apiInfos = allData[pageType]
    var treeDatas = []
    for (let group in apiInfos) {
        var nodes = []
        for (let apiInfo of apiInfos[group]) {
            nodes.push({
                text:getApiTreeItemHtml(apiInfo.url.method,apiInfo.url.url,apiInfo.definition),
                type:apiInfo.url.method,
                url:apiInfo.url.url,
                requestUrl:apiInfo.url.method+"_"+apiInfo.url.url
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
    var span = definition == null||definition =="" ? "":"<span>"+definition+"</span>";
    return "<div class='content-tree-item content-tree-item-get'></div><div class='content-tree-item'><p>"+type+" "+url+"</p>"+span+"</div>"
}

function initContent(){
    var treeData = treeDataParseFun[pageType]();
    $('.content-tree').treeview({
        "data":treeData,
        "expandIcon":"glyphicon glyphicon-chevron-down",
        "collapseIcon":"glyphicon glyphicon-chevron-up",
        "borderColor":"white",
        onNodeSelected: function(event, data) {
            if (data.requestUrl != null){
                openInvoke(data.requestUrl,data.url,data.type)
            }

        }
    })
}


function openInvoke(requestUrl,url,type){
    $('.invoke-content-items>.invoke-content-item').hide();
    $('.invoke-tabs>.nav-tabs>li').removeClass("active");
    if ($('.invoke-content-item[requestUrl="'+requestUrl+'"]').length == 0){
        $('.invoke-content-items')
            .append(getFrameHtml(requestUrl,url));
        $('.invoke-tabs>.nav-tabs')
            .append(getTabHtml(requestUrl,url,type));
    }
    $('.invoke-content-items>.invoke-content-item[requestUrl="'+requestUrl+'"]').show();
    $('.invoke-tabs>.nav-tabs>li[requestUrl="'+requestUrl+'"]').addClass("active");
    invokeInit()
}


/**
 * 执行区域模块
 */

function invokeInit(){
    $('.invoke>.invoke-tabs>.nav-tabs>li').click(function (){
        $('.invoke>.invoke-tabs>.nav-tabs>li').removeClass("active");
        $(this).addClass("active");
    })

    $('.invoke>.invoke-tabs>.nav-tabs>li').hover(function (){
        $('.invoke .tab-close').hide();
        $(this).find('.tab-close').show();
    },function (){
        $('.invoke .tab-close').hide();
    })
    $(".invoke .tab-close").click(function (){
        var chooseTab = $(this).parent().parent();
        var requestUrl = chooseTab.attr('requestUrl')
        if (chooseTab.attr('class').indexOf("active") != -1){
            if (chooseTab.next().length != 0){
                openInvoke(chooseTab.next().attr('requestUrl'),null,null);
            }else if (chooseTab.prev().length != 0){
                openInvoke(chooseTab.prev().attr('requestUrl'),null,null);
            }
        }

        $('.invoke-content-items>.invoke-content-item[requestUrl="'+requestUrl+'"]').remove();
        $('.invoke-tabs>.nav-tabs>li[requestUrl="'+requestUrl+'"]').remove();
    })
}


function getFrameHtml(requestUrl,url){
    var frameHtml ='<iframe class="embed-responsive-item invoke-content-item" src="page/'+pageType+'.html?url='+url+'" requestUrl="'+requestUrl+'"></iframe>';
    return frameHtml;
}

function getTabHtml(requestUrl,url,type){
    var tabHtml = '<li class="active" requestUrl="'+requestUrl+'" url="'+url+'" type="'+type+'"><a href="#"><span class="'+pageTypeIcon[pageType]+'"></span> '+type+' '+url+' <span class="glyphicon glyphicon-remove tab-close"></span></a></li>'
    return tabHtml;
}
