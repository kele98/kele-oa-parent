package top.aikele.test;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ProcessTest2 {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;
    @Test
    public void deployProcess(){
        repositoryService.createDeployment()
                .addClasspathResource("process/jiaban.png")
                .addClasspathResource("process/jiaban.bpmn20.xml")
                .name("jaban")
                .deploy();
    }
    @Test
    public void startProcess(){
        Map map = new HashMap();
        map.put("assign1","zhangsan");
        map.put("assign2","lishi");
        ProcessInstance jiaban = runtimeService.startProcessInstanceByKey("jiaban", map);
        System.out.println("流程定义ID:"+jiaban.getProcessDefinitionId());
        System.out.println("流程实例ID:"+jiaban.getId());
        System.out.println("活动ID"+jiaban.getActivityId());
    }
    //查询个人的代办任务--张三
    @Test
    public void findTaskList(){
        String assign = "zhangsan";
        List<Task> list = taskService.createTaskQuery().taskAssignee(assign).list();
        for (Task task : list) {
            System.out.println("流程实例id：" + task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }
}
