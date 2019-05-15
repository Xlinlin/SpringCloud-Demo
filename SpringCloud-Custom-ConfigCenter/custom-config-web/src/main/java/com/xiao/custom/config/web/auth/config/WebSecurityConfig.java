package com.xiao.custom.config.web.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * springoot + Security + jwt登录认证
 * Author: JoeTao
 * createAt: 2018/9/14
 */
@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Resource(name = "restAuthenticationAccessDeniedHandler")
    private AccessDeniedHandler accessDeniedHandler;

    @Resource(name = "configUserDetailsService")
    private UserDetailsService configUserDetailsService;

    @Autowired
    private JwtAuthenticationTokenFilter authenticationTokenFilter;

    @Value("${config.center.anonymous.urls}")
    private String anonymousUrls;

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception
    {
        authenticationManagerBuilder
                // 设置UserDetailsService
                .userDetailsService(configUserDetailsService)
                // 使用BCrypt进行密码的hash
                .passwordEncoder(passwordEncoder());
    }

    /**
     * 装载BCrypt密码编码器
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception
    {
        // 基于token 接口间鉴权
        //        tokenConfigure(httpSecurity);

        // 基于session 适应于浏览器
        sessionConfig(httpSecurity);

        httpSecurity.authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                //排除某些指定页面
                .antMatchers(anonymousUrls.split(",")).permitAll().anyRequest().authenticated();

        // 禁用缓存
        httpSecurity.headers().cacheControl();
        // 添加JWT filter
        httpSecurity.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 设置 鉴权失败和无权的处理
        httpSecurity.exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .accessDeniedHandler(accessDeniedHandler);
    }

    private void sessionConfig(HttpSecurity httpSecurity) throws Exception
    {
        //默认采用httpsession的方式
        SessionCreationPolicy sessionPolicy = SessionCreationPolicy.IF_REQUIRED;
        //屏蔽csrf，否则post无法访问
        httpSecurity.cors().and().csrf().disable()
                //session管理策略
                .sessionManagement().sessionCreationPolicy(sessionPolicy);

    }

    private void tokenConfigure(HttpSecurity httpSecurity) throws Exception
    {
        httpSecurity
                // 由于使用的是JWT，我们这里不需要csrf
                .csrf().disable()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(WebSecurity web)
    {
        //忽略鉴权的请求
        web.ignoring().antMatchers("/html/assets/**").antMatchers("/favicon.ico");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception
    {
        return super.authenticationManagerBean();
    }
}