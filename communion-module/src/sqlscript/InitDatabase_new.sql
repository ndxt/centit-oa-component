/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.134.7_3306
 Source Server Type    : MySQL
 Source Server Version : 50727
 Source Host           : 192.168.134.7:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 50727
 File Encoding         : 65001

 Date: 21/04/2020 16:23:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for f_inner_msg
-- ----------------------------
DROP TABLE IF EXISTS `f_inner_msg`;
CREATE TABLE `f_inner_msg`  (
  `MSG_CODE` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '消息编号',
  `REPLY_MSG_CODE` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '被回复的消息编号；如果是回复邮件，可以关联相关的邮件',
  `SENDER` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '发送人',
  `SEND_DATE` timestamp(0) NULL DEFAULT NULL COMMENT '发送时间',
  `MSG_TITLE` varchar(512) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '标题',
  `MSG_TYPE` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '消息类别：P = 私信人为消息   A=机构为公告  M =系统消息',
  `MAIL_TYPE` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '邮件类型这个字段是针对发信人来说的。消息类别：I=收件箱 O=发件箱 D=草稿箱 T=废件箱',
  `RECEIVE_NAME` varchar(2048) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '接收人中文名',
  `CARBON_COPY_NAME` varchar(2048) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '抄送人中文名',
  `MSG_STATE` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '消息状态：未读/已读/删除',
  `MSG_CONTENT` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '消息正文',
  `OPT_ID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '功能模块',
  `OPT_METHOD` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '操作方法',
  `OPT_TAG` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '操作业务标记'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.134.7_3306
 Source Server Type    : MySQL
 Source Server Version : 50727
 Source Host           : 192.168.134.7:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 50727
 File Encoding         : 65001

 Date: 21/04/2020 16:24:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for f_inner_msg_recipient
-- ----------------------------
DROP TABLE IF EXISTS `f_inner_msg_recipient`;
CREATE TABLE `f_inner_msg_recipient`  (
  `Receive` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '接收人编号',
  `Msg_Code` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '消息编码',
  `RECEIVE_Date` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '接收时间',
  `Reply_Msg_Code` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '回复消息编号',
  `Mail_Type` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '邮件类型,T=收件人,C=抄送,B=密送',
  `msg_State` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '消息的状态U=未读R=已读D=删除',
  PRIMARY KEY (`Msg_Code`, `Receive`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.134.7_3306
 Source Server Type    : MySQL
 Source Server Version : 50727
 Source Host           : 192.168.134.7:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 50727
 File Encoding         : 65001

 Date: 21/04/2020 16:23:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for f_inner_msg_annex
-- ----------------------------
DROP TABLE IF EXISTS `f_inner_msg_annex`;
CREATE TABLE `f_inner_msg_annex`  (
  `Annex_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '附件主键',
  `Msg_Code` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '消息代码',
  `ANNEX_file_name` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '文件名',
  `Annex_FILE_Id` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '文件代码',
  `upload_date` date NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`Annex_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.134.7_3306
 Source Server Type    : MySQL
 Source Server Version : 50727
 Source Host           : 192.168.134.7:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 50727
 File Encoding         : 65001

 Date: 21/04/2020 16:24:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for m_bbs_piece
-- ----------------------------
DROP TABLE IF EXISTS `m_bbs_piece`;
CREATE TABLE `m_bbs_piece`  (
  `PIECE_ID` char(22) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '信息id',
  `DELIVERER` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '发送消息的人',
  `DELIVER_DATE` datetime(0) NULL DEFAULT NULL COMMENT '消息发送的时间',
  `PIECE_CONTENT` varchar(2000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '消息的内容',
  `REPLY_ID` char(22) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '回复者回复的消息对应的id',
  `APPLICATION_ID` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '项目ID',
  `OPT_ID` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '功能模块',
  `OPT_METHOD` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '操作方法',
  `OPT_TAG` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '操作业务标记',
  `REPLY_NAME` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`PIECE_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

