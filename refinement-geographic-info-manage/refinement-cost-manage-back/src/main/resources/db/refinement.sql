/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : localhost:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 06/11/2020 14:06:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for decomposition
-- ----------------------------
DROP TABLE IF EXISTS `decomposition`;
CREATE TABLE `decomposition`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分解项id',
  `one_level_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '一级分项',
  `two_level_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '二级分项',
  `three_level_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '三级分项',
  `budget_scale` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '默认预算比例',
  `level` tinyint(1) NOT NULL COMMENT '级别 1->一级 2->二级 3->三级',
  `recorder_role_id` bigint(20) NULL DEFAULT NULL COMMENT '记录员角色id',
  `auditor_role_id` bigint(20) NULL DEFAULT NULL COMMENT '审核员角色id',
  `model_id` bigint(20) NOT NULL COMMENT '模板id',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父级id',
  `delete_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标记 0->正常 1->删除',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '添加时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of decomposition
-- ----------------------------

-- ----------------------------
-- Table structure for function
-- ----------------------------
DROP TABLE IF EXISTS `function`;
CREATE TABLE `function`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '功能id',
  `function_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '功能名称',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '上级功能id ',
  `level` tinyint(1) NOT NULL COMMENT '级别 1->一级 2->二级 ',
  `resource_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源id，多个英文逗号隔开',
  `delete_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标记 0->正常 1->删除',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '添加时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 55 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of function
-- ----------------------------
INSERT INTO `function` VALUES (1, '统一登录平台', 0, 1, '', 0, '2020-10-26 13:51:21', '2020-11-03 14:36:53');
INSERT INTO `function` VALUES (2, '成本管理子系统', 0, 1, '', 0, '2020-10-26 13:52:29', '2020-11-03 14:36:53');
INSERT INTO `function` VALUES (3, '形象进度子系统', 0, 1, '', 0, '2020-11-03 14:21:55', '2020-11-03 14:36:53');
INSERT INTO `function` VALUES (4, '用户', 1, 2, '', 0, '2020-11-03 14:22:55', '2020-11-05 13:51:57');
INSERT INTO `function` VALUES (5, '部门', 1, 2, '', 0, '2020-11-03 14:23:05', '2020-11-05 13:51:58');
INSERT INTO `function` VALUES (6, '角色', 1, 2, '', 0, '2020-11-03 14:26:44', '2020-11-05 13:52:00');
INSERT INTO `function` VALUES (7, '新项目', 2, 2, '', 0, '2020-11-03 14:27:04', '2020-11-03 14:36:53');
INSERT INTO `function` VALUES (8, '进行中项目', 2, 2, '', 0, '2020-11-03 14:27:15', '2020-11-03 14:36:53');
INSERT INTO `function` VALUES (9, '历史项目', 2, 2, '', 0, '2020-11-03 14:27:22', '2020-11-03 14:36:53');
INSERT INTO `function` VALUES (10, '模板', 2, 2, '', 0, '2020-11-03 14:27:31', '2020-11-05 13:51:53');
INSERT INTO `function` VALUES (11, '新项目', 3, 2, '', 0, '2020-11-03 14:27:43', '2020-11-05 13:51:42');
INSERT INTO `function` VALUES (12, '进行中项目', 3, 2, '', 0, '2020-11-03 14:27:52', '2020-11-05 13:51:48');
INSERT INTO `function` VALUES (13, '事业部', 3, 2, '', 0, '2020-11-03 14:27:59', '2020-11-05 13:51:50');
INSERT INTO `function` VALUES (14, '查询', 4, 3, ',1,', 0, '2020-11-03 14:28:53', '2020-11-03 17:48:51');
INSERT INTO `function` VALUES (15, '新增', 4, 3, ',2,', 0, '2020-11-03 14:29:10', '2020-11-03 17:48:56');
INSERT INTO `function` VALUES (16, '修改', 4, 3, ',3,', 0, '2020-11-03 14:29:33', '2020-11-03 17:49:00');
INSERT INTO `function` VALUES (17, '删除', 4, 3, ',4,', 0, '2020-11-03 14:31:52', '2020-11-03 17:49:06');
INSERT INTO `function` VALUES (18, '查询', 5, 3, ',15,', 0, '2020-11-03 14:34:45', '2020-11-03 17:49:11');
INSERT INTO `function` VALUES (19, '新增', 5, 3, ',16,', 0, '2020-11-03 14:35:10', '2020-11-03 17:49:16');
INSERT INTO `function` VALUES (20, '修改', 5, 3, ',17,18,', 0, '2020-11-03 14:35:43', '2020-11-03 17:49:23');
INSERT INTO `function` VALUES (21, '删除', 5, 3, ',19,', 0, '2020-11-03 14:37:23', '2020-11-03 17:49:28');
INSERT INTO `function` VALUES (22, '查询', 6, 3, ',9,10,', 0, '2020-11-03 14:38:05', '2020-11-03 17:49:37');
INSERT INTO `function` VALUES (23, '新增', 6, 3, ',12,', 0, '2020-11-03 14:38:35', '2020-11-03 17:49:42');
INSERT INTO `function` VALUES (24, '修改', 6, 3, ',13,14,', 0, '2020-11-03 14:39:00', '2020-11-03 17:49:48');
INSERT INTO `function` VALUES (25, '删除', 6, 3, ',11,', 0, '2020-11-03 14:39:17', '2020-11-03 17:49:56');
INSERT INTO `function` VALUES (26, '查询', 7, 3, ',20,34,', 0, '2020-11-03 14:40:43', '2020-11-04 11:25:22');
INSERT INTO `function` VALUES (27, '新建', 7, 3, ',21,', 0, '2020-11-03 14:41:34', '2020-11-03 17:50:07');
INSERT INTO `function` VALUES (28, '细化分解', 7, 3, ',22,', 0, '2020-11-03 14:42:02', '2020-11-03 17:50:10');
INSERT INTO `function` VALUES (29, '新建确认', 7, 3, ',23,', 0, '2020-11-03 14:42:23', '2020-11-03 17:50:19');
INSERT INTO `function` VALUES (30, '项目导出', 7, 3, ',29,', 0, '2020-11-03 14:45:37', '2020-11-03 17:50:23');
INSERT INTO `function` VALUES (31, '月报审评', 8, 3, ',24,', 0, '2020-11-03 14:43:03', '2020-11-03 17:50:26');
INSERT INTO `function` VALUES (32, '细化分解列表', 8, 3, ',25,50,', 0, '2020-11-03 14:43:42', '2020-11-03 17:50:29');
INSERT INTO `function` VALUES (33, '月报新增', 8, 3, ',26,51,', 0, '2020-11-03 14:43:55', '2020-11-03 17:50:32');
INSERT INTO `function` VALUES (34, '月报修改', 8, 3, ',27,', 0, '2020-11-03 14:44:27', '2020-11-03 17:50:34');
INSERT INTO `function` VALUES (35, '项目完成', 8, 3, ',28,', 0, '2020-11-03 14:46:55', '2020-11-03 17:50:37');
INSERT INTO `function` VALUES (36, '项目导出', 8, 3, ',30,', 0, '2020-11-03 14:47:32', '2020-11-03 17:50:42');
INSERT INTO `function` VALUES (37, '细化分解导出', 8, 3, ',31,', 0, '2020-11-03 14:47:56', '2020-11-03 17:50:45');
INSERT INTO `function` VALUES (38, '项目导出', 9, 3, ',32,33,', 0, '2020-11-03 14:49:56', '2020-11-03 17:50:47');
INSERT INTO `function` VALUES (39, '查询', 10, 3, ',5,6,', 0, '2020-11-03 14:50:56', '2020-11-03 17:50:49');
INSERT INTO `function` VALUES (40, '新增', 10, 3, ',7,', 0, '2020-11-03 14:51:09', '2020-11-03 17:50:52');
INSERT INTO `function` VALUES (41, '删除', 10, 3, ',8,', 0, '2020-11-03 14:51:24', '2020-11-03 17:50:55');
INSERT INTO `function` VALUES (42, '查询', 11, 3, ',39,', 0, '2020-11-05 13:40:47', '2020-11-05 13:42:19');
INSERT INTO `function` VALUES (43, '新建', 11, 3, ',40,', 0, '2020-11-05 13:43:06', '2020-11-05 13:43:06');
INSERT INTO `function` VALUES (44, '新建确认', 11, 3, ',41,', 0, '2020-11-05 13:43:31', '2020-11-05 13:43:31');
INSERT INTO `function` VALUES (45, '删除', 11, 3, ',49,', 0, '2020-11-05 13:46:41', '2020-11-05 13:46:41');
INSERT INTO `function` VALUES (46, '细化查询', 12, 3, ',42,', 0, '2020-11-05 13:47:48', '2020-11-05 13:51:24');
INSERT INTO `function` VALUES (47, '细化分解', 11, 3, ',43,', 0, '2020-11-05 13:48:36', '2020-11-05 13:51:28');
INSERT INTO `function` VALUES (48, '细化上报', 12, 3, ',44,', 0, '2020-11-05 13:49:36', '2020-11-05 13:51:19');
INSERT INTO `function` VALUES (49, '生产运营部审核', 12, 3, ',45,', 0, '2020-11-05 13:50:11', '2020-11-05 13:50:11');
INSERT INTO `function` VALUES (50, '事业部审核', 12, 3, ',46,', 0, '2020-11-05 13:50:29', '2020-11-05 13:50:29');
INSERT INTO `function` VALUES (51, '实施状态修改', 12, 3, ',48,', 0, '2020-11-05 13:51:05', '2020-11-05 13:52:23');
INSERT INTO `function` VALUES (52, '项目完成', 12, 3, ',47,', 0, '2020-11-05 13:52:45', '2020-11-05 13:52:45');
INSERT INTO `function` VALUES (53, '查询', 13, 3, ',35,', 0, '2020-11-05 13:53:29', '2020-11-05 13:53:29');
INSERT INTO `function` VALUES (54, '新增', 13, 3, ',36,', 0, '2020-11-05 13:53:43', '2020-11-05 13:53:43');

-- ----------------------------
-- Table structure for model
-- ----------------------------
DROP TABLE IF EXISTS `model`;
CREATE TABLE `model`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '模板id',
  `model_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模板名称',
  `status` int(1) NOT NULL DEFAULT 2 COMMENT '模板状态 1->使用中 2->未使用',
  `delete_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标记 0->正常 1->删除',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '添加时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of model
-- ----------------------------

-- ----------------------------
-- Table structure for organization
-- ----------------------------
DROP TABLE IF EXISTS `organization`;
CREATE TABLE `organization`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '组织id',
  `organization_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '组织名称',
  `parent_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '上级组织id',
  `level` int(10) NOT NULL COMMENT '级别 1->一级 2->二级 ..... n->n级',
  `dept_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否是事业部 0->否 1->是',
  `delete_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标记 0->正常 1->删除',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '添加时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of organization
-- ----------------------------
INSERT INTO `organization` VALUES (1, '总经理办公室', 0, 1, 0, 0, '2020-10-21 14:13:35', '2020-11-03 10:38:39');
INSERT INTO `organization` VALUES (2, '生产运营部', 0, 1, 0, 0, '2020-10-23 16:00:18', '2020-11-03 10:38:45');
INSERT INTO `organization` VALUES (3, '安全管理部', 0, 1, 0, 0, '2020-10-23 16:00:55', '2020-11-03 10:38:49');
INSERT INTO `organization` VALUES (4, '行政综合部', 0, 1, 0, 0, '2020-10-23 16:01:04', '2020-11-03 10:39:04');
INSERT INTO `organization` VALUES (5, '总经理', 1, 2, 0, 0, '2020-10-23 16:01:14', '2020-11-03 10:39:20');
INSERT INTO `organization` VALUES (6, '副总经理', 1, 2, 0, 0, '2020-10-23 16:01:52', '2020-11-03 10:39:28');
INSERT INTO `organization` VALUES (7, '部门主管', 2, 2, 0, 0, '2020-10-23 16:02:00', '2020-11-03 10:41:22');
INSERT INTO `organization` VALUES (8, '日常开支', 2, 2, 0, 0, '2020-10-23 16:02:07', '2020-11-03 10:41:34');
INSERT INTO `organization` VALUES (9, '外协管理', 2, 2, 0, 0, '2020-10-23 16:02:15', '2020-11-03 10:41:45');
INSERT INTO `organization` VALUES (10, '人员工资', 2, 2, 0, 0, '2020-10-23 16:03:46', '2020-11-03 10:41:54');
INSERT INTO `organization` VALUES (11, '测绘事业一部', 2, 2, 1, 0, '2020-10-23 17:20:43', '2020-11-06 13:55:50');
INSERT INTO `organization` VALUES (12, '测绘事业二部', 2, 2, 1, 0, '2020-10-23 17:21:31', '2020-11-06 13:55:52');
INSERT INTO `organization` VALUES (15, '测绘事业三部', 2, 2, 1, 0, '2020-10-26 16:37:24', '2020-11-06 13:55:53');
INSERT INTO `organization` VALUES (16, '管线探测事业部', 2, 2, 1, 0, '2020-10-26 16:48:12', '2020-11-06 13:55:56');
INSERT INTO `organization` VALUES (17, '数据处理中心', 2, 2, 1, 0, '2020-11-03 10:43:11', '2020-11-06 13:55:57');
INSERT INTO `organization` VALUES (18, '数据应用中心', 2, 2, 1, 0, '2020-11-03 10:43:25', '2020-11-06 13:55:58');
INSERT INTO `organization` VALUES (19, '部门主管', 3, 2, 0, 0, '2020-11-03 10:43:50', '2020-11-03 10:43:50');
INSERT INTO `organization` VALUES (20, '设备管理', 3, 2, 0, 0, '2020-11-03 10:43:59', '2020-11-03 10:43:59');
INSERT INTO `organization` VALUES (21, '部门主管', 4, 2, 0, 0, '2020-11-03 10:44:18', '2020-11-03 10:44:58');
INSERT INTO `organization` VALUES (22, '出行管理', 4, 2, 0, 0, '2020-11-03 10:44:30', '2020-11-03 10:44:59');

-- ----------------------------
-- Table structure for project_complete
-- ----------------------------
DROP TABLE IF EXISTS `project_complete`;
CREATE TABLE `project_complete`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `project_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '项目id',
  `system_type` int(1) NOT NULL COMMENT '系统类型 1-形象进度 2-成本管理',
  `state` int(1) NOT NULL COMMENT '项目状态 1-进行中 2-历史',
  `delete_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记 0->正常 1->删除',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of project_complete
-- ----------------------------

-- ----------------------------
-- Table structure for project_decomposition
-- ----------------------------
DROP TABLE IF EXISTS `project_decomposition`;
CREATE TABLE `project_decomposition`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关联id',
  `project_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '项目id',
  `budget_amount` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预算金额（元）',
  `decomposition_id` bigint(20) NOT NULL COMMENT '成本分解项id',
  `delete_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标记 0->正常 1->删除',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '添加时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 127 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of project_decomposition
-- ----------------------------

-- ----------------------------
-- Table structure for project_indirect_cost
-- ----------------------------
DROP TABLE IF EXISTS `project_indirect_cost`;
CREATE TABLE `project_indirect_cost`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '间接成本名称',
  `create_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '间接成本配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of project_indirect_cost
