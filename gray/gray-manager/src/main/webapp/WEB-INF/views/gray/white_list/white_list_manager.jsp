<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var whiteListDataGrid;

    $(function() {
        whiteListDataGrid = $('#whiteListDataGrid').datagrid({
            url : '${path }/whiteList/dataGrid',
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
                width : '400',
                title : '白名单',
                field : 'param',
                sortable : true
            },
            {
                width : '60',
                title : '状态',
                field : 'status',
                sortable : true,
                formatter : function(value, row, index) {
                    switch (value) {
                    case -1:
                        return '已删除';
                    case 0:
                        return '停用';
                    case 1:
                        return '正常';
                    }
                }
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
            },
            {
                width : '200',
                title : '备注',
                field : 'remark',
                sortable : false
            }
            , {
                field : 'action',
                title : '操作',
                width : 130,
                formatter : function(value, row, index) {
                    var str = '';
                        <shiro:hasPermission name="/whiteList/edit">
                            str += $.formatString('<a href="javascript:void(0)" class="easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="whiteListEditFun(\'{0}\');" >编辑</a>', row.id);
                        </shiro:hasPermission>
                        <shiro:hasPermission name="/whiteList/delete">
                            str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                            str += $.formatString('<a href="javascript:void(0)" class="easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="whiteListDeleteFun(\'{0}\');" >删除</a>', row.id);
                        </shiro:hasPermission>
                    return str;
                }
            }] ],
            onLoadSuccess:function(data){
                $('.easyui-linkbutton-edit').linkbutton({text:'编辑'});
                $('.easyui-linkbutton-del').linkbutton({text:'删除'});
            },
            toolbar : '#toolbar'
        });
    });
    
    function whiteListAddFun() {
        parent.$.modalDialog({
            title : '添加',
            width : 500,
            height : 300,
            href : '${path }/whiteList/addPage',
            buttons : [ {
                text : '添加',
                handler : function() {
                    parent.$.modalDialog.openner_dataGrid = whiteListDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#whiteListAddForm');
                    f.submit();
                }
            } ]
        });
    }
    
    function whiteListDeleteFun(id) {
        if (id == undefined) {//点击右键菜单才会触发这个
            var rows = whiteListDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {//点击操作里面的删除图标会触发这个
            whiteListDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.messager.confirm('询问', '您是否要删除当前白名单组？', function(b) {
            if (b) {
                 progressLoad();
                 $.post('${path }/whiteList/delete', {
                     id : id
                 }, function(result) {
                     if (result.success) {
                         parent.$.messager.alert('提示', result.msg, 'info');
                         whiteListDataGrid.datagrid('reload');
                     }
                     progressClose();
                 }, 'JSON');
            }
        });
    }
    
    function whiteListEditFun(id) {
        if (id == undefined) {
            var rows = whiteListDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
            whiteListDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.modalDialog({
            title : '编辑',
            width : 500,
            height : 300,
            href : '${path }/whiteList/editPage?id=' + id,
            buttons : [ {
                text : '确定',
                handler : function() {
                    parent.$.modalDialog.openner_dataGrid = whiteListDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#whiteListEditForm');
                    f.submit();
                }
            } ]
        });
    }
    
    function whiteListSearchFun() {
        whiteListDataGrid.datagrid('load', $.serializeObject($('#whiteListSearchForm')));
    }
    function whiteListCleanFun() {
        $('#whiteListSearchForm input').val('');
        whiteListDataGrid.datagrid('load', {});
    }
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="whiteListSearchForm">
            <table>
                <tr>
                    <th>白名单:</th>
                    <td><input name="param" placeholder="请输入白名单"/></td>
                    <th>创建人:</th>
                    <td>
                    	<input name="createUser" placeholder="请输入创建人名称"/>
                    </td>
                    <th>所属组:</th>
                    <td>
    				<input class="easyui-combobox" name="groupId" 
    					data-options="valueField:'id',textField:'name',url:'${path}/whiteListGroup/getAllGroup'" />  
                    </td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="whiteListSearchFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-x-circle',plain:true" onclick="whiteListCleanFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center',border:true,title:'白名单组列表'" >
        <table id="whiteListDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="toolbar" style="display: none;">
    <shiro:hasPermission name="/whiteList/add">
        <a onclick="whiteListAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-plus icon-green'">添加</a>
    </shiro:hasPermission>
</div>