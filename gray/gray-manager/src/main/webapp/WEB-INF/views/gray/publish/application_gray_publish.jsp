<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var applicationGrayDataGrid;
    $(function() {
    	applicationGrayOganizationTree = $('#applicationGrayOganizationTree').tree({
            url : '${path }/organization/tree',
            parentField : 'pid',
            lines : true,
            onClick : function(node) {
            	applicationGrayDataGrid.datagrid('clearSelections'); 
            	applicationGrayDataGrid.datagrid('load', {
                    organizationId: node.id
                });
            }
        });
    	
        applicationGrayDataGrid = $('#applicationGrayDataGrid').datagrid({
            url : '${path }/application/dataGrid',
            fit : true,
            striped : true,
            rownumbers : false,
            pagination : true,
            singleSelect : false,
            idField : 'id',
            sortName : 'createTime',
	        sortOrder : 'desc',
            pageSize : 20,
            pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
            columns : [ [ 
            { 
				field:'id',checkbox:true 
			},{
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
                title : '状态',
                field : 'status',
                sortable : true,
                formatter : function(value, row, index) {
                    switch (value) {
                    case 2:
                        return '服务灰度中';
                    case 1:
                        return '系统灰度中';
                    case 0:
                        return '灰度已关闭';
                    }
                }
            }
            , {
                field : 'action',
                title : '操作',
                width : 400,
                formatter : function(value, row, index) {
                    var str = '';
                        <shiro:hasPermission name="/applicationGray/startApplicationGray">
                            str += $.formatString('<a href="javascript:void(0)" class="easyui-linkbutton-start" data-options="plain:true,iconCls:\'fi-graph-pie icon-green\'" onclick="applicationGrayManagerCmdFun(\'{0}\',\'startApplicationGray\',\'开启灰度\');" >开启灰度</a>', row.id);
                        </shiro:hasPermission>
                        <shiro:hasPermission name="/applicationGray/stopApplicationGray">
                            str += $.formatString('<a href="javascript:void(0)" class="easyui-linkbutton-stop" data-options="plain:true,iconCls:\'fi-x-circle icon-red\'" onclick="applicationGrayManagerCmdFun(\'{0}\',\'stopApplicationGray\',\'停止灰度\');" >停止灰度</a>', row.id);
                        </shiro:hasPermission>
                        <shiro:hasPermission name="/applicationGray/reloadApplicationWhitelist">
                            str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                            str += $.formatString('<a href="javascript:void(0)" class="easyui-linkbutton-whitelist" data-options="plain:true,iconCls:\'fi-male-female icon-red\'" onclick="applicationGrayManagerCmdFun(\'{0}\',\'reloadApplicationWhitelist\',\'更新白名单\');">更新白名单</a>', row.id);
                        </shiro:hasPermission>
                        <shiro:hasPermission name="/applicationGray/reloadApplicationStrategy">
                            str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                            str += $.formatString('<a href="javascript:void(0)" class="easyui-linkbutton-strategy" data-options="plain:true,iconCls:\'fi-contrast icon-red\'" onclick="applicationGrayManagerCmdFun(\'{0}\',\'reloadApplicationStrategy\',\'更新策略\');">更新策略</a>', row.id);
                        </shiro:hasPermission>
                    return str;
                }
            }] ],
            onLoadSuccess:function(data){
                $('.easyui-linkbutton-start').linkbutton({text:'开启灰度'});
                $('.easyui-linkbutton-stop').linkbutton({text:'停止灰度'});
                $('.easyui-linkbutton-whitelist').linkbutton({text:'更新白名单'});
                $('.easyui-linkbutton-strategy').linkbutton({text:'更新策略'});
            },
            toolbar : '#applicationGrayToolbar'
        });
    });
    
    function applicationGrayManagerCmdFun(id,cmd,msg){
    	var rows = applicationGrayDataGrid.datagrid('getSelections');
    	var ids = "";
    	for(var i = 0;i < rows.length;i++){
    		ids += rows[i].id + ",";
    	}
    	if(ids == "" && cmd != "closeAllGray"){
    		if(ids == ""){
    			ids = id;
    		}
    		if(ids == ""){
	    		parent.$.messager.alert('提示', "请选中要灰度的系统", 'info');
	    		return ;
	    	}
    	}
    	parent.$.messager.confirm('询问', '您是否确认'+msg+'？', function(b) {
            if (b) {
                 $.post('${path }/applicationGray/managerCmd', {
                     ids : ids,
                     cmd : cmd
                 }, function(result) {
                     if (result.success) {
                     	//parent.$.messager.alert('提示', result.msg, 'info');
                     	parent.$.modalDialog({
                            title : '操作状态',
                            width : 500,
                            height : 300,
                            modal : true,
                            content:result.msg,
                            buttons : [ {
                                text : '关闭',
                                handler : function() {
                                	parent.$.modalDialog.handler.dialog('close');
                                }
                            } ]
                        });
                        applicationGrayDataGrid.datagrid('reload');
                     }
                 }, 'JSON');
            }
        });
    }
    
    function applicationGraySearchFun() {
        applicationGrayDataGrid.datagrid('load', $.serializeObject($('#applicationGraySearchForm')));
    }
    function applicationGrayCleanFun() {
        $('#applicationGraySearchForm input').val('');
        applicationGrayDataGrid.datagrid('load', {});
    }
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="applicationGraySearchForm">
            <table>
                <tr>
                    <th>系统名:</th>
                    <td><input name="name" placeholder="请输入被灰度系统名"/></td>
                    <th>创建人:</th>
                    <td>
                    	<input name="createUser" placeholder="请输入创建人名称"/>
                    </td>
                    <td>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="applicationGraySearchFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-x-circle',plain:true" onclick="applicationGrayCleanFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center',border:true,title:'被灰度系统列表'" >
        <table id="applicationGrayDataGrid" data-options="fit:true,border:false"></table>
    </div>
    <div data-options="region:'west',border:true,split:false,title:'组织机构'"  style="width:150px;overflow: hidden; ">
        <ul id="applicationGrayOganizationTree" style="width:160px;margin: 10px 10px 10px 10px"></ul>
    </div>
</div>
<div id="applicationGrayToolbar" style="display: none;">
    <shiro:hasPermission name="/applicationGray/startApplicationGray">
        <a onclick="applicationGrayAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-refresh icon-green'">批量开启灰度</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="/applicationGray/stopApplicationGray">
        <a onclick="applicationGrayAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-refresh icon-red'">批量停止灰度</a>
    </shiro:hasPermission>
    <%-- <shiro:hasPermission name="/applicationGray/closeAllGray">
        <a onclick="applicationGrayManagerCmdFun('','closeAllGray');" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-minus-circle icon-red'">全部停止灰度</a>
    </shiro:hasPermission> --%>
</div>