-- ----------------------------
INSERT INTO `project_indirect_cost` VALUES (1, '设备折旧', '2020-11-03 11:27:57', '2020-11-03 11:28:01');
INSERT INTO `project_indirect_cost` VALUES (2, '社保、附加等', '2020-11-03 11:27:59', '2020-11-03 11:28:03');
INSERT INTO `project_indirect_cost` VALUES (3, '验收费', '2020-11-03 11:28:03', '2020-11-03 11:28:06');
INSERT INTO `project_indirect_cost` VALUES (4, '配合费、入库费等', '2020-11-03 11:28:06', '2020-11-03 11:28:10');
INSERT INTO `project_indirect_cost` VALUES (5, '其他', '2020-11-03 11:28:09', '2020-11-03 11:28:12');

-- ----------------------------
-- Table structure for project_monthly_report
-- ----------------------------
DROP TABLE IF EXISTS `project_monthly_report`;
CREATE TABLE `project_monthly_report`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '月报id',
  `project_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '项目id',
  `project_decomposition_id` bigint(20) NOT NULL COMMENT '项目成本分解项关联id',
  `amount` decimal(20, 2) NULL COMMENT '本月费用（元）',
  `month` int(3) NOT NULL COMMENT '月份',
  `state` int(1) NOT NULL DEFAULT 0 COMMENT '审评状态 0-未审评 1-通过 2-不通过 3-未上报',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `delete_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标记 0->正常 1->删除',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '添加时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of project_monthly_report
-- ----------------------------

