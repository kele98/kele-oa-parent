package top.aikele.wechat.service.impl;

import org.springframework.beans.BeanUtils;
import top.aikele.model.wechat.Menu;
import top.aikele.vo.wechat.MenuVo;
import top.aikele.wechat.mapper.MenuMapper;
import top.aikele.wechat.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜单 服务实现类
 * </p>
 *
 * @author kele
 * @since 2023-03-23
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<MenuVo> findMenuInfo() {
        List<Menu> menuList = baseMapper.selectList(null);
        List<MenuVo> topList = new ArrayList<>();
        for (Menu menu : menuList) {
            if(menu.getParentId()==0){
                MenuVo topMenu =  new MenuVo();
                BeanUtils.copyProperties(menu,topMenu);
                topList.add(topMenu);
                List<MenuVo> sonList = new ArrayList<>();
                for (Menu sonMenu : menuList) {
                    if(sonMenu.getParentId()==menu.getId()) {
                        MenuVo son =  new MenuVo();
                        BeanUtils.copyProperties(sonMenu,son);
                        sonList.add(son);
                    }
                }
                topMenu.setChildren(sonList);
            }

        }
        return topList;
    }
}
