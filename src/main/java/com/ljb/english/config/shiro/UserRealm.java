package com.ljb.english.config.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ljb.english.mapper.EUserMapper;
import com.ljb.english.pojo.EUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class UserRealm extends AuthorizingRealm {

    @Autowired
    EUserMapper userMapper;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        // //获取角色信息进行验证
        // // 1、获取用户身份信息
        // String principal = principalCollection.getPrimaryPrincipal().toString();
        // // 2、调用业务层获取用户的角色信息（数据库，一个人可以多个角色）
        // Collection<String> roles = adminService.queryAdmin(***);
        // // 3、创建对象，封装当前的登录用户的角色、权限信息（在controller中，通过注解方式，验证角色信息是否可以访问当前链接，下同）
        // SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // info.addRoles(roles);


        //获取权限信息进行验证
        //创建对象，封装当前登录用户的角色，权限信息
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //拿到当前用户登录的信息
        Subject subject = SecurityUtils.getSubject();

        //拿到用户对象
        EUser currentUser = (EUser) subject.getPrincipal();

        //设置当前用户权限
        info.addStringPermission(currentUser.getURank());

        return info;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        //用户名，密码  ~ 数据库取

        EUser user = null;
        QueryWrapper<EUser> wrapper = new QueryWrapper<>();
        log.info("user_name:{}", usernamePasswordToken.getUsername());
        wrapper.eq("u_name", usernamePasswordToken.getUsername());
        try {
            user = userMapper.selectOne(wrapper);
            log.info("user realm:{}", user);
        } catch (Exception e) {
            return null;
        }

        if (user == null) {  //没有这个人
            return null;
        }
        //密码认证 shiro 自动做，  加密
        /*
        ====盐值加密====
            1. 以用户名作为盐值
        */
        ByteSource salt = ByteSource.Util.bytes(user.getUName());
        //密码验证：Shiro做
        return new SimpleAuthenticationInfo(user, user.getUPwd(), salt, this.getName());

    }
}