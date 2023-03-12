package top.aikele.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.aikele.common.reslut.Result;
import top.aikele.model.system.SysRole;
import top.aikele.service.SysRoleService;

import java.util.List;

@RestController
@Api(tags = "系统角色")
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {
    @Autowired
    SysRoleService service;
    @GetMapping("/findAll")
    @ApiOperation(value = "获取所有用户",notes = "获取所有用户的数据")
    @ApiImplicitParams(
            @ApiImplicitParam( name = "username",
            value = "姓名",
            readOnly = true,
            paramType = "query")
    )
    public Result<List<SysRole>> findAll(String username){
        List<SysRole> list = service.list();
        return Result.ok(list);
    }

    @ApiOperation(value = "条件分页查询" ,notes = "根据传递的条件查询数据")
    @GetMapping("/get/{page}/{limit}/{name}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "页数"),
            @ApiImplicitParam(name = "limit",value = "限制"),
            @ApiImplicitParam(name = "name",value = "名称")
    }
    )
    public Result<IPage<SysRole>> pageQueryRole(@PathVariable("page")int page,@PathVariable("limit") int limit,@PathVariable("name")String name){
        Page<SysRole> page1= new Page<>(page,limit);
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(SysRole::getRoleName,name);

        IPage<SysRole> iPage = service.page(page1,queryWrapper);

        return Result.ok(iPage);
    }
}
