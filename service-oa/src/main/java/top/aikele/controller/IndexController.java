package top.aikele.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import top.aikele.common.reslut.Result;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Api(tags = "后台登陆管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {
    //login
    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Result login(){
        //{"code":200,"data":{"token":"admin-token"}}
        Map<String, String> map = new HashMap<>();
        map.put("token","admin-token");
        return Result.ok(map);
    }
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
