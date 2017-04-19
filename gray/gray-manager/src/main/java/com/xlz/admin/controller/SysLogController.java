package com.xlz.admin.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xlz.admin.model.SysLog;
import com.xlz.admin.service.SysLogService;
import com.xlz.commons.base.mapper.FilterRule;
import com.xlz.commons.base.mapper.PageQuery;
import com.xlz.commons.utils.PageInfo;

/**
 * @description：日志管理
 * @author：zhangll
 */
@Controller
@RequestMapping("/sysLog")
public class SysLogController {
    @Autowired
    private SysLogService sysLogService;

    @GetMapping("/manager")
    public String manager() {
        return "admin/syslog/syslog_manager";
    }

    @PostMapping("/dataGrid")
    @ResponseBody
    public PageInfo<SysLog> dataGrid(Integer page, Integer rows, 
            @RequestParam(value = "sort", defaultValue = "create_time") String sort, 
            @RequestParam(value = "order", defaultValue = "DESC") String order) {
        List<FilterRule> filterRuleList = new ArrayList<>();
		PageQuery pageQuery = new PageQuery(page,rows,sort,order);
		PageInfo<SysLog> pageInfo = sysLogService.findByPage(filterRuleList, pageQuery);
        return pageInfo;
    }
}
