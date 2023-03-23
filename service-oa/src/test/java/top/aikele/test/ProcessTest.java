package top.aikele.test;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ProcessTest {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;


    //单个文件部署
    @Test
    public void deployProcess(){
        //流程部署
        System.out.println(1);
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/请假.bpmn20.xml")
                .addClasspathResource("process/diagram.png")
                .name("请假申请流程")
                .deploy();
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }
    //流程实例的启动
    @Test
    public void StartProcess(){
        ProcessInstance qingjia = runtimeService.startProcessInstanceByKey("请假");
        System.out.println("流程定义ID:"+qingjia.getProcessDefinitionId());
        System.out.println("流程实例ID:"+qingjia.getId());
        System.out.println("活动ID"+qingjia.getActivityId());
        //启动流程添加businessKey
        //ProcessInstance qingjia = runtimeService.startProcessInstanceByKey("请假","BusinessKey");
    }
    //查询个人的代办任务--张三
    @Test
    public void findTaskList(){
        String assign = "张三";
        List<Task> list = taskService.createTaskQuery().taskAssignee(assign).list();
        for (Task task : list) {
            System.out.println("流程实例id：" + task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }
    //用户处理任务
    @Test
    public void completeTask(){
        //查询负责人需要处理的任务
        Task task = taskService.createTaskQuery().taskAssignee("张三").singleResult();
        taskService.complete(task.getId());
    }
    //查询已经处理过的任务
    @Test
    public void findCompleteTaskList(){
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().taskAssignee("张三").finished().list();
        for (HistoricTaskInstance historicTaskInstance : list) {
            System.out.println("流程实例id：" + historicTaskInstance.getProcessInstanceId());
            System.out.println("任务id：" + historicTaskInstance.getId());
            System.out.println("任务负责人：" + historicTaskInstance.getAssignee());
            System.out.println("任务名称：" + historicTaskInstance.getName());
        }
    }
    //删除流程定义
    @Test
    public void delDeployment(){
        repositoryService.deleteDeployment("0b71d3b0-c79f-11ed-b4c7-00d861767189",true);
    }
    //全部实例挂起
    @Test
    public void suspendProcessInstanceAll(){
        //获取流程定义的对象
        ProcessDefinition qingjia = repositoryService.createProcessDefinitionQuery().processDefinitionKey("请假").singleResult();
        boolean suspended = qingjia.isSuspended();
        if(suspended==true){
            repositoryService.activateProcessDefinitionById(qingjia.getId(),true,null);
        }else {
            repositoryService.suspendProcessDefinitionByKey(qingjia.getKey(),true,null);
            System.out.println(qingjia.getId()+"被挂起");
        }

    }
    //单个实例挂起
    @Test
    public void SingleSuspendProcessInstance() {
        String processInstanceId = "557c9d5a-c7a3-11ed-a830-00d861767189";
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        //获取到当前流程定义是否为暂停状态   suspended方法为true代表为暂停   false就是运行的
        boolean suspended = processInstance.isSuspended();
        if (suspended) {
            runtimeService.activateProcessInstanceById(processInstanceId);
            System.out.println("流程实例:" + processInstanceId + "激活");
        } else {
            runtimeService.suspendProcessInstanceById(processInstanceId);
            System.out.println("流程实例:" + processInstanceId + "挂起");
        }
    }
}
