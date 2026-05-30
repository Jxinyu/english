package com.ljb.english.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ljb.english.pojo.EPhrase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 这是记录相关单词的短语 Mapper 接口
 * </p>
 *
 * @author ljb
 * @since 2023-04-14
 */
@Mapper
public interface EPhraseMapper extends BaseMapper<EPhrase> {
    List<EPhrase> selectByPWord(@Param("pWord") String pWord);
}
