package com.t13max.suyaclock.entity;

import lombok.Data;

/**
 * @author t13max
 * @since 11:15 2025/2/25
 */
@Data
public class PlanEntity {

    //唯一id 这一天零点的时间戳+ 计划id
    private long id;

    //计划描述
    private String desc;

    //时间段
    private String timeScope;

    //打卡总结
    private String clockIn;

    //开始时间
    private long startMills;

    //完成时间
    private long finishMills;

    //预期完成时间
    private long expectFinishMills;

    @Override
    public String toString() {
        return this.id % 100 + ". " + this.timeScope + ": " + this.desc + (this.finishMills > 0 ? " √" : "");
    }

}
