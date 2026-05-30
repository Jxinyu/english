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
 * 这是记录用户已经学习的单词表
 * </p>
 *
 * @author ljb
 * @since 2023-04-14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("e_studied")
public class EStudied extends Model<EStudied> {

    /**
     * 用户已经学习的id
     */
    @TableId(value = "s_id", type = IdType.AUTO)
    private Integer sId;

    /**
     * 用户id
     */
    @TableField("u_id")
    private Integer uId;

    /**
     * 单词id
     */
    @TableField("w_id")
    private Integer wId;

    /**
     * 单词的类型
     */
    @TableField("s_mold")
    private String sMold;

    /**
     * 单词学习的时间
     */
    @TableField("s_memo_date")
    private String sMemoDate;


}
