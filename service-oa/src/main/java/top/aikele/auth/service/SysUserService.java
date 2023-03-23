package top.aikele.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.aikele.model.system.SysUser;

import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author kele
 * @since 2023-03-15
 */
public interface SysUserService extends IService<SysUser> {
     int updateStatus(Integer id,Integer status);
    SysUser getByUsername(String username);

    Map<String, Object> getCurrentUser();
}
