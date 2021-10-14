package com.chen.conductorbackend.shiro;

import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Set;

/**
 * 自定义的授权验证类
 */
public class CustomModularRealmAuthorizer extends ModularRealmAuthorizer {
    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        //断言配置
        assertRealmsConfigured();
        //获取到所有realm的名称
        Set<String> realmNames = principals.getRealmNames();
        for (String realmName : realmNames) {
            //遍历所有的realm
            for (Realm realm : realms) {
                if (!(realm instanceof Authorizer)) {
                    continue;
                }
                //如果名称匹配上了，就走这个realm权限验证
                if (realm.getName().contains(realmName)) {
                    return ((Authorizer) realm).isPermitted(principals, permission);
                }
            }
        }
        return false;
    }
}
