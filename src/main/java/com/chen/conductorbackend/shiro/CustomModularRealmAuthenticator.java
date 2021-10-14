package com.chen.conductorbackend.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 自定义的登录验证类
 */
public class CustomModularRealmAuthenticator extends ModularRealmAuthenticator {

    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken)
        throws AuthenticationException {
        // 断言Realm是否配置了
        assertRealmsConfigured();
        CustomLoginToken token = (CustomLoginToken) authenticationToken;
        // 登录类型
        String loginType = token.getLoginType().getType();
        // 所有Realm  在shiroConfig中进行了设置
        Collection<Realm> realms = getRealms();
        // 登录类型对应的所有Realm
        Collection<Realm> typeRealms = new ArrayList<>();
        for (Realm realm : realms) {
            //如果类型匹配上了，我们就添加进去
            if (realm.getName().contains(loginType)){
                typeRealms.add(realm);
            }
        }
        // 判断是单Realm还是多Realm
        if (typeRealms.size() == 1) {
            return doSingleRealmAuthentication(typeRealms.iterator().next(), token);
        }
        else {
            return doMultiRealmAuthentication(typeRealms, token);
        }
    }


}
