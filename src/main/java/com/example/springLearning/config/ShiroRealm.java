package com.example.springLearning.config;


import com.example.springLearning.dao.RoleDao;
import com.example.springLearning.dao.UserDao;
import com.example.springLearning.pojo.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;


public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserDao userDao ;
    @Autowired
    private RoleDao roleDao ;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        
        String username = (String) authenticationToken.getPrincipal();
        // 获取身份证
        User user = userDao.queryUserByCard(username);
        if(user == null){
            return null;
        }
        return new SimpleAuthenticationInfo( user,user.getPassword(),ByteSource.Util.bytes(user.getUsername()),getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User user = (User) principalCollection.getPrimaryPrincipal();
        // 用户拥有的角色
        Set<String> roles = roleDao.queryRoleNameByUserId(user.getId());
        authorizationInfo.setRoles(roles);
//        Set<String> permissions = permissionService.queryPermissionByUser(user);
//        authorizationInfo.setStringPermissions(permissions);
        return authorizationInfo;
    }


}
