package top.aikele.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apiguardian.api.API;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.aikele.common.reslut.Result;
import top.aikele.model.system.SysMenu;
import top.aikele.service.SysMenuService;
import top.aikele.vo.system.AssginMenuVo;
import top.aikele.vo.system.AssignRoleVo;

import java.util.List;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author kele
 * @since 2023-03-16
 */
@RestController
@RequestMapping("/admin/system/sysMenu")
@Api(tags = "菜单管理")
public class SysMenuController {
    @Autowired
    SysMenuService sysMenuService;
    @ApiOperation("获取菜单")
    @GetMapping("/findNodes")
    public Result findNodes(){
        List<SysMenu> list = sysMenuService.findNodes();
        if(list !=null)
            return  Result.ok(list);
        return Result.fail();
    }
    @ApiOperation("新增菜单")
    @PostMapping("/save")
    public Result save(SysMenu sysMenu){
        if(sysMenuService.save(sysMenu))
        return  Result.ok();
        return Result.fail();
    }
    @ApiOperation("修改菜单")
    @PutMapping("/update")
    public Result update(SysMenu sysMenu){
        if(sysMenuService.updateById(sysMenu))
            return  Result.ok();
        return Result.fail();
    }
    @ApiOperation("删除菜单")
    @DeleteMapping ("/delete/{id}")
    public Result delete( @PathVariable Integer id){
        if(sysMenuService.removeMenuById(id))
            return  Result.ok();
        return Result.fail();
    }
    //查询所有菜单和角色分配菜单
    @ApiOperation("根据角色获取菜单")
    @GetMapping("toAssign/{roleID}")
    public Result toAssign(@PathVariable Integer roleID){
        List<SysMenu> list = sysMenuService.findSysMenuByRoleID(roleID);
        return Result.ok(list);

    }
    @ApiOperation("角色分配菜单")
    @PostMapping ("doAssign")
    public Result doAssign(@RequestBody AssginMenuVo assginMenuVo){
        sysMenuService.doAssign(assginMenuVo);
        return Result.ok();

    }
}
