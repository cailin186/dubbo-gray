package com.xlz.gray.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xlz.admin.service.OrganizationService;
import com.xlz.commons.base.BaseController;
import com.xlz.commons.base.mapper.FilterRule;
import com.xlz.commons.base.mapper.PageQuery;
import com.xlz.commons.config.Level;
import com.xlz.commons.config.Type;
import com.xlz.commons.result.Result;
import com.xlz.commons.result.ResultCode;
import com.xlz.commons.result.Tree;
import com.xlz.commons.utils.PageInfo;
import com.xlz.commons.utils.StringUtils;
import com.xlz.gray.model.Application;
import com.xlz.gray.model.ApplicationInstance;
import com.xlz.gray.model.ApplicationNginx;
import com.xlz.gray.model.ApplicationStrategy;
import com.xlz.gray.service.ApplicationInstanceService;
import com.xlz.gray.service.ApplicationNginxService;
import com.xlz.gray.service.ApplicationService;
import com.xlz.gray.service.ApplicationStrategyService;

/**
 * @description：被灰度系统管理
 * @author：zhagnll
 */
@Controller
@RequestMapping("/application")
public class ApplicationController extends BaseController {
    @Autowired
    private ApplicationService applicationService;
    @Autowired
	private ApplicationNginxService applicationNginxService;
    @Autowired
    private ApplicationStrategyService applicationStrategyService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private ApplicationInstanceService applicationInstanceService;
    
    /**
     * 被灰度系统管理页
     *
     * @return
     */
    @GetMapping("/manager")
    public String manager() {
        return "gray/application/application_manager";
    }

    /**
     * 被灰度系统管理列表
     *
     * @param entity
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     */
    @PostMapping("/dataGrid")
    @ResponseBody
    public Object dataGrid(Application entity,Integer page, Integer rows, String sort, String order) {
        List<FilterRule> filterRuleList = new ArrayList<>();
        if (StringUtils.isNotBlank(entity.getName())) {
            filterRuleList.add(new FilterRule("name","like",entity.getName() + "%"));
        }
        if (StringUtils.isNotBlank(entity.getCreateUser())) {
        	filterRuleList.add(new FilterRule("create_user","=",entity.getCreateUser()));
        }
        if (entity.getOrganizationId() == null) {
        	entity.setOrganizationId("-1");
        }
        filterRuleList.add(new FilterRule("organization_id","=",entity.getOrganizationId()));
		PageQuery pageQuery = new PageQuery(page,rows,sort,order);
		PageInfo<Application> pageInfo = applicationService.findByPage(filterRuleList, pageQuery);
        return pageInfo;
    }

    /**
     * 添加被灰度系统页
     *
     * @return
     */
    @GetMapping("/addPage")
    public String addPage() {
        return "gray/application/application_add";
    }

