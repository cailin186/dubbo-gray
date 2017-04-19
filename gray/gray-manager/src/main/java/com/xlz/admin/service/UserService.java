package com.xlz.admin.service;

import java.util.List;

import com.xlz.admin.model.User;
import com.xlz.commons.base.service.BaseService;

/**
 *
 * User 表数据服务层接口
 *
 */
public interface UserService extends BaseService<User> {

    List<User> selectByLoginName(User entity);

    void insertByVo(User entity);

    User selectVoById(Long id);

    void updateByVo(User entity);

    void updatePwdByUserId(Long userId, String md5Hex);

    void deleteUserById(Long id);
}