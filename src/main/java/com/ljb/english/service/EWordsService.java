package com.ljb.english.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.ljb.english.pojo.EWords;
import org.apache.ibatis.annotations.Param;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ljb
 * @since 2023-04-14
 */
public interface EWordsService extends IService<EWords> {
    /**
     * 得到下一个单词及其相关数据和操作=>
     *  <自动更新 e_user_data表中的 学习的单词数量(now_day_num)>
     *  <自动更新 e_user_data表中的 学习的单词总数量(all_num)>
     *  <自动更新 e_user_data表中的 当天打卡是否完成>
     *  <自动更新 e_studied 表中的 学习的单词的相关数据>
     *
     * @param uId   用户id
     * @param wMold 用户选择的模式
     * @param wUnit 选择的单元(自行判断，一般不需要注明，传入null即可)
     * @return {wMold='类型', wEn='英文', phraseEn='短语英文', wZh='中文', wAudio='音频', wMemoSkill='记忆技巧', phraseZh=''短语中文,
     * wPhonetic='音标', filed='反射状态', wUnit='单元', wImportance='重要程度', id='单词id',
     * "update_today_word_studied_nums_status": "success", "unit_all_words_nums": 99,"today_not_studied_word_nums": 1,
     * 	"today_studied_words_nums": 4,"update_studied_word_data_status": "success", "all_unit_nums": 12}
     */
    Map<String, Object> getNextInfo(@Param("uId") Integer uId, @Param("wMold") String wMold, @Param("wUnit") String wUnit);

    Map<String, Object> getNextInfoUp(Integer uid, String wMold);

    /**
     * 更新  e_sign
     * 不存在时，自动更新
     * @param uId 用户id
     * @param wId 单词id
     * @param add_del true:选择更新，false:选择减少
     * @return int -1:出错；0:表示已经记住；其他值：表示等级
     */
    int updateESign(@Param("uId") Integer uId, @Param("wId") Integer wId, Boolean add_del);

    /**
     * 查询 单词
     *
     * @param condition 条件
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    List<Map<String, Object>> queryWord(@Param("condition") String condition);

    /**
     * 已学单词 复习
     * 可按日期进行查找
     * @param uId           用户id
     * @param nowDay_allDay 传入日期。null时表示所有天数
     * @return {@link List}<{@link EWords}>
     */
    List<EWords> reviewStudiedWordsByDate(@Param("uId") Integer uId, String nowDay_allDay);

    /**
     * 标志词复习
     * 标志 复习
     *
     * @param uId    用户id
     * @param choice 选择类型  false：选择有标记的，true：选择所有标记过的（包括已经记住的）
     * @param pageNum   页面
     * @param pageSize  限制
     * @return {@link List}<{@link EWords}>
     */
    PageInfo<EWords> signWordReview(@Param("uId") Integer uId, @Param("choice") Boolean choice,
                                    Integer pageNum, Integer pageSize);

    /**
     * 清空打卡数据
     *
     * @param uId  用户id
     * @param mold 类型
     * @return int 其他值：表示删除的数量，-1：失败
     */
    int clearClockData(@Param("uId") Integer uId, @Param("mold") String mold);

    void scheduleUserData();
    void scheduleAllRecord();
}
