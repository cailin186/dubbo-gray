<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var strategyDataGrid;
    $(function() {
        strategyDataGrid = $('#strategyDataGrid').datagrid({
            url : '${path }/strategy/dataGrid',
            fit : true,
            striped : true,
            rownumbers : true,
            pagination : true,
            singleSelect : true,
            idField : 'id',
            sortName : 'createTime',
	        sortOrder : 'desc',
            pageSize : 20,
            pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
            columns : [ [ {
                width : '150',
                title : '策略名',
                field : 'name',
                sortable : false
            },{
                width : '100',
                title : '策略方式',
                field : 'way',
                sortable : true,
                formatter : function(value, row, index) {
                    switch (value) {
                    case 1:
                        return '白名单';
                    case 2:
                        return '小流量';
                    case 3:
                        return 'ip段策略';
                    case 4:
                        return '权重';
                    case 5:
                        return '业务线灰度';
                    case 6:
                        return '正则表达式';
                    }
                }
            },{
                width : '60',
                title : '匹配方向',
                field : 'forwardReverse',
                sortable : true,
                formatter : function(value, row, index) {
                    switch (value) {
                    case 0:
                        return '正向';
                    case 1:
                        return '取反';
                    }
                }
            },{
                width : '230',
                title : '正则表达式',
                field : 'regular',
                sortable : false
            },{
                width : '60',
                title : '小流量',
                field : 'flowTatio',
                sortable : false
            },{
                width : '130',
                title : '创建人',
                field : 'createUser',
                sortable : true
            },{
                width : '130',
                title : '修改人',
                field : 'updateUser',
                sortable : true
            },{
                width : '60',
                title : '状态',
                field : 'status',
                sortable : true,
                formatter : function(value, row, index) {
                    switch (value) {
                    case 1:
                        return '正常';
                    case 0:
                        return '停用';
                    }
                }
            }
            , {
                field : 'action',
                title : '操作',
                width : 130,
                formatter : function(value, row, index) {
                    var str = '';
                        <shiro:hasPermission name="/strategy/edit">
                            str += $.formatString('<a href="javascript:void(0)" class="easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="strategyEditFun(\'{0}\');" >编辑</a>', row.id);
                        </shiro:hasPermission>
                        <shiro:hasPermission name="/strategy/delete">
                            str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                            str += $.formatString('<a href="javascript:void(0)" class="easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="strategyDeleteFun(\'{0}\');" >删除</a>', row.id);
                        </shiro:hasPermission>
                    return str;
                }
            }] ],
            onLoadSuccess:function(data){
                $('.easyui-linkbutton-edit').linkbutton({text:'编辑'});
                $('.easyui-linkbutton-del').linkbutton({text:'删除'});
                //$('.easyui-linkbutton-del').linkbutton({text:'开启灰度'});
                //$('.easyui-linkbutton-del').linkbutton({text:'停止灰度'});
            },
            toolbar : '#strategyToolbar'
        });
    });
    
    function strategyAddFun() {
        parent.$.modalDialog({
            title : '添加',
            width : 500,
            height : 350,
            href : '${path }/strategy/addPage',
            buttons : [ {
                text : '添加',
                handler : function() {
                    parent.$.modalDialog.openner_dataGrid = strategyDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#strategyAddForm');
                    f.submit();
                }
            } ]
        });
    }
    
    function strategyDeleteFun(id) {
        if (id == undefined) {//点击右键菜单才会触发这个
            var rows = strategyDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {//点击操作里面的删除图标会触发这个
            strategyDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.messager.confirm('询问', '您是否要删除当前灰度策略？', function(b) {
            if (b) {
                 progressLoad();
                 $.post('${path }/strategy/delete', {
                     id : id
                 }, function(result) {
                     if (result.success) {
                         parent.$.messager.alert('提示', result.msg, 'info');
                         strategyDataGrid.datagrid('reload');
                     }
                     progressClose();
                 }, 'JSON');
            }
        });
    }
    
    function strategyEditFun(id) {
        if (id == undefined) {
            var rows = strategyDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
            strategyDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.modalDialog({
            title : '编辑',
            width : 500,
            height : 350,
            href : '${path }/strategy/editPage?id=' + id,
            buttons : [ {
                text : '确定',
                handler : function() {
                    parent.$.modalDialog.openner_dataGrid = strategyDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#strategyEditForm');
                    f.submit();
                }
            } ]
        });
    }
    
    function strategySearchFun() {
        strategyDataGrid.datagrid('load', $.serializeObject($('#strategySearchForm')));
    }
    function strategyCleanFun() {
        $('#strategySearchForm input').val('');
        strategyDataGrid.datagrid('load', {});
    }
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="strategySearchForm">
            <table>
                <tr>
                    <th>策略名:</th>
                    <td><input name="name" placeholder="请输入灰度策略名"/></td>
                    <th>创建人:</th>
                    <td>
                    	<input name="createUser" placeholder="请输入创建人名称"/>
                    </td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="strategySearchFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-x-circle',plain:true" onclick="strategyCleanFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center',border:true,title:'灰度策略列表'" >
        <table id="strategyDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="strategyToolbar" style="display: none;">
    <shiro:hasPermission name="/strategy/add">
        <a onclick="strategyAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-plus icon-green'">添加</a>
    </shiro:hasPermission>
</div>