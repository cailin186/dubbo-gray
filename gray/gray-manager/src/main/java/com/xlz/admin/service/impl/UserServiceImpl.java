package com.xlz.admin.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xlz.admin.mapper.UserMapper;
import com.xlz.admin.mapper.UserRoleMapper;
import com.xlz.admin.model.User;
import com.xlz.admin.model.UserRole;
import com.xlz.admin.service.UserService;
import com.xlz.commons.base.mapper.BaseMapper;
import com.xlz.commons.base.mapper.FilterRule;
import com.xlz.commons.base.service.impl.BaseServiceImpl;
import com.xlz.commons.utils.BeanUtils;
import com.xlz.commons.utils.StringUtils;

/**
 *
 * User 表数据服务层接口实现类
 *
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    
    @Override
    public List<User> selectByLoginName(User entity) {
        List<FilterRule> filterRules = new ArrayList<>();
        if (null != entity.getId()) {
        	filterRules.add(new FilterRule("id","!=",entity.getId() + "%"));
        }
        if (StringUtils.isNotBlank(entity.getLoginName())) {
        	filterRules.add(new FilterRule("login_name","=",entity.getLoginName()));
        }
        return this.findAll(filterRules );
    }

    @Override
    public void insertByVo(User entity) {
        User user = BeanUtils.copy(entity, User.class);
        user.setCreateTime(new Date());
        this.save(user);
        
        Long id = user.getId();
        String[] roles = entity.getRoleIds().split(",");
        UserRole userRole = new UserRole();

        for (String string : roles) {
            userRole.setUserId(id);
            userRole.setRoleId(Long.valueOf(string));
            userRoleMapper.save(userRole);
        }
    }

    @Override
    public User selectVoById(Long id) {
        return userMapper.selectUserVoById(id);
    }

    @Override
    public void updateByVo(User entity) {
        User user = BeanUtils.copy(entity, User.class);
        if (StringUtils.isBlank(user.getPassword())) {
            user.setPassword(null);
        }
        this.update(user);
        
        Long id = entity.getId();
        List<UserRole> userRoles = userRoleMapper.selectByUserId(id);
        if (userRoles != null && !userRoles.isEmpty()) {
            for (UserRole userRole : userRoles) {
                userRoleMapper.delete(userRole.getId());
            }
        }

        String[] roles = entity.getRoleIds().split(",");
        UserRole userRole = new UserRole();
        for (String string : roles) {
            userRole.setUserId(id);
            userRole.setRoleId(Long.valueOf(string));
            userRoleMapper.save(userRole);
        }
    }

    @Override
    public void updatePwdByUserId(Long userId, String md5Hex) {
        User user = new User();
        user.setId(userId);
        user.setPassword(md5Hex);
        this.update(user);
    }

    @Override
    public void deleteUserById(Long id) {
        this.delete(id.toString());
        List<UserRole> userRoles = userRoleMapper.selectByUserId(id);
        if (userRoles != null && !userRoles.isEmpty()) {
            for (UserRole userRole : userRoles) {
                userRoleMapper.delete(userRole.getId());
            }
        }
    }

	@Override
	protected BaseMapper<User> getDAO() {
		return userMapper;
	}

}