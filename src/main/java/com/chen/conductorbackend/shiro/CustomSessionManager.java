package com.chen.conductorbackend.shiro;

import io.micrometer.core.instrument.util.StringUtils;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * 自定义的session管理器
 */
public class CustomSessionManager extends DefaultWebSessionManager {
    /**
     * 获取请求头中key为“JSESSIONID”的value == sessionId
     */
    private static final String JSESSIONID ="JSESSIONID";

    //private static final String REFERENCED_SESSION_ID_SOURCE = "cookie";

    /**
     * 自定义session获取方式
     */
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        //自定义方式
        String sessionId = WebUtils.toHttp(request).getHeader(JSESSIONID);
        if (StringUtils.isNotEmpty(sessionId)) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, ShiroHttpServletRequest.COOKIE_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, sessionId);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return sessionId;
        }
        //默认方式
        return super.getSessionId(request, response);
    }
}
