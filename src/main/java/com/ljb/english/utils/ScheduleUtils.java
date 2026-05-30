package com.ljb.english.utils;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ljb.english.mapper.*;
import com.ljb.english.pojo.EAllRecord;
import com.ljb.english.pojo.EUser;
import com.ljb.english.pojo.EUserData;
import com.ljb.english.service.EWordsService;
import com.ljb.english.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@EnableScheduling
@Configuration
@Slf4j
public class ScheduleUtils {

    @Autowired
    private EWordsService wordsService;

    @Resource
    private EmailService emailService;

    @Resource
    private EUserMapper userMapper;

    /**
     * 计划更新 e_all_record表  每天 23:50:59
     */
    @Scheduled(cron = "59 55 23 * * ?")
    public void scheduleAllRecord() {
        wordsService.scheduleAllRecord();
        log.info("计划更新 e_all_record表，日期：{}", AcquireDate.getNowDays());
    }

    /**
     * 计划更新 e_user_data表中的 重置当天打卡的单词数  23:59:59
     */
    @Scheduled(cron = "59 59 23 * * ?")
    public void scheduleUserData() {
        wordsService.scheduleUserData();
        log.info("计划更新 e_user_data表中的 重置当天打卡的单词数，日期：{}", AcquireDate.getNowDays());
    }

    /**
     * 安排发送邮件
     *
     * @throws ParseException 解析异常
     */
    @Scheduled(cron = "30 30 7 * * ?")
    public void scheduleSendMail() {
        QueryWrapper<EUser> wrapper = new QueryWrapper<>();
        wrapper.isNotNull("u_email");
        List<EUser> list = userMapper.selectList(wrapper);

        try {
            // 获取日期时间差
            String dateStr1 = DateUtil.now();
            Date date1 = DateUtil.parse(dateStr1);

            String dateStr2 = "2023-12-23 12:12:12";
            Date date2 = DateUtil.parse(dateStr2);

            //相差一个月，31天
            long betweenDay = DateUtil.between(date1, date2, DateUnit.DAY);

            for (EUser user : list) {
                String content = "距离考研倒计时：<span style='color:red;'>" + betweenDay + "</span>天,<br> " +
                        "亲爱的<span style='color:green'>" + user.getUName() + "</span>抓紧单词打卡吧！";
                log.info("每日一提发送计划,发送给用户：{}", user.getUName());
                emailService.sendMessage(user.getUEmail(), "每日一提", content);
            }
        } catch (Exception e) {
            throw new RuntimeException("每日一提出错");
        }
    }

}
