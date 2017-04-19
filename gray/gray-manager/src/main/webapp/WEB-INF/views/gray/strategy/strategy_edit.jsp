<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
        $('#strategyEditForm').form({
            url : '${path }/strategy/edit',
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
        $("#way").val('${entity.way}');
        $("#forwardReverse").val('${entity.forwardReverse}');
    });
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form id="strategyEditForm" method="post">
            <table class="grid">
                <tr>
                    <td>策略名</td>
                    <td>
                    <input name="name" type="text"  placeholder="策略名" class="easyui-validatebox" data-options="required:true" value="${entity.name}">
                    </td>
	                <td>状态</td>                    <td>
                    <select id="editStatus" name="status" value="${entity.status}" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
                            <option value="1">正常</option>
                            <option value="0">停用</option>
                    </select>
                    </td>
                </tr>
                <tr>
	                <td>策略方式</td>
                    <td>
                    	<select id="way" name="way" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
                                <option value="1">白名单</option>
                                <option value="2">小流量</option>
                                <option value="3">ip段策略</option>
                                <option value="4">权重</option>
                                <option value="5">业务线灰度</option>
                                <option value="6">正则表达式</option>
                        </select>
                    </td>
                     <td>匹配方向</td>
                    <td>
                    	<select id="forwardReverse" name="forwardReverse" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
                                <option value="0">正向</option>
                                <option value="1">取反</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>正则表达式</td>
                    <td><input name="regular" type="text" placeholder="正则表达式" class="easyui-validatebox" data-options="required:true" value="${entity.regular}"></td>
	                <td>小流量</td>
                    <td>
                    	<input name="flowTatio" type="text" placeholder="小流量" class="easyui-validatebox" data-options="required:true" value="${entity.flowTatio}">
                    </td>
                </tr>
                <tr>
                    <td>备注</td>
                    <td colspan="3">
                    	<textarea rows="7" cols="60" name="remark">${entity.remark}</textarea>
                    </td>
                </tr>
            </table>
            <input name="id" type="hidden"  value="${entity.id}">
            <input name="oldname" type="hidden"  value="${entity.name}">
        </form>
    </div>
</div>