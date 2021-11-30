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
    let count = 0;
    for (let group in apiInfos) {
        var nodes = []
        for (let apiInfo of apiInfos[group]) {
            nodes.push({
                text:getApiTreeItemHtml(apiInfo.url.method,apiInfo.url.url,apiInfo.definition),
                type:apiInfo.url.method,
                url:apiInfo.url.url,
                unique:pageType+"_"+count++,
                data:apiInfo
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
            .append(getTabHtml(data.unique,data.url,data.type));
    }
    $('.invoke-content-items>.invoke-content-item[unique="'+data.unique+'"]').show();
    $('.invoke-tabs>.nav-tabs>li[unique="'+data.unique+'"]').addClass("active");
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
    })

    $('.invoke>.invoke-tabs>.nav-tabs>li').hover(function (){
        $('.invoke .tab-close').hide();
        $(this).find('.tab-close').show();
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


    var frameHtml ='<iframe class="embed-responsive-item invoke-content-item" src="page/'+pageType+'.html?url='+url+'" unique="'+unique+'"></iframe>';
    return frameHtml;
}

function getTabHtml(unique,url,type){
    var tabHtml = '<li class="active" unique="'+unique+'" url="'+url+'" type="'+type+'"><a href="#"><span class="'+pageTypeIcon[pageType]+'"></span> '+type+' '+url+' <span class="glyphicon glyphicon-remove tab-close"></span></a></li>'
    return tabHtml;
}


function searchData(chooseInfo){

}
