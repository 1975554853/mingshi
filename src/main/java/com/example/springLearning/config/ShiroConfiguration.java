package com.example.springLearning.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration {

    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String,String> defaultFilterMap = new LinkedHashMap<>();
        defaultFilterMap.put("/logout","logout");

//        放行静态资源
        defaultFilterMap.put("/login","anon");
        defaultFilterMap.put("/user/login","anon");
        defaultFilterMap.put("/","anon");
//        defaultFilterMap.put("/admin/css/**","anon");
//        defaultFilterMap.put("/admin/player/**","anon");
//        defaultFilterMap.put("/admin/js/**","anon");
//        defaultFilterMap.put("/admin/images/**","anon");
//        defaultFilterMap.put("/admin/fonts/**","anon");
//        defaultFilterMap.put("/admin/lib/**","anon");
//        defaultFilterMap.put("/admin/**","roles[administrator]");
//
//        defaultFilterMap.put("/index","anon");
//        defaultFilterMap.put("/register","anon");
//        defaultFilterMap.put("/sendCode","anon");
//
//        defaultFilterMap.put("/video","anon");
//        defaultFilterMap.put("/video/dir/*","anon");
//        defaultFilterMap.put("/video_detail/**","anon");
//        defaultFilterMap.put("/video/**","authc");
//
//        defaultFilterMap.put("/css/**","anon");
//        defaultFilterMap.put("/fonts/**","anon");
//        defaultFilterMap.put("/img/**","anon");
//        defaultFilterMap.put("/js/**","anon");
//        defaultFilterMap.put("/player/**","anon");
//        defaultFilterMap.put("/imageCode/**","anon");
//        defaultFilterMap.put("/images/**","anon");
        shiroFilterFactoryBean.setLoginUrl("/login");
//        shiroFilterFactoryBean.setSuccessUrl("/video");
        shiroFilterFactoryBean.setUnauthorizedUrl("/login");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(defaultFilterMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager defaultSecurityManager = new DefaultWebSecurityManager();
        defaultSecurityManager.setRealm(myShiroRealm());
        return defaultSecurityManager;
    }

    @Bean
    public ShiroRealm myShiroRealm(){
        ShiroRealm myShiroRealm = new ShiroRealm();
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myShiroRealm;
    }


    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(5);
        return hashedCredentialsMatcher;
    }

}
