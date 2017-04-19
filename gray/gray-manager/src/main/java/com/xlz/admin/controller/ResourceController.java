package com.xlz.admin.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xlz.admin.model.Resource;
import com.xlz.admin.service.ResourceService;
import com.xlz.commons.base.BaseController;
import com.xlz.commons.result.Tree;
import com.xlz.commons.shiro.ShiroUser;

/**
 * @description：资源管理
 * @author：zhangll
 */
@Controller
@RequestMapping("/resource")
public class ResourceController extends BaseController {

    @Autowired
    private ResourceService resourceService;

    /**
     * 菜单树
     *
     * @return
     */
    @PostMapping("/tree")
    @ResponseBody
    public Object tree(Long id) {
        ShiroUser shiroUser = getShiroUser();
        List<Tree> trees = resourceService.selectTree(shiroUser);
        List<Tree> childrens = new ArrayList<>();
        for(Tree tree : trees){
        	if(id != null){
        		if(tree.getPid() != null && id.longValue() == tree.getPid().longValue()){
        			childrens.add(tree);
        		}
        	}else{
        		if(tree.getPid() == null){
        			childrens.add(tree);
        		}
        	}
        }
        return childrens;
    }

    private void getChildrens(List<Tree> childrens,Tree node){
    	
    }
    /**
     * 资源管理页
     *
     * @return
     */
    @GetMapping("/manager")
    public String manager() {
        return "admin/resource/resource_manager";
    }

    /**
     * 资源管理列表
     *
     * @return
     */
    @PostMapping("/treeGrid")
    @ResponseBody
    public Object treeGrid() {
        return resourceService.selectAll();
    }

    /**
     * 添加资源页
     *
     * @return
     */
    @GetMapping("/addPage")
    public String addPage() {
        return "admin/resource/resource_add";
    }

    /**
     * 添加资源
     *
     * @param resource
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public Object add(Resource resource) {
        resource.setCreateTime(new Date());
        // 选择菜单时将openMode设置为null
        Integer type = resource.getResourceType();
        if (null != type && type == 0) {
            resource.setOpenMode(null);
        }
        resourceService.save(resource);
        return renderSuccess("添加成功！");
    }

    /**
     * 查询所有的菜单
     */
    @RequestMapping("/allTree")
    @ResponseBody
    public Object allMenu() {
        return resourceService.selectAllMenu();
    }

    /**
     * 查询所有的资源tree
     */
    @RequestMapping("/allTrees")
    @ResponseBody
    public Object allTree() {
        return resourceService.selectAllTree();
    }

    /**
     * 编辑资源页
     *
     * @param model
     * @param id
     * @return
     */
    @RequestMapping("/editPage")
    public String editPage(Model model, Long id) {
        Resource resource = resourceService.findById(id);
        model.addAttribute("resource", resource);
        return "admin/resource/resource_edit";
    }

    /**
     * 编辑资源
     *
     * @param resource
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(Resource resource) {
        // 选择菜单时将openMode设置为null
        Integer type = resource.getResourceType();
        if (null != type && type == 0) {
            resource.setOpenMode(null);
        }
        resourceService.update(resource);
        return renderSuccess("编辑成功！");
    }

    /**
     * 删除资源
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Object delete(String id) {
        resourceService.delete(id);
        return renderSuccess("删除成功！");
    }

}
