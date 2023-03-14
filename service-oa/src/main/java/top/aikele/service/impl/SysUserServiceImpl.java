package top.aikele.service.impl;

import top.aikele.mapper.SysUserMapper;
import top.aikele.model.system.SysUser;
import top.aikele.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author kele
 * @since 2023-03-15
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

}
