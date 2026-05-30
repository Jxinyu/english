package com.ljb.english.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 这是记录相关单词的短语
 * </p>
 *
 * @author ljb
 * @since 2023-04-14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("e_phrase")
public class EPhrase extends Model<EPhrase> {

    @TableId(value = "p_id", type = IdType.AUTO)
    private Integer pId;

    /**
     * 单词
     */
    @TableField("p_word")
    private String pWord;

    /**
     * 英文
     */
    @TableField("p_en")
    private String pEn;

    /**
     * 中文
     */
    @TableField("p_zh")
    private String pZh;

}
