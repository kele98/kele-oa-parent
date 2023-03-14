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
import top.aikele.vo.system.SysRoleQueryVo;

import java.util.List;

@RestController
@Api(tags = "系统角色")
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {
    @Autowired
    SysRoleService service;
    @GetMapping("/findAll")
    @ApiOperation(value = "获取所有用户",notes = "获取所有用户的数据")
    public Result<List<SysRole>> findAll()  {
        List<SysRole> list = service.list();
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
        IPage<SysRole> iPage = service.page(page1,queryWrapper);
        return Result.ok(iPage);
    }
    //添加角色
    @ApiOperation(value = "添加角色")
    @PostMapping("/save")
    public Result save(SysRole sysRole){
        if(service.save(sysRole))
        return Result.ok();
        return Result.fail();
    }
    //根据id查询
    @ApiOperation("根据id查询")
    @GetMapping("/get/{id}")
    public Result<SysRole> get(@PathVariable Integer id){
        System.out.println(id);
        SysRole sysRole = service.getById(id);
        if(sysRole!=null)
            return Result.ok(sysRole);
        return Result.fail();
    }
    //修改角色
    @ApiOperation(value = "修改角色")
    @PutMapping ("/update")
    public Result update(SysRole sysRole){
        if(service.updateById(sysRole))
            return Result.ok();
        return Result.fail();
    }
    //根据id删除角色
    @ApiOperation("根据id删除角色")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        if(service.removeById(id))
            return Result.ok();
        return Result.fail();
    }
    //根据id批量删除
    @ApiOperation("根据id批量删除")
    @DeleteMapping("/delete")
    public Result delete(@RequestBody List<Integer> list){
        if (service.removeBatchByIds(list))
            return Result.ok();
        return Result.fail();
    }
}
