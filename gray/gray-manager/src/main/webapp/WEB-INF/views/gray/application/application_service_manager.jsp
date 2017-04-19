<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var applicationServiceDataGrid;
    var applicationServiceOganizationTree;
    var sId;
    $(function() {
    	applicationServiceOganizationTree = $('#applicationServiceOganizationTree').tree({
            url : '${path }/application/tree',
            parentField : 'pid',
            lines : true,
            onClick : function(node) {
            	if(node.id / 10000000 <= 1){
                	return;
        		}
            	sId = node.id - 10000000;
            	applicationServiceDataGrid.datagrid('load', {
                    applicationId: sId
                });
            }
        });
        applicationServiceDataGrid = $('#applicationServiceDataGrid').datagrid({
            url : '${path }/applicationService/dataGrid',
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
                width : '300',
                title : '服务名',
                field : 'name',
                sortable : false
            },{
                width : '100',
                title : '类型',
                field : 'type',
                sortable : true
            },{
                width : '100',
                title : '版本',
                field : 'version',
                sortable : false
            },{
                width : '130',
                title : '灰度参数',
                field : 'param',
                sortable : true
            },{
                width : '80',
                title : '创建人',
                field : 'createUser',
                sortable : true
            },{
                width : '80',
                title : '修改人',
                field : 'updateUser',
                sortable : true
            }
            , {
                field : 'action',
                title : '操作',
                width : 230,
                formatter : function(value, row, index) {
                    var str = '';
                        <shiro:hasPermission name="/applicationService/edit">
                            str += $.formatString('<a href="javascript:void(0)" class="easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="applicationServiceEditFun(\'{0}\');" >编辑</a>', row.id);
                        </shiro:hasPermission>
                        <shiro:hasPermission name="/applicationService/delete">
                            str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                            str += $.formatString('<a href="javascript:void(0)" class="easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="applicationServiceDeleteFun(\'{0}\');" >删除</a>', row.id);
                        </shiro:hasPermission>
                        <shiro:hasPermission name="/applicationService/whitelistPage">
                            str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                            str += $.formatString('<a href="javascript:void(0)" class="easyui-linkbutton-fi-male-female" data-options="plain:true,iconCls:\'fi-male-female icon-red\'" onclick="applicationServiceWhitelistEditFun(\'{0}\');" >白名单</a>', row.id);
                        </shiro:hasPermission>
                    return str;
                }
            }] ],
            onLoadSuccess:function(data){
                $('.easyui-linkbutton-edit').linkbutton({text:'编辑'});
                $('.easyui-linkbutton-del').linkbutton({text:'删除'});
                $('.easyui-linkbutton-fi-male-female').linkbutton({text:'白名单'});
            },
            toolbar : '#applicationServiceToolbar'
        });
    });
    
    function applicationServiceAddFun() {
    	if(!sId){
    		parent.$.messager.alert('提示', '请先选中要添加服务的系统', 'info');
    		return;
    	}
    	parent.$.modalDialog({
            title : '添加',
            width : 500,
            height : 300,
            href : '${path }/applicationService/addPage?applicationId='+sId,
            buttons : [ {
                text : '添加',
                handler : function() {
                    parent.$.modalDialog.openner_dataGrid = applicationServiceDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#applicationServiceAddForm');
                    f.submit();
                }
            } ]
        });
    }
    
    function applicationServiceDeleteFun(id) {
        if (id == undefined) {//点击右键菜单才会触发这个
            var rows = applicationServiceDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {//点击操作里面的删除图标会触发这个
            applicationServiceDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.messager.confirm('询问', '您是否要删除当前被灰度服务？', function(b) {
            if (b) {
                 progressLoad();
                 $.post('${path }/applicationService/delete', {
                     id : id
                 }, function(result) {
                     if (result.success) {
                         parent.$.messager.alert('提示', result.msg, 'info');
                         applicationServiceDataGrid.datagrid('reload');
                     }
                     progressClose();
                 }, 'JSON');
            }
        });
    }
    
    function applicationServiceEditFun(id) {
        if (id == undefined) {
            var rows = applicationServiceDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
            applicationServiceDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.modalDialog({
            title : '编辑',
            width : 500,
            height : 300,
            href : '${path }/applicationService/editPage?id=' + id,
            buttons : [ {
                text : '确定',
                handler : function() {
                    parent.$.modalDialog.openner_dataGrid = applicationServiceDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#applicationServiceEditForm');
                    f.submit();
                }
            } ]
        });
    }
    
    function applicationServiceWhitelistEditFun(id) {
        if (id == undefined) {
            var rows = applicationServiceDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
            applicationServiceDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.modalDialog({
            title : '编辑',
            width : 1200,
            height : 500,
            href : '${path }/whiteList/whitelistPage?level=service&id=' + id,
            buttons : [ {
            	text : '关闭',
                handler : function() {
                	parent.$.modalDialog.handler.dialog('close');
                }
            } ]
        });
    }
    
    function applicationServiceServiceEditFun(id) {
        if (id == undefined) {
            var rows = applicationServiceDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
            applicationServiceDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.modalDialog({
            title : '编辑',
            width : 1200,
            height : 500,
            href : '${path }/applicationService/manager?id=' + id,
            buttons : [ {
                text : '确定',
                handler : function() {
                    parent.$.modalDialog.openner_dataGrid = applicationServiceDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#applicationServiceStrategyForm');
                    f.submit();
                }
            } ]
        });
    }
    
    function applicationServiceSearchFun() {
        applicationServiceDataGrid.datagrid('load', $.serializeObject($('#applicationServiceSearchForm')));
    }
    function applicationServiceCleanFun() {
        $('#applicationServiceSearchForm input').val('');
        applicationServiceDataGrid.datagrid('load', {});
    }
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="applicationServiceSearchForm">
            <table>
                <tr>
                    <th>服务名:</th>
                    <td><input name="name" placeholder="请输入被灰度服务名"/></td>
                    <th>创建人:</th>
                    <td>
                    	<input name="createUser" placeholder="请输入创建人名称"/>
                    </td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="applicationServiceSearchFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-x-circle',plain:true" onclick="applicationServiceCleanFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center',border:true,title:'被灰度服务列表'" >
        <table id="applicationServiceDataGrid" data-options="fit:true,border:false"></table>
    </div>
    <div data-options="region:'west',border:true,split:false,title:'组织机构'"  style="width:150px;overflow: hidden; ">
        <ul id="applicationServiceOganizationTree" style="width:160px;margin: 10px 10px 10px 10px"></ul>
    </div>
</div>
<div id="applicationServiceToolbar" style="display: none;">
    <shiro:hasPermission name="/applicationService/add">
        <a onclick="applicationServiceAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-plus icon-green'">添加</a>
    </shiro:hasPermission>
</div>