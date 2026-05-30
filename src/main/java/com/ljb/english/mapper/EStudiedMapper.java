package com.ljb.english.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ljb.english.pojo.EStudied;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 这是记录用户已经学习的单词表 Mapper 接口
 * </p>
 *
 * @author ljb
 * @since 2023-04-14
 */
@Mapper
public interface EStudiedMapper extends BaseMapper<EStudied> {

    int getCompletedTypeAllNums(@Param("wMold") String wMold, @Param("uId") Integer uId);

}
