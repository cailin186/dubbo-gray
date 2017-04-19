<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#whiteListEditForm').form({
            url : '${path }/whiteList/edit',
            onSubmit : function() {
                progressLoad();
                var isValid = $(this).form('validate');
                if (!isValid) {
                    progressClose();
                }
                return isValid;
            },
            success : function(result) {
                progressClose();
                result = $.parseJSON(result);
                if (result.success) {
                    parent.$.modalDialog.openner_dataGrid.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
                    parent.$.modalDialog.handler.dialog('close');
                } else {
                    parent.$.messager.alert('错误', result.msg, 'error');
                }
            }
        });
        $("#editStatus").val('${entity.status}');
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form id="whiteListEditForm" method="post">
            <table class="grid">
                <tr>
                    <td>所属组</td>
                    <td>
                    <input name="id" type="hidden"  value="${entity.id}">
                    <input class="easyui-combobox" name="groupId" value="${entity.groupId}"  
    					data-options="required:true,valueField:'id',textField:'name',url:'${path}/whiteListGroup/getAllGroup'" />  
	                <td>状态</td>                    <td>
                    <select id="editStatus" name="status" value="${entity.status}" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
                            <option value="1">正常</option>
                            <option value="0">停用</option>
                            <option value="-1">已删除</option>
                    </select>
                    </td>
                </tr>
                <tr>
                    <td>白名单</td>
                    <td colspan="3">
                    	<textarea id="param" rows="2" class="easyui-validatebox" data-options="required:true" cols="65" name="param">${entity.param}</textarea>
                    </td>
                </tr>
                <tr>
                    <td>备注</td>
                    <td colspan="3">
                    	<textarea rows="6" cols="65" name="remark">${entity.remark}</textarea>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>