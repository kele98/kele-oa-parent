package top.aikele.process.service.impl;

import top.aikele.model.process.ProcessType;
import top.aikele.process.mapper.OaProcessTypeMapper;
import top.aikele.process.service.OaProcessTypeService;
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
public class OaProcessTypeServiceImpl extends ServiceImpl<OaProcessTypeMapper, ProcessType> implements OaProcessTypeService {

}
