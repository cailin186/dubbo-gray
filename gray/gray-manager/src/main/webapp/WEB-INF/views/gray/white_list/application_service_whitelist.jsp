<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    var applicationServiceWhiteListDataGrid;
    var selectedWhiteListDataGrid;

    $(function() {
        applicationServiceWhiteListDataGrid = $('#applicationServiceWhiteListDataGrid').datagrid({
            url : '${path }/whiteList/dataGrid',
            fit : true,
            striped : true,
            rownumbers : true,
            pagination : true,
            singleSelect : false,
            idField : 'id',
            sortName : 'createTime',
	        sortOrder : 'desc',
            pageSize : 20,
            pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
            onLoadSuccess: function(data){//加载完毕后获取所有的checkbox遍历
                if (data.rows.length > 0) {
                    //循环判断操作为新增的不能选择
                    for (var i = 0; i < data.rows.length; i++) {
                        //根据operate让某些行不可选
                        if (data.rows[i].status == -1) {
                            $("input[type='checkbox']")[i + 1].disabled = true;
                        }
                    }
                }
            },
            onClickRow: function(rowIndex, rowData){
                //加载完毕后获取所有的checkbox遍历
                $("input[type='checkbox']").each(function(index, el){
                    //如果当前的复选框不可选，则不让其选中
                    if (el.disabled == true) {
                    	applicationServiceWhiteListDataGrid.datagrid('unselectRow', index - 1);
                    }
                })
            },
            onCheckAll: function(rowIndex, rowData){
                //加载完毕后获取所有的checkbox遍历
                $("input[type='checkbox']").each(function(index, el){
                    //如果当前的复选框不可选，则不让其选中
                    if (el.disabled == true) {
                    	applicationServiceWhiteListDataGrid.datagrid('unselectRow', index - 1);
                    }
                })
            },
            columns : [ [ 
			{ 
				field:'id',
				checkbox:true 
			},
            {
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
            },
            {
                width : '200',
                title : '备注',
                field : 'remark',
                sortable : false
            }
            ] ],
            toolbar : '#whiteListToolbar'
        });
        
        selectedWhiteListDataGrid = $('#selectedWhiteListDataGrid').datagrid({
            url : '${path }/whiteList/selectedWhitelistdataGrid?linkId=${parentId}&level=${level}',
            fit : true,
            striped : true,
            rownumbers : true,
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
			},
            {
                width : '400',
                title : '白名单',
                field : 'param',
                sortable : true
            },
            {
                width : '200',
                title : '备注',
                field : 'remark',
                sortable : false
            }
            ] ],
            toolbar : '#selectedWhitelistToolbar'
        });
    });
    function whitelistAddFun() {
    	var rows = applicationServiceWhiteListDataGrid.datagrid('getSelections');
    	var ids = "";
    	for(var i = 0;i < rows.length;i++){
    		ids += rows[i].id + ",";
    	}
    	if(ids == ""){
    		parent.$.messager.alert('提示', "请选中要添加的白明单", 'info');
    		return ;
    	}
    	$.post('${path }/whiteList/whitelistAddFun', {
            ids : ids,
            linkId : '${parentId}',
            level : '${level}'
        }, function(result) {
            if (result.success) {
                //parent.$.messager.alert('提示', result.msg, 'info');
                selectedWhiteListDataGrid.datagrid('reload');
            }
            progressClose();
        }, 'JSON');	
    }
    function whitelistDeleteFun() {
    	var rows = selectedWhiteListDataGrid.datagrid('getSelections');
    	var ids = "";
    	for(var i = 0;i < rows.length;i++){
    		ids += rows[i].id + ",";
    	}
    	if(ids == ""){
    		parent.$.messager.alert('提示', "请选中要添加的白明单", 'info');
    		return ;
    	}
    	$.post('${path }/whiteList/whitelistDelFun', {
            ids : ids,
            linkId : '${parentId}',
            level : '${level}'
        }, function(result) {
            if (result.success) {
                //parent.$.messager.alert('提示', result.msg, 'info');
                selectedWhiteListDataGrid.datagrid('reload');
            }
            progressClose();
        }, 'JSON');	
    }
    function whiteListSearchFun() {
        applicationServiceWhiteListDataGrid.datagrid('load', $.serializeObject($('#whiteListSearchForm')));
    }
    function whiteListCleanFun() {
        $('#whiteListSearchForm input').val('');
        applicationServiceWhiteListDataGrid.datagrid('load', {});
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
    <div region="west" split="true" title="待选择的白名单列表" style="width:700px;">
		<table id="applicationServiceWhiteListDataGrid" data-options="fit:true,border:false"></table>
	</div>
    <div data-options="region:'center',border:true,title:'已选定的白名单'" >
        <table id="selectedWhiteListDataGrid" data-options="fit:true,border:false"></table>
    </div>
</div>
<div id="whiteListToolbar" style="display: none;">
    <a onclick="whitelistAddFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-plus icon-green'">添加所选</a>
</div>
<div id="selectedWhitelistToolbar" style="display: none;">
    <a onclick="whitelistDeleteFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-del icon-red'">删除所选</a>
</div>