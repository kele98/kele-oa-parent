package top.aikele.process.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.aikele.model.process.Process;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.aikele.vo.process.ProcessQueryVo;
import top.aikele.vo.process.ProcessVo;

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

    IPage<ProcessVo> selectPageVo(Page<ProcessVo> pageParam, @Param("vo") ProcessQueryVo processQueryVo);
}
