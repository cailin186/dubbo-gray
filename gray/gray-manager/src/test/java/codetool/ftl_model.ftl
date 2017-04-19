package ${basePackege}.model;

import com.xlz.commons.base.model.BaseDomain;

public class ${className} extends BaseDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	<#list models as model>  
	/** ${model.comment} */
	private ${model.javaType} ${model.field};  
  	</#list> 
  	
	<#list models as model>  
	public ${model.javaType} get${model.firstUpfield}() {
		return ${model.field};
	}

	public void set${model.firstUpfield}(${model.javaType} ${model.field}) {
		this.${model.field} = ${model.field};
	}
  	</#list> 

}
