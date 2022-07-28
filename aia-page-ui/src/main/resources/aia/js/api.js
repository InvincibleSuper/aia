
import {gainChooseInfo} from './utils/other-utils.js';
var info = gainChooseInfo();




(function initApi(){
    $(".method").text(info['url']['method']);
    $(".url-text").text(info.url.url)
    $(".definition").html(info.definition == null || info.definition == "" ? "这个请求没有说明":info.definition.replaceAll('\n','<br/>'))
    initHeaders()
    initParams()
    initResultValue()
})()

function initHeaders(){
    if (Object.keys(info.headers).length == 0) {
        $('.header-table').prev().html("")
        $('.header-table').html("<tr><td>无</td></tr>")
    }else{
        $('.header-table').html("")
        for (let headersKey in info.headers) {
            var value = info.headers[headersKey] == null ? "无":info.headers[headersKey]
            $('.header-table').append('<tr><td>'+headersKey+'</td><td>'+value+'</td></tr>')
        }
    }
}

function initParams(){
    if (Object.keys(info.params).length == 0) {
        $('.param-table').prev().html("")
        $('.param-table').html("<tr><td>无</td></tr>")
    }else{
        $('.param-table').html("")
        var params = []
        for (let param of info.params) {
            params.push(param.model)
            params[params.length-1]['contentType'] = param.contentType
        }
        var data = []
        parse(params,"",data)

        var $table = $('#request-table')
        startTree($table,data,[{
            field: 'name',
            title: '名称',
        },{
            field: 'contentType',
            title: '参数类型'
        },{
            field: 'type',
            title: '数据类型'
        },{
            field:'definition',
            title:'说明'
        }]);
    }
}

function startTree($table,data,columns){

    $table.bootstrapTable({
        data: data,
        idField: 'id',
        dataType: 'jsonp',
        columns: columns,

        //在哪一列展开树形
        treeShowField: 'name',
        //指定父id列
        parentIdField: 'pid',
        onResetView: function(data) {
            //console.log('load');
            $table.treegrid({
                // initialState: 'collapsed', // 所有节点都折叠
                initialState: 'expanded',// 所有节点都展开，默认展开
                treeColumn: 0,
                expanderExpandedClass: 'glyphicon glyphicon-chevron-up',  //图标样式
                expanderCollapsedClass: 'glyphicon glyphicon-chevron-down',
                onChange: function() {
                    $table.bootstrapTable('resetWidth');
                }
            });
            //只展开树形的第一级节点
            //$table.treegrid('getRootNodes').treegrid('expand');

        },
    });
}

function parse(params,pid,data){
    if (params==null)return pid;

    var id = parseInt(pid+1);
    for (let i = 0; i < params.length; i++) {
        data.push({
            id:id,
            pid:pid,
            name:params[i].name,
            contentType:params[i].contentType,
            type:params[i].type,
            definition:params[i].definition,
            value:params[i].value
        })

        if (params[i].model != null){
            id= parse(params[i].model.properties,id,data)
        }else if (params[i].component != null){
            params[i].component['name'] = 'item'
            id = parse([params[i].component],id,data)
        }

        id++;
    }
    return id;
}

function initResultValue(){
    if (info.returnValue == null){
        $('.return-table').prev().html("")
        $('.return-table').html("<tr><td>无</td></tr>")
        $('.return-example').parent().hide()
    }else if (!info.returnValue.dataOfPage){
        $('.return-table').prev().html("")
        $('.return-table').html("<tr><td>返回的是页面数据</td></tr>")
        $('.return-example').parent().hide()
    }else{
        $('.return-table').html("")
        var params = [info.returnValue.returnModel]
        var data = []
        parse(params,"",data)
        var $table = $('#response-table')
        startTree($table,data,[{
            field: 'name',
            title: '名称',
        },{
            field: 'type',
            title: '数据类型'
        },{
            field:'definition',
            title:'说明'
        }]);
        $('.return-example').jsonViewer(JSON.parse(info.returnValue.example))
    }
}