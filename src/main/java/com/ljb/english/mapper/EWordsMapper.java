package com.ljb.english.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ljb.english.pojo.EWords;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ljb
 * @since 2023-04-14
 */
@Mapper
public interface EWordsMapper extends BaseMapper<EWords> {


    /**
     * 得到下一个单词
     *
     * @param uId   用户id
     * @param wMold 用户选择的模式
     * @param wUnit 选择的单元(自行判断)
     * @return 单词id，英文，中文，音标，音频，记忆技巧，单元,类型
     */
    EWords getNext(@Param("uId") Integer uId, @Param("wMold") String wMold, @Param("wUnit") String wUnit);


    EWords getNextUp(@Param("uId") Integer uId, @Param("wMold") String wMold);

    /**
     * 现在每天已学单词 复习
     *
     * @return {@link List}<{@link EWords}>
     */
    List<EWords> nowDayStudiedWordReview(@Param("uId") Integer uId, @Param("date") String date);

    /**
     * 标志词复习
     * 标志 复习
     *
     * @param uId    用户id
     * @param choice 选择类型  false：选择有标记的，true：选择所有标记过的（包括已经记住的）
     * @return {@link List}<{@link EWords}>
     */
    List<EWords> signWordReview(@Param("uId") Integer uId, @Param("choice") Boolean choice);


    int selectAllByWUnit(@Param("w_mold") String w_mold);

    /**
     * 查找标志词
     * @param uid
     * @return
     */
    List<EWords> allReviewSignRapid(@Param("uId") Integer uid);
}
