package com.ljb.english.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljb.english.mapper.ESignMapper;
import com.ljb.english.pojo.ESign;
import com.ljb.english.service.ESignService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 这是记录用户标记单词的信息的表，标记次数越多用户越不熟悉 服务实现类
 * </p>
 *
 * @author ljb
 * @since 2023-04-14
 */
@Service
public class ESignServiceImpl extends ServiceImpl<ESignMapper, ESign> implements ESignService {

}
