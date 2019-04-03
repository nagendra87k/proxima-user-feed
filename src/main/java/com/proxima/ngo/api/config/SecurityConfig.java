package com.proxima.ngo.api.config;

import com.proxima.ngo.api.security.CustomUserDetailsService;
import com.proxima.ngo.api.security.JwtAuthenticationEntryPoint;
import com.proxima.ngo.api.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf()
                    .disable()
                .exceptionHandling()
                    .authenticationEntryPoint(unauthorizedHandler)
                    .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .authorizeRequests()
                    .antMatchers("/",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js")
                        .permitAll()
                    .antMatchers("/api/auth/**","/organization/auth/signin","/organization/auth/signup")
                        .permitAll()
                    .antMatchers("/api/user/checkUsernameAvailability", "/api/user/checkEmailAvailability","/api/user/sendOTP","/api/user/validateOTP","/api/user/resetPassword","/api/user/profile","/api/user/profilePic","/api/user/loadfile")
                        .permitAll()
                    .antMatchers("/organization/user/checkUsernameAvailability", "/organization/user/checkEmailAvailability","/organization/user/sendOTP","/organization/user/validateOTP","/organization/user/resetPassword","/organization/user/profile","/organization/user/profilePic","/organization/user/loadfile")
                    .permitAll()
                .antMatchers("/cause/casueType","/cause/create","/cause/organization","/cause/details","/cause/update","/cause/delete","/cause/causeList","/cause/loadfile","/cause/create-post","/cause/feeds")
                .permitAll()
                .antMatchers(HttpMethod.GET,  "/api/polls/**","/api/users/**","/organization/users/**","/cause/loadfile/**")
                .permitAll()
                .anyRequest()
                .authenticated();
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    }
}