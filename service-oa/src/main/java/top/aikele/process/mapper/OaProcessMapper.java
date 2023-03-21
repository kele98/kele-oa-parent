package top.aikele.process.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.aikele.model.process.Process;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 审批类型 Mapper 接口
 * </p>
 *
 * @author kele
 * @since 2023-03-21
 */
@Mapper
public interface OaProcessMapper extends BaseMapper<Process> {

}
