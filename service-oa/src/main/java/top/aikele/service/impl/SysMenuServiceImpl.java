package top.aikele.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import top.aikele.config.GlobalException;
import top.aikele.mapper.SysMenuMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.aikele.mapper.SysUserRoleMapper;
import top.aikele.model.system.SysMenu;
import top.aikele.model.system.SysRoleMenu;
import top.aikele.model.system.SysUserRole;
import top.aikele.service.SysMenuService;
import top.aikele.service.SysRoleMenuService;
import top.aikele.utils.MenuHelper;
import top.aikele.vo.system.AssginMenuVo;
import top.aikele.vo.system.AssignRoleVo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author kele
 * @since 2023-03-16
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    SysRoleMenuService sysRoleMenuService;
    @Override
    public List<SysMenu> findNodes() {
        List<SysMenu> menuList = baseMapper.selectList(null);
        List<SysMenu> sysMenus = MenuHelper.buildTree(menuList);
        return sysMenus;
    }

    @Override
    public boolean removeMenuById(Integer id) throws GlobalException.myException {

        if(baseMapper.selectCount(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId, id))!=0){
            throw new GlobalException.myException(201,"菜单不能删除");
        }
        if(baseMapper.deleteById(id)!=0)
            return true;
            return false;

    }

    @Override
    public List<SysMenu> findSysMenuByRoleID(Integer roleID) {
        //所有菜单
        List<SysMenu> sysMenus = baseMapper.selectList(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus,1));
        //获取角色对应的菜单id
        List<SysRoleMenu> list = sysRoleMenuService.list(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleID));
        List<Long> menuIdList = list.stream().map(obj -> obj.getMenuId()).collect(Collectors.toList());
        sysMenus.stream().forEach(obj -> {
            if(menuIdList.contains(obj.getId()))
                obj.setSelect(true);
            else obj.setSelect(false);
        });
        //返回树形结构的菜单
        List<SysMenu> menus = MenuHelper.buildTree(sysMenus);
        return menus;
    }

    @Override
    public boolean doAssign(AssginMenuVo assginMenuVo) {
        // 删除角色已有id
        sysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId,assginMenuVo.getRoleId()));
        //循环添加新id
        for (Long id : assginMenuVo.getMenuIdList()) {
            sysRoleMenuService.save(new SysRoleMenu(assginMenuVo.getRoleId(),id));
        }
        return true;
    }
}
