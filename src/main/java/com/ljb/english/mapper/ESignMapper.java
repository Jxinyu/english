package com.ljb.english.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ljb.english.pojo.ESign;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 这是记录用户标记单词的信息的表，标记次数越多用户越不熟悉 Mapper 接口
 * </p>
 *
 * @author ljb
 * @since 2023-04-14
 */
@Mapper
public interface ESignMapper extends BaseMapper<ESign> {

}
