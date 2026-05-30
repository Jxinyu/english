package com.ljb.english.controller;

import com.ljb.english.service.EWordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/a")
public class ApiController {
    @Autowired
    private EWordsService wordsService;

    @GetMapping("/a")
    public Map<String, Object> getUserById() {
        return wordsService.getNextInfoUp(1, "love");
    }

    // {
    //      "phrase":
    //          [
    //              {"pen":"死还是活","pzh":"死了或活着","pid":45,"pword":"活着"},
    //              {"pen":"仍然alive","pzh":"仍然存在的；仍然存在的","pid":46,"pword":"Alive"}
    //          ],
    //      "word_info":
    //          {
    //          "id":9449,
    //          "typeTotalNumber":2147,
    //          "todayStudiedWordsNumber":110,
    //          "eachDayWordNumber":100,
    //          "learnedTypeWordNumber":50,
    //          "wmold":"love ",
    //          "wen":"活着",
    //          "wzh":"adj.活着的；有活力的，活跃的",
    //          "wimportance":null,
    //          "waudio":"818cf02c-f805-3862-955f-7f9204b743ec",
    //          "wunit":"1",
    //          "wmemoSkill":"a 在…， …的 + live 活 → 活的",
    //          "wphonetic":"/əˈlaɪv/"
    //          }
    // }
}
