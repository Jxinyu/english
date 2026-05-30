package com.ljb.english.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ljb
 * @since 2023-04-14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("e_words")
public class EWords extends Model<EWords> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 英文
     */
    @TableField("w_en")
    private String wEn;

    /**
     * 中文
     */
    @TableField("w_zh")
    private String wZh;

    /**
     * 音标
     */
    @TableField("w_phonetic")
    private String wPhonetic;

    /**
     * 音频
     */
    @TableField("w_audio")
    private String wAudio;

    /**
     * 类型
     */
    @TableField("w_mold")
    private String wMold;

    /**
     * 单元
     */
    @TableField("w_unit")
    private String wUnit;

    /**
     * 记忆技巧
     */
    @TableField("w_memo_skill")
    private String wMemoSkill;

    /**
     * 重要程度
     */
    @TableField("w_importance")
    private Integer wImportance;

    /**
     * 类型单词总数
     */
    private Integer typeTotalNumber;

    /**
     * 当日已经学习的单词数量
     */
    private Integer todayStudiedWordsNumber;

    /**
     * 每日应该学习的单词数量
     */
    private Integer eachDayWordNumber;

    /**
     * 已学类型单词数量
     */
    private Integer learnedTypeWordNumber;

}
