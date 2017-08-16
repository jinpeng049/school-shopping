package com.zj.shop.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import com.zj.shop.security.interceptor.AvoidDuplicateSubmissionInterceptor;
import com.zj.shop.security.interceptor.SessionCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.util.UrlPathHelper;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.zj.shop.controller", "com.zj.shop.service", "com.zj.shop.logaop"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class MVCConfigurator extends WebMvcConfigurerAdapter {

    @Autowired
    private SessionCheckInterceptor             sessionCheckInterceptor;
    @Autowired
    private AvoidDuplicateSubmissionInterceptor duplicateSubmissionInterceptor;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // enable matrix variable support
        if (configurer.getUrlPathHelper() == null) {
            UrlPathHelper urlPathHelper = new UrlPathHelper();
            urlPathHelper.setRemoveSemicolonContent(false);
            configurer.setUrlPathHelper(urlPathHelper);
        } else {
            configurer.getUrlPathHelper().setRemoveSemicolonContent(false);
        }
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
        /**
         ObjectMapper objectMapper = new ObjectMapper();
         objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
         objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
         */

        ArrayList supportedMediaTypes = new ArrayList();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
        supportedMediaTypes.add(MediaType.TEXT_HTML);
        //        supportedMediaTypes.add(MediaType.MULTIPART_FORM_DATA);

        /**
         MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
         jackson2HttpMessageConverter.setObjectMapper(objectMapper);
         jackson2HttpMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
         */

        FastJsonHttpMessageConverter4 fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter4();
        FastJsonConfig fastJsonConfig = fastJsonHttpMessageConverter.getFastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect);
        // fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        fastJsonHttpMessageConverter.setSupportedMediaTypes(supportedMediaTypes);

        messageConverters.add(fastJsonHttpMessageConverter);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String[] resources = {"/css/**", "/image/**", "/js/**", "/jslib/**", "/style/**", "/error/**", "/json/**", "/ueditor/**", "/model/**", "/m/**", "/website/**"};
        String[] locations = {"/css/", "/image/", "/js/", "/jslib/", "/style/", "/error/", "/json/", "/ueditor/", "/model/", "/m/", "/website/"};
        registry.addResourceHandler(resources).addResourceLocations(locations);
    }

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/view/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setDefaultEncoding("UTF-8");
        multipartResolver.setMaxUploadSize(10485760000l); //1024*200即200k
        multipartResolver.setMaxInMemorySize(40960);
        return multipartResolver;
    }

    @Bean
    public SessionCheckInterceptor sessionCheckInterceptor() {
        SessionCheckInterceptor sessionCheckInterceptor = new SessionCheckInterceptor();
        return sessionCheckInterceptor;
    }

    @Bean
    public AvoidDuplicateSubmissionInterceptor duplicateSubmissionInterceptor() {
        AvoidDuplicateSubmissionInterceptor duplicateSubmissionInterceptor = new AvoidDuplicateSubmissionInterceptor();
        return duplicateSubmissionInterceptor;
    }

    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        RequestMappingHandlerMapping requestMappingHandlerMapping = new RequestMappingHandlerMapping();
        requestMappingHandlerMapping.setInterceptors(new Object[]{sessionCheckInterceptor, duplicateSubmissionInterceptor});
        return requestMappingHandlerMapping;
    }

}
