package com.refinement;

import cn.hutool.core.io.FileUtil;
import com.refinement.config.BusinessException;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 数据库表字段更改
 * @author wn
 * @date 2020/11/05
 */
public class ExecuteSQLMain {

    /**
     * 数据库连接路径
     */
    private static final String url = "jdbc:mysql://localhost:3306/image?characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2b8";

    /**
     * 用户名
     */
    private static final String username = "root";

    /**
     * 密码
     */
    private static final String password = "root";

    /**
     * 数据库连接对象
     */
    public static Connection connection;

    /**
     * sql执行队形
     */
    public static Statement statement;

    /**
     * 初始化连接，并创建table
     * 注意：！！
     * 需要启动下 CostManageApplication 服务将sql文件编译到 target 下，否则会找不到文件
     * 运行过程会较慢，稍等片刻吧
     */
    @BeforeAll
    static void before()  {
        // 加载驱动
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 获取数据库连接对象
            connection = DriverManager.getConnection(url, username, password);
            // 获取语句对象
            statement = connection.createStatement();

            //================== 创建table start ==================
            //  mybatis 执行器
            ScriptRunner runner = new ScriptRunner(connection);
            runner.setAutoCommit(true);
            // 读取sql文件
            File file = FileUtil.file("db/refinement.sql");
            runner.setFullLineDelimiter(false);
            runner.setDelimiter(";");//语句结束符号设置
            runner.setLogWriter(null);//日志数据输出，这样就不会输出过程
            runner.setSendFullScript(false);
            runner.setAutoCommit(true);
            runner.setStopOnError(true);
            runner.runScript(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            //================== 创建table end ==================
        } catch (ClassNotFoundException | SQLException | FileNotFoundException e) {
            // 终止后续操作
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 关闭资源
     */
    @AfterAll
    static void after() {
        try {
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 操作表
     */
    @Test
    public void run() {
        try {
            // user表sql
            statement.addBatch("alter table user modify type tinyint(3) null comment '用户类型 0-root 1-生产运营部 2-事业部 3-项目部'");
            statement.addBatch("alter table user add phone varchar(20) null comment '电话'");
            statement.addBatch("alter table user change deleted delete_flag tinyint(1) default 0 not null comment '删除标记 0-正常 1-删除'");
            //设置username敏感型，区分大小写
            statement.addBatch("alter table user change username username varchar(50) binary not null comment '用户名'");
            statement.addBatch("alter table user add remark varchar(64) null comment '备注'");
            // project表sql
            statement.addBatch("alter table project add delete_flag tinyint(1) default 0 not null comment '删除标记 0-正常 1-删除'");
            statement.addBatch("alter table project add budget_amount decimal(20, 6) null  comment '预算金额'");
            statement.addBatch("alter table project add system_type tinyint(1) default 1 not null comment '1->形象进度系统 2->成本管理系统'");
            statement.addBatch("alter table project add model_id bigint(20) null comment '模板id'");
            int[] executeBatch = statement.executeBatch();
            if (executeBatch.length == 0) {
                // 回滚
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
