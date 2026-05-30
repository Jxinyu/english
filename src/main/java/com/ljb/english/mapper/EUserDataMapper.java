package com.ljb.english.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ljb.english.pojo.EUserData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ljb
 * @since 2023-04-14
 */
@Mapper
public interface EUserDataMapper extends BaseMapper<EUserData> {

    int updateWordsNums(@Param("uId") Integer uId, @Param("now_day_complete") Boolean now_day_complete);

    int updateNumberAndStatus(@Param("uId") Integer uId, @Param("now_day_complete") Boolean now_day_complete);
}
