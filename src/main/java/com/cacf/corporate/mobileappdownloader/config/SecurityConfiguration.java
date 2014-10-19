package com.cacf.corporate.mobileappdownloader.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.inject.Inject;

/**
 * Created by cacf on 09/10/14.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Inject
    private Environment env;

    private static final String USERNAME_CONFIG_KEY = "mad.username";
    private static final String PASSWORD_CONFIG_KEY = "mad.password";

    @Inject
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        String user = env.getProperty(USERNAME_CONFIG_KEY,"u");
        String password = env.getProperty(PASSWORD_CONFIG_KEY,"p");

        auth
                .inMemoryAuthentication()
                .withUser(user).password(password).roles("DOWNLOADER");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {

        web.ignoring()
                .antMatchers("/webjars/**");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .antMatchers("/download").authenticated()
                .and()
                .csrf().disable()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login/auth")
                .defaultSuccessUrl("/download",true)
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll();


    }

    @EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
    private static class GlobalSecurityConfiguration extends GlobalMethodSecurityConfiguration {
    }

}
