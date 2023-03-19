package top.aikele.service;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.IService;
import top.aikele.config.GlobalException;
import top.aikele.model.system.SysMenu;
import top.aikele.vo.system.AssginMenuVo;
import top.aikele.vo.system.AssignRoleVo;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author kele
 * @since 2023-03-16
 */
public interface SysMenuService extends IService<SysMenu> {
    public List<SysMenu> findNodes();

    public boolean removeMenuById(Integer id) throws GlobalException.myException;

    List<SysMenu> findSysMenuByRoleID(Integer roleID);

    boolean doAssign(AssginMenuVo assginMenuVo);
}
