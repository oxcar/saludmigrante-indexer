package com.copili.indexer.configuration;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebMvc
@EnableScheduling
@ComponentScan("com.copili.indexer")
public class WebAppConfiguration extends WebMvcConfigurerAdapter {

    private final static Logger log = LoggerFactory.getLogger(WebAppConfiguration.class);

    @Override
    public void addResourceHandlers( ResourceHandlerRegistry resourceHandlerRegistry ) {
        log.debug( "Add Resource Handlers" );
        resourceHandlerRegistry.addResourceHandler( "/resources/**" ).addResourceLocations( "/resources/" );
        resourceHandlerRegistry.addResourceHandler( "/views/**" ).addResourceLocations( "/views/" );
        resourceHandlerRegistry.addResourceHandler( "/**" ).addResourceLocations( "/" );
    }

    @Bean
    public ViewResolver internalResourceViewResolver() {
        log.debug( "Internal Resource View Resolver" );
        InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
        internalResourceViewResolver.setViewClass( JstlView.class );
        internalResourceViewResolver.setPrefix( "/WEB-INF/views/jsp/" );
        internalResourceViewResolver.setSuffix( ".jsp" );
        return internalResourceViewResolver;
    }


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver = new PageableHandlerMethodArgumentResolver();
        pageableHandlerMethodArgumentResolver.setMaxPageSize(100);
        PageRequest pageRequest = new PageRequest(0, 10); // Por defecto regresamos la primera pagina y 10 registros
        pageableHandlerMethodArgumentResolver.setFallbackPageable(pageRequest);
        argumentResolvers.add(pageableHandlerMethodArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.debug("Agregando Interceptors");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.debug("Configurando MessageConverters");
        converters.add(byteArrayConverter());
        converters.add(jsonConverter());
        converters.add(stringConverter());
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        log.debug("Instanciando ResourceBundleMessageSource");
        ResourceBundleMessageSource resource = new ResourceBundleMessageSource();
        resource.setBasenames("messages", "validation");
        resource.setCacheSeconds(0);
        resource.setUseCodeAsDefaultMessage(true);
        resource.setDefaultEncoding("UTF-8");
        return resource;
    }

    @Bean
    public MappingJackson2HttpMessageConverter jsonConverter() {
        log.debug("Instanciando MappingJackson2HttpMessageConverter");
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setPrefixJson(false);
        jsonConverter.setPrettyPrint(true);
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        jsonConverter.setSupportedMediaTypes(supportedMediaTypes);
        jsonConverter.getObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return jsonConverter;
    }

    @Bean
    public StringHttpMessageConverter stringConverter() {
        log.debug("Instanciando StringHttpMessageConverter");
        StringHttpMessageConverter converter = new StringHttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
        converter.setSupportedMediaTypes(supportedMediaTypes);
        return converter;
    }

    @Bean
    public ByteArrayHttpMessageConverter byteArrayConverter() {
        log.debug("Instanciando ByteArrayHttpMessageConverter");
        ByteArrayHttpMessageConverter converter = new ByteArrayHttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        converter.setSupportedMediaTypes(supportedMediaTypes);
        return converter;
    }

}
