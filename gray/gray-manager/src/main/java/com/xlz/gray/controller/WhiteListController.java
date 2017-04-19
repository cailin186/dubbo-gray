package com.xlz.gray.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xlz.commons.base.BaseController;
import com.xlz.commons.base.mapper.FilterRule;
import com.xlz.commons.base.mapper.PageQuery;
import com.xlz.commons.config.Level;
import com.xlz.commons.utils.JsonUtils;
import com.xlz.commons.utils.PageInfo;
import com.xlz.commons.utils.StringUtils;
import com.xlz.gray.model.ApplicationWhitelist;
import com.xlz.gray.model.WhiteList;
import com.xlz.gray.service.ApplicationWhitelistService;
import com.xlz.gray.service.WhiteListService;

/**
 * @description：白名单管理
 * @author：zhagnll
 */
@Controller
@RequestMapping("/whiteList")
public class WhiteListController extends BaseController {
    @Autowired
	private WhiteListService whiteListService;
    @Autowired
    private ApplicationWhitelistService applicationWhitelistService;
    /**
     * 白名单组管理页
     *
     * @return
     */
    @GetMapping("/manager")
    public String manager() {
        return "gray/white_list/white_list_manager";
    }

    /**
     * 白名单组管理列表
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
    public Object dataGrid(WhiteList entity,Integer page, Integer rows, String sort, String order) {
        List<FilterRule> filterRuleList = new ArrayList<>();
        if (StringUtils.isNotBlank(entity.getParam())) {
            filterRuleList.add(new FilterRule("param","like","%" + entity.getParam() + "%"));
        }
        if (StringUtils.isNotBlank(entity.getCreateUser())) {
        	filterRuleList.add(new FilterRule("create_user","=",entity.getCreateUser()));
        }
        if (entity.getGroupId() != null) {
        	filterRuleList.add(new FilterRule("group_id","=",entity.getGroupId()));
        }
		PageQuery pageQuery = new PageQuery(page,rows,sort,order);
		PageInfo<WhiteList> pageInfo = whiteListService.findByPage(filterRuleList, pageQuery);
        return pageInfo;
    }

    /**
     * 添加白名单组页
     *
     * @return
     */
    @GetMapping("/addPage")
    public String addPage() {
        return "gray/white_list/white_list_add";
    }

    /**
     * 添加白名单组
     *
     * @param entity
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Object add(WhiteList entity) {
    	//校验json格式
    	try{
    		JsonUtils.parse(entity.getParam(), Map.class);
    	}catch(Exception e){
    		return renderError("白名单格式必须为Json格式");
    	}
        entity.setCreateUser(getStaffName());
        whiteListService.save(entity);
        return renderSuccess("添加成功");
    }

    /**
     * 编辑白名单组页
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/editPage")
    public String editPage(Model model, Long id) {
        WhiteList entity = whiteListService.findById(id);
        model.addAttribute("entity", entity);
        return "gray/white_list/white_list_edit";
    }

    /**
     * 编辑白名单组
     *
     * @param entity
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(WhiteList entity,String oldname) {
    	entity.setUpdateUser(getStaffName());
    	//校验json格式
    	try{
    		JsonUtils.parse(entity.getParam(), Map.class);
    	}catch(Exception e){
    		return renderError("白名单格式必须为Json格式");
    	}
    	if(entity.getStatus().intValue() == -1){
    		return renderError("不可以设置成删除状态，请到列表页点击删除");
    	}
        whiteListService.update(entity);
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
    	whiteListService.delete(id);
    	applicationWhitelistService.delete(id,null,null);
        return renderSuccess("删除成功！");
    }
    
    /**
     * 白名单组管理列表
     *
     * @param entity
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     */
    @PostMapping("/selectedWhitelistdataGrid")
    @ResponseBody
    public Object selectedWhitelistdataGrid(WhiteList entity,Integer linkId,
    		Level level,Integer page, Integer rows, String sort, String order) {
        List<FilterRule> filterRuleList = new ArrayList<>();
        if (StringUtils.isNotBlank(entity.getParam())) {
            filterRuleList.add(new FilterRule("param","like","%" + entity.getParam() + "%"));
        }
        if (entity.getGroupId() != null) {
        	filterRuleList.add(new FilterRule("group_id","=",entity.getGroupId()));
        }
        filterRuleList.add(new FilterRule("s.status","=",1));
        filterRuleList.add(new FilterRule("link_id","=",linkId));
        filterRuleList.add(new FilterRule("level","=",level));
		PageQuery pageQuery = new PageQuery(page,rows,sort,order);
		PageInfo<WhiteList> pageInfo = whiteListService.findSelectedByPage(filterRuleList, pageQuery);
        return pageInfo;
    }
    
    /**
     * 为系统或者服务添加白名单
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/whitelistPage")
    public String whitelistPage(Model model,Integer id,Level level) {
    	model.addAttribute("parentId", id);
    	model.addAttribute("level", level);
        return "gray/white_list/application_service_whitelist";
    }
    
    /**
     * 添加选定的用户
     *
     * @param id
     * @return
     */
    @RequestMapping("/whitelistAddFun")
    @ResponseBody
    public Object whitelistAddFun(String ids[],Level level,Integer linkId) {
    	//删除
    	List<FilterRule> filterRuleList = new ArrayList<>();
        filterRuleList.add(new FilterRule("link_id","=",linkId));
        filterRuleList.add(new FilterRule("level","=",level));
        List<ApplicationWhitelist> list = applicationWhitelistService.findAll(filterRuleList);
        Map<String,ApplicationWhitelist> set = new HashMap<>();
        for(ApplicationWhitelist entity : list){
	        set.put(entity.getWhiteListId().toString(),entity);
        }
        
    	//添加
    	for(String id : ids){
    		if(StringUtils.isNotBlank(id)){
    			ApplicationWhitelist entity = new ApplicationWhitelist();
    			if(!set.containsKey(id)){
    				entity.setLevel(level);
        			entity.setLinkId(linkId);
        			entity.setWhiteListId(new Integer(id));
        			entity.setStatus(1);
    				applicationWhitelistService.save(entity );
    			}else if(set.get(id).getStatus().intValue() == 0){
    				entity.setId(set.get(id).getId());
    				entity.setStatus(1);
    				applicationWhitelistService.update(entity );
    			}
    		}
    	}
        return renderSuccess("添加成功！");
    }
    /**
     * 删除选定的用户
     *
     * @param id
     * @return
     */
    @RequestMapping("/whitelistDelFun")
    @ResponseBody
    public Object whitelistDelFun(String ids,Integer linkId,Level level) {
    	//删除
    	applicationWhitelistService.delete(ids,linkId,level);
    	return renderSuccess("删除成功！");
    }
}
