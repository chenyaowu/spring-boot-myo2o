package com.chen.myo2o.config.web;

import com.chen.myo2o.interceptor.shopadmin.ShopLoginInteceptor;
import com.chen.myo2o.interceptor.shopadmin.ShopPermissionInterceptor;
import com.google.code.kaptcha.servlet.KaptchaServlet;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * 开启Mvc,自动注入spring容器，WebMvcConfigurationAdapter:配置视图解析器
 * 当一个类实现了这个接口（ApplicationContextAware）之后，这个类就可以方便获得ApplicationContext
 * 中所有的bean
 */
@Configuration
@EnableWebMvc
public class MvcConfiguration extends WebMvcConfigurerAdapter implements ApplicationContextAware{
    //Spring容器
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations("file:/E:/projectdev/image/upload/");
    }

    /**
     * 视图解析器
     * @return
     */
    @Bean(name ="viewResolver" )
    public ViewResolver createViewResolver(){
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        //设置Spring容器
        viewResolver.setApplicationContext(applicationContext);
        //取消缓存
        viewResolver.setCache(false);
        //设置解析的前缀
        viewResolver.setPrefix("/WEB-INF/html/");
        //设置视图解析后缀
        viewResolver.setSuffix(".html");
        return viewResolver;
    }

    /**
     * 文件上传解析器
     * @return
     */
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver createCommonsMultipartResolver(){
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setDefaultEncoding("utf-8");
        multipartResolver.setMaxUploadSize(1024*1024*20);
        multipartResolver.setMaxInMemorySize(1024*1024*20);
        return multipartResolver;
    }

    @Value("${kaptcha.border}")
    private String border;
    @Value("${kaptcha.textproducer.font.color}")
    private String fcolor;
    @Value("${kaptcha.image.width}")
    private String width;
    @Value("${kaptcha.image.height}")
    private String height;
    @Value("${kaptcha.textproducer.font.size}")
    private String fsize;
    @Value("${kaptcha.textproducer.char.string}")
    private String cstring;
    @Value("${kaptcha.noise.color}")
    private String color;
    @Value("${kaptcha.textproducer.char.length}")
    private String clength;
    @Value("${kaptcha.textproducer.font.names}")
    private String fnames;


    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        ServletRegistrationBean servlet = new ServletRegistrationBean(new KaptchaServlet(),"/Kaptcha");
        servlet.addInitParameter("kaptcha.border",border);
        servlet.addInitParameter("kaptcha.textproducer.font.color",fcolor);
        servlet.addInitParameter("kaptcha.image.width",width);
        servlet.addInitParameter("kaptcha.image.height",height);
        servlet.addInitParameter("kaptcha.textproducer.font.size",fsize);
        servlet.addInitParameter("kaptcha.textproducer.char.string",cstring);
        servlet.addInitParameter("kaptcha.noise.color",color);
        servlet.addInitParameter("kaptcha.textproducer.char.length",clength);
        servlet.addInitParameter("kaptcha.textproducer.font.names",fnames);
        return servlet;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String intercaptPath = "/shopadmin/**";
        //注册拦截器
        InterceptorRegistration loginIR = registry.addInterceptor(new ShopLoginInteceptor());
        //配置拦截路径;
        loginIR.addPathPatterns(intercaptPath);
        //还可以注册其他的拦截器
        InterceptorRegistration permissionIR = registry.addInterceptor(new ShopPermissionInterceptor());
        //配置拦截路径
        permissionIR.addPathPatterns(intercaptPath);
        //配置不拦截路径

//        shoplist page
        permissionIR.excludePathPatterns("/shopadmin/getshoplist");
        permissionIR.excludePathPatterns("/shopadmin/shoplist");
//        shop register page
        permissionIR.excludePathPatterns("/shopadmin/getshopinitinfo");
        permissionIR.excludePathPatterns("/shopadmin/registershop");
        permissionIR.excludePathPatterns("/shopadmin/shopoperation");
//         shopmanager page shopmanagement
        permissionIR.excludePathPatterns("/shopadmin/shopmanagement");
        permissionIR.excludePathPatterns("/shopadmin/getshopmanagementinfo");
//

    }
}
