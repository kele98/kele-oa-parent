package top.aikele.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import top.aikele.common.reslut.Result;
import top.aikele.model.system.SysRole;
import top.aikele.service.SysRoleService;
import top.aikele.vo.system.AssginRoleVo;
import top.aikele.vo.system.SysRoleQueryVo;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "系统角色管理模块")
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {
    @Autowired
    SysRoleService sysRoleService;

    @GetMapping("/findAll")
    @ApiOperation(value = "获取所有角色",notes = "获取所有角色的数据")
    public Result<List<SysRole>> findAll()  {
        List<SysRole> list = sysRoleService.list();
        return Result.ok(list);
    }

    @ApiOperation(value = "条件分页查询" ,notes = "根据传递的条件查询数据")
    @GetMapping("/get/{page}/{limit}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "页数"),
            @ApiImplicitParam(name = "limit",value = "限制"),
    }
    )
    public Result<IPage<SysRole>> pageQueryRole(@PathVariable("page")int page,@PathVariable("limit") int limit, SysRoleQueryVo sysRoleQueryVo){
        Page<SysRole> page1= new Page<>(page,limit);
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper();
        String roleName = sysRoleQueryVo.getRoleName();
        if(!StringUtils.isEmpty(roleName)){
            queryWrapper.like(SysRole::getRoleName,roleName);
        }
        IPage<SysRole> iPage = sysRoleService.page(page1,queryWrapper);
        return Result.ok(iPage);
    }
    //添加角色
    @ApiOperation(value = "添加角色")
    @PostMapping("/save")
    public Result save(SysRole sysRole){
        if(sysRoleService.save(sysRole))
        return Result.ok();
        return Result.fail();
    }
    //根据id查询
    @ApiOperation("根据id查询")
    @GetMapping("/get/{id}")
    public Result<SysRole> get(@PathVariable Integer id){
        System.out.println(id);
        SysRole sysRole = sysRoleService.getById(id);
        if(sysRole!=null)
            return Result.ok(sysRole);
        return Result.fail();
    }
    //修改角色
    @ApiOperation(value = "修改角色")
    @PutMapping ("/update")
    public Result update(SysRole sysRole){
        if(sysRoleService.updateById(sysRole))
            return Result.ok();
        return Result.fail();
    }
    //根据id删除角色
    @ApiOperation("根据id删除角色")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        if(sysRoleService.removeById(id))
            return Result.ok();
        return Result.fail();
    }
    //根据id批量删除
    @ApiOperation("根据id批量删除")
    @DeleteMapping("/delete")
    public Result delete(@RequestBody List<Integer> list){
        if (sysRoleService.removeBatchByIds(list))
            return Result.ok();
        return Result.fail();
    }
    //获取用户角色数据
    @ApiOperation("获取用户角色")
    @GetMapping("/getUserRole/{id}")
    public Result<Map<String, Object>> getUserRole(@PathVariable Integer id){
        Map<String, Object> userRole = sysRoleService.getUserRole(id);
        return Result.ok(userRole);
    }
    //分配用户角色
    @ApiOperation("分配用户角色")
    @PostMapping("/assignUser")
    public Result<List<SysRole>> assignUser(@RequestBody AssginRoleVo AssginRoleVo){
        sysRoleService.assignUser(AssginRoleVo);
        return Result.ok();
    }
}
