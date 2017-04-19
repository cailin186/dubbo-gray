package com.xlz.gray.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xlz.commons.base.mapper.FilterRule;
import com.xlz.commons.base.mapper.PageQuery;
import com.xlz.commons.config.Level;
import com.xlz.commons.result.Result;
import com.xlz.commons.result.ResultCode;
import com.xlz.commons.utils.PageInfo;
import com.xlz.commons.utils.StringUtils;
import com.xlz.gray.model.Application;
import com.xlz.gray.model.ApplicationService;
import com.xlz.gray.model.ApplicationStrategy;
import com.xlz.gray.service.ApplicationServiceService;
import com.xlz.gray.service.ApplicationStrategyService;

/**
 * @description：被灰度系统服务管理
 * @author：zhagnll
 */
@Controller
@RequestMapping("/applicationService")
public class ApplicationServiceController extends GrayBaseController {
    @Autowired
    private ApplicationServiceService applicationServiceService;
    @Autowired
    private ApplicationStrategyService applicationStrategyService;
    
    /**
     * 被灰度系统管理页
     *
     * @return
     */
    @GetMapping("/manager")
    public String manager() {
        return "gray/application/application_service_manager";
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
    public Object dataGrid(ApplicationService entity,Integer page, Integer rows, 
    		String sort, String order) {
        List<FilterRule> filterRuleList = new ArrayList<>();
        if (StringUtils.isNotBlank(entity.getName())) {
            filterRuleList.add(new FilterRule("name","like",entity.getName() + "%"));
        }
        if (StringUtils.isNotBlank(entity.getCreateUser())) {
        	filterRuleList.add(new FilterRule("create_user","=",entity.getCreateUser()));
        }
        if (entity.getApplicationId() == null) {
        	entity.setApplicationId(-1l);
        }
        filterRuleList.add(new FilterRule("application_id","=",entity.getApplicationId()));
		PageQuery pageQuery = new PageQuery(page,rows,sort,order);
		PageInfo<ApplicationService> pageInfo = applicationServiceService.findByPage(filterRuleList, pageQuery);
        return pageInfo;
    }

    /**
     * 添加被灰度系统页
     *
     * @return
     */
    @GetMapping("/addPage")
    public String addPage(Model model,Integer applicationId) {
    	model.addAttribute("applicationId", applicationId);
        return "gray/application/application_service_add";
    }

    /**
     * 添加被灰度系统
     *
     * @param entity
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Object add(ApplicationService entity,HttpServletRequest request) {
    	List<FilterRule> filterRuleList = new ArrayList<>();
    	filterRuleList.add(new FilterRule("application_id","=",entity.getApplicationId()));
    	filterRuleList.add(new FilterRule("name","=",entity.getName()));
        Integer count = applicationServiceService.getTotalCount(filterRuleList);
        if (count != null && count.intValue() > 0) {
            return renderError("服务名已存在!");
        }
        entity.setCreateUser(getStaffName());
        
        //对所选的策略的灰度方式进行校验
        Result<Application> result = applicationStrategyService.checkStrategy(request,entity.getParam());
    	if(result != null && !result.getCode().equals(ResultCode.COMMON_SUCCESS.getCode())){
			return renderError(result.getMsg());
		}
    	
        applicationServiceService.save(entity);
        applicationStrategyService.updateApplicationStrategy(request, entity,Level.service);
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
        ApplicationService entity = applicationServiceService.findById(id);
        List<FilterRule> filterRuleList = new ArrayList<>();
    	filterRuleList.add(new FilterRule("application_id","=",id));
        
        filterRuleList.clear();
    	filterRuleList.add(new FilterRule("link_id","=",entity.getId()));
    	filterRuleList.add(new FilterRule("level","=",Level.service));
    	List<ApplicationStrategy> applicationStrategys = applicationStrategyService.findAll(filterRuleList);
    	entity.setStrategy(applicationStrategys.get(0).getStrategyId());
    	
    	model.addAttribute("entity", entity);
        return "gray/application/application_service_edit";
    }

    /**
     * 编辑被灰度系统
     *
     * @param entity
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(ApplicationService entity,String oldname,HttpServletRequest request) {
    	if(!oldname.equals(entity.getName())){
	    	List<FilterRule> filterRuleList = new ArrayList<>();
	    	filterRuleList.add(new FilterRule("application_id","=",entity.getApplicationId()));
	    	filterRuleList.add(new FilterRule("name","=",entity.getName()));
	        Integer count = applicationServiceService.getTotalCount(filterRuleList);
	        if (count != null && count.intValue() > 0) {
	            return renderError("服务名已存在!");
	        }
    	}
    	//对所选的策略的灰度方式进行校验
        Result<Application> result = applicationStrategyService.checkStrategy(request,entity.getParam());
    	if(result != null && !result.getCode().equals(ResultCode.COMMON_SUCCESS.getCode())){
			return renderError(result.getMsg());
		}
    	entity.setUpdateUser(getStaffName());
        applicationServiceService.update(entity);
        applicationStrategyService.updateApplicationStrategy(request, entity,Level.service);
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
    	applicationServiceService.delete(id);
        return renderSuccess("删除成功！");
    }
}
