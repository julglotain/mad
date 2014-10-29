package com.cacf.corporate.mobileappdownloader.config;

import com.cacf.corporate.mobileappdownloader.security.AuthorityBasedRedirectionSuccessHandler;
import com.cacf.corporate.mobileappdownloader.usermanagement.GrantedAuthorityImpl;
import com.cacf.corporate.mobileappdownloader.usermanagement.UserManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
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
    private UserManager userManager;

    @Inject
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userManager).passwordEncoder(new Md5PasswordEncoder());

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
                .antMatchers("/download/").authenticated()
                .antMatchers("/download.json").authenticated()
                .antMatchers("/admin").hasAuthority(GrantedAuthorityImpl.ADMIN.getAuthority())
                .antMatchers("/admin/**").hasAuthority(GrantedAuthorityImpl.ADMIN.getAuthority())
                .and()
                .exceptionHandling().accessDeniedPage("/accessDenied")
                .and()
                .csrf().disable()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login/auth")
                .successHandler(new AuthorityBasedRedirectionSuccessHandler())
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll();


    }

    @EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
    private static class GlobalSecurityConfiguration extends GlobalMethodSecurityConfiguration {
    }

}
