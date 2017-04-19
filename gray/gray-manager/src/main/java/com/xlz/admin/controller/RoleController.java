package com.xlz.admin.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xlz.admin.model.Role;
import com.xlz.admin.service.RoleService;
import com.xlz.commons.base.BaseController;
import com.xlz.commons.base.mapper.FilterRule;
import com.xlz.commons.base.mapper.PageQuery;
import com.xlz.commons.utils.PageInfo;

/**
 * @description：权限管理
 * @author：zhangll
 */
@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    /**
     * 权限管理页
     *
     * @return
     */
    @GetMapping("/manager")
    public String manager() {
        return "admin/role/role_manager";
    }

    /**
     * 权限列表
     *
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     */
    @PostMapping("/dataGrid")
    @ResponseBody
    public Object dataGrid(Integer page, Integer rows, String sort, String order) {
        List<FilterRule> filterRuleList = new ArrayList<>();
		PageQuery pageQuery = new PageQuery(page,rows,sort,order);
		PageInfo<Role> pageInfo = roleService.findByPage(filterRuleList, pageQuery);
        return pageInfo;
    }

    /**
     * 权限树
     *
     * @return
     */
    @PostMapping("/tree")
    @ResponseBody
    public Object tree() {
        return roleService.selectTree();
    }

    /**
     * 添加权限页
     *
     * @return
     */
    @GetMapping("/addPage")
    public String addPage() {
        return "admin/role/role_add";
    }

    /**
     * 添加权限
     *
     * @param role
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Object add(Role role) {
        roleService.save(role);
        return renderSuccess("添加成功！");
    }

    /**
     * 删除权限
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Object delete(String id) {
        roleService.delete(id);
        return renderSuccess("删除成功！");
    }

    /**
     * 编辑权限页
     *
     * @param model
     * @param id
     * @return
     */
    @RequestMapping("/editPage")
    public String editPage(Model model, Long id) {
        Role role = roleService.findById(id);
        model.addAttribute("role", role);
        return "admin/role/role_edit";
    }

    /**
     * 删除权限
     *
     * @param role
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(Role role) {
        roleService.update(role);
        return renderSuccess("编辑成功！");
    }

    /**
     * 授权页面
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/grantPage")
    public String grantPage(Model model, Long id) {
        model.addAttribute("id", id);
        return "admin/role/role_grant";
    }

    /**
     * 授权页面页面根据角色查询资源
     *
     * @param id
     * @return
     */
    @RequestMapping("/findResourceIdListByRoleId")
    @ResponseBody
    public Object findResourceByRoleId(Long id) {
        List<Long> resources = roleService.selectResourceIdListByRoleId(id);
        return renderSuccess(resources);
    }

    /**
     * 授权
     *
     * @param id
     * @param resourceIds
     * @return
     */
    @RequestMapping("/grant")
    @ResponseBody
    public Object grant(Long id, String resourceIds) {
        roleService.updateRoleResource(id, resourceIds);
        return renderSuccess("授权成功！");
    }

}
