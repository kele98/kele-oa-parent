package top.aikele.process.service.impl;

import top.aikele.model.process.ProcessRecord;
import top.aikele.process.mapper.OaProcessRecordMapper;
import top.aikele.process.service.OaProcessRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 审批记录 服务实现类
 * </p>
 *
 * @author kele
 * @since 2023-03-21
 */
@Service
public class OaProcessRecordServiceImpl extends ServiceImpl<OaProcessRecordMapper, ProcessRecord> implements OaProcessRecordService {

}
