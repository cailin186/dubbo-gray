package codetool;

import java.io.File;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class FreemarkerTemplate {

	//public static final String ftlDir = "D:/BaiduYunDownload/gray/src/test/java/codetool";
	public static void createCode4Template(String ftl,String basePath,String fileName,Map<String,Object> data){
		Configuration configuration = new Configuration();
        configuration.setObjectWrapper(new DefaultObjectWrapper());
        configuration.setTemplateLoader(new ClassTemplateLoader(GenerateCodeFromTable.class, "/codetool"));
        
        try {
        	//configuration.setDirectoryForTemplateLoading(new File(ftlDir));
            Template template = configuration.getTemplate(ftl);
	        /* 合并数据模型和模版*/  
	        Writer out = new OutputStreamWriter(System.out);  
	        template.process(data, out);  
	        out.flush();  
	        
	        /* 生成输出到文件 */  
	        File fileDir = new File(basePath);  
	        // 创建文件夹，不存在则创建  
	        org.apache.commons.io.FileUtils.forceMkdir(fileDir);  
	        // 指定生成输出的文件  
	        File output = new File(fileDir + "/"+fileName);  
	        Writer writer = new FileWriter(output);  
	        template.process(data, writer);  
	        writer.close(); 
        }catch(Exception e){
        	
        }
	}
}
