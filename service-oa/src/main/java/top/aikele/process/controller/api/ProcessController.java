package top.aikele.process.controller.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.aikele.auth.service.SysUserService;
import top.aikele.common.reslut.Result;
import top.aikele.model.process.Process;
import top.aikele.model.process.ProcessTemplate;
import top.aikele.model.process.ProcessType;
import top.aikele.process.service.OaProcessService;
import top.aikele.process.service.OaProcessTemplateService;
import top.aikele.vo.process.ApprovalVo;
import top.aikele.vo.process.ProcessFormVo;
import top.aikele.vo.process.ProcessVo;

import java.util.List;
import java.util.Map;

@Api(tags = "审批流管理")
@RestController
@RequestMapping(value="/admin/process")
@CrossOrigin  //跨域
public class ProcessController {
    @Autowired
    private SysUserService service;
    @Autowired
    private OaProcessService oaProcessService;
    @Autowired
    private OaProcessTemplateService oaProcessTemplateService;
    @GetMapping("getProcessTemplate/{processTemplateId}")
    public Result getProcessTemplate(@PathVariable Long processTemplateId){
        ProcessTemplate template = oaProcessTemplateService.getById(processTemplateId);
        return Result.ok(template);
    }
    @GetMapping("findProcessType")
    @ApiOperation("查询所有审批分类和每个分类所有审批模板")
    public Result findProcessType(){
       List<ProcessType> list = oaProcessTemplateService.findProcessType();
        return Result.ok(list);
    }

    @ApiOperation("启动审批流")
    @PostMapping("/startup")
    public Result startup(@RequestBody ProcessFormVo processFormVo){
        oaProcessService.startup(processFormVo);
        return Result.ok();
    }
    @GetMapping("/findPending/{page}/{limit}")
    @ApiOperation("获取待处理的审批")
    public Result<Page<ProcessVo>> findPending(@PathVariable Long page, @PathVariable Long limit){
        Page<Process> targetPage = new Page(page,limit);
        Page<ProcessVo> resultPage =  oaProcessService.findPending(targetPage);
        return Result.ok(resultPage);
    }
    //查看审批详情信息
    @GetMapping("show/{id}")
    public Result show(@PathVariable Long id){
      Map<String,Object> map =  oaProcessService.show(id);
      return Result.ok(map);
    }
    //审批
    @ApiOperation(value = "审批")
    @PostMapping("approve")
    public Result approve(@RequestBody ApprovalVo approvalVo) {
        oaProcessService.approve(approvalVo);
        return Result.ok();
    }
    //已处理
    @ApiOperation(value = "已处理")
    @GetMapping("/findProcessed/{page}/{limit}")
    public Result findProcessed(@PathVariable Long page, @PathVariable Long limit) {
        Page<Process> pageParam = new Page<>(page, limit);
        Page<ProcessVo> pageModel1 = oaProcessService.findProcessed(pageParam);
        return Result.ok(pageModel1);
    }
    //已发起
    @ApiOperation(value = "已发起")
    @GetMapping("/findStarted/{page}/{limit}")
    public Result findStarted(@PathVariable Long page, @PathVariable Long limit) {
        Page<ProcessVo> pageParam = new Page<>(page, limit);
        return Result.ok(oaProcessService.findStarted(pageParam));
    }
    @ApiOperation(value = "获取当前用户基本信息")
    @GetMapping("getCurrentUser")
    public Result getCurrentUser() {
        return Result.ok(service.getCurrentUser());
    }
}
