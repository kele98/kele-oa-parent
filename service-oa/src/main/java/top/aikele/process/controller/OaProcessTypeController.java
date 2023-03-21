package top.aikele.process.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import top.aikele.common.reslut.Result;
import top.aikele.model.process.ProcessType;
import top.aikele.process.service.OaProcessTypeService;

import java.util.List;

/**
 * <p>
 * 审批类型 前端控制器
 * </p>
 *
 * @author kele
 * @since 2023-03-21
 */
@RestController
@Api(tags = "审批类型")
@RequestMapping("/admin/process/processType")
public class OaProcessTypeController {

    @Autowired
    private OaProcessTypeService oaProcessTypeService;
    @ApiOperation(value = "获取分页列表")
    @GetMapping("/{page}/{limit}")
    public Result<Page> index(@PathVariable("page") int page, @PathVariable("limit")int limit){
        Page<ProcessType> pageParam = new Page(page,limit);
        Page resultPage = oaProcessTypeService.page(pageParam);
        return Result.ok(resultPage);
    }
    @ApiOperation(value = "获取")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        ProcessType processType = oaProcessTypeService.getById(id);
        return Result.ok(processType);
    }

    @ApiOperation(value = "新增")
    @PostMapping("save")
    public Result save(@RequestBody ProcessType processType) {
        oaProcessTypeService.save(processType);
        return Result.ok();
    }

    @ApiOperation(value = "修改")
    @PutMapping("update")
    public Result updateById(@RequestBody ProcessType processType) {
        oaProcessTypeService.updateById(processType);
        return Result.ok();
    }
    @ApiOperation(value = "删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        oaProcessTypeService.removeById(id);
        return Result.ok();
    }
    @ApiOperation(value = "获取全部审批分类")
    @GetMapping("findAll")
    public Result findAll() {
        return Result.ok(oaProcessTypeService.list());
    }

}
