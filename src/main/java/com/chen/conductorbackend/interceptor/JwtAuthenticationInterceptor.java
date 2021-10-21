//package com.chen.conductorbackend.interceptor;
//
//import cn.krl.visiteducationbackend.common.annotation.PassToken;
//import cn.krl.visiteducationbackend.common.utils.JwtUtil;
//import cn.krl.visiteducationbackend.entity.Admin;
//import cn.krl.visiteducationbackend.service.IAdminService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.lang.reflect.Method;
//
//
//public class JwtAuthenticationInterceptor implements HandlerInterceptor {
//    @Autowired
//    IAdminService adminService;
//
//    @Override
//    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
//        //options请求直接返回ok
//        if(httpServletRequest.getMethod().equals("OPTIONS")){
//            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
//            return true;
//        }
//
//        // 从请求头中取出 token  这里需要和前端约定好把jwt放到请求头一个叫token的地方
//         String token = httpServletRequest.getHeader("token");
//        // 如果不是映射到方法直接通过
//        if (!(object instanceof HandlerMethod)) {
//            return true;
//        }
//        HandlerMethod handlerMethod = (HandlerMethod) object;
//        Method method = handlerMethod.getMethod();
//        //检查是否有passtoken注释，有则跳过认证
//        if (method.isAnnotationPresent(PassToken.class)) {
//            PassToken passToken = method.getAnnotation(PassToken.class);
//            if (passToken.required()) {
//                return true;
//            }
//        }
//        //默认全部检查
//        else {
//            // 执行认证
//            if (token == null) {
//                //这里其实是登录失效,没token了
//                System.out.println("需要登录");
//            }
//            // 获取 token 中的 user Name
//            String id = JwtUtil.getAudience(token);
//            Admin admin = adminService.getById(id);
//            if (admin == null) {
//                System.out.println("用户不存在");
//            }
//            // 验证 token
//            JwtUtil.verifyToken(token);
//            return true;
//        }
//        return true;
//    }
//
////    @Override
////    public void postHandle(HttpServletRequest httpServletRequest,
////                           HttpServletResponse httpServletResponse,
////                           Object o, ModelAndView modelAndView) throws Exception {
////
////    }
////
////    @Override
////    public void afterCompletion(HttpServletRequest httpServletRequest,
////                                HttpServletResponse httpServletResponse,
////                                Object o, Exception e) throws Exception {
////    }
//}
