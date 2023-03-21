package top.aikele.process.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.aikele.model.process.ProcessRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 审批记录 Mapper 接口
 * </p>
 *
 * @author kele
 * @since 2023-03-21
 */
@Mapper
public interface OaProcessRecordMapper extends BaseMapper<ProcessRecord> {

}
