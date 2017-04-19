<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#applicationServiceEditForm').form({
            url : '${path }/applicationService/edit',
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
        <form id="applicationServiceEditForm" method="post">
            <table class="grid">
                <tr>
                    <td>服务名</td>
                    <td><input name="name" type="text" placeholder="服务名" class="easyui-validatebox" data-options="required:true" value="${entity.name}"></td>
	                <td>类型</td>
                    <td>
                    	<select id="type" value="${entity.type}" name="type" class="easyui-combobox" data-options="width:140,required:true,editable:false,panelHeight:'auto'">
                             <option value="nginx">nginx</option>
                             <option value="dubbo">dubbo</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>版本</td>
                    <td><input name="version" type="text" placeholder="请输入版本" class="easyui-validatebox" data-options="required:true" value="${entity.version}"></td>
	                <td>灰度参数</td>
                    <td>
                        <input name="param" type="text" placeholder="请输入灰度参数" class="easyui-validatebox" data-options="required:true" value="${entity.param}">
                    </td>
                </tr>
                <tr>
                    <td>灰度策略</td>
                    <td colspan="3">
                    	<input id="serviceStrategy" value="${entity.strategy}" name="strategy_${entity.type}" class="easyui-combobox" 
                    	data-options="width:200,required:true,valueField:'id',textField:'name',url:'${path}/strategy/getStrategyByType'">
                    </td>
                </tr>
                <tr>
                    <td>备注</td>
                    <td colspan="3">
                    	<textarea rows="3" cols="60" name="remark">${entity.remark}</textarea>
                    </td>
                </tr>
            </table>
            <input name="id" type="hidden"  value="${entity.id}">
            <input name="oldname" type="hidden"  value="${entity.name}">
        </form>
    </div>
</div>