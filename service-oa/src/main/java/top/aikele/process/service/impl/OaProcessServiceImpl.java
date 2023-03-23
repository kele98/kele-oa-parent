package top.aikele.process.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.aikele.auth.service.SysUserService;
import top.aikele.model.process.Process;
import top.aikele.model.process.ProcessRecord;
import top.aikele.model.process.ProcessTemplate;
import top.aikele.model.system.SysUser;
import top.aikele.process.mapper.OaProcessMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.aikele.process.service.OaProcessRecordService;
import top.aikele.process.service.OaProcessService;
import top.aikele.process.service.OaProcessTemplateService;
import top.aikele.vo.process.ApprovalVo;
import top.aikele.vo.process.ProcessFormVo;
import top.aikele.vo.process.ProcessQueryVo;
import top.aikele.vo.process.ProcessVo;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * <p>
 * 审批类型 服务实现类
 * </p>
 *
 * @author kele
 * @since 2023-03-21
 */
@Service
public class OaProcessServiceImpl extends ServiceImpl<OaProcessMapper, Process> implements OaProcessService {
    @Autowired
    RepositoryService repositoryService;
    @Autowired
    OaProcessTemplateService oaProcessTemplateService;
    @Autowired
    OaProcessRecordService oaProcessRecordService;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    TaskService taskService;
    @Autowired
    HistoryService historyService;
    @Override
    public IPage<ProcessVo> selectPage(Page<ProcessVo> pageParam, ProcessQueryVo processQueryVo) {
        IPage<ProcessVo> page= baseMapper.selectPageVo(pageParam,processQueryVo);
        return page;
    }

