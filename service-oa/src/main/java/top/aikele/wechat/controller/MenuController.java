package top.aikele.wechat.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import top.aikele.common.reslut.Result;
import top.aikele.wechat.service.MenuService;

/**
 * <p>
 * 菜单 前端控制器
 * </p>
 *
 * @author kele
 * @since 2023-03-23
 */
@RestController
@CrossOrigin
@RequestMapping("/admin/wechat/menu")
public class MenuController {
    @Autowired
    MenuService menuService;
    @ApiOperation(value = "获取全部菜单")
    @GetMapping("findMenuInfo")
    public Result findMenuInfo() {
        return Result.ok(menuService.findMenuInfo());
    }
}
