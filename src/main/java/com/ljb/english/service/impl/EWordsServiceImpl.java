package com.ljb.english.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ljb.english.mapper.*;
import com.ljb.english.pojo.*;
import com.ljb.english.service.EWordsService;
import com.ljb.english.utils.AcquireDate;
import com.ljb.english.utils.JudgeENZH;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.plugins.jpeg.JPEGImageReadParam;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ljb
 * @since 2023-04-14
 */
@Service
@Slf4j
public class EWordsServiceImpl extends ServiceImpl<EWordsMapper, EWords> implements EWordsService {

    @Autowired
    private EWordsMapper wordsMapper;

    @Autowired
    private EPhraseMapper phraseMapper;

    @Autowired
    private EStudiedMapper studiedMapper;

    @Autowired
    private EUserDataMapper userDataMapper;

    @Autowired
    private ESignMapper signMapper;

    @Autowired
    private EAllRecordMapper allRecordMapper;

    @Override
    public Map<String, Object> getNextInfo(Integer uId, String wMold, String wUnit) {
        Map<String, Object> map = new HashMap<>();

        //查找单词
        EWords words = wordsMapper.getNext(uId, wMold, wUnit);
        if (words == null) {
            map.put("completed", "true");
            return map;
        } else {
            map.put("completed", "false");
            Field[] fields = words.getClass().getDeclaredFields(); //利用反射 获取全部属性
            map.put("filed", "null");
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    map.put(field.getName(), field.get(words));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    map.put("filed", "err");
                }
            }
        }

        //获取单元的单词数量
        try {
            QueryWrapper<EWords> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("w_mold", wMold);
            // 类型单词总数
            map.put("type_words_all_nums", wordsMapper.selectCount(queryWrapper));
            // 已学类型，单词数量
            map.put("completed_type_all_nums", studiedMapper.getCompletedTypeAllNums(wMold, uId));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //更新单词记录表(e_studied)
        EStudied studied = new EStudied();
        studied.setUId(uId);
        studied.setWId(words.getId());
        studied.setSMold(words.getWMold());
        studied.setSMemoDate(AcquireDate.getNowDays());
        try {
            studiedMapper.insert(studied);
            map.put("update_studied_word_data_status", "success");
        } catch (Exception e) {
            map.put("update_studied_word_data_status", "error");
        }

        // 获取今日已学单词数量
        QueryWrapper<EStudied> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("u_id", uId).eq("s_memo_date", AcquireDate.getNowDays());
        Long aLong1 = studiedMapper.selectCount(wrapper1);
        map.put("today_studied_words_nums", aLong1);


        //查找单词的相关短语
        QueryWrapper<EPhrase> wrapper = new QueryWrapper<>();
        wrapper.eq("p_word", words.getWEn());
        List<EPhrase> phrases = phraseMapper.selectList(wrapper);
        List<Map<String, Object>> listP = new ArrayList<>();
        for (EPhrase phrase : phrases) {
            Map<String, Object> mapP = new HashMap<>();
            mapP.put("p_en", phrase.getPEn());
            mapP.put("p_zn", phrase.getPZh());
            listP.add(mapP);
        }
        map.put("phrases", listP);

        // 更新学习的单词的数量和总单词数和当天打卡是否完成
        EUserData userData = userDataMapper.selectById(uId);
        try {
            if (userData.getNowDayNum() + 1 >= userData.getEachDayNum()) {
                userDataMapper.updateWordsNums(uId, true);
                map.put("update_today_word_studied_nums_status", "success");
            } else {
                userDataMapper.updateWordsNums(uId, false);
                map.put("update_today_word_studied_nums_status", "success");
            }
        } catch (Exception e) {
            map.put("update_today_word_studied_nums_status", "error");
        }

        map.put("today_not_studied_word_nums", userData.getEachDayNum() - aLong1);

        return map;
    }

    @Override
    public Map<String, Object> getNextInfoUp(Integer uid, String wMold) {
        Map<String, Object> result = new HashMap<>();

        // System.out.println("查询单词信息时间 开始："+ DateUtil.date());

        // 查询单词信息
        EWords nextUp = wordsMapper.getNextUp(uid, wMold); // 线上时间过慢

        if (nextUp == null){ // 没有查询到
            result.put("completed", true);
            return result;
        }

        result.put("word_info", nextUp);
        // System.out.println("结束 更新学习的单词的数量和总单词数和当天打卡是否完成 时间："+ DateUtil.date());

        // 更新学习的单词的数量和总单词数和当天打卡是否完成
        if (nextUp.getEachDayWordNumber() > nextUp.getTodayStudiedWordsNumber()) { //当天打卡 没有完成
            userDataMapper.updateNumberAndStatus(uid, false);
        } else {
            userDataMapper.updateNumberAndStatus(uid, true); // 当天打卡已经完成
        }

        // System.out.println("结束 更新单词记录表 时间："+ DateUtil.date());

        //更新单词记录表(e_studied)  =================================================================
        EStudied studied = new EStudied();
        studied.setUId(uid);
        studied.setWId(nextUp.getId());
        studied.setSMold(nextUp.getWMold());
        studied.setSMemoDate(AcquireDate.getNowDays());
        studiedMapper.insert(studied);

        // System.out.println("结束 查询相关的短语 时间："+ DateUtil.date());

        // 查询相关的短语
        List<EPhrase> ePhrases = phraseMapper.selectByPWord(nextUp.getWEn());
        result.put("phrase", ePhrases);
        // System.out.println("结束 时间："+ DateUtil.date());
        return result;
    }

    @Override
    public int updateESign(Integer uId, Integer wId, Boolean add_del) {
        try {
            QueryWrapper<ESign> wrapper = new QueryWrapper<>();
            wrapper.eq("u_id", uId)
                    .eq("w_id", wId);

            ESign eSign = null;
            try {
                eSign = signMapper.selectOne(wrapper);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (eSign != null) {  // 存在，进行更新
                if (add_del) {  // 更新操作
                    System.out.println("更新操作");
                    eSign.setLRank(eSign.getLRank() + 1);
                    eSign.setLMaxRank(eSign.getLMaxRank() + 1);
                    eSign.setLIsMemo(1);
                    signMapper.updateById(eSign);
                    return eSign.getLRank();
                }
                // 减少操作
                System.out.println("减少操作~~~~~~");
                System.out.println(eSign.getLRank());
                if (eSign.getLRank() != 0) {  // rank ！= 0  表示还没有记住

                    System.out.println("等级减少前：" + eSign.getLRank() + "；单词：" + eSign.getWId());
                    eSign.setLRank(eSign.getLRank() - 1);
                    System.out.println("等级减少后：" + eSign.getLRank() + "；单词：" + eSign.getWId());
                    if (eSign.getLRank() == 0) {  // 已经记住
                        eSign.setLIsMemo(0);
                    }

                    signMapper.updateById(eSign);
                    return eSign.getLRank();
                } else {  // rank == 0  表示已经记住
                    eSign.setLIsMemo(0);
                    signMapper.updateById(eSign);
                    return 0;
                }
            }
            if (add_del) { // false 减少，只有增加的时候才能进行插入操作
                // 不存在，进行插入操作
                eSign = new ESign();
                eSign.setUId(uId);
                eSign.setWId(wId);
                eSign.setLRank(1);
                eSign.setLMaxRank(1);
                signMapper.insert(eSign);
                System.out.println("esgin表不存在，进行插入操作");
                return 1;
            } else {
                System.out.println("最后一种情况，不存在且减少，数据混淆");
                return 0;
            }
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public List<Map<String, Object>> queryWord(String condition) {
        QueryWrapper<EWords> wrapper = new QueryWrapper<>();
        if (JudgeENZH.isContainZH(condition)) { // 包含中文字符
            wrapper.select("DISTINCT w_en, id, w_zh, w_phonetic, w_audio, w_mold, w_unit, w_memo_skill, w_importance")
                    .like("w_zh", condition);
        } else {  // 不包含中文字符
            wrapper.select("DISTINCT w_en, id, w_zh, w_phonetic, w_audio, w_mold, w_unit, w_memo_skill, w_importance")
                    .like("w_en", condition);
        }
        // 查找有关的单词
        List<EWords> wordsList = wordsMapper.selectList(wrapper);
        if (wordsList.size() == 0) {
            return null;
        }
        List<Map<String, Object>> result = new ArrayList<>();  // 存储最后的结果
        for (EWords words : wordsList) {
            // 存入map，并查找相关的短语
            Map<String, Object> map = new HashMap<>();
            // 通过反射放入map
            Field[] fields = words.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    map.put(field.getName(), field.get(words));
                } catch (IllegalAccessException e) {
                    map.put(field.getName(), "error");
                }
            }
            // 查找单词相关的短语
            QueryWrapper<EPhrase> wrapperP = new QueryWrapper<>();
            wrapperP.eq("p_word", words.getWEn());
            List<EPhrase> phrases = phraseMapper.selectList(wrapperP);
            List<Map<String, Object>> listP = new ArrayList<>();
            for (EPhrase phrase : phrases) {
                Map<String, Object> mapP = new HashMap<>();
                mapP.put("p_en", phrase.getPEn());
                mapP.put("p_zn", phrase.getPZh());
                listP.add(mapP);
            }
            map.put("phrases", listP);
            result.add(map);
        }

        return result;
    }

    @Override
    public List<EWords> reviewStudiedWordsByDate(Integer uId, String nowDay_allDay) {
        return wordsMapper.nowDayStudiedWordReview(uId, nowDay_allDay);
    }

    @Override
    public PageInfo<EWords> signWordReview(Integer uId, Boolean choice,
                                           Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<EWords> wordsList = wordsMapper.signWordReview(uId, choice);
        return new PageInfo<>(wordsList);
    }

    @Override
    public int clearClockData(Integer uId, String mold) {
        QueryWrapper<EStudied> wrapper = new QueryWrapper<>();
        wrapper.eq("u_id", uId)
                .eq("s_mold", mold);

        try {
            return studiedMapper.delete(wrapper);
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public void scheduleUserData() {
        List<EUserData> userData = userDataMapper.selectList(null);
        for (EUserData userDatum : userData) {
            try {
                userDatum.setNowDayNum(0);
                userDatum.setNowDayComplete(0);
                userDataMapper.updateById(userDatum);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("计划更新 e_user_data 中的  {} 出错", userDatum);
            }
        }
    }

    @Override
    public void scheduleAllRecord() {
        List<EUserData> userData = userDataMapper.selectList(null); // 获取所有用户的打卡信息
        EAllRecord allRecord;
        for (EUserData datum : userData) {
            allRecord = new EAllRecord();
            try {
                allRecord.setCWordNum((int) datum.getNowDayNum());
                allRecord.setUId((int) datum.getUId());
                allRecord.setCDate(AcquireDate.getNowDays());
                allRecordMapper.insert(allRecord);
                log.info("计划更新 e_all_record表 中 {} 成功", datum.getUId());
            } catch (Exception e) {
                log.info("计划更新 e_all_record表 中 {} 出错", datum.getUId());
            }
        }
    }


}
