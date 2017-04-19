<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript">
    $(function() {
    	$('#applicationAddOrganizationId').combotree({
            url : '${path }/organization/tree',
            parentField : 'pid',
            lines : true,
            panelHeight : 'auto'
        });
        $('#applicationAddForm').form({
            url : '${path}/application/add',
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
                    parent.$.messager.alert('提示', result.msg, 'warning');
                }
            }
        });
    });
    function strategyClickFun(obj){
    	var flag = obj.checked;
    	$("#strategy_"+obj.value).combobox({required:flag,disabled:!flag});
    	$("#gray_"+obj.value).textbox({required:flag,disabled:!flag});
    	$("#deploy_"+obj.value).textbox({required:flag,disabled:!flag});
    }
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
        <form id="applicationAddForm" method="post">
            <table class="grid">
                <tr>
                    <td>系统名</td>
                    <td><input name="name" type="text" placeholder="请输入组名称" class="easyui-validatebox" data-options="required:true" value=""></td>
	                <td>唯一标识</td>
                    <td>
                        <input name="applicationId" type="text" placeholder="请输入唯一标识" class="easyui-validatebox" data-options="required:true" value="">
                    </td>
                </tr>
                <tr>
                    <td>部门</td>
                    <td><select id="applicationAddOrganizationId" style="width: 125px; " name="organizationId" class="easyui-validatebox" data-options="required:true"></select></td>
                    <td>灰度参数</td>
                    <td>
                        <input name="param" type="text" placeholder="请输入灰度参数" class="easyui-validatebox" data-options="required:true" value="">
                    </td>
                </tr>
                <tr>
                	<td>激活引擎</td>
                    <td colspan="3">
                    	<input type="checkbox" onclick="strategyClickFun(this)" class="easyui-validatebox"  value="nginx">nginx
                    	<input type="checkbox" onclick="strategyClickFun(this)" class="easyui-validatebox"  value="dubbo">dubbo
                    </td>
                </tr>
                <tr>
                    <td>Nginx策略</td>
                    <td><input id="strategy_nginx" name="strategy_nginx" class="easyui-combobox" 
                    	data-options="disabled:true,required:false,valueField:'id',textField:'name',url:'${path}/strategy/getStrategyByType'"></td>
	                <td>Dubbo策略</td>
                    <td>
                    	<input id="strategy_dubbo" name="strategy_dubbo" class="easyui-combobox"
                    	data-options="disabled:true,required:false,valueField:'id',textField:'name',url:'${path}/strategy/getStrategyByType'">
                    </td>
                </tr>
                <tr>
                    <td>http</td>
                    <td colspan="3">
						<input id="gray_nginx" name="nginx_gray"  class="easyui-textbox" data-options="disabled:true,required:false,multiline:true,prompt:'请输入指定灰度机'" value="" style="width:190px;height:48px">
						<input id="deploy_nginx" name="nginx_deploy"  class="easyui-textbox" data-options="disabled:true,required:false,multiline:true,prompt:'请输入正常应用'" value="" style="width:190px;height:48px">
                    </td>
                </tr>
                <tr>
                    <td>dubbo</td>
                    <td colspan="3">
                    	<input id="gray_dubbo" name="gray_dubbo"  class="easyui-textbox" data-options="disabled:true,required:false,multiline:true,prompt:'请输入指定灰度机'" value="" style="width:190px;height:48px">
						<input id="deploy_dubbo" name="deploy_dubbo"  class="easyui-textbox" data-options="disabled:true,required:false,multiline:true,prompt:'请输入正常应用'" value="" style="width:190px;height:48px">
                    </td>
                </tr>
                <tr>
                    <td>Nginx</td>
                    <td colspan="3">
                    	<textarea rows="2" cols="60" name="nginxs"></textarea>
                    </td>
                </tr>
                <tr>
                    <td>备注</td>
                    <td colspan="3">
                    	<textarea rows="2" cols="60" name="remark"></textarea>
                    </td>
                </tr>
            </table>
            <input name="status" type="hidden"  value="0">
        </form>
    </div>
</div>