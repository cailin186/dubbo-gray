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
import com.xlz.admin.model.User;
import com.xlz.admin.service.UserService;
import com.xlz.commons.base.BaseController;
import com.xlz.commons.base.mapper.FilterRule;
import com.xlz.commons.base.mapper.PageQuery;
import com.xlz.commons.utils.DigestUtils;
import com.xlz.commons.utils.PageInfo;
import com.xlz.commons.utils.StringUtils;

/**
 * @description：用户管理
 * @author：zhangll
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;

    /**
     * 用户管理页
     *
     * @return
     */
    @GetMapping("/manager")
    public String manager() {
        return "admin/user/user_manager";
    }

    /**
     * 用户管理列表
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
    public Object dataGrid(User entity, Integer page, Integer rows, String sort, String order) {
        List<FilterRule> filterRuleList = new ArrayList<>();
        if (StringUtils.isNotBlank(entity.getName())) {
            filterRuleList.add(new FilterRule("t.name","like",entity.getName() + "%"));
        }
        if (entity.getOrganizationId() != null) {
        	filterRuleList.add(new FilterRule("t.organization_id","=",entity.getOrganizationId() ));
        }
        if (entity.getCreatedateStart() != null) {
        	filterRuleList.add(new FilterRule("t.create_time",">=",entity.getCreatedateStart()));
        }
        if (entity.getCreatedateEnd() != null) {
        	filterRuleList.add(new FilterRule("t.create_time","<=",entity.getCreatedateEnd()));
        }
		PageQuery pageQuery = new PageQuery(page,rows,sort,order);
		PageInfo<User> pageInfo = userService.findByPage(filterRuleList, pageQuery);
        return pageInfo;
    }

    /**
     * 添加用户页
     *
     * @return
     */
    @GetMapping("/addPage")
    public String addPage() {
        return "admin/user/user_add";
    }

    /**
     * 添加用户
     *
     * @param entity
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Object add(User entity) {
        List<User> list = userService.selectByLoginName(entity);
        if (list != null && !list.isEmpty()) {
            return renderError("用户名已存在!");
        }
        entity.setPassword(DigestUtils.md5Hex(entity.getPassword()));
        userService.insertByVo(entity);
        return renderSuccess("添加成功");
    }

    /**
     * 编辑用户页
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/editPage")
    public String editPage(Model model, Long id) {
    	User entity = userService.selectVoById(id);
        List<Role> rolesList = entity.getRolesList();
        List<Long> ids = new ArrayList<>();
        for (Role role : rolesList) {
            ids.add(role.getId());
        }
        model.addAttribute("roleIds", ids);
        model.addAttribute("user", entity);
        return "admin/user/user_edit";
    }

    /**
     * 编辑用户
     *
     * @param entity
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(User entity) {
        List<User> list = userService.selectByLoginName(entity);
        if (list != null && !list.isEmpty()) {
            return renderError("用户名已存在!");
        }
        if (StringUtils.isNotBlank(entity.getPassword())) {
            entity.setPassword(DigestUtils.md5Hex(entity.getPassword()));
        }
        userService.updateByVo(entity);
        return renderSuccess("修改成功！");
    }

    /**
     * 修改密码页
     *
     * @return
     */
    @GetMapping("/editPwdPage")
    public String editPwdPage() {
        return "admin/user/user_editpwd";
    }

    /**
     * 修改密码
     *
     * @param oldPwd
     * @param pwd
     * @return
     */
    @RequestMapping("/editUserPwd")
    @ResponseBody
    public Object editUserPwd(String oldPwd, String pwd) {
        User user = userService.findById(getUserId());
        if (!user.getPassword().equals(DigestUtils.md5Hex(oldPwd))) {
            return renderError("老密码不正确!");
        }
        userService.updatePwdByUserId(getUserId(), DigestUtils.md5Hex(pwd));
        return renderSuccess("密码修改成功！");
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Object delete(Long id) {
        userService.deleteUserById(id);
        return renderSuccess("删除成功！");
    }
}
