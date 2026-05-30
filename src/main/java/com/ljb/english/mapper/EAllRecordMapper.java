package com.ljb.english.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ljb.english.pojo.EAllRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 记录所有用户打卡的时间，和当天的单词数 Mapper 接口
 * </p>
 *
 * @author ljb
 * @since 2023-04-14
 */
@Mapper
public interface EAllRecordMapper extends BaseMapper<EAllRecord> {

}
