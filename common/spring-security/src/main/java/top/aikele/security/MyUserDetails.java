package top.aikele.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import top.aikele.model.system.SysUser;

import java.util.Collection;

public class MyUserDetails extends User {
    private SysUser sysUser;
    public MyUserDetails(SysUser sysUser , Collection<? extends GrantedAuthority> authorities) {
        super(sysUser.getUsername(), sysUser.getPassword(), authorities);
        this.sysUser=sysUser;
    }

    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }
}
