<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var applicationDataGrid;
    var applicationOganizationTree;
    $(function() {
    	applicationOganizationTree = $('#applicationOganizationTree').tree({
            url : '${path }/organization/tree',
            parentField : 'pid',
            lines : true,
            onClick : function(node) {
            	applicationDataGrid.datagrid('load', {
                    organizationId: node.id
                });
            }
        });
    	
        applicationDataGrid = $('#applicationDataGrid').datagrid({
            url : '${path }/application/dataGrid',
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
                width : '80',
                title : '系统名',
                field : 'name',
                sortable : false
            },{
                width : '100',
                title : '唯一标识',
                field : 'applicationId',
                sortable : true
            },{
                width : '230',
                title : '指定灰度机',
                field : 'grayIp',
                sortable : false
            },{
                width : '130',
                title : 'nginx中的upstream',
                field : 'upstream',
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
                width : 150,
                formatter : function(value, row, index) {
                    var str = '';
                        <shiro:hasPermission name="/application/edit">
                            str += $.formatString('<a href="javascript:void(0)" class="easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="applicationEditFun(\'{0}\');" >编辑</a>', row.id);
                        </shiro:hasPermission>
                        <shiro:hasPermission name="/application/delete">
                            //str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                            //str += $.formatString('<a href="javascript:void(0)" class="easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="applicationDeleteFun(\'{0}\');" >删除</a>', row.id);
                        </shiro:hasPermission>
                        <shiro:hasPermission name="/application/whitelistPage">
                            str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                            str += $.formatString('<a href="javascript:void(0)" class="easyui-linkbutton-fi-male-female" data-options="plain:true,iconCls:\'fi-male-female icon-red\'" onclick="applicationWhitelistEditFun(\'{0}\');" >白名单</a>', row.id);
                        </shiro:hasPermission>
                    return str;
                }
            }] ],
            onLoadSuccess:function(data){
                $('.easyui-linkbutton-edit').linkbutton({text:'编辑'});
                //$('.easyui-linkbutton-del').linkbutton({text:'删除'});
                $('.easyui-linkbutton-fi-male-female').linkbutton({text:'白名单'});
            },
            toolbar : '#applicationToolbar'
        });
    });
    
    function applicationAddFun() {
        parent.$.modalDialog({
            title : '添加',
            width : 500,
            height : 470,
            href : '${path }/application/addPage',
            buttons : [ {
                text : '添加',
                handler : function() {
                    parent.$.modalDialog.openner_dataGrid = applicationDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#applicationAddForm');
                    f.submit();
                }
            } ]
        });
    }
    
    function applicationDeleteFun(id) {
        if (id == undefined) {//点击右键菜单才会触发这个
            var rows = applicationDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {//点击操作里面的删除图标会触发这个
            applicationDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.messager.confirm('询问', '您是否要删除当前被灰度系统？', function(b) {
            if (b) {
                 progressLoad();
                 $.post('${path }/application/delete', {
                     id : id
                 }, function(result) {
                     if (result.success) {
                         parent.$.messager.alert('提示', result.msg, 'info');
                         applicationDataGrid.datagrid('reload');
                     }
                     progressClose();
                 }, 'JSON');
            }
        });
    }
    
    function applicationEditFun(id) {
        if (id == undefined) {
            var rows = applicationDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
            applicationDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.modalDialog({
            title : '编辑',
            width : 500,
            height : 470,
            href : '${path }/application/editPage?id=' + id,
            buttons : [ {
                text : '确定',
                handler : function() {
                    parent.$.modalDialog.openner_dataGrid = applicationDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#applicationEditForm');
                    f.submit();
                }
            } ]
        });
    }
    
    function applicationWhitelistEditFun(id) {
        if (id == undefined) {
            var rows = applicationDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
            applicationDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.modalDialog({
            title : '编辑',
            width : 1200,
            height : 500,
            href : '${path }/whiteList/whitelistPage?level=application&id=' + id,
            buttons : [ {
            	text : '关闭',
                handler : function() {
                	parent.$.modalDialog.handler.dialog('close');
                }
            } ]
        });
    }
    
    function applicationSearchFun() {
        applicationDataGrid.datagrid('load', $.serializeObject($('#applicationSearchForm')));
    }
    function applicationCleanFun() {
        $('#applicationSearchForm input').val('');
        applicationDataGrid.datagrid('load', {});
    }
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="applicationSearchForm">
            <table>
                <tr>
                    <th>系统名:</th>
                    <td><input name="name" placeholder="请输入被灰度系统名"/></td>
                    <th>创建人:</th>
                    <td>
                    	<input name="createUser" placeholder="请输入创建人名称"/>
                    </td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="applicationSearchFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-x-circle',plain:true" onclick="applicationCleanFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center',border:true,title:'被灰度系统列表'" >
        <table id="applicationDataGrid" data-options="fit:true,border:false"></table>
    </div>
    <div data-options="region:'west',border:true,split:false,title:'组织机构'"  style="width:150px;overflow: hidden; ">
        <ul id="applicationOganizationTree" style="width:160px;margin: 10px 10px 10px 10px"></ul>
    </div>
</div>
<div id="applicationToolbar" style="display: none;">
    <shiro:hasPermission name="/application/add">
        <a onclick="applicationAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-plus icon-green'">添加</a>
    </shiro:hasPermission>
</div>