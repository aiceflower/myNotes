package com.kankan.service.admin.tmp;

import com.kankan.cache.CacheHelper;
import com.kankan.cache.CacheStrategy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.UUID;

@Service
public class TokenService {

    /**
     * redis操作
     */
    @Autowired
    private CacheHelper cacheHelper;

    private CacheStrategy getCache()
    {
        CacheStrategy cacheStrategy = cacheHelper.newCache("APIIDEM"+TokenService.class);
        return cacheStrategy;
    }

    public String createToken(){
        StringBuilder sb = new StringBuilder();
        sb.append(Constant.Redis.TOKEN_PREFIX).append(UUID.randomUUID().toString().replaceAll("-",""));
        getCache().put(sb.toString(),sb.toString(),30L);
        return sb.toString();
    }

    public void chech(HttpServletRequest request){

        String token = request.getHeader(Constant.Redis.TOKEN_HEAD);
        //header中没有token
        if (StringUtils.isBlank(token)){
            token = request.getParameter(Constant.Redis.TOKEN_HEAD);
            if (StringUtils.isBlank(token)){
                throw new IllegalArgumentException("非法的参数异常");
            }
        }
        if (StringUtils.isBlank(getCache().get(String.class,token))){
            throw new RuntimeException("无效或重复的token");
        }

        getCache().remove(token);
        //不能只走删除而不判断是否删除成功，会出现并发安全问题，可能多个线程同时走到这一行，此时token未删除，继续往下执行，只能让一次删除成功
        /*Long id = getCache().remove(token);
        if (id <= 0){
            throw new RuntimeException("删除失败");
        }*/
    }

    /**
     * 添加到启动类中，解决跨域问题
     * @return
     */
    @Bean
    public CorsFilter corsFilter(){
        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}


@Configuration
class ApiConfiguration implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //接口幂等性拦截器
        registry.addInterceptor(apiIdempotentInterceptor());
    }

    @Bean
    public ApiIdempotentInterceptor apiIdempotentInterceptor(){
        return new ApiIdempotentInterceptor();
    }
}
/**
 * 需要保证接口幂等性的接口上加此注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@interface ApiIdempotent {
}

/**
 * 常量类
 */
class Constant {
    public static class Redis{
        public static final String TOKEN_PREFIX = "api";
        public static final String TOKEN_HEAD = "api_head";
    }
}


/**
 * 接口幂等性拦截器，防止重复提交
 * 原理：先获取token(存放于redis中)，请求接口时带上token，token使用后删除。发现无token或token无效请求失败
 */
class ApiIdempotentInterceptor implements HandlerInterceptor {

    @Autowired
    TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        Method method = handlerMethod.getMethod();
        ApiIdempotent apiIdempotent = method.getAnnotation(ApiIdempotent.class);
        if (apiIdempotent != null){
            check(request);
        }
        return true;
    }

    private void check(HttpServletRequest request) {
        tokenService.chech(request);
    }
}
