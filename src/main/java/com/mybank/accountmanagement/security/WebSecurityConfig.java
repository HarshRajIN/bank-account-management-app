package com.mybank.accountmanagement.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    private Integer securityStrength = 12;

    @Bean
    protected RestAuthenticationEntryPoint authenticationEntryPoint(){
        return new RestAuthenticationEntryPoint();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        String[] resources = new String[]{
                "/resources/**", "/static/**","/css/**","/images/**",
                "/v2/api-docs",
                "/swagger-resources/**",
                "/swagger-resources/configuration/ui",
                "/swagger-resources/configuration/security",
                "/swagger-ui.html**",
        };
        httpSecurity
                .authorizeRequests()
                    .antMatchers(resources).permitAll()
                    .antMatchers(("/h2-console/**")).permitAll()
                    .antMatchers("/", "/login/").permitAll()
                    .antMatchers(HttpMethod.OPTIONS).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().defaultSuccessUrl("/dashboard.html")
                .failureUrl("/login-error")
                .loginPage("/login").permitAll()
                .and().httpBasic().authenticationEntryPoint(authenticationEntryPoint())
                .and().headers().frameOptions().disable()
                .and().csrf().disable(); //disable Cross-site request forgery


    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(securityStrength);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                .withUser("foo")
                .password(passwordEncoder().encode("password"))
                .roles("USER")

                .and()
                .withUser("admin")
                .password(passwordEncoder().encode("password"))
                .authorities("WRITE_PRIVILEGES", "READ_PRIVILEGES")
                .roles("ADMIN");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**");
    }
}