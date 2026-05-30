package com.ljb.english.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ljb.english.mapper.*;
import com.ljb.english.pojo.*;
import com.ljb.english.service.EWordsService;
import com.ljb.english.service.EmailService;
import com.ljb.english.utils.AcquireDate;
import com.ljb.english.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ljb
 * @since 2023-04-14
 */
@RestController
@RequestMapping("/english")
@Slf4j
public class EWordsController {
    @Resource
    EWordsService wordsService;

    @Resource
    EUserMapper userMapper;

    @Resource
    EUserDataMapper userDataMapper;

    @Resource
    EStudiedMapper studiedMapper;

    @Resource
    EmailService emailService;

    @Resource
    EAllRecordMapper recordMapper;

    @Resource
    ESignMapper signMapper;

    /**
     * 传入返回参数的map
     */
    private final Map<String, Object> map = new HashMap<>();

    /**
     * 用户登录
     *
     * @param name 用户名
     * @param pwd  用户密码
     * @return {@link R}<{@link }>
     */
    @PostMapping("/login")
    @ResponseBody
    public R login(@RequestParam("name") String name, @RequestParam("pwd") String pwd) {

        log.info(" {} 用户登录，时间为： {}", name, AcquireDate.getNowDays());
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(name, pwd);
        try {
            subject.login(token);  //执行登录的方法，如果没有异常就OK
            return R.ok();
        } catch (UnknownAccountException e) {
            return R.ok("name_error");
        } catch (IncorrectCredentialsException e) {
            return R.ok("pwd_error");
        } catch (Exception e) {
            return R.ok("other_error");
        }
    }

    /**
     * 得到下一个单词的相关信息
     *
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    @GetMapping("/getNextInfo")
    @ResponseBody
    public R getNextInfo() {

        // System.out.println("查询用户信息 时间 开始："+ DateUtil.date());
        Subject subject = SecurityUtils.getSubject();
        EUser user = (EUser) subject.getPrincipal();

        if (user == null) {
            return R.error(HttpStatus.UNAUTHORIZED.value(), String.valueOf(HttpStatus.UNAUTHORIZED));
        }
        EUserData userData = userDataMapper.selectById(user.getUId());
        // System.out.println("查询用户信息 时间 结束："+ DateUtil.date());
        try {
            // Map<String, Object> info = wordsService.getNextInfo(user.getUId(), userData.getWMoldChoice(), null);
            Map<String, Object> info = wordsService.getNextInfoUp(user.getUId(), userData.getWMoldChoice());
            return R.ok(info);
        } catch (Exception e) {
            log.error("用户：{},得到下一个单词的相关信息 出错：{}", user.getUId(), e.getMessage());
            return R.error(e.getMessage());
        }
    }

    /**
     * 根据日期复习单词
     *
     * @param date : 日期格式：2022-12-12
     * @return {@link R}
     */
    @GetMapping("/reviewStudiedWordsByDate")
    @ResponseBody
    public R reviewStudiedWordsByDate(@RequestParam(value = "date", required = false) String date,
                                      @RequestParam(value = "page", required = false) Integer pageNum,
                                        @RequestParam(value = "limit", required = false) Integer pageSize) {
        Subject subject = SecurityUtils.getSubject();
        EUser user = (EUser) subject.getPrincipal();
        if (user == null) {
            return R.error(HttpStatus.UNAUTHORIZED.value(), String.valueOf(HttpStatus.UNAUTHORIZED));
        }
        try {
            log.info("{}", date);
            if (date == null) {
                date = AcquireDate.getNowDays();
            }
            PageHelper.startPage(pageNum, pageSize);
            List<EWords> words = wordsService.reviewStudiedWordsByDate(user.getUId(), date);
            PageInfo<EWords> pageInfo = new PageInfo<>(words);
            map.clear();
            map.put("data", pageInfo.getList());
            map.put("count", pageInfo.getTotal());
            return R.ok(map);
        } catch (Exception e) {
            log.info("用户：{},开始复习当天的单词 出错：{}", user.getUName(), e.toString());
            return R.error(e.getMessage());
        }
    }

    /**
     * 更新 标志
     *
     * @param wid    单词id
     * @param choice 选择  true:更新，false:减少
     * @return {@link R}
     */
    @GetMapping("/updateSign/{wid}/{choice}")
    @ResponseBody
    public R updateSign(@PathVariable("wid") Integer wid, @PathVariable("choice") Boolean choice) {
        Subject subject = SecurityUtils.getSubject();
        EUser user = (EUser) subject.getPrincipal();
        if (user == null) {
            return R.error(HttpStatus.UNAUTHORIZED.value(), String.valueOf(HttpStatus.UNAUTHORIZED));
        }
        try {
            int i = wordsService.updateESign(user.getUId(), wid, choice);
            if (i == -1) {
                return R.error();
            }
            return R.ok(String.valueOf(i));
        } catch (Exception e) {
            log.info("用户：{},更新标记 出错：{}", user.getUName(), e.toString());
            return R.error();
        }
    }


    /**
     * 查询词
     *
     * @param condition 条件
     * @return {@link R}
     */
    @GetMapping("/queryWord")
    @ResponseBody
    public R queryWord(@RequestParam("condition") String condition) {
        Subject subject = SecurityUtils.getSubject();
        EUser user = (EUser) subject.getPrincipal();
        if (user == null) {
            return R.error(HttpStatus.UNAUTHORIZED.value(), String.valueOf(HttpStatus.UNAUTHORIZED));
        }
        try {
            List<Map<String, Object>> list = wordsService.queryWord(condition);
            map.clear();
            map.put("data", list);
            return R.ok(map);
        } catch (Exception e) {
            log.info("用户：{}, 查询词 出错：{}", user.getUName(), e.toString());
            e.printStackTrace();
            return R.error();
        }
    }

