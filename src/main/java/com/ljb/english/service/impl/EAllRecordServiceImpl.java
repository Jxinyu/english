package com.ljb.english.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljb.english.mapper.EAllRecordMapper;
import com.ljb.english.pojo.EAllRecord;
import com.ljb.english.service.EAllRecordService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 记录所有用户打卡的时间，和当天的单词数 服务实现类
 * </p>
 *
 * @author ljb
 * @since 2023-04-14
 */
@Service
public class EAllRecordServiceImpl extends ServiceImpl<EAllRecordMapper, EAllRecord> implements EAllRecordService {

}
