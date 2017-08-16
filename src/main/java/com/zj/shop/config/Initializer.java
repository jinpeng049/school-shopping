package com.zj.shop.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.zj.shop.security.filter.XssFilter;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.util.Log4jConfigListener;

import javax.servlet.*;
import java.util.EnumSet;

public class Initializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext container) throws ServletException {

        container.setInitParameter("webAppRootKey", "school-shopping");

        FilterRegistration.Dynamic characterEncodingFilter = container.addFilter("CharacterEncodingFilter", CharacterEncodingFilter.class);
        characterEncodingFilter.setInitParameter("encoding", "UTF-8");
        characterEncodingFilter.setInitParameter("forceEncoding", "true");
        characterEncodingFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");

        container.setInitParameter("log4jConfigLocation", "classpath:log4j.properties");
        container.addListener(Log4jConfigListener.class);

        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(Configurator.class, MyBatisMapperScannerConfigurer.class);
        container.addListener(new ContextLoaderListener(context));

        AnnotationConfigWebApplicationContext dispatcherServletContext = new AnnotationConfigWebApplicationContext();
        dispatcherServletContext.register(MVCConfigurator.class);
        ServletRegistration.Dynamic dispatcherServlet = container.addServlet("DispatcherServlet", new DispatcherServlet(dispatcherServletContext));
        dispatcherServlet.setLoadOnStartup(1);
        dispatcherServlet.addMapping("/");

        ServletRegistration.Dynamic statViewServlet = container.addServlet("StatViewServlet", StatViewServlet.class);
        statViewServlet.setInitParameter("allow", "127.0.0.1");
        statViewServlet.addMapping("/druid/*");

        XssFilter xssSqlFilter = new XssFilter();
        FilterRegistration.Dynamic filterRegistration = container.addFilter("xssSqlFilter", xssSqlFilter);
        filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/");

        LogFactory.useLog4JLogging();

    }

}
