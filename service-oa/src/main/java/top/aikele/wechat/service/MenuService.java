package top.aikele.wechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.aikele.model.wechat.Menu;
import top.aikele.vo.wechat.MenuVo;

import java.util.List;

/**
 * <p>
 * 菜单 服务类
 * </p>
 *
 * @author kele
 * @since 2023-03-23
 */
public interface MenuService extends IService<Menu> {

    List<MenuVo> findMenuInfo();
}
