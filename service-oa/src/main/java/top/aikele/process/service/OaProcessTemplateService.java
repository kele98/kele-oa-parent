package top.aikele.process.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.aikele.model.process.ProcessTemplate;
import com.baomidou.mybatisplus.extension.service.IService;
import top.aikele.model.process.ProcessType;

import java.util.List;

/**
 * <p>
 * 审批模板 服务类
 * </p>
 *
 * @author kele
 * @since 2023-03-21
 */
public interface OaProcessTemplateService extends IService<ProcessTemplate> {
    Page selectPageProcessTemplate(Page pageParam);

    void publish(Long id);

    List<ProcessType> findProcessType();
}
