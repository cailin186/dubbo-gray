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
import com.xlz.gray.model.Strategy;
import com.xlz.gray.service.StrategyService;
import com.xlz.gray.service.ApplicationStrategyService;

/**
 * @description：灰度策略管理
 * @author：zhagnll
 */
@Controller
@RequestMapping("/strategy")
public class StrategyController extends BaseController {
    @Autowired
    private StrategyService strategyService;
    @Autowired
    private ApplicationStrategyService applicationStrategyService;
    
    /**
     * 灰度策略管理页
     *
     * @return
     */
    @GetMapping("/manager")
    public String manager() {
        return "gray/strategy/strategy_manager";
    }

    /**
     * 灰度策略管理列表
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
    public Object dataGrid(Strategy entity,Integer page, Integer rows, String sort, String order) {
        List<FilterRule> filterRuleList = new ArrayList<>();
        if (StringUtils.isNotBlank(entity.getName())) {
            filterRuleList.add(new FilterRule("name","like",entity.getName() + "%"));
        }
        if (StringUtils.isNotBlank(entity.getCreateUser())) {
        	filterRuleList.add(new FilterRule("create_user","=",entity.getCreateUser()));
        }
		PageQuery pageQuery = new PageQuery(page,rows,sort,order);
		PageInfo<Strategy> pageInfo = strategyService.findByPage(filterRuleList, pageQuery);
        return pageInfo;
    }

    /**
     * 添加灰度策略管理页
     *
     * @return
     */
    @GetMapping("/addPage")
    public String addPage() {
        return "gray/strategy/strategy_add";
    }

    /**
     * 添加灰度策略管理
     *
     * @param entity
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Object add(Strategy entity) {
    	List<FilterRule> filterRuleList = new ArrayList<>();
    	filterRuleList.add(new FilterRule("name","=",entity.getName()));
        Integer count = strategyService.getTotalCount(filterRuleList);
        if (count != null && count.intValue() > 0) {
            return renderError("灰度策略管理名已存在!");
        }
        entity.setCreateUser(getStaffName());
        strategyService.save(entity);
        return renderSuccess("添加成功");
    }

    /**
     * 编辑灰度策略管理页
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/editPage")
    public String editPage(Model model, Long id) {
        Strategy entity = strategyService.findById(id);
    	model.addAttribute("entity", entity);
        return "gray/strategy/strategy_edit";
    }

    /**
     * 编辑灰度策略管理
     *
     * @param entity
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(Strategy entity,String oldname) {
    	if(!oldname.equals(entity.getName())){
	    	List<FilterRule> filterRuleList = new ArrayList<>();
	    	filterRuleList.add(new FilterRule("name","=",entity.getName()));
	        Integer count = strategyService.getTotalCount(filterRuleList);
	        if (count != null && count.intValue() > 0) {
	            return renderError("灰度策略管理名已存在!");
	        }
    	}
    	entity.setUpdateUser(getStaffName());
        strategyService.update(entity);
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
    	filterRuleList.add(new FilterRule("strategy_id","=",id));
    	filterRuleList.add(new FilterRule("status","=",1));
        Integer count = applicationStrategyService.getTotalCount(filterRuleList);
        if (count != null && count.intValue() > 0) {
            return renderSuccess("此策略目前正在被系统使用，不允许删除!");
        }
    	strategyService.delete(id);
        return renderSuccess("删除成功！");
    }
    
    @RequestMapping("/getStrategyByType")
    @ResponseBody
    public Object getStrategyByType(Integer type) {
    	List<FilterRule> filterRuleList = new ArrayList<>();
		List<Strategy> list = strategyService.findAll(filterRuleList);
        return list;
    }
}
