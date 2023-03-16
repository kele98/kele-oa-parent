package top.aikele.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.aikele.model.system.SysUser;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author kele
 * @since 2023-03-15
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

}
