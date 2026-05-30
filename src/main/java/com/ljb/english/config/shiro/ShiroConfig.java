package com.ljb.english.config.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class ShiroConfig {
    //Filter工厂，设置对应的过滤条件和跳转条件
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) throws IOException {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> map = new HashMap<>();

        //添加shiro 的内置过滤器
        /*
        anon：无需认证就可以访问
        authc：必须认证才能访问
        user：必须拥有记住我 功能才能用
        perms：拥有对某个资源的权限才能访问
        role：拥有某个角色才能访问
        logout：注销功能
         */

        //静态资源可匿名访问
        map.put("/index", "anon");
        map.put("/toIndex", "anon");
        map.put("/english/login", "anon");
        map.put("/logout", "anon");
        map.put("/toLogin", "anon");
        map.put("/static/**", "anon");

        map.put("/audio/**", "authc");
        map.put("/toReview/**", "authc");
        map.put("/english/**", "authc");
        map.put("/toStart", "authc");

        map.put("/a/*", "authc");

        //登出
        // map.put("/logout", "logout");
        //开启rememberMe功能用user，否则用authc
        // map.put("/admin/*", "user");
        //登录

        //首页
        shiroFilterFactoryBean.setSuccessUrl("/index");

        //错误页面，认证不通过跳转
        shiroFilterFactoryBean.setUnauthorizedUrl("/index");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }


    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     */
    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     */

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 开启 在页面使用shiro标签
     * <shiro:guest> 用户没有身份验证时显示相应信息，即游客访问信息。
     * <shiro:user>  用户已经身份验证/记住我登录后显示相应的信息。
     * <shiro:authenticated>　 用户已经身份验证通过，即Subject.login登录成功，不是记住我登录的。
     * <shiro:notAuthenticated>  用户已经身份验证通过，即没有调用Subject.login进行登录，包括记住我自动登录的也属于未进行身份验证。
     * <shiro:principal property="username"/>  相当于((User)Subject.getPrincipals()).getUsername()。
     * <shiro:lacksPermission name="org:create">　如果当前Subject没有权限将显示body体内容。
     * <shiro:hasRole name="admin">　如果当前Subject有角色将显示body体内容。
     * <shiro:hasAnyRoles name="admin,user"> 如果当前Subject有任意一个角色（或的关系）将显示body体内容。
     * <shiro:lacksRole name="abc">　如果当前Subject没有角色将显示body体内容。
     * <shiro:hasPermission name="user:create">　如果当前Subject有权限将显示body体内容
     */
    @Bean(name = "shiroDialect")
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }


    /**
     * 安全管理器
     *
     * @param userRealm 用户领域
     * @return {@link SecurityManager}
     */
    @Bean
    public SecurityManager securityManager(@Qualifier("userRealm") UserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联userRealm
        securityManager.setRealm(userRealm);
        // SESSIONID
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    /**
     * 散列凭证匹配器
     * 密码匹配凭证管理器
     *
     * @return {@link HashedCredentialsMatcher}
     */
    @Bean(name = "hashedCredentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        // 采用MD5方式加密
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        // 设置加密次数
        hashedCredentialsMatcher.setHashIterations(1024);
        return hashedCredentialsMatcher;
    }

    /**
     * 用户领域
     * 将自己的验证方式加入容器
     *
     * @param matcher 匹配器
     * @return {@link UserRealm}
     */
    @Bean(name = "userRealm")
    public UserRealm userRealm(@Qualifier("hashedCredentialsMatcher") HashedCredentialsMatcher matcher) {
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(matcher);
        return userRealm;
    }

    /**
     * 去掉shiro登录时url里的JSESSIONID
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        // 设置session过期时间
        sessionManager.setGlobalSessionTimeout(60 * 60 * 24 * 30 * 1000L);
        return sessionManager;
    }
}