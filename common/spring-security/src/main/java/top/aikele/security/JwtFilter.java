package top.aikele.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import top.aikele.common.reslut.Result;
import top.aikele.common.reslut.jwt.JWTHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JwtFilter extends OncePerRequestFilter {

    private StringRedisTemplate redisTemplate;

    public JwtFilter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("uri:"+request.getRequestURI());
        List<String> urlPermit = new ArrayList<>();
        urlPermit.add("/admin/system/index/login");
        urlPermit.add("http://localhost:8080/swagger-ui.html");
        String requestURI = request.getRequestURI();
        //如果是登录接口，直接放行
        for (String s : urlPermit) {
            if(requestURI.matches(".*"+s+".*")){
                filterChain.doFilter(request,response);
                return;
            }
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if(null != authentication) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } else {
            System.out.println("无权限");
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(403);
            response.setCharacterEncoding("utf-8");
            response.getWriter().println(JSON.toJSONString(Result.fail("无权限")));
            response.getWriter().flush();
            response.getWriter().close();
        }
    }
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        // token置于header里
        String token = request.getHeader("token");
        logger.info("token:"+token);
        if (!StringUtils.isEmpty(token)) {
            String useruame = JWTHelper.getUsername(token);
            logger.info("useruame:"+useruame);
            if (!StringUtils.isEmpty(useruame)) {
                //根据用户名在redis中获取权限
                String  authString = (String) redisTemplate.opsForValue().get(useruame);
                List<Map<String,String>> authList = JSON.parseArray(authString);
                ArrayList<GrantedAuthority> permitList = new ArrayList<>();
                authList.stream().forEach(map -> {
                    permitList.add(new SimpleGrantedAuthority(map.get("authority")));
                });
                return new UsernamePasswordAuthenticationToken(useruame, null, permitList);
            }
        }
        return null;
    }
}