-- ----------------------------
-- Table structure for resource
-- ----------------------------
DROP TABLE IF EXISTS `resource`;
CREATE TABLE `resource`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资源id',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '资源名称',
  `uri` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '资源路径',
  `method` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '请求方式',
  `remark` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标记（描述）',
  `delete_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标记 0->正常 1->删除',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '添加时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 50 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of resource
-- ----------------------------
INSERT INTO `resource` VALUES (1, '用户列表', '/cost/user/list', 'GET', '查询', 0, '2020-10-22 18:09:58', '2020-10-23 11:12:37');
INSERT INTO `resource` VALUES (2, '新增用户', '/cost/user/add', 'POST', '新增', 0, '2020-10-23 11:06:51', '2020-10-23 11:12:27');
INSERT INTO `resource` VALUES (3, '修改用户', '/cost/user/update', 'POST', '修改', 0, '2020-10-23 11:13:01', '2020-10-23 11:13:01');
INSERT INTO `resource` VALUES (4, '删除用户', '/cost/user/delete', 'POST', '删除', 0, '2020-10-23 13:13:10', '2020-10-23 13:13:10');
INSERT INTO `resource` VALUES (5, '模板列表', '/cost/model/list', 'GET', '查询', 0, '2020-10-26 10:15:50', '2020-10-26 10:15:50');
INSERT INTO `resource` VALUES (6, '模板详情', '/cost/model/detail', 'POST', '详情', 0, '2020-10-26 10:16:34', '2020-10-26 10:16:34');
INSERT INTO `resource` VALUES (7, '新增模板', '/cost/model/add', 'POST', '新增', 0, '2020-10-26 10:17:06', '2020-10-26 10:17:06');
INSERT INTO `resource` VALUES (8, '删除模板', '/cost/model/delete', 'POST', '删除', 0, '2020-10-26 10:17:40', '2020-10-26 10:17:40');
INSERT INTO `resource` VALUES (9, '角色列表', '/cost/role/list', 'POST', '查询', 0, '2020-11-03 10:48:31', '2020-11-03 10:48:31');
INSERT INTO `resource` VALUES (10, '角色详情', '/cost/role/detail', 'POST', '详情', 0, '2020-11-03 10:49:07', '2020-11-03 10:49:07');
INSERT INTO `resource` VALUES (11, '删除角色', '/cost/role/delete', 'POST', '删除', 0, '2020-11-03 10:49:34', '2020-11-03 10:49:34');
INSERT INTO `resource` VALUES (12, '新增角色', '/cost/role/add', 'POST', '新增', 0, '2020-11-03 10:49:57', '2020-11-03 10:49:57');
INSERT INTO `resource` VALUES (13, '修改角色', '/cost/role/edit', 'POST', '修改', 0, '2020-11-03 10:50:39', '2020-11-03 10:50:39');
INSERT INTO `resource` VALUES (14, '移动角色', '/cost/role/update', 'POST', '修改', 0, '2020-11-03 10:51:04', '2020-11-03 10:51:04');
INSERT INTO `resource` VALUES (15, '组织列表', '/cost/organization/list', 'POST', '查询', 0, '2020-11-03 10:51:46', '2020-11-03 10:51:46');
INSERT INTO `resource` VALUES (16, '新增组织', '/cost/organization/add', 'POST', '新增', 0, '2020-11-03 10:52:18', '2020-11-03 10:52:18');
INSERT INTO `resource` VALUES (17, '修改组织', '/cost/organization/update', 'POST', '修改', 0, '2020-11-03 10:52:54', '2020-11-03 10:52:54');
INSERT INTO `resource` VALUES (18, '修改组织名称', '/cost/organization/edit', 'POST', '修改', 0, '2020-11-03 10:53:28', '2020-11-03 10:53:28');
INSERT INTO `resource` VALUES (19, '删除组织', '/cost/organization/delete', 'POST', '删除', 0, '2020-11-03 10:53:54', '2020-11-03 10:53:54');
INSERT INTO `resource` VALUES (20, '项目列表', '/cost/project/list', 'POST', '查询', 0, '2020-11-03 10:56:21', '2020-11-03 10:56:21');
INSERT INTO `resource` VALUES (21, '新建项目', '/cost/project/add', 'POST', '新建', 0, '2020-11-03 10:56:42', '2020-11-03 10:56:42');
INSERT INTO `resource` VALUES (22, '细化分解', '/cost/project/decomposition', 'POST', '细化', 0, '2020-11-03 10:57:14', '2020-11-03 10:57:14');
INSERT INTO `resource` VALUES (23, '新建确认', '/cost/project/confirm', 'POST', '新建确认', 0, '2020-11-03 10:57:48', '2020-11-03 10:57:48');
INSERT INTO `resource` VALUES (24, '月报审评', '/cost/project/verify', 'POST', '审评', 0, '2020-11-03 10:58:16', '2020-11-03 10:58:16');
INSERT INTO `resource` VALUES (25, '成本细化分级列表', '/cost/project/specifyList', 'POST', '细化列表', 0, '2020-11-03 10:58:56', '2020-11-03 10:58:56');
INSERT INTO `resource` VALUES (26, '月报新增', '/cost/project/addMonthlyReport', 'POST', '新增', 0, '2020-11-03 10:59:24', '2020-11-03 10:59:24');
INSERT INTO `resource` VALUES (27, '月报修改', '/cost/project/updateMonthlyReport', 'POST', '修改', 0, '2020-11-03 10:59:50', '2020-11-03 10:59:50');
INSERT INTO `resource` VALUES (28, '项目完成', '/cost/project/complete', 'POST', '完成', 0, '2020-11-03 11:00:13', '2020-11-03 11:00:13');
INSERT INTO `resource` VALUES (29, '新建项目-项目导出', '/cost/excel/project/new', 'POST', '新建导出', 0, '2020-11-03 11:03:22', '2020-11-03 11:10:15');
INSERT INTO `resource` VALUES (30, '进行中项目-项目导出', '/cost/excel/project/ing', 'POST', '进行导出', 0, '2020-11-03 11:08:42', '2020-11-03 11:10:30');
INSERT INTO `resource` VALUES (31, '进行中项目-细化分解导出', '/cost/excel/project/decomposition', 'POST', '进行细化分解导出', 0, '2020-11-03 11:12:36', '2020-11-03 11:12:36');
INSERT INTO `resource` VALUES (32, '历史项目-项目导出', '/cost/excel/project/history', 'POST', '历史项目导出', 0, '2020-11-03 11:13:05', '2020-11-03 11:13:05');
INSERT INTO `resource` VALUES (33, '历史项目-项目导出-能导出的项目', '/cost/excel/projectList', 'POST', '历史项目导出关联', 0, '2020-11-03 11:16:29', '2020-11-03 11:16:29');
INSERT INTO `resource` VALUES (34, '项目详情', '/cost/project/detail', 'POST', '详情', 0, '2020-11-04 11:24:40', '2020-11-04 11:24:40');
INSERT INTO `resource` VALUES (35, '事业部列表', '/quality/dept/list', 'POST', '查询', 0, '2020-11-05 13:14:36', '2020-11-05 13:14:36');
INSERT INTO `resource` VALUES (36, '新建事业部', '/quality/dept/addDept', 'POST', '新增', 0, '2020-11-05 13:15:08', '2020-11-05 13:15:08');
INSERT INTO `resource` VALUES (37, '事业部导出查询所有', '/quality/dept/findAll', 'GET', '事业部导出', 1, '2020-11-05 13:16:00', '2020-11-05 13:45:48');
INSERT INTO `resource` VALUES (38, '项目部导出查询所有', '/quality/project/findAll', 'GET', '项目部导出', 1, '2020-11-05 13:16:00', '2020-11-05 13:45:50');
INSERT INTO `resource` VALUES (39, '项目列表', '/quality/project/list', 'POST', '查询', 0, '2020-11-05 13:17:44', '2020-11-05 13:17:44');
INSERT INTO `resource` VALUES (40, '新建项目', '/quality/project/addProject', 'POST', '新增', 0, '2020-11-05 13:18:37', '2020-11-05 13:18:37');
INSERT INTO `resource` VALUES (41, '新建确认', '/quality/project/confirmNew', 'POST', '新建确认', 0, '2020-11-05 13:19:41', '2020-11-05 13:19:41');
INSERT INTO `resource` VALUES (42, '细化分解表', '/quality/project/specifyList', 'POST', '细化列表', 0, '2020-11-05 13:20:16', '2020-11-05 13:20:16');
INSERT INTO `resource` VALUES (43, '修改细化分解', '/quality/project/updateSpecify', 'POST', '修改', 0, '2020-11-05 13:21:13', '2020-11-05 13:21:13');
INSERT INTO `resource` VALUES (44, '细化上报', '/quality/project/progress', 'POST', '新增', 0, '2020-11-05 13:21:40', '2020-11-05 13:21:40');
INSERT INTO `resource` VALUES (45, '生产运营部审核', '/quality/project/dpCheck', 'POST', '审核', 0, '2020-11-05 13:22:35', '2020-11-05 13:22:35');
INSERT INTO `resource` VALUES (46, '事业部审核', '/quality/project/deptCheck', 'POST', '审核', 0, '2020-11-05 13:23:05', '2020-11-05 13:23:05');
INSERT INTO `resource` VALUES (47, '项目完成', '/quality/project/projectCmplete', 'POST', '项目完成', 0, '2020-11-05 13:31:35', '2020-11-05 13:31:35');
INSERT INTO `resource` VALUES (48, '实施状态修改', '/quality/project/updateProjectStatus', 'POST', '修改', 0, '2020-11-05 13:32:32', '2020-11-05 13:32:32');
INSERT INTO `resource` VALUES (49, '新项目删除', '/quality/project/delProject', 'POST', '删除', 0, '2020-11-05 13:33:20', '2020-11-05 13:33:20');
INSERT INTO `resource` VALUES (50, '细化分解获取月报标题列表', '/cost/project/getMonthlyReportTitleList', 'POST', '查询', 0, '2020-11-25 17:03:54', '2020-11-25 17:03:54');
INSERT INTO `resource` VALUES (51, '获取月报模板', '/cost/project/getReportModel', 'POST', '查询', 0, '2020-11-25 17:03:54', '2020-11-25 17:03:54');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  `role_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色编码',
  `parent_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '上级角色id 默认0时为一级',
  `introduction` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色简介',
  `delete_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标记 0->正常 1->删除',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '添加时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, 'root', 'auth_root', 0, '管理员角色', 0, '2020-10-21 11:00:17', '2020-10-21 11:01:55');
INSERT INTO `role` VALUES (2, '生产主管', 'auth_supervisor', 0, '生产运营部角色', 0, '2020-10-21 11:01:19', '2020-10-21 11:02:00');
INSERT INTO `role` VALUES (3, '事业部', 'auth_department', 0, '事业部角色', 0, '2020-10-21 11:01:50', '2020-10-21 11:01:50');
INSERT INTO `role` VALUES (4, '项目部', 'auth_project', 0, '项目部角色', 0, '2020-10-21 11:02:24', '2020-10-21 11:02:24');
INSERT INTO `role` VALUES (5, '记录员', 'auth_recorder', 0, '记录员角色', 0, '2020-10-21 11:02:47', '2020-10-21 11:02:47');
INSERT INTO `role` VALUES (6, '总监', 'auth_director', 0, '总监角色', 0, '2020-10-21 11:03:16', '2020-10-21 11:03:16');
INSERT INTO `role` VALUES (7, '项目管理开支审核员', 'auth_supervisor_expenses_auditor', 2, '审核员角色', 0, '2020-10-23 17:27:02', '2020-10-23 17:36:58');
INSERT INTO `role` VALUES (8, '项目管理开支记录员', 'auth_supervisor_expenses_recorder', 2, '记录员角色', 0, '2020-10-23 17:27:29', '2020-10-23 17:37:00');
INSERT INTO `role` VALUES (9, '日常开支审核员', 'auth_supervisor_daily_expenses_auditor', 2, '审核员角色', 0, '2020-10-23 17:28:13', '2020-10-23 17:38:39');
INSERT INTO `role` VALUES (10, '外协开支审核员', 'auth_supervisor_daily_expenses_auditor', 2, '审核员角色', 0, '2020-10-23 17:28:34', '2020-10-23 17:38:43');
INSERT INTO `role` VALUES (11, '人员工资审核员', 'auth_supervisor_wage_auditor', 2, '审核员角色', 0, '2020-10-23 17:28:53', '2020-10-23 17:39:13');
INSERT INTO `role` VALUES (12, '测绘事业一部', 'auth_department_map_one', 3, '事业部角色', 0, '2020-10-23 17:29:32', '2020-10-23 17:42:39');
INSERT INTO `role` VALUES (13, '测绘事业二部', 'auth_department_map_two', 3, '事业部角色', 0, '2020-10-23 17:29:49', '2020-10-23 17:44:47');
INSERT INTO `role` VALUES (14, '测绘事业三部', 'auth_department_map_three', 3, '事业部角色', 0, '2020-10-23 17:29:58', '2020-10-23 17:44:53');
INSERT INTO `role` VALUES (15, '管线探测事业部', 'auth_department_detect', 3, '事业部角色', 0, '2020-10-23 17:30:24', '2020-10-23 17:49:42');
INSERT INTO `role` VALUES (16, '数据处理中心', 'auth_department_data_deal_center', 3, '事业部角色', 0, '2020-10-23 17:30:44', '2020-10-23 17:50:22');
INSERT INTO `role` VALUES (17, '数据应用中心', 'auth_department_data_application_center', 3, '事业部角色', 0, '2020-10-23 17:31:24', '2020-10-23 17:50:42');
INSERT INTO `role` VALUES (18, '外协开支记录员', 'auth_recorder_outsourcing_expenses_recorder', 5, '记录员角色', 0, '2020-10-23 17:32:14', '2020-10-23 17:54:16');
INSERT INTO `role` VALUES (19, '日常开支记录员', 'auth_recorder_daily_expenses_recorder', 5, '记录员角色', 0, '2020-10-23 17:32:42', '2020-10-23 17:54:26');
INSERT INTO `role` VALUES (20, '设备支出审核员', 'auth_recorder_decive_expenses_auditor', 5, '审核员角色', 0, '2020-10-23 17:33:04', '2020-10-23 17:54:34');
INSERT INTO `role` VALUES (21, '设备支出记录员', 'auth_recorder_decive_expenses_recorder', 5, '记录员角色', 0, '2020-10-23 17:33:41', '2020-10-23 17:54:43');
INSERT INTO `role` VALUES (22, '出行费用审核员', 'auth_recorder_travel_cost_auditor', 5, '审核员角色', 0, '2020-10-23 17:34:17', '2020-10-23 17:55:34');
INSERT INTO `role` VALUES (23, '出行费用记录员', 'auth_recorder_travel_cost_recorder', 5, '记录员角色', 0, '2020-10-23 17:34:39', '2020-10-23 17:55:48');
INSERT INTO `role` VALUES (24, '人员工资记录员', 'auth_recorder_wage_recorder', 5, '记录员角色', 0, '2020-10-23 18:06:33', '2020-10-23 18:06:56');

