package top.aikele.process.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import top.aikele.auth.service.SysUserService;
import top.aikele.model.process.ProcessRecord;
import top.aikele.model.system.SysUser;
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
    @Autowired
    SysUserService sysUserService;

    @Override
    public void record(Long ProcessID, Integer status, String description) {
        ProcessRecord processRecord = new ProcessRecord();
        processRecord.setProcessId(ProcessID);
        processRecord.setStatus(status);
        processRecord.setDescription(description);
        //获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SysUser user = sysUserService.getByUsername((String) authentication.getPrincipal());
        processRecord.setOperateUser(user.getName());
        processRecord.setOperateUserId(user.getId());
        baseMapper.insert(processRecord);
    }
}
