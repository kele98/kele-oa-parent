package top.aikele.auth.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import top.aikele.model.system.SysUser;
import top.aikele.security.MyUserDetails;
import top.aikele.auth.service.SysMenuService;
import top.aikele.auth.service.SysUserService;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysMenuService sysMenuService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //获取用户
        SysUser sysUser = sysUserService.getByUsername(username);
        if(null == sysUser) {
            throw new UsernameNotFoundException("用户名不存在！");
        }

        if(sysUser.getStatus().intValue() == 0) {
            throw new RuntimeException("账号已停用");
        }
        //获取用户权限
        List<String> permitsList = sysMenuService.findUserPermsByUserId(sysUser.getId());
        //封装数据
        List<SimpleGrantedAuthority> authList = new ArrayList<>();
        permitsList.stream().forEach( o -> authList.add(new SimpleGrantedAuthority(o)));
        return new MyUserDetails(sysUser, authList);
    }
}
