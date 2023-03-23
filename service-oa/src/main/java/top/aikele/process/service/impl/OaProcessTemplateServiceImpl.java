package top.aikele.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import top.aikele.model.process.ProcessTemplate;
import top.aikele.model.process.ProcessType;
import top.aikele.process.mapper.OaProcessTemplateMapper;
import top.aikele.process.service.OaProcessService;
import top.aikele.process.service.OaProcessTemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.aikele.process.service.OaProcessTypeService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 审批模板 服务实现类
 * </p>
 *
 * @author kele
 * @since 2023-03-21
 */
@Service
public class OaProcessTemplateServiceImpl extends ServiceImpl<OaProcessTemplateMapper, ProcessTemplate> implements OaProcessTemplateService {
    @Autowired
    OaProcessTemplateService oaProcessTemplateService;
    @Autowired
    OaProcessTypeService oaProcessTypeService;
    @Autowired
    OaProcessService oaProcessService;
    @Override
    public Page selectPageProcessTemplate(Page pageParam) {
        List<ProcessType> list = oaProcessTypeService.list();
        Page<ProcessTemplate> page = baseMapper.selectPage(pageParam, null);
        page.getRecords().stream().forEach(obj -> {
            for (ProcessType type : list) {
                if (obj.getProcessTypeId().equals(type.getId()))
                    obj.setProcessTypeName(type.getName());
            }
        });
        return page;
    }

    @Override
    public void publish(Long id) {
        //修改模板发布状态
        ProcessTemplate template = baseMapper.selectById(id);
        template.setStatus(1);
        baseMapper.updateById(template);
        //流程定义部署
        oaProcessService.deployByZIP(template.getProcessDefinitionPath());

    }

    //查询所有审批分类和每个分类所有审批模板
    @Override
    public List<ProcessType> findProcessType() {
        //所有分类
        List<ProcessType> typeList = oaProcessTypeService.list();
        List<ProcessType> resultList = typeList.stream().map(obj -> {
            //当前分类所有的template
            List<ProcessTemplate> list = oaProcessTemplateService.list(new LambdaQueryWrapper<ProcessTemplate>().eq(ProcessTemplate::getProcessTypeId, obj.getId()));
            obj.setProcessTemplateList(list);
            return obj;
        }).collect(Collectors.toList());
        return resultList;
    }
}
