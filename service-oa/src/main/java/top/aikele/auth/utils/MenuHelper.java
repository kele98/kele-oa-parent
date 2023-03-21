package top.aikele.auth.utils;

import top.aikele.model.system.SysMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MenuHelper {
    public static List<SysMenu> buildTree(List<SysMenu> list){
        List<SysMenu> resultList = new ArrayList<>();
        //找到根节点
        AtomicReference<SysMenu> root = new AtomicReference<>(new SysMenu());
        list.stream().forEach( obj -> {
            if(obj.getParentId()==0) {
                build(obj,list);
                resultList.add(obj);
            }
        });
        //构建tree
        return resultList;
    }
    public static void build(SysMenu root,List<SysMenu> list){
        Long id = root.getId();
        for (int i = 0; i < list.size(); i++) {
            SysMenu sysMenu = list.get(i);
            if(sysMenu.getParentId()==id){
                //找到子类添加进去
                if(root.getChildren()==null)
                    root.setChildren(new ArrayList<>());
                root.getChildren().add(sysMenu);
                build(sysMenu,list);
            }
        }
        //找不到了就返回
        return ;
    }
}
