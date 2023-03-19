package top.aikele.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import top.aikele.common.reslut.Result;
import top.aikele.common.reslut.jwt.JWTHelper;
import top.aikele.model.system.SysUser;
import top.aikele.service.SysUserService;
import top.aikele.vo.system.LoginVo;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Api(tags = "后台登陆管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {
    @Autowired
    SysUserService sysUserService;
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
    @ApiImplicitParams(
            @ApiImplicitParam(name = "token",value = "登录用户token")
    )
    public Result info(String token){
        Map<String, String> map = new HashMap<>();
        map.put("roles","[\"admin\"]");
        map.put("introduction","I am a super administrator");
        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        map.put("name","Super Admin");
        return Result.ok(map);
    }
    //{"code":20000,"data":{"roles":["admin"],"introduction":"I am a super administrator","avatar":"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif","name":"Super Admin"}}
    //logout
    @PostMapping("/logout")
    public Result logout(){
        return Result.ok();
    }
}
