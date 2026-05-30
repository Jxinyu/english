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
 * 这是记录用户标记单词的信息的表，标记次数越多用户越不熟悉
 * </p>
 *
 * @author ljb
 * @since 2023-04-14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("e_sign")
public class ESign extends Model<ESign> {

    /**
     * 标记id
     */
    @TableId(value = "l_id", type = IdType.AUTO)
    private Integer lId;

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
     * 不会的重要程度
     */
    @TableField("l_rank")
    private Integer lRank;

    /**
     * 是否已经记住(0:true, 1:false)
     */
    @TableField("l_is_memo")
    private Integer lIsMemo;

    /**
     * 记录最大的等级
     */
    @TableField("l_max_rank")
    private Integer lMaxRank;

}
