package top.aikele.process.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import top.aikele.model.process.Process;
import top.aikele.process.mapper.OaProcessMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 审批类型 服务实现类
 * </p>
 *
 * @author kele
 * @since 2023-03-21
 */
@Service
public class OaProcessServiceImpl extends ServiceImpl<OaProcessMapper, Process> implements IService<Process> {

}
