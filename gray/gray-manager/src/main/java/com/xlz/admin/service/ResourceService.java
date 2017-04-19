package com.xlz.admin.service;

import java.util.List;

import com.xlz.admin.model.Resource;
import com.xlz.commons.base.service.BaseService;
import com.xlz.commons.result.Tree;
import com.xlz.commons.shiro.ShiroUser;

/**
 *
 * Resource 表数据服务层接口
 *
 */
public interface ResourceService extends BaseService<Resource> {

    List<Resource> selectAll();

    List<Tree> selectAllMenu();

    List<Tree> selectAllTree();

    List<Tree> selectTree(ShiroUser shiroUser);

}