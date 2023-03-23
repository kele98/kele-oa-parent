package top.aikele.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.aikele.model.process.Process;
import top.aikele.vo.process.ApprovalVo;
import top.aikele.vo.process.ProcessFormVo;
import top.aikele.vo.process.ProcessQueryVo;
import top.aikele.vo.process.ProcessVo;

import java.util.Map;

/**
 * <p>
 * 审批类型 服务类
 * </p>
 *
 * @author kele
 * @since 2023-03-21
 */
public interface OaProcessService extends IService<Process> {

    IPage<ProcessVo> selectPage(Page<ProcessVo> pageParam, ProcessQueryVo processQueryVo);
    //部署流程定义
    void deployByZIP(String deployPath);

    void startup(ProcessFormVo processFormVo);

    Page<ProcessVo> findPending(Page<Process> targetPage);

    Map<String, Object> show(Long id);

    void approve(ApprovalVo approvalVo);

    Page<ProcessVo>  findProcessed(Page<Process> pageParam);

    IPage<ProcessVo> findStarted(Page<ProcessVo> pageParam);
}