    @Override
    public void deployByZIP(String deployPath) {

        URL url = ClassLoader.getSystemResource("");
        System.out.println(url.getPath());
        File file = new File(url.getPath()+"/"+deployPath);
        if(!file.exists()){
            throw new RuntimeException("文件不存在");
        }else {
            InputStream inputStream = null;
            ZipInputStream zipInputStream = null;
            try {
                inputStream  = new FileInputStream(file);
                zipInputStream = new ZipInputStream(inputStream);
                Deployment deployment = repositoryService.createDeployment().addZipInputStream(zipInputStream).deploy();
                System.out.println(deployment.getId());
                System.out.println(deployment.getName());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }finally {
                try {
                    inputStream.close();
                    zipInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }

    }

    @Override
    public void startup(ProcessFormVo processFormVo) {
        //获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        SysUser user = sysUserService.getByUsername(username);
        //根据模板id拿到模板信息
        ProcessTemplate template = oaProcessTemplateService.getById(processFormVo.getProcessTemplateId());
        //记录提交信息到process表中
        Process process = new Process();
        //将processFormVo的值复制到process
        BeanUtils.copyProperties(processFormVo,process);
        process.setStatus(1);
        process.setProcessCode(System.currentTimeMillis()+"");
        process.setUserId(user.getId());
        process.setFormValues(processFormVo.getFormValues());
        process.setTitle(user.getName()+"发起"+template.getName()+"申请");
        //业务key
        String processKey = String.valueOf(baseMapper.insert(process));
        //启动流程实例 流程定义Key 业务key 流程参数
        String formValues = processFormVo.getFormValues();
        JSONObject jsonObject = JSON.parseObject(formValues);
        JSONObject formData = jsonObject.getJSONObject("formData");
        Map map = new HashMap();
        for (Map.Entry<String, Object> entry : formData.entrySet()) {
            map.put(entry.getKey(),entry.getValue());
        }
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(template.getProcessDefinitionKey(), processKey, map);
        //查询下个一审批人
        List<Task> list = this.getCurrentTaskList(processInstance.getId());
        List<String> nameList = new ArrayList<>();
        for (Task task : list) {
            String assignee = task.getAssignee();
            SysUser sysUser = sysUserService.getByUsername(assignee);
            String name = sysUser.getName();
            nameList.add(name);
            //推送消息
        }

        //业务和流程关联
        process.setProcessInstanceId(processInstance.getProcessInstanceId());
        process.setDescription("等待"+ nameList +"审批");
        //更新id和描述
        baseMapper.updateById(process);
        oaProcessRecordService.record(process.getId(),1,"发起申请");
    }

    @Override
    public Page<ProcessVo> findPending(Page<Process> targetPage) {
        //获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        TaskQuery query = taskService.createTaskQuery().taskAssignee(username).orderByTaskCreateTime().desc();
        long count = query.count();
        List<Task> tasks = query.listPage((int) ((targetPage.getCurrent() - 1) * targetPage.getSize()), (int) targetPage.getSize());
        List<ProcessVo> processVoList = new ArrayList<>();
        for (Task task : tasks) {
            ProcessVo processVo = new ProcessVo();
            String id = task.getProcessInstanceId();
            Process process = baseMapper.selectOne(new LambdaQueryWrapper<Process>().eq(Process::getProcessInstanceId,id));
            BeanUtils.copyProperties(process,processVo);
            processVo.setTaskId(task.getId());
            processVoList.add(processVo);
        }
        return new Page<ProcessVo>(targetPage.getCurrent(),targetPage.getSize(),count).setRecords(processVoList);
    }
    //查看审批详情信息
    @Override
    public Map<String, Object> show(Long id) {
        //根据流程id获取流程信息
        Process process = baseMapper.selectById(id);
        //根据id获取流程记录信息
        List<ProcessRecord> list = oaProcessRecordService.list(new LambdaQueryWrapper<ProcessRecord>().eq(ProcessRecord::getProcessId, process.getId()));
        //根据模板id查询模板信息
        ProcessTemplate template = oaProcessTemplateService.getById(process.getProcessTemplateId());
        //判断当前用户是否可以审批 不能重复审批
        boolean isApprove = false;
        for (Task task : this.getCurrentTaskList(process.getProcessInstanceId())) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = (String) authentication.getPrincipal();
            if(task.getAssignee().equals(username))
                isApprove=true;

        }
        Map<String, Object> map = new HashMap<>();
        map.put("process", process);
        map.put("processRecordList", list);
        map.put("processTemplate", template);
        map.put("isApprove", isApprove);
        //查询数据封装到map中返回
        return map;
    }

    @Override
    public void approve(ApprovalVo approvalVo) {
        //获取任务流程变量
        String taskId = approvalVo.getTaskId();
        Map<String, Object> variables = taskService.getVariables(taskId);
        //判断审批状态 =1 审批通过 =-1驳回
        if(approvalVo.getStatus()==1){
            taskService.complete(taskId);
        }else {
            this.endTask(taskId);
        }
        String description = approvalVo.getStatus() ==1 ? "通过":"驳回";
        //记录下相关信息
        oaProcessRecordService.record(approvalVo.getProcessId(),approvalVo.getStatus(),description);
        //查询下个审批人，更新流程记录表
        Process process = this.getById(approvalVo.getProcessId());
        List<Task> taskList = this.getCurrentTaskList(process.getProcessInstanceId());
        if (!CollectionUtils.isEmpty(taskList)) {
            List<String> assigneeList = new ArrayList<>();
            for(Task task : taskList) {
                SysUser sysUser = sysUserService.getByUsername(task.getAssignee());
                assigneeList.add(sysUser.getName());

                //推送消息给下一个审批人
            }
            process.setDescription("等待" + assigneeList.toString()+ "审批");
            process.setStatus(1);
        } else {
            if(approvalVo.getStatus().intValue() == 1) {
                process.setDescription("审批完成（同意）");
                process.setStatus(2);
            } else {
                process.setDescription("审批完成（拒绝）");
                process.setStatus(-1);
            }
        }
        baseMapper.updateById(process);
    }
    //分页查询
    @Override
    public Page<ProcessVo>  findProcessed(Page<Process> pageParam) {
        //封装查询条件
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String  username = (String) authentication.getPrincipal();
        //调用方法条件分页查询 返回list
        HistoricActivityInstanceQuery query = historyService.createHistoricActivityInstanceQuery().taskAssignee(username).orderByHistoricActivityInstanceStartTime().desc().finished();
        List<HistoricActivityInstance> list = query.listPage((int) ((pageParam.getCurrent() - 1) * pageParam.getSize()), (int) pageParam.getSize());
        long totalCount = query.count();
        //遍历返回list集合 封装list<ProcessVo>
        List<ProcessVo> processVoList = new ArrayList<>();
        for (HistoricActivityInstance instance : list) {
            //流程实例ID
            String processInstanceId = instance.getProcessInstanceId();
            //根据流程实例id查询获取process信息
            Process process = baseMapper.selectOne(new LambdaQueryWrapper<Process>().eq(Process::getProcessInstanceId, processInstanceId));
            //process --processvo
            ProcessVo processVo = new ProcessVo();
            BeanUtils.copyProperties(process,processVo);
            processVoList.add(processVo);
        }
        //封装page返回数据
        return new Page<ProcessVo>(pageParam.getCurrent(),pageParam.getSize(),count()).setRecords(processVoList);
    }
    //已发起
    @Override
    public IPage<ProcessVo> findStarted(Page<ProcessVo> pageParam) {
        //获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        SysUser user = sysUserService.getByUsername(username);
        ProcessQueryVo processQueryVo = new ProcessQueryVo();
        processQueryVo.setUserId(user.getId());
        IPage<ProcessVo> page = baseMapper.selectPageVo(pageParam, processQueryVo);
        return page;
    }

    //结束流程
    private void endTask(String taskId) {
        //  当前任务
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        List endEventList = bpmnModel.getMainProcess().findFlowElementsOfType(EndEvent.class);
        // 并行任务可能为null
        if(CollectionUtils.isEmpty(endEventList)) {
            return;
        }
        FlowNode endFlowNode = (FlowNode) endEventList.get(0);
        FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(task.getTaskDefinitionKey());

        //  临时保存当前活动的原始方向
        List originalSequenceFlowList = new ArrayList<>();
        originalSequenceFlowList.addAll(currentFlowNode.getOutgoingFlows());
        //  清理活动方向
        currentFlowNode.getOutgoingFlows().clear();

        //  建立新方向
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId("newSequenceFlowId");
        newSequenceFlow.setSourceFlowElement(currentFlowNode);
        newSequenceFlow.setTargetFlowElement(endFlowNode);
        List newSequenceFlowList = new ArrayList<>();
        newSequenceFlowList.add(newSequenceFlow);
        //  当前节点指向新的方向
        currentFlowNode.setOutgoingFlows(newSequenceFlowList);

        //  完成当前任务
        taskService.complete(task.getId());
    }

    //获取当前流程实例的审批人
    private List<Task> getCurrentTaskList(String id) {
        List<Task> list = taskService.createTaskQuery().processInstanceId(id).list();
        return list;
    }
}
