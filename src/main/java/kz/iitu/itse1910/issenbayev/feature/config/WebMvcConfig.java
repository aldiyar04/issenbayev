package kz.iitu.itse1910.issenbayev.feature.config;

import kz.iitu.itse1910.issenbayev.controller.compoundrequestparam.argumentresolver.CompoundRequestParamArgumentResolver;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@AllArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final ApplicationContext context;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(compoundRequestParamArgumentResolver());
    }

    private CompoundRequestParamArgumentResolver compoundRequestParamArgumentResolver() {
        return context.getBean(CompoundRequestParamArgumentResolver.class);
    }
}
