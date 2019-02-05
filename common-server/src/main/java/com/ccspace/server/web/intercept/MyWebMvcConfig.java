package com.ccspace.server.web.intercept;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * @AUTHOR CF
 * @DATE Created on 2018/12/1/001 18:22.
 */
@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {

    @Autowired
    SessionInterceptor sessionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getExceptionInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**")
        .excludePathPatterns("/v1/user/llRealTime/**", "/notice/**");
    }
    @Bean
     public ExceptionInterceptor getExceptionInterceptor(){
        return new ExceptionInterceptor();
    }

     /**
     * 配置自定义的HttpMessageConverter
     *注：
     *1.configureMessageConverters:
      * 重载会覆盖掉spring mvc默认注册的 多个HttpMessageConverter。
     *2.extendsMessageConverter：仅添加一个自定义 的HttpMessageConverter，
      * 不覆盖默认注册 的HttpMessageConverter.
     **/
    //使用extendsMessageConverter 添加一个自定义的HttpMessageConverter
    @Bean
    public HttpMessageConverter fastJsonHttpMessageConverter(){
        //创建FastJson信息转换对象
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();

        //创建Fastjosn对象并设定序列化规则
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        // 中文乱码解决方案
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);//设定json格式且编码为UTF-8
        mediaTypes.add(MediaType.MULTIPART_FORM_DATA);
        fastJsonHttpMessageConverter.setSupportedMediaTypes(mediaTypes);

        //规则赋予转换对象
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);

        return fastJsonHttpMessageConverter;

    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(fastJsonHttpMessageConverter());
    }


   /* @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        //resolveLazily属性启用是为了推迟文件解析，以在在UploadAction中捕获文件大小异常
        resolver.setResolveLazily(true);
        resolver.setMaxInMemorySize(40960);
        //上传文件大小 50M 50*1024*1024
        resolver.setMaxUploadSize(50 * 1024 * 1024);
        return resolver;

    }*/

}
