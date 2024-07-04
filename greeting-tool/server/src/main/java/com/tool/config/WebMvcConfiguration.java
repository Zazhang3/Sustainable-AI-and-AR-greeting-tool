package com.tool.config;

import com.tool.interceptor.JwtTokenAdminInterceptor;
import com.tool.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

/**
 * Configuration class for registering web tier related components
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    /**
     * Register a Custom Interceptor
     * @param registry
     */
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("Registering a Custom Interceptor...");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/user/**")
                .addPathPatterns("/greeting_card/**")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/register")
                .excludePathPatterns("/user/update")
                .excludePathPatterns("/user/verification");
    }

    /**
     * Generate interface documentation with Knife4j
     * @return Docket
     */
    @Bean
    public Docket docket() {
        log.info("Start generating interface documentation");
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("GreetingTool Api Documentation")
                .version("1.0")
                .description("GreetingTool Api Documentation")
                .build();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.tool.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    /**
     * Set up static resource mapping
     * @param registry
     */
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/static/");
    }

    /**
     * Extend the SpringMVC Message Converter
     * @param converters
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //New Message Converter
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        //Set up a message converter specifics
        converter.setObjectMapper(new JacksonObjectMapper());
        //Add to the framework, with 0 being the highest priority
        converters.add(0,converter);
    }
}
