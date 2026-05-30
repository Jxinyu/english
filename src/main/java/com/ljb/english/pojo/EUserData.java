package com.ljb.english.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
@TableName("e_user_data")
public class EUserData {

  @TableId(value = "ul_id", type = IdType.AUTO)
  private long ulId;

  @TableField("u_id")
  private long uId;

  @TableField("each_day_num")
  private long eachDayNum;

  @TableField("all_num")
  private long allNum;

  @TableField("w_mold_choice")
  private String wMoldChoice;

  @TableField("now_day_num")
  private long nowDayNum;

  @TableField("now_day_complete")
  private long nowDayComplete;

  @TableField("all_day")
  private long allDay;

  @TableField("all_comple_day")
  private long allCompleDay;

  @TableField("memo_mode")
  private String memoMode;

  @TableField("u_conn_id")
  private String uConnId;


}
