package codetool;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 简单的根据table生成java字段，mybatis的resultMap等,一个一个的敲也是个巨大工作量啊。。。。
 * 
 * 映射规则，将有下划线的列名转换成驼峰结构，如user_name->userName
 * 
 * @author: zhangll
 * @date: 
 */
public class GenerateCodeFromTable {
	
	public static final String DB_NAME = "gray";
	/*public static final String MODEL_PRIFIX = "WhiteList";
	public static final String TABLE_NAME = "gray_white_list";
	public static final String MODEL_PRIFIX = "Application";
	public static final String TABLE_NAME = "gray_system_manager";
	public static final String MODEL_PRIFIX = "Strategy";
	public static final String TABLE_NAME = "gray_strategy";
	public static final String MODEL_PRIFIX = "SystemService";
	public static final String TABLE_NAME = "gray_system_service";
	public static final String MODEL_PRIFIX = "WhiteListGroup";
	public static final String TABLE_NAME = "gray_white_list_group";
	public static final String MODEL_PRIFIX = "ApplicationNginx";
	public static final String TABLE_NAME = "gray_system_manager_nginx";
	public static final String MODEL_PRIFIX = "SystemStrategy";
	public static final String TABLE_NAME = "gray_system_strategy";
	public static final String MODEL_PRIFIX = "SystemWhitelist";
	public static final String TABLE_NAME = "gray_system_white_list";
	public static final String MODEL_PRIFIX = "ApplicationInstance";
	public static final String TABLE_NAME = "gray_application_instance";
	public static final String MODEL_PRIFIX = "Organization";
	public static final String TABLE_NAME = "organization";*/
	public static final String MODEL_PRIFIX = "User";
	public static final String TABLE_NAME = "user";
	
	public static final String BASE_PACKAGE = "com.xlz.admin";
	
	public static final String BASE_PATH = "D:/BaiduYunDownload/gray/gray-manager/src/main/java/com/xlz/admin/";
	public static final String MAPPER_BASE_PATH = "D:/BaiduYunDownload/gray/gray-manager/src/main/resources/sqlMapperXml/admin/";
	
	public static final List<String> fields = new ArrayList<>();
	public static final List<String> columns = new ArrayList<>();
	public static final List<String> dbTypes = new ArrayList<>();
	public static final List<String> javaTypes = new ArrayList<>();
	public static final List<String> comments = new ArrayList<>();
	public static final List<Model> listModels = new ArrayList<>();
	
	public static void main(String[] args) {
		generateCode();
	}

	public static void generateCode(){
		initColumnFieldComment();
		//generateModel();
		//generateMapper();
		generateSqlMap();
		//generateService();
		//generateServiceImpl();
		
	}
	
	public static void generateModel() {
		List<Model> list = new ArrayList<>();
		for(Model model : listModels){
			String columnName = model.getColumn();
			 if("id".equals(columnName) || "status".equals(columnName) || "create_time".equals(columnName)
             		|| "create_user".equals(columnName) || "update_time".equals(columnName) || "update_user".equals(columnName)){ 
					continue;
				}
			list.add(model);
		}
		Map<String,Object> data = new HashMap<>();
		data.put("models", list);
		data.put("className", MODEL_PRIFIX);
		data.put("basePackege", BASE_PACKAGE);
		FreemarkerTemplate.createCode4Template("ftl_model.ftl",BASE_PATH+"model/",MODEL_PRIFIX+".java", data);
	}
	public static void generateMapper() {
		Map<String,Object> data = new HashMap<>();
		data.put("className", MODEL_PRIFIX);
		data.put("className1", firstLowerCase(MODEL_PRIFIX));
		data.put("basePackege", BASE_PACKAGE);
		FreemarkerTemplate.createCode4Template("ftl_mapper.ftl",BASE_PATH+"mapper/",MODEL_PRIFIX+"Mapper.java", data);
	}
	public static void generateSqlMap() {
		Map<String,Object> data = new HashMap<>();
		data.put("models", listModels);
		data.put("tableName", TABLE_NAME);
		data.put("className", MODEL_PRIFIX);
		data.put("basePackege", BASE_PACKAGE);
		FreemarkerTemplate.createCode4Template("ftl_sqlmap.ftl",MAPPER_BASE_PATH,TABLE_NAME+".xml", data);
	}
	public static void generateService() {
		Map<String,Object> data = new HashMap<>();
		data.put("className", MODEL_PRIFIX);
		data.put("basePackege", BASE_PACKAGE);
		FreemarkerTemplate.createCode4Template("ftl_service.ftl",BASE_PATH+"service/",MODEL_PRIFIX+"Service.java", data);
	}
	public static void generateServiceImpl() {
		Map<String,Object> data = new HashMap<>();
		data.put("className", MODEL_PRIFIX);
		data.put("className1", firstLowerCase(MODEL_PRIFIX));
		data.put("basePackege", BASE_PACKAGE);
		FreemarkerTemplate.createCode4Template("ftl_service_impl.ftl",BASE_PATH+"service/impl/",MODEL_PRIFIX+"ServiceImpl.java", data);
	}
	
	private static String convertColumnToFiledName(String column) {
		column = column.toLowerCase();
		Pattern pattern = Pattern.compile("(_[a-z])");
		Matcher matcher = pattern.matcher(column);
		while (matcher.find()) {
			String str = matcher.group(0);
			column = column.replaceAll(str, str.replaceAll("_", "")
					.toUpperCase());
		}
		return column;
	}
	
	public static void initColumnFieldComment() {
		ResultSetMetaData metaData = SqlTool.getTableMeta(TABLE_NAME,DB_NAME);
        Map<String,String> colAndCommentMap = SqlTool.getColumnAndComment(TABLE_NAME,DB_NAME);
		try {
			String fieldType;
			int count = metaData.getColumnCount();
			for (int index = 1; index <= count; index++) {
				int type = metaData.getColumnType(index);
				if (type == Types.VARCHAR) {
					fieldType = "String";
					dbTypes.add("VARCHAR");
				} else if (type == Types.DECIMAL) {
					fieldType = "BigDecimal";
					dbTypes.add("VARCHAR");
				} else if (type == Types.INTEGER) {
					fieldType = "Integer";
					dbTypes.add("VARCHAR");
				} else if (type == Types.TINYINT) {
					fieldType = "Boolean";
					dbTypes.add("VARCHAR");
				} else if (type == Types.DATE || type == Types.TIMESTAMP) {
					fieldType = "Date";
					dbTypes.add("VARCHAR");
				} else if (type == Types.DOUBLE) {
					fieldType = "Double";
					dbTypes.add("VARCHAR");
				} else {
					fieldType = "String";
					dbTypes.add("VARCHAR");
				}
                String columnName =  metaData.getColumnName(index);
                String commentString = colAndCommentMap.get(columnName);
                String field = convertColumnToFiledName(columnName);
                
                fields.add(field);
                columns.add(columnName);
                javaTypes.add(fieldType);
                comments.add(commentString);
                
			}
			
			for(int i = 0;i < fields.size();i++){
				Model model = new Model();
				model.setColumn(columns.get(i));
				model.setComment(comments.get(i));
				model.setDbType(dbTypes.get(i));
				model.setField(fields.get(i));
				model.setJavaType(javaTypes.get(i));
				model.setFirstUpfield( model.getField().substring(0, 1).toUpperCase() + model.getField().substring(1));
				listModels.add(model);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static String firstLowerCase(String str){
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}
}
