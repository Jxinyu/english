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
 *
 * </p>
 *
 * @author ljb
 * @since 2023-04-14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("e_user")
public class EUser extends Model<EUser> {

    @TableId(value = "u_id", type = IdType.AUTO)
    private Integer uId;

    /**
     * 用户名
     */
    @TableField("u_name")
    private String uName;

    /**
     * 密码
     */
    @TableField("u_pwd")
    private String uPwd;

    /**
     * 权限等级
     */
    @TableField("u_rank")
    private String uRank;

    /**
     * 邮箱
     */
    @TableField("u_email")
    private String uEmail;
}
