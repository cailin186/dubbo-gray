package com.xlz.gray.controller;

import java.util.ArrayList;
import java.util.List;

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
import com.xlz.commons.utils.PageInfo;
import com.xlz.commons.utils.StringUtils;
import com.xlz.gray.model.WhiteListGroup;
import com.xlz.gray.service.WhiteListGroupService;
import com.xlz.gray.service.WhiteListService;

/**
 * @description：白名单组管理
 * @author：zhagnll
 */
@Controller
@RequestMapping("/whiteListGroup")
public class WhiteListGroupController extends BaseController {
    @Autowired
    private WhiteListGroupService whiteListGroupService;
    @Autowired
	private WhiteListService whiteListService;
    
    /**
     * 白名单组管理页
     *
     * @return
     */
    @GetMapping("/manager")
    public String manager() {
        return "gray/white_list/white_list_group_manager";
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
    public Object dataGrid(WhiteListGroup entity,Integer page, Integer rows, String sort, String order) {
        List<FilterRule> filterRuleList = new ArrayList<>();
        if (StringUtils.isNotBlank(entity.getName())) {
            filterRuleList.add(new FilterRule("name","like",entity.getName() + "%"));
        }
        if (StringUtils.isNotBlank(entity.getCreateUser())) {
        	filterRuleList.add(new FilterRule("create_user","=",entity.getCreateUser()));
        }
		PageQuery pageQuery = new PageQuery(page,rows,sort,order);
		PageInfo<WhiteListGroup> pageInfo = whiteListGroupService.findByPage(filterRuleList, pageQuery);
        return pageInfo;
    }

    /**
     * 添加白名单组页
     *
     * @return
     */
    @GetMapping("/addPage")
    public String addPage() {
        return "gray/white_list/white_list_group_add";
    }

    /**
     * 添加白名单组
     *
     * @param entity
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Object add(WhiteListGroup entity) {
    	List<FilterRule> filterRuleList = new ArrayList<>();
    	filterRuleList.add(new FilterRule("name","=",entity.getName()));
        Integer count = whiteListGroupService.getTotalCount(filterRuleList);
        if (count != null && count.intValue() > 0) {
            return renderError("白名单组名已存在!");
        }
        entity.setCreateUser(getStaffName());
        whiteListGroupService.save(entity);
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
        WhiteListGroup entity = whiteListGroupService.findById(id);
        model.addAttribute("entity", entity);
        return "gray/white_list/white_list_group_edit";
    }

    /**
     * 编辑白名单组
     *
     * @param entity
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(WhiteListGroup entity,String oldname) {
    	if(!oldname.equals(entity.getName())){
	    	List<FilterRule> filterRuleList = new ArrayList<>();
	    	filterRuleList.add(new FilterRule("name","=",entity.getName()));
	        Integer count = whiteListGroupService.getTotalCount(filterRuleList);
	        if (count != null && count.intValue() > 0) {
	            return renderError("白名单组名已存在!");
	        }
    	}
    	entity.setUpdateUser(getStaffName());
        whiteListGroupService.update(entity);
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
    	List<FilterRule> filterRuleList = new ArrayList<>();
    	filterRuleList.add(new FilterRule("group_id","=",id));
    	filterRuleList.add(new FilterRule("status",">=",0));
        Integer count = whiteListService.getTotalCount(filterRuleList);
        if (count != null && count.intValue() > 0) {
            return renderSuccess("请先删除组下的白名单!");
        }
    	whiteListGroupService.delete(id);
        return renderSuccess("删除成功！");
    }
    
    @RequestMapping("/getAllGroup")
    @ResponseBody
    public Object getAllGroup() {
    	List<FilterRule> filterRuleList = new ArrayList<>();
		List<WhiteListGroup> list = whiteListGroupService.findAll(filterRuleList);
        return list;
    }
}
