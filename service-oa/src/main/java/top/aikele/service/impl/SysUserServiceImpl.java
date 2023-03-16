package top.aikele.service.impl;

import top.aikele.mapper.SysUserMapper;
import top.aikele.model.system.SysUser;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.aikele.service.SysUserService;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author kele
 * @since 2023-03-15
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public int updateStatus(Integer id, Integer status) {
        SysUser sysUser = baseMapper.selectById(id);
        sysUser.setStatus(status);
        return baseMapper.updateById(sysUser);
    }
}
