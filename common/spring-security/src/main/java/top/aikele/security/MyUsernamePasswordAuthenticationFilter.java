package top.aikele.security;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import top.aikele.common.reslut.Result;
import top.aikele.common.reslut.ResultCodeEnum;
import top.aikele.common.reslut.jwt.JWTHelper;
import top.aikele.vo.system.LoginVo;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private boolean postOnly = true;
    private StringRedisTemplate redisTemplate;
    public MyUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager,StringRedisTemplate redisTemplate) {
        this.setAuthenticationManager(authenticationManager);
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/admin/system/index/login","POST"));
        this.redisTemplate = redisTemplate;
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        try {
            LoginVo loginVo = new ObjectMapper().readValue(request.getInputStream(), LoginVo.class);
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(loginVo.getUsername(), loginVo.getPassword());
            return this.getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("登录成功");
        MyUserDetails userDetails = (MyUserDetails) authResult.getPrincipal();
        //获取token
        String token = JWTHelper.creatToken(userDetails.getSysUser().getId(), userDetails.getSysUser().getUsername());
        //获取当前用户的权限数据 redis key是用户名 value 是权限数据
        redisTemplate.opsForValue().set(userDetails.getSysUser().getUsername(),JSON.toJSONString(userDetails.getAuthorities()));
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(JSON.toJSONString(Result.ok(map)));
        response.getWriter().flush();
        response.getWriter().close();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        System.out.println("登录失败");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(Result.fail());
        response.getWriter().flush();
        response.getWriter().close();
    }
}
