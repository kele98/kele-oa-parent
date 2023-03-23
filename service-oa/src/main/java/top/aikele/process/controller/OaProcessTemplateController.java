package top.aikele.process.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import top.aikele.common.reslut.Result;
import top.aikele.model.process.ProcessTemplate;
import top.aikele.model.process.ProcessType;
import top.aikele.process.service.OaProcessService;
import top.aikele.process.service.OaProcessTemplateService;
import top.aikele.process.service.OaProcessTypeService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 审批模板 前端控制器
 * </p>
 *
 * @author kele
 * @since 2023-03-21
 */
@RestController
@Api(tags = "审批模板")
@RequestMapping("/admin/process/processTemplate")
public class OaProcessTemplateController {
    @Autowired
    private OaProcessTemplateService processTemplateService;
    @Autowired
    private OaProcessService oaProcessService;
    //分页查询审批模板
    @ApiOperation("获取分页审批模板数据")
    @GetMapping("{page}/{limit}")
    public Result<Page> index(@PathVariable("page") int page, @PathVariable("limit")int limit){
        Page pageParam = new Page(page,limit);
        Page resultPage = processTemplateService.selectPageProcessTemplate(pageParam);

        return Result.ok(resultPage);
    }
    //@PreAuthorize("hasAuthority('bnt.processTemplate.list')")
    @ApiOperation(value = "获取")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        ProcessTemplate processTemplate = processTemplateService.getById(id);
        return Result.ok(processTemplate);
    }

    //@PreAuthorize("hasAuthority('bnt.processTemplate.templateSet')")
    @ApiOperation(value = "新增")
    @PostMapping("save")
    public Result save(@RequestBody ProcessTemplate processTemplate) {
        processTemplateService.save(processTemplate);
        return Result.ok();
    }

    //@PreAuthorize("hasAuthority('bnt.processTemplate.templateSet')")
    @ApiOperation(value = "修改")
    @PutMapping("update")
    public Result updateById(@RequestBody ProcessTemplate processTemplate) {
        processTemplateService.updateById(processTemplate);
        return Result.ok();
    }

    //@PreAuthorize("hasAuthority('bnt.processTemplate.remove')")
    @ApiOperation(value = "删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        processTemplateService.removeById(id);
        return Result.ok();
    }
    @ApiOperation(value = "上传流程定义")
    @PostMapping("/uploadProcessDefinition")
    public Result uploadProcessDefinition(MultipartFile file) throws FileNotFoundException {
        //获取classes目录
        String path = ResourceUtils.getURL("classpath:").getPath();
        String filename = file.getOriginalFilename();
        File temp = new File(path + "/processes/");
        if(!temp.exists()){
            temp.mkdirs();
        }
        File zipFile = new File(temp.getPath()+"/"+filename);
        try {
            file.transferTo(zipFile);
        } catch (IOException e) {
           e.printStackTrace();
           return Result.fail("上传失败");
        }
        Map<String, Object> map = new HashMap<>();
        //根据上传地址后续部署流程定义，文件名称为流程定义的默认key
        map.put("processDefinitionPath", "processes/" + filename);
        map.put("processDefinitionKey", filename.substring(0, filename.lastIndexOf(".")));
        return Result.ok(map);
    }
    //部署流程定义(发布)
    @ApiOperation(value = "发布")
    @GetMapping("/publish/{id}")
    public Result publish(@PathVariable Long id){
        //修改模板发布状态 1 已经发布
        processTemplateService.publish(id);
        return  Result.ok();
    }

}
