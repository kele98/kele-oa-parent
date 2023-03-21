package top.aikele.auth.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import top.aikele.auth.service.SysUserService;
import top.aikele.common.reslut.Result;
import top.aikele.model.system.SysUser;
import top.aikele.auth.service.SysMenuService;
import top.aikele.vo.system.RouterVo;

import java.util.*;

@Api(tags = "后台登陆管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {
    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysMenuService sysMenuService;
    //login
//    @PostMapping("/login")
//    @ApiOperation("用户登录")
//    public Result login(@RequestBody LoginVo loginVo){
//        SysUser user = sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, loginVo.getUsername()));
//        if(user==null){
//            return Result.fail("用户不存在");
//        }
//        if(!new BCryptPasswordEncoder().matches(loginVo.getPassword(),user.getPassword())){
//            return Result.fail("密码错误");
//        }
//        if(user.getStatus()==0){
//            return Result.fail("用户被禁用");
//        }
//        String token = JWTHelper.creatToken(user.getId(), user.getUsername());
//        Map<String,String> map = new HashMap<>();
//        map.put("token",token);
//        return Result.ok(map);
//    }
    //info
    @GetMapping("/info")
    @ApiOperation("获取用户信息")
    public Result info(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = (String) authentication.getPrincipal();
        SysUser user = sysUserService.getByUsername(userName);
        System.out.println(user);
        Map<String, Object> map = new HashMap<>();
        //获取用户可以显示的菜单
       List<RouterVo> menuList = sysMenuService.findUserMenuListByUserId(user.getId());
        //获取用户可以操作的按钮
        List<String> premsList = sysMenuService.findUserPermsByUserId(user.getId());
        map.put("roles","[admin]");
        map.put("name",userName);
        map.put("introduction","I am a super administrator");
        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        //返回用户可以操作的菜单
        map.put("routers",menuList);
        //返回用户可以操作的按钮
        map.put("buttons",premsList);
        return Result.ok(map);
    }
    //{"code":20000,"data":{"roles":["admin"],"introduction":"I am a super administrator","avatar":"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif","name":"Super Admin"}}
    //logout
    @PostMapping("/logout")
    public Result logout(){
        return Result.ok();
    }
}