    /**
     * 标志词复习
     *
     * @param choice true:所有标记过的，false:选择有标记的
     * @param pageNum   当前页
     * @param pageSize  每页大小
     * @return {@link R}
     */
    @GetMapping("/signWordReview/{choice}")
    @ResponseBody
    public R signWordReview(@PathVariable("choice") Boolean choice, @RequestParam("page") Integer pageNum,
                            @RequestParam("limit") Integer pageSize) {
        Subject subject = SecurityUtils.getSubject();
        EUser user = (EUser) subject.getPrincipal();
        if (user == null) {
            log.info("标志词复习 出错：{}","null");
            return R.error(HttpStatus.UNAUTHORIZED.value(), String.valueOf(HttpStatus.UNAUTHORIZED));
        }
        try {
            // 测试
            PageInfo<EWords> pageInfo = wordsService.signWordReview(user.getUId(), choice, pageNum, pageSize);
            map.clear();
            map.put("data", pageInfo.getList());
            map.put("count", pageInfo.getTotal());
            log.info("用户：{} 标志词复习；时间：{}, 类型：{}", user.getUName(), AcquireDate.getNowDays(), choice);
            return R.ok(map);
        } catch (Exception e) {
            log.info("用户：{}, 标志词复习 出错：{}", user.getUName(), e.toString());
            e.printStackTrace();
            return R.error();
        }
    }

    /**
     * 清空打卡数据
     *
     * @param mold 清空的打卡的类型
     * @return {@link R}
     */
    @PostMapping("/clearClockData/{mold}")
    @ResponseBody
    public R clearClockData(@PathVariable("mold") String mold) {
        Subject subject = SecurityUtils.getSubject();
        EUser user = (EUser) subject.getPrincipal();
        if (user == null) {
            return R.error(HttpStatus.UNAUTHORIZED.value(), String.valueOf(HttpStatus.UNAUTHORIZED));
        }
        int i = wordsService.clearClockData(user.getUId(), mold);
        if (i == -1) {
            return R.error();
        }
        return R.ok("清空的数量为：" + i);
    }

    @GetMapping("/getSelf")
    public R getSelf() throws Exception {
        map.clear();
        Map<String, Object> map1 = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        Subject subject = SecurityUtils.getSubject();
        EUser user = (EUser) subject.getPrincipal();


        EUserData data = userDataMapper.selectById(user.getUId());
        Field[] fields = data.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            map1.put(field.getName(), field.get(data));
        }
        EUserData connUser = userDataMapper.selectById(data.getUConnId());// 关联的用户数据
        fields = connUser.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            map1.put("o_" + field.getName(), field.get(connUser));
        }

        map1.put("user_name", user.getUName());

        QueryWrapper<EStudied> wrapper = new QueryWrapper<>();
        wrapper.eq("u_id", user.getUId()).eq("s_mold", "common");
        map1.put("commonNums", studiedMapper.selectCount(wrapper));
        wrapper.clear();
        wrapper.eq("u_id", user.getUId()).eq("s_mold", "red");
        map1.put("redNums", studiedMapper.selectCount(wrapper));
        wrapper.clear();
        wrapper.eq("u_id", user.getUId()).eq("s_mold", "love");
        map1.put("loveNums", studiedMapper.selectCount(wrapper));
        wrapper.clear();
        wrapper.eq("u_id", user.getUId()).eq("s_mold", "love_middle");
        map1.put("loveMiddleNums", studiedMapper.selectCount(wrapper));
        map1.put("count", map1.size());

        log.info(" map1 {}", map1);
        list.add(map1);
        map.put("data", list);
        return R.ok(map);
    }

    @PostMapping("/self/update")
    public R selfUpdate(EUserData userData) {
        log.info("用户自动更新的(EUserData)id: {}", userData.getUId());
        int i = userDataMapper.updateById(userData);
        return R.ok(String.valueOf(i));
    }

    /**
     * 发送邮件
     *
     * @return boolean
     */
    @GetMapping("/sendEmail")
    @ResponseBody
    public R sendMail(@RequestParam("content") String content, @RequestParam("subject") String subject) {

        log.info("发送邮件 {}", content);
        Subject subjects = SecurityUtils.getSubject();
        EUser users = (EUser) subjects.getPrincipal();
        EUserData userData = userDataMapper.selectById(users.getUId());
        EUser user = userMapper.selectById(userData.getUConnId());
        if (user == null) {
            return R.error();
        }
        String email = user.getUEmail();
        if (email == null) {
            return R.error();
        }
        try {
            emailService.sendMessage(email, subject,
                    content);
            log.info("邮件发送成功：{}", email);
            return R.ok();
        } catch (Exception e) {
            return R.error();
        }
    }

    /**
     * 查询所有线数据
     * 查询所有打卡数据
     *
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @return {@link R}
     */
    @GetMapping("/queryAllCordData")
    public R queryAllCordData(@RequestParam("page") Integer pageNum, @RequestParam("limit") Integer pageSize) {
        log.info("queryAllCordData 当前页：{} 当前页大小：{}", pageNum, pageSize);
        PageHelper.startPage(pageNum, pageSize);
        Subject subject = SecurityUtils.getSubject();
        EUser user = (EUser) subject.getPrincipal();
        QueryWrapper<EAllRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("u_id", user.getUId());
        List<EAllRecord> records = recordMapper.selectList(wrapper);

        PageInfo<EAllRecord> pageInfo = new PageInfo<>(records);
        map.clear();
        map.put("data", pageInfo.getList());
        map.put("count", pageInfo.getTotal());
        return R.ok(map);
    }

}
