package ${basePackege}.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xlz.commons.base.mapper.BaseMapper;
import com.xlz.commons.base.service.impl.BaseServiceImpl;
import ${basePackege}.mapper.${className}Mapper;
import ${basePackege}.model.${className};
import ${basePackege}.service.${className}Service;

@Service
public class ${className}ServiceImpl extends BaseServiceImpl<${className}> implements ${className}Service {

	@Autowired
	private ${className}Mapper ${className1}Mapper;
	@Override
	protected BaseMapper<${className}> getDAO() {
		return ${className1}Mapper;
	}
	
}