    /**
     * 添加被灰度系统
     *
     * @param entity
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Object add(Application entity,HttpServletRequest request) {
    	List<FilterRule> filterRuleList = new ArrayList<>();
    	filterRuleList.add(new FilterRule("application_id","=",entity.getApplicationId()));
        Integer count = applicationService.getTotalCount(filterRuleList);
        if (count != null && count.intValue() > 0) {
            return renderError("被灰度系统唯一标识已存在!");
        }
        
        //对所选的策略的灰度方式进行校验
        Result<Application> result = applicationStrategyService.checkStrategy(request,entity.getParam());
    	if(result != null && !result.getCode().equals(ResultCode.COMMON_SUCCESS.getCode())){
			return renderError(result.getMsg());
		}
		
        entity.setCreateUser(getStaffName());
        applicationService.save(entity);
        updateApplicationNginx(entity.getNginxs(),entity.getId());
        applicationStrategyService.updateApplicationStrategy(request, entity,Level.application);
        applicationInstanceService.updateInstance(request, entity);
        return renderSuccess("添加成功");
    }

    /**
     * 编辑被灰度系统页
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/editPage")
    public String editPage(Model model, Long id) {
        Application entity = applicationService.findById(id);
        List<FilterRule> filterRuleList = new ArrayList<>();
    	filterRuleList.add(new FilterRule("application_id","=",id));
    	List<ApplicationNginx>  list = applicationNginxService.findAll(filterRuleList);
        StringBuilder sbuilder = new StringBuilder();
        int index = 0;
    	for(ApplicationNginx nginx : list){
    		if(index != 0){
    			sbuilder.append(",\n");
    		}
    		index ++;
    		sbuilder.append(nginx.getIp()).append(":").append(nginx.getPort());
    	}
        entity.setNginxs(sbuilder.toString());
        
        //获取应用策略
        filterRuleList.clear();
    	filterRuleList.add(new FilterRule("link_id","=",entity.getId()));
    	filterRuleList.add(new FilterRule("level","=",Level.application));
    	List<ApplicationStrategy> applicationStrategys = applicationStrategyService.findAll(filterRuleList);
    	
    	Map<String,ApplicationStrategy> strategys = (Map<String,ApplicationStrategy>)entity.getStrategy();
    	for(ApplicationStrategy applicationStrategy :  applicationStrategys){
    		strategys.put(applicationStrategy.getType().name(), applicationStrategy);
    	}
    	
    	//获取应用实例
    	filterRuleList.clear();
    	filterRuleList.add(new FilterRule("application_id","=",id));
    	List<ApplicationInstance>  applicationInstances = applicationInstanceService.findAll(filterRuleList);
    	Map<String,ApplicationInstance> instances = (Map<String,ApplicationInstance>)entity.getInstance();
    	for(ApplicationInstance instance :  applicationInstances){
    		instances.put(instance.getType().name(), instance);
    	}
    	
    	for (Type type : Type.values()) {  
    		ApplicationStrategy applicationStrategy = strategys.get(type.name());
    		if(applicationStrategy == null){
    			applicationStrategy = new ApplicationStrategy();
    			applicationStrategy.setType(type);
    			applicationStrategy.setStatus(0);
    			strategys.put(type.name(),applicationStrategy);
    		}
    		ApplicationInstance instance = instances.get(type.name());
    		if(instance == null){
    			instance = new ApplicationInstance();
    			instance.setType(type);
    			instances.put(type.name(),instance);
    		}
    	}
    	
    	model.addAttribute("entity", entity);
        return "gray/application/application_edit";
    }

    /**
     * 编辑被灰度系统
     *
     * @param entity
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(Application entity,String oldApplicationId,HttpServletRequest request) {
    	if(!oldApplicationId.equals(entity.getApplicationId())){
	    	List<FilterRule> filterRuleList = new ArrayList<>();
	    	filterRuleList.add(new FilterRule("application_id","=",entity.getApplicationId()));
	        Integer count = applicationService.getTotalCount(filterRuleList);
	        if (count != null && count.intValue() > 0) {
	            return renderError("被灰度系统名已存在!");
	        }
    	}
    	//对所选的策略的灰度方式进行校验
        Result<Application> result = applicationStrategyService.checkStrategy(request,entity.getParam());
    	if(result != null && !result.getCode().equals(ResultCode.COMMON_SUCCESS.getCode())){
			return renderError(result.getMsg());
		}
    	entity.setUpdateUser(getStaffName());
        applicationService.update(entity);
        updateApplicationNginx(entity.getNginxs(),entity.getId());
        applicationStrategyService.updateApplicationStrategy(request, entity,Level.application);
        applicationInstanceService.updateInstance(request, entity);
        return renderSuccess("修改成功！");
    }
    
    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Object delete(String id) {
    	//检查是否下面有白名单，如果有不允许删除
    	/*List<FilterRule> filterRuleList = new ArrayList<>();
    	filterRuleList.add(new FilterRule("group_id","=",id));
        Integer count = whiteListService.getTotalCount(filterRuleList);
        if (count != null && count.intValue() > 0) {
            return renderSuccess("请先删除组下的白名单!");
        }*/
    	applicationService.delete(id);
        return renderSuccess("删除成功！");
    }
    
    /**
     * 无修改的不变，如果有改动的需要调整，删除的需要删除
     * @param nginxs
     */
    private void updateApplicationNginx(String nginxs,Long applicationId){
    	List<FilterRule> filterRuleList = new ArrayList<>();
    	filterRuleList.add(new FilterRule("application_id","=",applicationId));
    	List<ApplicationNginx>  list = applicationNginxService.findAll(filterRuleList);
    	String nginxArray [] = null;
    	if(StringUtils.isNotBlank(nginxs)){
    		nginxs = nginxs.replaceAll("\r\n", "");
	    	nginxArray = nginxs.split(",");
	    	for(String nginx : nginxArray){
	    		if("".equals(nginx)){
	    			continue;
	    		}
	    		String nginxParams[] = nginx.split(":");
	    		boolean exist = true;
	    		for(ApplicationNginx existNginx :  list){
	    			//端口和ip有不同的,需要添加
	    			if(nginxParams[0].equals(existNginx.getIp()) && nginxParams[1].equals(existNginx.getPort())){
	    				exist = false;
	    			}
	    		}
	    		if(exist){ 
	    			ApplicationNginx entity = new ApplicationNginx();
					entity.setApplicationId(applicationId);
					entity.setIp(nginxParams[0]);
					entity.setPort(nginxParams[1]);
					entity.setStatus(1);
					entity.setCreateUser(getStaffName());
					applicationNginxService.save(entity);
	    		}
	    	}
    	}
    	
    	//删除
    	for(ApplicationNginx existNginx :  list){
    		boolean exist = true;
			//端口和ip有不同的,需要添加
    		if(StringUtils.isNotBlank(nginxs)){
	    		for(String nginx : nginxArray){
	        		String nginxParams[] = nginx.split(":");
	        		//端口和ip有不同的,需要添加
	    			if(nginxParams[0].equals(existNginx.getIp()) && nginxParams[1].equals(existNginx.getPort())){
	    				exist = false;
	    			}
	    		}
    		}
    		if(exist){ 
				applicationNginxService.delete(existNginx.getId().toString());;
    		}
		}
    }
    
    /**
     * 部门和系统资源树
     *
     * @return
     */
    @PostMapping(value = "/tree")
    @ResponseBody
    public Object selectTree() {
    	List<Tree> trees = organizationService.selectTree();
    	List<FilterRule> filterRuleList = new ArrayList<>();
    	List<Application> applicationlist = applicationService.findAll(filterRuleList);
        for(Application application : applicationlist){
        	Tree tree = new Tree();
            tree.setId(application.getId()+10000000);
            tree.setText(application.getName());
            tree.setIconCls("fi-widget");
            tree.setPid(new Long(application.getOrganizationId()));
            trees.add(tree);
        }
        
        return trees;
    }
}
