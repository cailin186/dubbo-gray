package com.xlz.admin.service;

import java.util.List;

import com.xlz.commons.base.service.BaseService;
import com.xlz.commons.result.Tree;
import com.xlz.admin.model.Organization;

/**
 *
 * Organization 表数据服务层接口
 *
 */
public interface OrganizationService extends BaseService<Organization> {

    List<Tree> selectTree();

    List<Organization> selectTreeGrid();

}