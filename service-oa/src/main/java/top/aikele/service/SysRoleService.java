package top.aikele.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.aikele.model.system.SysRole;
import top.aikele.model.system.SysUserRole;
import top.aikele.vo.system.AssignRoleVo;

import java.util.List;
import java.util.Map;

public interface SysRoleService extends IService<SysRole> {

    Map<String,Object> getUserRole(Integer id);
    boolean  assignUser(AssignRoleVo assignRoleVo);
}