-- ----------------------------
-- Table structure for role_function
-- ----------------------------
DROP TABLE IF EXISTS `role_function`;
CREATE TABLE `role_function`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关联id',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `function_id` bigint(20) NOT NULL COMMENT '功能id',
  `delete_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标记 0->正常 1->删除',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '添加时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 573 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_function
-- ----------------------------
INSERT INTO `role_function` VALUES (1, 1, 14, 0, '2020-11-03 15:35:47', '2020-11-03 15:35:52');
INSERT INTO `role_function` VALUES (2, 1, 15, 0, '2020-11-03 15:36:38', '2020-11-03 15:36:38');
INSERT INTO `role_function` VALUES (3, 1, 16, 0, '2020-11-03 15:36:42', '2020-11-03 15:36:42');
INSERT INTO `role_function` VALUES (4, 1, 17, 0, '2020-11-03 15:36:47', '2020-11-03 15:36:47');
INSERT INTO `role_function` VALUES (5, 1, 18, 0, '2020-11-03 15:36:51', '2020-11-03 15:36:51');
INSERT INTO `role_function` VALUES (6, 1, 19, 0, '2020-11-03 15:36:54', '2020-11-03 15:36:54');
INSERT INTO `role_function` VALUES (7, 1, 20, 0, '2020-11-03 15:36:56', '2020-11-03 15:36:56');
INSERT INTO `role_function` VALUES (8, 1, 21, 0, '2020-11-03 15:36:59', '2020-11-03 15:36:59');
INSERT INTO `role_function` VALUES (9, 1, 22, 0, '2020-11-03 15:37:02', '2020-11-03 15:37:02');
INSERT INTO `role_function` VALUES (10, 1, 23, 0, '2020-11-03 15:37:05', '2020-11-03 15:37:05');
INSERT INTO `role_function` VALUES (11, 1, 24, 0, '2020-11-03 15:37:08', '2020-11-03 15:37:08');
INSERT INTO `role_function` VALUES (12, 1, 25, 0, '2020-11-03 15:37:10', '2020-11-03 15:37:10');
INSERT INTO `role_function` VALUES (13, 1, 26, 0, '2020-11-03 15:37:13', '2020-11-03 15:37:13');
INSERT INTO `role_function` VALUES (14, 1, 27, 0, '2020-11-03 15:37:16', '2020-11-03 15:37:16');
INSERT INTO `role_function` VALUES (15, 1, 28, 0, '2020-11-03 15:37:19', '2020-11-03 15:37:19');
INSERT INTO `role_function` VALUES (16, 1, 29, 0, '2020-11-03 15:37:22', '2020-11-03 15:37:22');
INSERT INTO `role_function` VALUES (17, 1, 30, 0, '2020-11-03 15:37:25', '2020-11-03 15:37:25');
INSERT INTO `role_function` VALUES (18, 1, 31, 0, '2020-11-03 15:37:30', '2020-11-03 15:37:30');
INSERT INTO `role_function` VALUES (19, 1, 32, 0, '2020-11-03 15:37:35', '2020-11-03 15:37:35');
INSERT INTO `role_function` VALUES (20, 1, 33, 0, '2020-11-03 15:37:38', '2020-11-03 15:37:38');
INSERT INTO `role_function` VALUES (21, 1, 34, 0, '2020-11-03 15:37:41', '2020-11-03 15:37:41');
INSERT INTO `role_function` VALUES (22, 1, 35, 0, '2020-11-03 15:37:44', '2020-11-03 15:37:44');
INSERT INTO `role_function` VALUES (23, 1, 36, 0, '2020-11-03 15:37:47', '2020-11-03 15:37:47');
INSERT INTO `role_function` VALUES (24, 1, 37, 0, '2020-11-03 15:37:51', '2020-11-03 15:37:51');
INSERT INTO `role_function` VALUES (25, 1, 38, 0, '2020-11-03 15:37:54', '2020-11-03 15:37:54');
INSERT INTO `role_function` VALUES (26, 1, 39, 0, '2020-11-03 15:37:57', '2020-11-03 15:37:57');
INSERT INTO `role_function` VALUES (27, 1, 40, 0, '2020-11-03 15:38:01', '2020-11-03 15:38:01');
INSERT INTO `role_function` VALUES (28, 1, 41, 0, '2020-11-03 15:38:30', '2020-11-03 15:38:30');
INSERT INTO `role_function` VALUES (29, 1, 42, 0, '2020-11-05 14:05:03', '2020-11-05 14:30:27');
INSERT INTO `role_function` VALUES (30, 1, 43, 0, '2020-11-05 14:05:05', '2020-11-05 14:30:29');
INSERT INTO `role_function` VALUES (31, 1, 44, 0, '2020-11-05 14:05:10', '2020-11-05 14:30:32');
INSERT INTO `role_function` VALUES (32, 1, 45, 0, '2020-11-05 14:05:13', '2020-11-05 14:30:34');
INSERT INTO `role_function` VALUES (33, 1, 46, 0, '2020-11-05 14:05:15', '2020-11-05 14:30:36');
INSERT INTO `role_function` VALUES (34, 1, 47, 0, '2020-11-05 14:05:18', '2020-11-05 14:05:18');
INSERT INTO `role_function` VALUES (35, 1, 48, 0, '2020-11-05 14:05:20', '2020-11-05 14:05:20');
INSERT INTO `role_function` VALUES (36, 1, 49, 0, '2020-11-05 14:05:23', '2020-11-05 14:05:23');
INSERT INTO `role_function` VALUES (37, 1, 50, 0, '2020-11-05 14:05:28', '2020-11-05 14:05:28');
INSERT INTO `role_function` VALUES (38, 1, 51, 0, '2020-11-05 14:05:30', '2020-11-05 14:05:30');
INSERT INTO `role_function` VALUES (39, 1, 52, 0, '2020-11-05 14:05:35', '2020-11-05 14:05:35');
INSERT INTO `role_function` VALUES (40, 1, 53, 0, '2020-11-05 14:06:33', '2020-11-05 14:06:33');
INSERT INTO `role_function` VALUES (41, 1, 54, 0, '2020-11-05 14:06:35', '2020-11-05 14:06:35');
INSERT INTO `role_function` VALUES (42, 2, 1, 0, '2020-11-05 14:08:47', '2020-11-05 14:08:47');
INSERT INTO `role_function` VALUES (43, 2, 4, 0, '2020-11-05 14:08:47', '2020-11-05 14:08:47');
INSERT INTO `role_function` VALUES (44, 2, 14, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (45, 2, 15, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (46, 2, 16, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (47, 2, 17, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (48, 2, 5, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (49, 2, 18, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (50, 2, 19, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (51, 2, 20, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (52, 2, 21, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (53, 2, 6, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (54, 2, 22, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (55, 2, 23, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (56, 2, 24, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (57, 2, 25, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (58, 2, 7, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (59, 2, 26, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (60, 2, 27, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (61, 2, 28, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (62, 2, 29, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (63, 2, 30, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (64, 2, 32, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (65, 2, 35, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (66, 2, 36, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (67, 2, 37, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (68, 2, 9, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (69, 2, 38, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (70, 2, 10, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (71, 2, 39, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (72, 2, 40, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (73, 2, 41, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (74, 2, 11, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (75, 2, 42, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (76, 2, 43, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (77, 2, 44, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (78, 2, 45, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (79, 2, 46, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (80, 2, 49, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (81, 2, 51, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (82, 2, 52, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (83, 2, 13, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (84, 2, 53, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (85, 2, 54, 0, '2020-11-05 14:08:48', '2020-11-05 14:08:48');
INSERT INTO `role_function` VALUES (86, 4, 4, 0, '2020-11-05 14:13:22', '2020-11-05 14:13:22');
INSERT INTO `role_function` VALUES (87, 4, 14, 0, '2020-11-05 14:13:22', '2020-11-05 14:13:22');
INSERT INTO `role_function` VALUES (88, 4, 15, 0, '2020-11-05 14:13:22', '2020-11-05 14:13:22');
INSERT INTO `role_function` VALUES (89, 4, 16, 0, '2020-11-05 14:13:22', '2020-11-05 14:13:22');
INSERT INTO `role_function` VALUES (90, 4, 17, 0, '2020-11-05 14:13:22', '2020-11-05 14:13:22');
INSERT INTO `role_function` VALUES (91, 4, 26, 0, '2020-11-05 14:13:22', '2020-11-05 14:13:22');
INSERT INTO `role_function` VALUES (92, 4, 30, 0, '2020-11-05 14:13:22', '2020-11-05 14:13:22');
INSERT INTO `role_function` VALUES (93, 4, 32, 0, '2020-11-05 14:13:22', '2020-11-05 14:13:22');
INSERT INTO `role_function` VALUES (94, 4, 36, 0, '2020-11-05 14:13:22', '2020-11-05 14:13:22');
INSERT INTO `role_function` VALUES (95, 4, 37, 0, '2020-11-05 14:13:22', '2020-11-05 14:13:22');
INSERT INTO `role_function` VALUES (96, 4, 42, 0, '2020-11-05 14:13:22', '2020-11-05 14:13:22');
INSERT INTO `role_function` VALUES (97, 4, 47, 0, '2020-11-05 14:13:22', '2020-11-05 14:13:22');
INSERT INTO `role_function` VALUES (98, 4, 48, 0, '2020-11-05 14:13:22', '2020-11-05 14:13:22');
INSERT INTO `role_function` VALUES (99, 5, 4, 0, '2020-11-05 14:17:30', '2020-11-05 14:17:30');
INSERT INTO `role_function` VALUES (100, 5, 14, 0, '2020-11-05 14:17:30', '2020-11-05 14:17:30');
INSERT INTO `role_function` VALUES (101, 5, 15, 0, '2020-11-05 14:17:30', '2020-11-05 14:17:30');
INSERT INTO `role_function` VALUES (102, 5, 16, 0, '2020-11-05 14:17:30', '2020-11-05 14:17:30');
INSERT INTO `role_function` VALUES (103, 5, 17, 0, '2020-11-05 14:17:30', '2020-11-05 14:17:30');
INSERT INTO `role_function` VALUES (104, 5, 26, 0, '2020-11-05 14:17:30', '2020-11-05 14:17:30');
INSERT INTO `role_function` VALUES (105, 5, 30, 0, '2020-11-05 14:17:30', '2020-11-05 14:17:30');
INSERT INTO `role_function` VALUES (106, 5, 32, 0, '2020-11-05 14:17:30', '2020-11-05 14:17:30');
INSERT INTO `role_function` VALUES (107, 5, 36, 0, '2020-11-05 14:17:30', '2020-11-05 14:17:30');
INSERT INTO `role_function` VALUES (108, 5, 37, 0, '2020-11-05 14:17:30', '2020-11-05 14:17:30');
INSERT INTO `role_function` VALUES (109, 5, 42, 0, '2020-11-05 14:17:30', '2020-11-05 14:17:30');
INSERT INTO `role_function` VALUES (110, 5, 46, 0, '2020-11-05 14:17:30', '2020-11-05 14:17:30');
INSERT INTO `role_function` VALUES (111, 6, 4, 0, '2020-11-05 14:18:31', '2020-11-05 14:18:31');
INSERT INTO `role_function` VALUES (112, 6, 14, 0, '2020-11-05 14:18:31', '2020-11-05 14:18:31');
INSERT INTO `role_function` VALUES (113, 6, 15, 0, '2020-11-05 14:18:31', '2020-11-05 14:18:31');
INSERT INTO `role_function` VALUES (114, 6, 16, 0, '2020-11-05 14:18:31', '2020-11-05 14:18:31');
INSERT INTO `role_function` VALUES (115, 6, 17, 0, '2020-11-05 14:18:31', '2020-11-05 14:18:31');
INSERT INTO `role_function` VALUES (116, 6, 26, 0, '2020-11-05 14:18:31', '2020-11-05 14:18:31');
INSERT INTO `role_function` VALUES (117, 6, 30, 0, '2020-11-05 14:18:31', '2020-11-05 14:18:31');
INSERT INTO `role_function` VALUES (118, 6, 32, 0, '2020-11-05 14:18:31', '2020-11-05 14:18:31');
INSERT INTO `role_function` VALUES (119, 6, 36, 0, '2020-11-05 14:18:31', '2020-11-05 14:18:31');
INSERT INTO `role_function` VALUES (120, 6, 37, 0, '2020-11-05 14:18:31', '2020-11-05 14:18:31');
INSERT INTO `role_function` VALUES (121, 6, 9, 0, '2020-11-05 14:18:31', '2020-11-05 14:18:31');
INSERT INTO `role_function` VALUES (122, 6, 38, 0, '2020-11-05 14:18:31', '2020-11-05 14:18:31');
INSERT INTO `role_function` VALUES (123, 6, 42, 0, '2020-11-05 14:18:31', '2020-11-05 14:18:31');
INSERT INTO `role_function` VALUES (124, 6, 46, 0, '2020-11-05 14:18:31', '2020-11-05 14:18:31');
INSERT INTO `role_function` VALUES (125, 7, 1, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (126, 7, 4, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (127, 7, 14, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (128, 7, 15, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (129, 7, 16, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (130, 7, 17, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (131, 7, 5, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (132, 7, 18, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (133, 7, 19, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (134, 7, 20, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (135, 7, 21, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (136, 7, 6, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (137, 7, 22, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (138, 7, 23, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (139, 7, 24, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (140, 7, 25, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (141, 7, 7, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (142, 7, 26, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (143, 7, 27, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (144, 7, 28, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (145, 7, 29, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (146, 7, 30, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (147, 7, 31, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (148, 7, 32, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (149, 7, 35, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (150, 7, 36, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (151, 7, 37, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (152, 7, 9, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (153, 7, 38, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (154, 7, 10, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (155, 7, 39, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (156, 7, 40, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (157, 7, 41, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (158, 7, 11, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (159, 7, 42, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (160, 7, 43, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (161, 7, 44, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (162, 7, 45, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (163, 7, 46, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (164, 7, 49, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (165, 7, 51, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (166, 7, 52, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (167, 7, 13, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (168, 7, 53, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (169, 7, 54, 0, '2020-11-05 14:19:21', '2020-11-05 14:19:21');
INSERT INTO `role_function` VALUES (170, 8, 1, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (171, 8, 4, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (172, 8, 14, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (173, 8, 15, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (174, 8, 16, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (175, 8, 17, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (176, 8, 5, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (177, 8, 18, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (178, 8, 19, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (179, 8, 20, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (180, 8, 21, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (181, 8, 6, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (182, 8, 22, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (183, 8, 23, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (184, 8, 24, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (185, 8, 25, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (186, 8, 7, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (187, 8, 26, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (188, 8, 27, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (189, 8, 28, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (190, 8, 29, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (191, 8, 30, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (192, 8, 32, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (193, 8, 33, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (194, 8, 34, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (195, 8, 35, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (196, 8, 36, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (197, 8, 37, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (198, 8, 9, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (199, 8, 38, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (200, 8, 10, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (201, 8, 39, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (202, 8, 40, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (203, 8, 41, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (204, 8, 11, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (205, 8, 42, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (206, 8, 43, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (207, 8, 44, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (208, 8, 45, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (209, 8, 46, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (210, 8, 49, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (211, 8, 51, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (212, 8, 52, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (213, 8, 13, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (214, 8, 53, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (215, 8, 54, 0, '2020-11-05 14:19:56', '2020-11-05 14:19:56');
INSERT INTO `role_function` VALUES (216, 9, 1, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (217, 9, 4, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (218, 9, 14, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (219, 9, 15, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (220, 9, 16, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (221, 9, 17, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (222, 9, 5, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (223, 9, 18, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (224, 9, 19, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (225, 9, 20, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (226, 9, 21, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (227, 9, 6, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (228, 9, 22, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (229, 9, 23, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (230, 9, 24, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (231, 9, 25, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (232, 9, 7, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (233, 9, 26, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (234, 9, 27, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (235, 9, 28, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (236, 9, 29, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (237, 9, 30, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (238, 9, 31, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (239, 9, 32, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (240, 9, 35, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (241, 9, 36, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (242, 9, 37, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (243, 9, 9, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (244, 9, 38, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (245, 9, 10, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (246, 9, 39, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (247, 9, 40, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (248, 9, 41, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (249, 9, 11, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (250, 9, 42, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (251, 9, 43, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (252, 9, 44, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (253, 9, 45, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (254, 9, 46, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (255, 9, 49, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (256, 9, 51, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (257, 9, 52, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (258, 9, 13, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (259, 9, 53, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (260, 9, 54, 0, '2020-11-05 14:20:19', '2020-11-05 14:20:19');
INSERT INTO `role_function` VALUES (261, 10, 1, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (262, 10, 4, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (263, 10, 14, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (264, 10, 15, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (265, 10, 16, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (266, 10, 17, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (267, 10, 5, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (268, 10, 18, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (269, 10, 19, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (270, 10, 20, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (271, 10, 21, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (272, 10, 6, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (273, 10, 22, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (274, 10, 23, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (275, 10, 24, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (276, 10, 25, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (277, 10, 7, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (278, 10, 26, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (279, 10, 27, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (280, 10, 28, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (281, 10, 29, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (282, 10, 30, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (283, 10, 31, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (284, 10, 32, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (285, 10, 35, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (286, 10, 36, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (287, 10, 37, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (288, 10, 9, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (289, 10, 38, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (290, 10, 10, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (291, 10, 39, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (292, 10, 40, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (293, 10, 41, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (294, 10, 11, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (295, 10, 42, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (296, 10, 43, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (297, 10, 44, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (298, 10, 45, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (299, 10, 46, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (300, 10, 49, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (301, 10, 51, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (302, 10, 52, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (303, 10, 13, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (304, 10, 53, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (305, 10, 54, 0, '2020-11-05 14:20:32', '2020-11-05 14:20:32');
INSERT INTO `role_function` VALUES (306, 11, 1, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (307, 11, 4, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (308, 11, 14, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (309, 11, 15, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (310, 11, 16, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (311, 11, 17, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (312, 11, 5, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (313, 11, 18, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (314, 11, 19, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (315, 11, 20, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (316, 11, 21, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (317, 11, 6, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (318, 11, 22, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (319, 11, 23, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (320, 11, 24, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (321, 11, 25, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (322, 11, 7, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (323, 11, 26, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (324, 11, 27, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (325, 11, 28, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (326, 11, 29, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (327, 11, 30, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (328, 11, 31, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (329, 11, 32, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (330, 11, 35, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (331, 11, 36, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (332, 11, 37, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (333, 11, 9, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (334, 11, 38, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (335, 11, 10, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (336, 11, 39, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (337, 11, 40, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (338, 11, 41, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (339, 11, 11, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (340, 11, 42, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (341, 11, 43, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (342, 11, 44, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (343, 11, 45, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (344, 11, 46, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (345, 11, 49, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (346, 11, 51, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (347, 11, 52, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (348, 11, 13, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (349, 11, 53, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (350, 11, 54, 0, '2020-11-05 14:20:50', '2020-11-05 14:20:50');
INSERT INTO `role_function` VALUES (351, 3, 4, 0, '2020-11-05 14:22:17', '2020-11-05 14:22:17');
INSERT INTO `role_function` VALUES (352, 3, 14, 0, '2020-11-05 14:22:17', '2020-11-05 14:22:17');
INSERT INTO `role_function` VALUES (353, 3, 15, 0, '2020-11-05 14:22:17', '2020-11-05 14:22:17');
INSERT INTO `role_function` VALUES (354, 3, 16, 0, '2020-11-05 14:22:17', '2020-11-05 14:22:17');
INSERT INTO `role_function` VALUES (355, 3, 17, 0, '2020-11-05 14:22:17', '2020-11-05 14:22:17');
INSERT INTO `role_function` VALUES (356, 3, 26, 0, '2020-11-05 14:22:17', '2020-11-05 14:22:17');
INSERT INTO `role_function` VALUES (357, 3, 30, 0, '2020-11-05 14:22:17', '2020-11-05 14:22:17');
INSERT INTO `role_function` VALUES (358, 3, 32, 0, '2020-11-05 14:22:17', '2020-11-05 14:22:17');
INSERT INTO `role_function` VALUES (359, 3, 36, 0, '2020-11-05 14:22:17', '2020-11-05 14:22:17');
INSERT INTO `role_function` VALUES (360, 3, 37, 0, '2020-11-05 14:22:17', '2020-11-05 14:22:17');
INSERT INTO `role_function` VALUES (361, 3, 9, 0, '2020-11-05 14:22:17', '2020-11-05 14:22:17');
INSERT INTO `role_function` VALUES (362, 3, 38, 0, '2020-11-05 14:22:17', '2020-11-05 14:22:17');
INSERT INTO `role_function` VALUES (363, 3, 42, 0, '2020-11-05 14:22:17', '2020-11-05 14:22:17');
INSERT INTO `role_function` VALUES (364, 3, 46, 0, '2020-11-05 14:22:17', '2020-11-05 14:22:17');
INSERT INTO `role_function` VALUES (365, 3, 50, 0, '2020-11-05 14:22:17', '2020-11-05 14:22:17');
INSERT INTO `role_function` VALUES (366, 3, 13, 0, '2020-11-05 14:22:17', '2020-11-05 14:22:17');
INSERT INTO `role_function` VALUES (367, 3, 53, 0, '2020-11-05 14:22:17', '2020-11-05 14:22:17');
INSERT INTO `role_function` VALUES (368, 3, 54, 0, '2020-11-05 14:22:17', '2020-11-05 14:22:17');
INSERT INTO `role_function` VALUES (369, 12, 4, 0, '2020-11-05 14:22:50', '2020-11-05 14:22:50');
INSERT INTO `role_function` VALUES (370, 12, 14, 0, '2020-11-05 14:22:50', '2020-11-05 14:22:50');
INSERT INTO `role_function` VALUES (371, 12, 15, 0, '2020-11-05 14:22:50', '2020-11-05 14:22:50');
INSERT INTO `role_function` VALUES (372, 12, 16, 0, '2020-11-05 14:22:50', '2020-11-05 14:22:50');
INSERT INTO `role_function` VALUES (373, 12, 17, 0, '2020-11-05 14:22:50', '2020-11-05 14:22:50');
INSERT INTO `role_function` VALUES (374, 12, 26, 0, '2020-11-05 14:22:50', '2020-11-05 14:22:50');
INSERT INTO `role_function` VALUES (375, 12, 30, 0, '2020-11-05 14:22:50', '2020-11-05 14:22:50');
INSERT INTO `role_function` VALUES (376, 12, 32, 0, '2020-11-05 14:22:50', '2020-11-05 14:22:50');
INSERT INTO `role_function` VALUES (377, 12, 36, 0, '2020-11-05 14:22:50', '2020-11-05 14:22:50');
INSERT INTO `role_function` VALUES (378, 12, 37, 0, '2020-11-05 14:22:50', '2020-11-05 14:22:50');
INSERT INTO `role_function` VALUES (379, 12, 9, 0, '2020-11-05 14:22:50', '2020-11-05 14:22:50');
INSERT INTO `role_function` VALUES (380, 12, 38, 0, '2020-11-05 14:22:50', '2020-11-05 14:22:50');
INSERT INTO `role_function` VALUES (381, 12, 42, 0, '2020-11-05 14:22:50', '2020-11-05 14:22:50');
INSERT INTO `role_function` VALUES (382, 12, 46, 0, '2020-11-05 14:22:50', '2020-11-05 14:22:50');
INSERT INTO `role_function` VALUES (383, 12, 50, 0, '2020-11-05 14:22:50', '2020-11-05 14:22:50');
INSERT INTO `role_function` VALUES (384, 12, 13, 0, '2020-11-05 14:22:50', '2020-11-05 14:22:50');
INSERT INTO `role_function` VALUES (385, 12, 53, 0, '2020-11-05 14:22:50', '2020-11-05 14:22:50');
INSERT INTO `role_function` VALUES (386, 12, 54, 0, '2020-11-05 14:22:50', '2020-11-05 14:22:50');
INSERT INTO `role_function` VALUES (387, 13, 4, 0, '2020-11-05 14:23:11', '2020-11-05 14:23:11');
INSERT INTO `role_function` VALUES (388, 13, 14, 0, '2020-11-05 14:23:11', '2020-11-05 14:23:11');
INSERT INTO `role_function` VALUES (389, 13, 15, 0, '2020-11-05 14:23:11', '2020-11-05 14:23:11');
INSERT INTO `role_function` VALUES (390, 13, 16, 0, '2020-11-05 14:23:11', '2020-11-05 14:23:11');
INSERT INTO `role_function` VALUES (391, 13, 17, 0, '2020-11-05 14:23:11', '2020-11-05 14:23:11');
INSERT INTO `role_function` VALUES (392, 13, 26, 0, '2020-11-05 14:23:11', '2020-11-05 14:23:11');
INSERT INTO `role_function` VALUES (393, 13, 30, 0, '2020-11-05 14:23:11', '2020-11-05 14:23:11');
INSERT INTO `role_function` VALUES (394, 13, 32, 0, '2020-11-05 14:23:11', '2020-11-05 14:23:11');
INSERT INTO `role_function` VALUES (395, 13, 36, 0, '2020-11-05 14:23:11', '2020-11-05 14:23:11');
INSERT INTO `role_function` VALUES (396, 13, 37, 0, '2020-11-05 14:23:11', '2020-11-05 14:23:11');
INSERT INTO `role_function` VALUES (397, 13, 9, 0, '2020-11-05 14:23:11', '2020-11-05 14:23:11');
INSERT INTO `role_function` VALUES (398, 13, 38, 0, '2020-11-05 14:23:11', '2020-11-05 14:23:11');
INSERT INTO `role_function` VALUES (399, 13, 42, 0, '2020-11-05 14:23:11', '2020-11-05 14:23:11');
INSERT INTO `role_function` VALUES (400, 13, 46, 0, '2020-11-05 14:23:11', '2020-11-05 14:23:11');
INSERT INTO `role_function` VALUES (401, 13, 50, 0, '2020-11-05 14:23:11', '2020-11-05 14:23:11');
INSERT INTO `role_function` VALUES (402, 13, 13, 0, '2020-11-05 14:23:11', '2020-11-05 14:23:11');
INSERT INTO `role_function` VALUES (403, 13, 53, 0, '2020-11-05 14:23:11', '2020-11-05 14:23:11');
INSERT INTO `role_function` VALUES (404, 13, 54, 0, '2020-11-05 14:23:11', '2020-11-05 14:23:11');
INSERT INTO `role_function` VALUES (405, 14, 4, 0, '2020-11-05 14:23:25', '2020-11-05 14:23:25');
INSERT INTO `role_function` VALUES (406, 14, 14, 0, '2020-11-05 14:23:25', '2020-11-05 14:23:25');
INSERT INTO `role_function` VALUES (407, 14, 15, 0, '2020-11-05 14:23:25', '2020-11-05 14:23:25');
INSERT INTO `role_function` VALUES (408, 14, 16, 0, '2020-11-05 14:23:25', '2020-11-05 14:23:25');
INSERT INTO `role_function` VALUES (409, 14, 17, 0, '2020-11-05 14:23:25', '2020-11-05 14:23:25');
INSERT INTO `role_function` VALUES (410, 14, 26, 0, '2020-11-05 14:23:25', '2020-11-05 14:23:25');
INSERT INTO `role_function` VALUES (411, 14, 30, 0, '2020-11-05 14:23:25', '2020-11-05 14:23:25');
INSERT INTO `role_function` VALUES (412, 14, 32, 0, '2020-11-05 14:23:25', '2020-11-05 14:23:25');
INSERT INTO `role_function` VALUES (413, 14, 36, 0, '2020-11-05 14:23:25', '2020-11-05 14:23:25');
INSERT INTO `role_function` VALUES (414, 14, 37, 0, '2020-11-05 14:23:25', '2020-11-05 14:23:25');
INSERT INTO `role_function` VALUES (415, 14, 9, 0, '2020-11-05 14:23:25', '2020-11-05 14:23:25');
INSERT INTO `role_function` VALUES (416, 14, 38, 0, '2020-11-05 14:23:25', '2020-11-05 14:23:25');
INSERT INTO `role_function` VALUES (417, 14, 42, 0, '2020-11-05 14:23:25', '2020-11-05 14:23:25');
INSERT INTO `role_function` VALUES (418, 14, 46, 0, '2020-11-05 14:23:25', '2020-11-05 14:23:25');
INSERT INTO `role_function` VALUES (419, 14, 50, 0, '2020-11-05 14:23:25', '2020-11-05 14:23:25');
INSERT INTO `role_function` VALUES (420, 14, 13, 0, '2020-11-05 14:23:25', '2020-11-05 14:23:25');
INSERT INTO `role_function` VALUES (421, 14, 53, 0, '2020-11-05 14:23:25', '2020-11-05 14:23:25');
INSERT INTO `role_function` VALUES (422, 14, 54, 0, '2020-11-05 14:23:25', '2020-11-05 14:23:25');
INSERT INTO `role_function` VALUES (423, 15, 4, 0, '2020-11-05 14:23:52', '2020-11-05 14:23:52');
INSERT INTO `role_function` VALUES (424, 15, 14, 0, '2020-11-05 14:23:52', '2020-11-05 14:23:52');
INSERT INTO `role_function` VALUES (425, 15, 15, 0, '2020-11-05 14:23:52', '2020-11-05 14:23:52');
INSERT INTO `role_function` VALUES (426, 15, 16, 0, '2020-11-05 14:23:52', '2020-11-05 14:23:52');
INSERT INTO `role_function` VALUES (427, 15, 17, 0, '2020-11-05 14:23:52', '2020-11-05 14:23:52');
INSERT INTO `role_function` VALUES (428, 15, 26, 0, '2020-11-05 14:23:52', '2020-11-05 14:23:52');
INSERT INTO `role_function` VALUES (429, 15, 30, 0, '2020-11-05 14:23:52', '2020-11-05 14:23:52');
INSERT INTO `role_function` VALUES (430, 15, 32, 0, '2020-11-05 14:23:52', '2020-11-05 14:23:52');
INSERT INTO `role_function` VALUES (431, 15, 36, 0, '2020-11-05 14:23:52', '2020-11-05 14:23:52');
INSERT INTO `role_function` VALUES (432, 15, 37, 0, '2020-11-05 14:23:52', '2020-11-05 14:23:52');
INSERT INTO `role_function` VALUES (433, 15, 9, 0, '2020-11-05 14:23:52', '2020-11-05 14:23:52');
INSERT INTO `role_function` VALUES (434, 15, 38, 0, '2020-11-05 14:23:52', '2020-11-05 14:23:52');
INSERT INTO `role_function` VALUES (435, 15, 42, 0, '2020-11-05 14:23:52', '2020-11-05 14:23:52');
INSERT INTO `role_function` VALUES (436, 15, 46, 0, '2020-11-05 14:23:52', '2020-11-05 14:23:52');
INSERT INTO `role_function` VALUES (437, 15, 50, 0, '2020-11-05 14:23:52', '2020-11-05 14:23:52');
INSERT INTO `role_function` VALUES (438, 15, 13, 0, '2020-11-05 14:23:52', '2020-11-05 14:23:52');
INSERT INTO `role_function` VALUES (439, 15, 53, 0, '2020-11-05 14:23:52', '2020-11-05 14:23:52');
INSERT INTO `role_function` VALUES (440, 15, 54, 0, '2020-11-05 14:23:52', '2020-11-05 14:23:52');
INSERT INTO `role_function` VALUES (441, 16, 4, 0, '2020-11-05 14:24:09', '2020-11-05 14:24:09');
INSERT INTO `role_function` VALUES (442, 16, 14, 0, '2020-11-05 14:24:09', '2020-11-05 14:24:09');
INSERT INTO `role_function` VALUES (443, 16, 15, 0, '2020-11-05 14:24:09', '2020-11-05 14:24:09');
INSERT INTO `role_function` VALUES (444, 16, 16, 0, '2020-11-05 14:24:09', '2020-11-05 14:24:09');
INSERT INTO `role_function` VALUES (445, 16, 17, 0, '2020-11-05 14:24:09', '2020-11-05 14:24:09');
INSERT INTO `role_function` VALUES (446, 16, 26, 0, '2020-11-05 14:24:09', '2020-11-05 14:24:09');
INSERT INTO `role_function` VALUES (447, 16, 30, 0, '2020-11-05 14:24:09', '2020-11-05 14:24:09');
INSERT INTO `role_function` VALUES (448, 16, 32, 0, '2020-11-05 14:24:09', '2020-11-05 14:24:09');
INSERT INTO `role_function` VALUES (449, 16, 36, 0, '2020-11-05 14:24:09', '2020-11-05 14:24:09');
INSERT INTO `role_function` VALUES (450, 16, 37, 0, '2020-11-05 14:24:09', '2020-11-05 14:24:09');
INSERT INTO `role_function` VALUES (451, 16, 9, 0, '2020-11-05 14:24:09', '2020-11-05 14:24:09');
INSERT INTO `role_function` VALUES (452, 16, 38, 0, '2020-11-05 14:24:09', '2020-11-05 14:24:09');
INSERT INTO `role_function` VALUES (453, 16, 42, 0, '2020-11-05 14:24:09', '2020-11-05 14:24:09');
INSERT INTO `role_function` VALUES (454, 16, 46, 0, '2020-11-05 14:24:09', '2020-11-05 14:24:09');
INSERT INTO `role_function` VALUES (455, 16, 50, 0, '2020-11-05 14:24:09', '2020-11-05 14:24:09');
INSERT INTO `role_function` VALUES (456, 16, 13, 0, '2020-11-05 14:24:09', '2020-11-05 14:24:09');
INSERT INTO `role_function` VALUES (457, 16, 53, 0, '2020-11-05 14:24:09', '2020-11-05 14:24:09');
INSERT INTO `role_function` VALUES (458, 16, 54, 0, '2020-11-05 14:24:09', '2020-11-05 14:24:09');
INSERT INTO `role_function` VALUES (459, 17, 4, 0, '2020-11-05 14:24:26', '2020-11-05 14:24:26');
INSERT INTO `role_function` VALUES (460, 17, 14, 0, '2020-11-05 14:24:26', '2020-11-05 14:24:26');
INSERT INTO `role_function` VALUES (461, 17, 15, 0, '2020-11-05 14:24:26', '2020-11-05 14:24:26');
INSERT INTO `role_function` VALUES (462, 17, 16, 0, '2020-11-05 14:24:26', '2020-11-05 14:24:26');
INSERT INTO `role_function` VALUES (463, 17, 17, 0, '2020-11-05 14:24:26', '2020-11-05 14:24:26');
INSERT INTO `role_function` VALUES (464, 17, 26, 0, '2020-11-05 14:24:26', '2020-11-05 14:24:26');
INSERT INTO `role_function` VALUES (465, 17, 30, 0, '2020-11-05 14:24:26', '2020-11-05 14:24:26');
INSERT INTO `role_function` VALUES (466, 17, 32, 0, '2020-11-05 14:24:26', '2020-11-05 14:24:26');
INSERT INTO `role_function` VALUES (467, 17, 36, 0, '2020-11-05 14:24:26', '2020-11-05 14:24:26');
INSERT INTO `role_function` VALUES (468, 17, 37, 0, '2020-11-05 14:24:26', '2020-11-05 14:24:26');
INSERT INTO `role_function` VALUES (469, 17, 9, 0, '2020-11-05 14:24:26', '2020-11-05 14:24:26');
INSERT INTO `role_function` VALUES (470, 17, 38, 0, '2020-11-05 14:24:26', '2020-11-05 14:24:26');
INSERT INTO `role_function` VALUES (471, 17, 42, 0, '2020-11-05 14:24:26', '2020-11-05 14:24:26');
INSERT INTO `role_function` VALUES (472, 17, 46, 0, '2020-11-05 14:24:26', '2020-11-05 14:24:26');
INSERT INTO `role_function` VALUES (473, 17, 50, 0, '2020-11-05 14:24:26', '2020-11-05 14:24:26');
INSERT INTO `role_function` VALUES (474, 17, 13, 0, '2020-11-05 14:24:26', '2020-11-05 14:24:26');
INSERT INTO `role_function` VALUES (475, 17, 53, 0, '2020-11-05 14:24:26', '2020-11-05 14:24:26');
INSERT INTO `role_function` VALUES (476, 17, 54, 0, '2020-11-05 14:24:26', '2020-11-05 14:24:26');
INSERT INTO `role_function` VALUES (477, 18, 4, 0, '2020-11-05 14:25:04', '2020-11-05 14:25:04');
INSERT INTO `role_function` VALUES (478, 18, 14, 0, '2020-11-05 14:25:04', '2020-11-05 14:25:04');
INSERT INTO `role_function` VALUES (479, 18, 15, 0, '2020-11-05 14:25:04', '2020-11-05 14:25:04');
INSERT INTO `role_function` VALUES (480, 18, 16, 0, '2020-11-05 14:25:04', '2020-11-05 14:25:04');
INSERT INTO `role_function` VALUES (481, 18, 17, 0, '2020-11-05 14:25:04', '2020-11-05 14:25:04');
INSERT INTO `role_function` VALUES (482, 18, 26, 0, '2020-11-05 14:25:04', '2020-11-05 14:25:04');
INSERT INTO `role_function` VALUES (483, 18, 30, 0, '2020-11-05 14:25:04', '2020-11-05 14:25:04');
INSERT INTO `role_function` VALUES (484, 18, 32, 0, '2020-11-05 14:25:04', '2020-11-05 14:25:04');
INSERT INTO `role_function` VALUES (485, 18, 33, 0, '2020-11-05 14:25:04', '2020-11-05 14:25:04');
INSERT INTO `role_function` VALUES (486, 18, 34, 0, '2020-11-05 14:25:04', '2020-11-05 14:25:04');
INSERT INTO `role_function` VALUES (487, 18, 36, 0, '2020-11-05 14:25:04', '2020-11-05 14:25:04');
INSERT INTO `role_function` VALUES (488, 18, 37, 0, '2020-11-05 14:25:04', '2020-11-05 14:25:04');
INSERT INTO `role_function` VALUES (489, 18, 42, 0, '2020-11-05 14:25:04', '2020-11-05 14:25:04');
INSERT INTO `role_function` VALUES (490, 18, 46, 0, '2020-11-05 14:25:04', '2020-11-05 14:25:04');
INSERT INTO `role_function` VALUES (491, 19, 4, 0, '2020-11-05 14:25:21', '2020-11-05 14:25:21');
INSERT INTO `role_function` VALUES (492, 19, 14, 0, '2020-11-05 14:25:21', '2020-11-05 14:25:21');
INSERT INTO `role_function` VALUES (493, 19, 15, 0, '2020-11-05 14:25:21', '2020-11-05 14:25:21');
INSERT INTO `role_function` VALUES (494, 19, 16, 0, '2020-11-05 14:25:21', '2020-11-05 14:25:21');
INSERT INTO `role_function` VALUES (495, 19, 17, 0, '2020-11-05 14:25:21', '2020-11-05 14:25:21');
INSERT INTO `role_function` VALUES (496, 19, 26, 0, '2020-11-05 14:25:21', '2020-11-05 14:25:21');
INSERT INTO `role_function` VALUES (497, 19, 30, 0, '2020-11-05 14:25:21', '2020-11-05 14:25:21');
INSERT INTO `role_function` VALUES (498, 19, 32, 0, '2020-11-05 14:25:21', '2020-11-05 14:25:21');
INSERT INTO `role_function` VALUES (499, 19, 33, 0, '2020-11-05 14:25:21', '2020-11-05 14:25:21');
INSERT INTO `role_function` VALUES (500, 19, 34, 0, '2020-11-05 14:25:21', '2020-11-05 14:25:21');
INSERT INTO `role_function` VALUES (501, 19, 36, 0, '2020-11-05 14:25:21', '2020-11-05 14:25:21');
INSERT INTO `role_function` VALUES (502, 19, 37, 0, '2020-11-05 14:25:21', '2020-11-05 14:25:21');
INSERT INTO `role_function` VALUES (503, 19, 42, 0, '2020-11-05 14:25:21', '2020-11-05 14:25:21');
INSERT INTO `role_function` VALUES (504, 19, 46, 0, '2020-11-05 14:25:21', '2020-11-05 14:25:21');
INSERT INTO `role_function` VALUES (505, 20, 4, 0, '2020-11-05 14:25:32', '2020-11-05 14:25:32');
INSERT INTO `role_function` VALUES (506, 20, 14, 0, '2020-11-05 14:25:32', '2020-11-05 14:25:32');
INSERT INTO `role_function` VALUES (507, 20, 15, 0, '2020-11-05 14:25:32', '2020-11-05 14:25:32');
INSERT INTO `role_function` VALUES (508, 20, 16, 0, '2020-11-05 14:25:32', '2020-11-05 14:25:32');
INSERT INTO `role_function` VALUES (509, 20, 17, 0, '2020-11-05 14:25:32', '2020-11-05 14:25:32');
INSERT INTO `role_function` VALUES (510, 20, 26, 0, '2020-11-05 14:25:32', '2020-11-05 14:25:32');
INSERT INTO `role_function` VALUES (511, 20, 30, 0, '2020-11-05 14:25:32', '2020-11-05 14:25:32');
INSERT INTO `role_function` VALUES (512, 20, 31, 0, '2020-11-05 14:25:32', '2020-11-05 14:25:32');
INSERT INTO `role_function` VALUES (513, 20, 32, 0, '2020-11-05 14:25:32', '2020-11-05 14:25:32');
INSERT INTO `role_function` VALUES (514, 20, 36, 0, '2020-11-05 14:25:32', '2020-11-05 14:25:32');
INSERT INTO `role_function` VALUES (515, 20, 37, 0, '2020-11-05 14:25:32', '2020-11-05 14:25:32');
INSERT INTO `role_function` VALUES (516, 20, 42, 0, '2020-11-05 14:25:32', '2020-11-05 14:25:32');
INSERT INTO `role_function` VALUES (517, 20, 46, 0, '2020-11-05 14:25:32', '2020-11-05 14:25:32');
INSERT INTO `role_function` VALUES (518, 21, 4, 0, '2020-11-05 14:25:58', '2020-11-05 14:25:58');
INSERT INTO `role_function` VALUES (519, 21, 14, 0, '2020-11-05 14:25:58', '2020-11-05 14:25:58');
INSERT INTO `role_function` VALUES (520, 21, 15, 0, '2020-11-05 14:25:58', '2020-11-05 14:25:58');
INSERT INTO `role_function` VALUES (521, 21, 16, 0, '2020-11-05 14:25:58', '2020-11-05 14:25:58');
INSERT INTO `role_function` VALUES (522, 21, 17, 0, '2020-11-05 14:25:58', '2020-11-05 14:25:58');
INSERT INTO `role_function` VALUES (523, 21, 26, 0, '2020-11-05 14:25:58', '2020-11-05 14:25:58');
INSERT INTO `role_function` VALUES (524, 21, 30, 0, '2020-11-05 14:25:58', '2020-11-05 14:25:58');
INSERT INTO `role_function` VALUES (525, 21, 32, 0, '2020-11-05 14:25:58', '2020-11-05 14:25:58');
INSERT INTO `role_function` VALUES (526, 21, 33, 0, '2020-11-05 14:25:58', '2020-11-05 14:25:58');
INSERT INTO `role_function` VALUES (527, 21, 34, 0, '2020-11-05 14:25:58', '2020-11-05 14:25:58');
INSERT INTO `role_function` VALUES (528, 21, 36, 0, '2020-11-05 14:25:58', '2020-11-05 14:25:58');
INSERT INTO `role_function` VALUES (529, 21, 37, 0, '2020-11-05 14:25:58', '2020-11-05 14:25:58');
INSERT INTO `role_function` VALUES (530, 21, 42, 0, '2020-11-05 14:25:58', '2020-11-05 14:25:58');
INSERT INTO `role_function` VALUES (531, 21, 46, 0, '2020-11-05 14:25:58', '2020-11-05 14:25:58');
INSERT INTO `role_function` VALUES (532, 22, 4, 0, '2020-11-05 14:26:09', '2020-11-05 14:26:09');
INSERT INTO `role_function` VALUES (533, 22, 14, 0, '2020-11-05 14:26:09', '2020-11-05 14:26:09');
INSERT INTO `role_function` VALUES (534, 22, 15, 0, '2020-11-05 14:26:09', '2020-11-05 14:26:09');
INSERT INTO `role_function` VALUES (535, 22, 16, 0, '2020-11-05 14:26:09', '2020-11-05 14:26:09');
INSERT INTO `role_function` VALUES (536, 22, 17, 0, '2020-11-05 14:26:09', '2020-11-05 14:26:09');
INSERT INTO `role_function` VALUES (537, 22, 26, 0, '2020-11-05 14:26:09', '2020-11-05 14:26:09');
INSERT INTO `role_function` VALUES (538, 22, 30, 0, '2020-11-05 14:26:09', '2020-11-05 14:26:09');
INSERT INTO `role_function` VALUES (539, 22, 31, 0, '2020-11-05 14:26:09', '2020-11-05 14:26:09');
INSERT INTO `role_function` VALUES (540, 22, 32, 0, '2020-11-05 14:26:09', '2020-11-05 14:26:09');
INSERT INTO `role_function` VALUES (541, 22, 36, 0, '2020-11-05 14:26:09', '2020-11-05 14:26:09');
INSERT INTO `role_function` VALUES (542, 22, 37, 0, '2020-11-05 14:26:09', '2020-11-05 14:26:09');
INSERT INTO `role_function` VALUES (543, 22, 42, 0, '2020-11-05 14:26:09', '2020-11-05 14:26:09');
INSERT INTO `role_function` VALUES (544, 22, 46, 0, '2020-11-05 14:26:09', '2020-11-05 14:26:09');
INSERT INTO `role_function` VALUES (545, 23, 4, 0, '2020-11-05 14:26:36', '2020-11-05 14:26:36');
INSERT INTO `role_function` VALUES (546, 23, 14, 0, '2020-11-05 14:26:36', '2020-11-05 14:26:36');
INSERT INTO `role_function` VALUES (547, 23, 15, 0, '2020-11-05 14:26:36', '2020-11-05 14:26:36');
INSERT INTO `role_function` VALUES (548, 23, 16, 0, '2020-11-05 14:26:36', '2020-11-05 14:26:36');
INSERT INTO `role_function` VALUES (549, 23, 17, 0, '2020-11-05 14:26:36', '2020-11-05 14:26:36');
INSERT INTO `role_function` VALUES (550, 23, 26, 0, '2020-11-05 14:26:36', '2020-11-05 14:26:36');
INSERT INTO `role_function` VALUES (551, 23, 30, 0, '2020-11-05 14:26:36', '2020-11-05 14:26:36');
INSERT INTO `role_function` VALUES (552, 23, 32, 0, '2020-11-05 14:26:36', '2020-11-05 14:26:36');
INSERT INTO `role_function` VALUES (553, 23, 33, 0, '2020-11-05 14:26:36', '2020-11-05 14:26:36');
INSERT INTO `role_function` VALUES (554, 23, 34, 0, '2020-11-05 14:26:36', '2020-11-05 14:26:36');
INSERT INTO `role_function` VALUES (555, 23, 36, 0, '2020-11-05 14:26:36', '2020-11-05 14:26:36');
INSERT INTO `role_function` VALUES (556, 23, 37, 0, '2020-11-05 14:26:36', '2020-11-05 14:26:36');
INSERT INTO `role_function` VALUES (557, 23, 42, 0, '2020-11-05 14:26:36', '2020-11-05 14:26:36');
INSERT INTO `role_function` VALUES (558, 23, 46, 0, '2020-11-05 14:26:36', '2020-11-05 14:26:36');
INSERT INTO `role_function` VALUES (559, 24, 4, 0, '2020-11-05 14:26:48', '2020-11-05 14:26:48');
INSERT INTO `role_function` VALUES (560, 24, 14, 0, '2020-11-05 14:26:48', '2020-11-05 14:26:48');
INSERT INTO `role_function` VALUES (561, 24, 15, 0, '2020-11-05 14:26:48', '2020-11-05 14:26:48');
INSERT INTO `role_function` VALUES (562, 24, 16, 0, '2020-11-05 14:26:48', '2020-11-05 14:26:48');
INSERT INTO `role_function` VALUES (563, 24, 17, 0, '2020-11-05 14:26:48', '2020-11-05 14:26:48');
INSERT INTO `role_function` VALUES (564, 24, 26, 0, '2020-11-05 14:26:48', '2020-11-05 14:26:48');
INSERT INTO `role_function` VALUES (565, 24, 30, 0, '2020-11-05 14:26:48', '2020-11-05 14:26:48');
INSERT INTO `role_function` VALUES (566, 24, 32, 0, '2020-11-05 14:26:48', '2020-11-05 14:26:48');
INSERT INTO `role_function` VALUES (567, 24, 33, 0, '2020-11-05 14:26:48', '2020-11-05 14:26:48');
INSERT INTO `role_function` VALUES (568, 24, 34, 0, '2020-11-05 14:26:48', '2020-11-05 14:26:48');
INSERT INTO `role_function` VALUES (569, 24, 36, 0, '2020-11-05 14:26:48', '2020-11-05 14:26:48');
INSERT INTO `role_function` VALUES (570, 24, 37, 0, '2020-11-05 14:26:48', '2020-11-05 14:26:48');
INSERT INTO `role_function` VALUES (571, 24, 42, 0, '2020-11-05 14:26:48', '2020-11-05 14:26:48');
INSERT INTO `role_function` VALUES (572, 24, 46, 0, '2020-11-05 14:26:48', '2020-11-05 14:26:48');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关联id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `delete_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标记 0->正常 1->删除',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '添加时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
