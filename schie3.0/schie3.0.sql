/*
 Navicat Premium Data Transfer

 Source Server         : 本机mysql
 Source Server Type    : MySQL
 Source Server Version : 50525
 Source Host           : localhost:3306
 Source Schema         : schie

 Target Server Type    : MySQL
 Target Server Version : 50525
 File Encoding         : 65001

 Date: 25/11/2020 15:41:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for schie_data
-- ----------------------------
DROP TABLE IF EXISTS `schie_data`;
CREATE TABLE `schie_data`  (
  `task_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务ID，不能为空',
  `task_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务别名，不为空',
  `src_jdbcurl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据源数据库url，不能为空',
  `src_driverClass` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据源数据库驱动，不能为空',
  `src_usr` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据源数据库用户名，不能为空',
  `src_pw` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据源数据库密码，不能为空',
  `des_jdbcurl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '目标数据库url,不能为空',
  `des_driverClass` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '目标数据库驱动，不能为空',
  `des_usr` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '目标数据库用户，不能为空',
  `des_pw` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '目标数据库密码，不能为空',
  `creat_table` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '目标数据库是否建表，',
  `task_cron` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务cron表达式，不能为空',
  `tables_map` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '一个json体，包含表名，增量字段；',
  `update_time` datetime NOT NULL COMMENT '上一次执行更新时间，不能为空',
  `create_time` datetime NOT NULL COMMENT '任务创建时间，不能为空',
  PRIMARY KEY (`task_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据交换通道配置表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of schie_data
-- ----------------------------
INSERT INTO `schie_data` VALUES ('1667a816202011184190', '任务1', 'jdbc:oracle:thin:@localhost:1521:ORCL', 'oracle.jdbc.driver.OracleDriver', 'leosrc', 'leosrc', 'jdbc:mysql://localhost:3306/schie?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai', 'com.mysql.cj.jdbc.Driver', 'root', 'root', 'yes', NULL, '[{\"table_name\":\"srctest\",\"table_zlfild\":\"crtime\"}]', '2020-11-18 22:10:05', '2020-11-18 22:08:58');
INSERT INTO `schie_data` VALUES ('28948a67202011184cd0', '任务2', 'jdbc:postgresql://localhost:5432/leodest', 'org.postgresql.Driver', 'postgres', 'postgres', 'jdbc:mysql://localhost:3306/schie?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai', 'com.mysql.cj.jdbc.Driver', 'root', 'root', 'yes', NULL, '[{\"table_name\":\"srctest\",\"table_zlfild\":\"crtime\"}]', '2020-11-18 22:23:56', '2020-11-18 22:22:41');
INSERT INTO `schie_data` VALUES ('e152bf58202011184f14', '任务3', 'jdbc:postgresql://localhost:5432/leodest', 'org.postgresql.Driver', 'postgres', 'postgres', 'jdbc:oracle:thin:@localhost:1521:ORCL', 'oracle.jdbc.driver.OracleDriver', 'leosrc', 'leosrc', 'yes', NULL, '[{\"table_name\":\"srctest\",\"table_zlfild\":\"crtime\"}]', '2020-11-18 22:20:09', '2020-11-18 22:13:49');

SET FOREIGN_KEY_CHECKS = 1;
