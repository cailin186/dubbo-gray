<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="${basePackege}.mapper.${className}Mapper">
	
	<sql id="columnListWithoutId" >
		<#list models as model>  
	<#if model_index!=0>
		<#if models?size!=model_index+1>
		${model.column},
		<#else>
		${model.column}
		</#if>
	</#if>
	  	</#list> 
	</sql>
	
	<sql id="columnList" >
	    id , <include refid="columnListWithoutId" />
	</sql>
	
	<resultMap type="${basePackege}.model.${className}" id="result">
		<#list models as model>  
		<result column="${model.column}" property="${model.field}"/>
	  	</#list> 
	</resultMap>
	
	<insert id="save" keyColumn="id" keyProperty="id" useGeneratedKeys="true">
		insert into ${tableName} (	
				<#list models as model>  
			<#if models?size!=model_index+1>
			${model.column},
			<#else>
			${model.column}
			</#if>
			  	</#list> 
			)
		values(
				<#list models as model>  
			<#if models?size!=model_index+1>
			${"#"}{${model.field}},
			<#else>
			${"#"}{${model.field}}
			</#if>
			  	</#list> 
	
		)
	</insert>
	
	<update id="update">
		update 
			${tableName}
		<set>
			<#list models as model>  
			<#if model_index!=0>
			<if test="${model.field} != null">
				${model.column}=${"#"}{${model.field}},
			</if>
			</#if>
			</#list>
		</set>
		where 
			id = ${"#"}{id}
	</update>
	
	<delete id="delete">
		delete from 
			${tableName} 
		where 
			id = ${"#"}{id}
	</delete>
	
	<select id="findById" resultMap="result">
		select 
			<include refid="columnList" />
		from 
			${tableName} 
		where 
			id = ${"#"}{id}
	</select>
	
	<select id="findAll" resultMap="result">
		select 
			<include refid="columnList" />
		from 
			${tableName}  
		<include refid="Common.parseFilterRules"/>
	</select>
	
	<select id="getTotalCount" resultType="int">
        select count(*)
        from 
        	${tableName}
        <include refid="Common.parseFilterRules"/>
    </select>
    
	<select id="findByPage" resultMap="result">
		select 
			<include refid="columnList" />
		from 
			${tableName} 
		<include refid="Common.parseFilterRules"/>
		order by ${"$"}{pageQuery.sort} ${"$"}{pageQuery.order}
		limit ${"#"}{pageQuery.start} , ${"#"}{pageQuery.pageSize}
	</select>
</mapper>