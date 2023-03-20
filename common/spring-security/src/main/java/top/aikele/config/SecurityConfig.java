package top.aikele.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import top.aikele.security.JwtFilter;
import top.aikele.security.MyUsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig  {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

                //所有请求必须有授权
                .authorizeRequests(authorize -> authorize
                        .antMatchers("/admin/system/index/login","/favicon.ico","/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**", "/doc.html").permitAll()
                        .anyRequest().authenticated()
                )
                //禁用session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //允许跨域
                .csrf().disable();
//                .formLogin(Customizer.withDefaults());
//                .httpBasic(Customizer.withDefaults());
        //添加自定义过滤器
        http.addFilterAt(myUsernamePasswordAuthenticationFilter(), MyUsernamePasswordAuthenticationFilter.class);
        http.addFilterAt(new JwtFilter(redisTemplate), MyUsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    //添加自定义用户认证过滤器
    @Bean
    MyUsernamePasswordAuthenticationFilter myUsernamePasswordAuthenticationFilter(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        ProviderManager providerManager = new ProviderManager(daoAuthenticationProvider);
        MyUsernamePasswordAuthenticationFilter myUsernamePasswordAuthenticationFilter = new MyUsernamePasswordAuthenticationFilter(providerManager,redisTemplate);
        return myUsernamePasswordAuthenticationFilter;
    }

    //设置密码过滤器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //注入获取用户信息的Services
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    StringRedisTemplate redisTemplate;
}
