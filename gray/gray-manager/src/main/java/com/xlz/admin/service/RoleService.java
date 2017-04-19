package com.xlz.admin.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.xlz.admin.model.Role;
import com.xlz.commons.base.service.BaseService;
import com.xlz.commons.utils.PageInfo;

/**
 *
 * Role 表数据服务层接口
 *
 */
public interface RoleService extends BaseService<Role> {

    Object selectTree();

    List<Long> selectResourceIdListByRoleId(Long id);

    void updateRoleResource(Long id, String resourceIds);

    Map<String, Set<String>> selectResourceMapByUserId(Long userId);

}