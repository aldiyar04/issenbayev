package kz.iitu.itse1910.issenbayev.feature.config;

import kz.iitu.itse1910.issenbayev.feature.security.CustomAuthenticationFailureHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                // AFAIK, there is no option for the Bearer token authentication type:
                .and().formLogin().failureHandler(authenticationFailureHandler())
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and().headers().xssProtection().and().contentSecurityPolicy("script-src 'self'");
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }
}
