package top.aikele.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.aikele.auth.mapper.SysRoleMapper;
import top.aikele.auth.mapper.SysUserRoleMapper;
import top.aikele.model.system.SysRole;
import top.aikele.model.system.SysUserRole;
import top.aikele.auth.service.SysRoleService;
import top.aikele.vo.system.AssignRoleVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService{
    @Autowired
    SysUserRoleMapper mapper;
    //获取用户的角色
    @Override
    public Map<String, Object> getUserRole(Integer id) {
        //用户的角色列表
        List<SysUserRole> sysUserRolesList = mapper.selectList(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, id));
        //获取所有角色
        List<SysRole> sysRoleList = baseMapper.selectList(null);
        //获取用户角色id的集合
        List<Long> roleIdList = sysUserRolesList.stream().map(obj -> obj.getRoleId()).collect(Collectors.toList());
        //获取用户分配的角色信息
        List<SysRole> assignList = new ArrayList<>();
        for (SysRole sysRole : sysRoleList) {
            if(roleIdList.contains(sysRole.getId()))
            assignList.add(sysRole);
        }
        Map<String ,Object> map = new HashMap<>();
        //用户分配到的角色
        map.put("assignList",assignList);
        //全部的角色
        map.put("sysRoleList",sysRoleList);
        return map;
    }
    //分配用户角色
    @Override
    public boolean assignUser(AssignRoleVo assignRoleVo) {
        //1删除用户角色
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(SysUserRole::getUserId,assignRoleVo.getUserId());
        mapper.delete(queryWrapper);
        //2重新分配用户角色
        assignRoleVo.getRoleIdList().stream().forEach(roleId -> mapper.insert(new SysUserRole(roleId,assignRoleVo.getUserId())));
        return true;
    }
}
