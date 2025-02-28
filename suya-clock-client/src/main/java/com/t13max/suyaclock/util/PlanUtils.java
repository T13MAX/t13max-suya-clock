package com.t13max.suyaclock.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.t13max.suyaclock.consts.Const;
import com.t13max.suyaclock.entity.PlanEntity;
import com.t13max.util.TimeUtil;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author t13max
 * @since 16:41 2025/2/28
 */
public class PlanUtils {

    public static List<PlanEntity> checkGetPlan() {
        long nowMills = TimeUtil.nowMills();
        long zeroOfDayTime = TimeUtil.getZeroOfDayTime(nowMills);
        List<PlanEntity> planEntities = DataUtils.getPlansByIdRange(zeroOfDayTime, zeroOfDayTime + TimeUnit.DAYS.toMillis(1) - 1);
        if (planEntities == null || planEntities.isEmpty()) {
            planEntities = initPlan(zeroOfDayTime);
            for (PlanEntity planEntity : planEntities) {
                DataUtils.insertPlan(planEntity);
            }
        }
        return planEntities;
    }

    public static List<PlanEntity> initPlan(long zeroOfDayTime) {
        List<PlanEntity> result = new LinkedList<>();
        // 获取 JAR 所在目录
        String jarDirectory = System.getProperty(Const.SCRIPT_DIR);
        if (jarDirectory != null) {
            // 假设 config.json 文件放在 JAR 所在目录
            String filePath = jarDirectory + "/plan.json";

            try (Reader reader = new FileReader(filePath)) {
                // 使用 FastJSON 解析 JSON 文件
                JSONObject jsonObject = JSON.parseObject(reader);

                // 访问 JSON 数据
                JSONArray dayOfTheWeekJsonArray = jsonObject.getJSONArray("dayOfTheWeek");
                JSONArray planJsonArray = jsonObject.getJSONArray("plan");
                for (int i = 0; i < planJsonArray.size(); i++) {
                    JSONObject planJsonObject = planJsonArray.getJSONObject(i);
                    PlanEntity planEntity = createPlanEntity(zeroOfDayTime, planJsonObject);
                    result.add(planEntity);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static PlanEntity createPlanEntity(long zeroOfDayTime, JSONObject planJsonObject) {

        Integer id = planJsonObject.getInteger("id");
        String desc = planJsonObject.getString("desc");
        String timeScope = planJsonObject.getString("timeScope");

        String[] split = timeScope.split("-");
        String[] split1 = split[1].split(":");
        int hours = Integer.parseInt(split1[0]);
        int minutes = Integer.parseInt(split1[1]);
        long triggerMills = zeroOfDayTime + TimeUnit.HOURS.toMillis(hours) + TimeUnit.MINUTES.toMillis(minutes);

        PlanEntity planEntity = new PlanEntity();
        planEntity.setId(zeroOfDayTime + id);
        planEntity.setDesc(desc);
        planEntity.setTimeScope(timeScope);
        planEntity.setExpectFinishMills(triggerMills);
        return planEntity;
    }

    public static void saveTemplate(List<PlanEntity> planEntities) {
        // 获取 JAR 所在目录
        String jarDirectory = System.getProperty(Const.SCRIPT_DIR);
        if (jarDirectory != null) {
            // 假设 config.json 文件放在 JAR 所在目录
            String filePath = jarDirectory + "/plan.json";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("dayOfTheWeek", "");
            JSONArray planJsonArray = new JSONArray();
            int id = 1;
            for (PlanEntity planEntity : planEntities) {
                JSONObject planObject = new JSONObject();
                planObject.put("id", id++);
                planObject.put("desc", planEntity.getDesc());
                planObject.put("timeScope", planEntity.getTimeScope());
                planJsonArray.add(planObject);
            }
            jsonObject.put("plan", planJsonArray);
            writeJsonToFile(jsonObject, filePath);
        }
    }

    public static void writeJsonToFile(JSONObject jsonObject, String filePath) {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            // 使用fastjson的toJSONString方法将JSONObject对象转为JSON字符串并写入文件
            fileWriter.write(JSON.toJSONString(jsonObject));  // true表示格式化输出，带有缩进
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
