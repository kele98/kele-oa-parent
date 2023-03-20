package top.aikele.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import top.aikele.annotation.CheckData;
import top.aikele.common.reslut.Result;
import top.aikele.model.base.BaseEntity;
import top.aikele.model.system.SysUser;
import top.aikele.service.SysUserService;
import top.aikele.service.impl.SysUserServiceImpl;
import top.aikele.vo.system.SysUserQueryVo;

import java.security.Security;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author kele
 * @since 2023-03-15
 */
@RestController
@Api(tags = "用户管理模块")
@RequestMapping("/admin/system/sysUser")
public class SysUserController {
    @Autowired
    SysUserService service;
    @ApiOperation("条件分页查询")
    @PreAuthorize("hasAuthority('bnt.sysUser.list')")
    @GetMapping("/get/{page}/{limit}")
    public Result<Page<SysUser>> pageQueryUser(@PathVariable("page")Integer page, @PathVariable("limit") Integer limit, SysUserQueryVo sysUserQueryVo){
        Page<SysUser> page1 = new Page<>(page,limit);
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        String keyword = sysUserQueryVo.getKeyword();
        String createTimeBegin = sysUserQueryVo.getCreateTimeBegin();
        String createTimeEnd = sysUserQueryVo.getCreateTimeEnd();
        if(!StringUtils.isEmpty(keyword))
            queryWrapper.like(SysUser::getUsername,keyword);
        if(!StringUtils.isEmpty(createTimeBegin))
            queryWrapper.ge(BaseEntity::getCreateTime,createTimeBegin);
        if(!StringUtils.isEmpty(createTimeEnd))
            queryWrapper.le(BaseEntity::getCreateTime,createTimeEnd);
        Page<SysUser> iPage = service.page(page1, queryWrapper);
        return Result.ok(iPage);
    }
    @ApiOperation("获取用户")
    @GetMapping("/get/{id}")
    public Result<SysUser> get(@PathVariable("id")Integer id){
        SysUser user = service.getById(id);
        if(user!=null)
        return Result.ok(user);
        return Result.fail();
    }
//    @CheckData
    @ApiOperation("添加用户")
    @PostMapping("/save")
    public Result get(SysUser user){
        String password = user.getPassword();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(password);
        user.setPassword(encode);
        if(service.save(user))
        return Result.ok();
        return Result.fail();
    }
    @ApiOperation("更新用户")
    @PutMapping("/update")
    public Result updateById(SysUser user){
        if(service.updateById(user))
        return Result.ok();
        return Result.fail();
    }
    @ApiOperation("删除用户")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id")Integer id){
        if(service.removeById(id))
            return Result.ok();
        return Result.fail();
    }
    @ApiOperation("用户状态管理")
    @PutMapping("/updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable("id")Integer id,@PathVariable("status")Integer status){
        if(service.updateStatus(id,status)!=0)
        return Result.ok();
        return Result.fail();
    }
}
