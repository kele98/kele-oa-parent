package top.aikele.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.aikele.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author kele
 * @since 2023-03-16
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

}
