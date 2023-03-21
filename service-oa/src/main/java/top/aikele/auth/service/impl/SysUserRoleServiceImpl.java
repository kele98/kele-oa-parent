package top.aikele.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.aikele.auth.mapper.SysUserRoleMapper;
import top.aikele.model.system.SysUserRole;
import top.aikele.auth.service.SysUserRoleService;

@Service
public class SysUserRoleServiceImpl  extends ServiceImpl<SysUserRoleMapper,SysUserRole> implements SysUserRoleService {

}
