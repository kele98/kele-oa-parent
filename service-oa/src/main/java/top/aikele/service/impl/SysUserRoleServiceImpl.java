package top.aikele.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.aikele.mapper.SysUserRoleMapper;
import top.aikele.model.system.SysRole;
import top.aikele.model.system.SysUserRole;
import top.aikele.service.SysRoleService;
import top.aikele.service.SysUserRoleService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysUserRoleServiceImpl  extends ServiceImpl<SysUserRoleMapper,SysUserRole> implements SysUserRoleService {

}
