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
import java.util.Date;

/**
 * <p>
 * 记录所有用户打卡的时间，和当天的单词数
 * </p>
 *
 * @author ljb
 * @since 2023-04-14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("e_all_record")
public class EAllRecord extends Model<EAllRecord> {

    @TableId(value = "c_id", type = IdType.AUTO)
    private Integer cId;

    /**
     * 用户id
     */
    @TableField("u_id")
    private Integer uId;

    /**
     * 打卡时间
     */
    @TableField("c_date")
    private String cDate;

    /**
     * 当天的单词数
     */
    @TableField("c_word_num")
    private Integer cWordNum;

}
