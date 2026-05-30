package com.ljb.english;

import com.ljb.english.pojo.EWords;
import com.ljb.english.service.EWordsService;
import com.ljb.english.utils.ScheduleUtils;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Slf4j
class EnglishApplicationTests {
    @Autowired
    EWordsService wordsService;

    @Test
    void contextLoads() {

    }
}
