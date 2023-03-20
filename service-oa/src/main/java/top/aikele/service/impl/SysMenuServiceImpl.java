package top.aikele.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
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
import top.aikele.vo.system.MetaVo;
import top.aikele.vo.system.RouterVo;

import java.util.ArrayList;
import java.util.Collections;
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
    //根据用户id获取可以操作的菜单列表
    @Override
    public List<RouterVo> findUserMenuListByUserId(Long id) {
        List<SysMenu> MenuList = null;
        //管理员直接查询所有列表
        if(id==1){
            MenuList = baseMapper.selectList(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus, 1).orderByAsc(SysMenu::getSortValue));
        }else {
            MenuList = baseMapper.findListByUserId(id);
        }
        //使用工具类将菜单构建成树形结构
        List<SysMenu> treeMenuList = MenuHelper.buildTree(MenuList);
        List<RouterVo> routerVos = this.buildRouter(treeMenuList);
        return routerVos;
    }
    public List<RouterVo> buildRouter(List<SysMenu> list){
        List<RouterVo> resultList = new ArrayList<>();
        for (SysMenu menu : list) {
            RouterVo router = new RouterVo();
            router.setHidden(false);
            router.setAlwaysShow(false);
            router.setPath("/"+menu.getPath());
            router.setComponent(menu.getComponent());
            router.setMeta(new MetaVo(menu.getName(), menu.getIcon()));
            List<SysMenu> children = menu.getChildren();
            //加载菜单下面的隐藏路由
            if(menu.getType()==1){
                List<SysMenu> hiddenList = children.stream().filter(m -> !StringUtils.isEmpty(m.getComponent())).collect(Collectors.toList());
                for (SysMenu hiddenMenu : hiddenList) {
                    RouterVo hiddenRouter = new RouterVo();
                    hiddenRouter.setHidden(true);
                    hiddenRouter.setAlwaysShow(false);
                    hiddenRouter.setPath("/"+hiddenMenu.getPath());
                    hiddenRouter.setComponent(hiddenMenu.getComponent());
                    hiddenRouter.setMeta(new MetaVo(hiddenMenu.getName(), hiddenMenu.getIcon()));
                    resultList.add(hiddenRouter);
                }
            }else {
                if(!CollectionUtils.isEmpty(children)) {
                    if (children.size() > 1) {
                        router.setAlwaysShow(true);
                    }
                    router.setChildren(buildRouter(children));
                }
            }
            resultList.add(router);
        }
        return resultList;
    }
    //根据用户id获取可以操作的按钮列表
    @Override
    public List<String> findUserPermsByUserId(Long id) {
        //超级管理员admin账号id为：1
        List<SysMenu> sysMenuList = null;
        if (id.longValue() == 1) {
            sysMenuList = this.list(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus, 1));
        } else {
            sysMenuList = baseMapper.findListByUserId(id);
        }
        List<String> permsList = sysMenuList.stream().filter(item -> item.getType() == 2).map(item -> item.getPerms()).collect(Collectors.toList());
        return permsList;
    }
}
