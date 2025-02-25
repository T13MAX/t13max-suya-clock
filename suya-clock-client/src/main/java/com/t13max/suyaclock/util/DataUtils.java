package com.t13max.suyaclock.util;

import com.t13max.suyaclock.entity.PlanEntity;
import lombok.experimental.UtilityClass;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author t13max
 * @since 11:13 2025/2/25
 */
@UtilityClass
public class DataUtils {

    private final static String URL = "jdbc:sqlite:suya.db"; // 数据库文件路径
    private final static String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS plan_entity (" +
            "id BIGINT PRIMARY KEY, " +
            "desc TEXT, " +
            "time_scope TEXT, " +
            "clock_in TEXT, " +
            "start_mills BIGINT, " +
            "finish_mills BIGINT," +
            "expect_finish_mills BIGINT)";
    private final static String INSERT_SQL = "INSERT INTO plan_entity (id, desc, time_scope, clock_in, start_mills, finish_mills, expect_finish_mills) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private final static String SELECT_SQL = "SELECT * FROM plan_entity";
    private final static String UPDATE_SQL = "UPDATE plan_entity SET desc = ?, time_scope = ?, clock_in = ?, start_mills = ?, finish_mills = ? , expect_finish_mills=? WHERE id = ?";
    private final static String DELETE_SQL = "DELETE FROM plan_entity WHERE id = ?";
    private final static String SELECT_RANGE_SQL = "SELECT * FROM plan_entity WHERE id BETWEEN ? AND ?";

    static {
        // 连接到 SQLite 数据库
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE_TABLE_SQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 插入数据
    public void insertPlan(PlanEntity plan) {

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL)) {
            pstmt.setLong(1, plan.getId());
            pstmt.setString(2, plan.getDesc());
            pstmt.setString(3, plan.getTimeScope());
            pstmt.setString(4, plan.getClockIn());
            pstmt.setLong(5, plan.getStartMills());
            pstmt.setLong(6, plan.getFinishMills());
            pstmt.setLong(7, plan.getExpectFinishMills());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 查询数据
    public List<PlanEntity> getAllPlans() {
        List<PlanEntity> plans = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_SQL)) {

            while (rs.next()) {
                PlanEntity plan = new PlanEntity();
                plan.setId(rs.getLong("id"));
                plan.setDesc(rs.getString("desc"));
                plan.setTimeScope(rs.getString("time_scope"));
                plan.setClockIn(rs.getString("clock_in"));
                plan.setStartMills(rs.getLong("start_mills"));
                plan.setFinishMills(rs.getLong("finish_mills"));
                plan.setExpectFinishMills(rs.getLong("expect_finish_mills"));
                plans.add(plan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plans;
    }

    // 根据指定ID范围查询数据
    public List<PlanEntity> getPlansByIdRange(long startId, long endId) {
        List<PlanEntity> plans = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(SELECT_RANGE_SQL)) {
            pstmt.setLong(1, startId);
            pstmt.setLong(2, endId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PlanEntity plan = new PlanEntity();
                    plan.setId(rs.getLong("id"));
                    plan.setDesc(rs.getString("desc"));
                    plan.setTimeScope(rs.getString("time_scope"));
                    plan.setClockIn(rs.getString("clock_in"));
                    plan.setStartMills(rs.getLong("start_mills"));
                    plan.setFinishMills(rs.getLong("finish_mills"));
                    plan.setExpectFinishMills(rs.getLong("expect_finish_mills"));
                    plans.add(plan);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plans;
    }

    // 更新数据
    public void updatePlan(PlanEntity plan) {

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {
            pstmt.setString(1, plan.getDesc());
            pstmt.setString(2, plan.getTimeScope());
            pstmt.setString(3, plan.getClockIn());
            pstmt.setLong(4, plan.getStartMills());
            pstmt.setLong(5, plan.getFinishMills());
            pstmt.setLong(6, plan.getExpectFinishMills());
            pstmt.setLong(7, plan.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删除数据
    public void deletePlan(long id) {

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(DELETE_SQL)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}