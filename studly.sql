/*
 Navicat Premium Data Transfer

 Source Server         : 231自己服务器
 Source Server Type    : MySQL
 Source Server Version : 80017
 Source Host           : 47.92.101.231:3306
 Source Schema         : studly

 Target Server Type    : MySQL
 Target Server Version : 80017
 File Encoding         : 65001

 Date: 15/11/2020 22:19:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自定id,主要供前端展示权限列表分类排序使用.',
  `menu_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '归属菜单,前端判断并展示菜单使用,',
  `menu_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '菜单的中文释义',
  `permission_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '权限的代码/通配符,对应代码中@RequiresPermissions 的value',
  `permission_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '本权限的中文释义',
  `required_permission` int(1) NULL DEFAULT NULL COMMENT '菜单等级：0-顶级，1-列表，2-按钮',
  `sort` int(4) NULL DEFAULT NULL COMMENT '拍寻',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '上级菜单得id',
  `status` int(255) NULL DEFAULT 0 COMMENT '0-正常，1-关闭',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '后台权限表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `create_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '后台角色表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', '2017-11-22 16:24:34', '2020-11-08 17:43:31', 1);

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色id',
  `permission_id` int(11) NULL DEFAULT NULL COMMENT '权限id',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24890 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色-权限关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `mobile` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话号码-登录账号',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称-管理员',
  `role_id` int(11) NULL DEFAULT 1 COMMENT '角色ID',
  `create_id` int(11) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `delete_status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '是否有效  1有效  2无效',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `mobile`(`mobile`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41461 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '运营后台用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', 'admin', '123456', '超级用户', 1, 1, '2017-10-30 11:52:38', '2020-11-08 17:43:16', '0');

-- ----------------------------
-- Table structure for tb_article
-- ----------------------------
DROP TABLE IF EXISTS `tb_article`;
CREATE TABLE `tb_article`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `type` tinyint(4) NULL DEFAULT NULL,
  `course_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `participant_numbers` int(11) NULL DEFAULT NULL,
  `unlock_class_hours` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `unlock_rules` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `category_id` int(11) NULL DEFAULT NULL,
  `money` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `describes` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL,
  `images` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL,
  `videos` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL,
  `status` tinyint(4) NULL DEFAULT NULL,
  `contact_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `gmt_create` datetime(0) NULL DEFAULT NULL,
  `gmt_modify` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_article
-- ----------------------------
INSERT INTO `tb_article` VALUES (1, 'http://static.yqcode0563.cn/20201031-213501025-703.jpg', '学习学习2222', 1, '学习学习', 1, '1', '1', 5, '1', '1', '    一、努力学习不一定会有出息，但不学习一定不会有出息。——马云\r\n        二、水再浑浊，只要长久沉淀，依然会分外清澄；人再愚钝，只要足够努力，一样能改写命运。\r\n        三、人生，就是一场自己与自己的较量：让积极打败消极，让快乐打败忧郁，让勤奋打败懒惰，让坚强打败脆弱。在每一个充满希望的时候，告诉自己：努力，就总能遇见更好的自己。\r\n        ', 'http://static.yqcode0563.cn/20201031-213501025-703.jpg,http://static.yqcode0563.cn/20201031-213501025-703.jpg,http://static.yqcode0563.cn/20201031-213501025-703.jpg,', 'http://static.yqcode0563.cn/a2f8087fbc27476aa1daf700cf97ee72.mp4', 1, '1', '2020-11-13 00:58:45', '2020-11-13 00:58:47');
INSERT INTO `tb_article` VALUES (10, 'http://static.yqcode0563.cn/20201031-213501025-703.jpg', '学习学习', 1, '学习学习', 1, '1', '1', 1, '1', '1', '1', 'http://static.yqcode0563.cn/20201031-213501025-703.jpg', 'http://static.yqcode0563.cn/a2f8087fbc27476aa1daf700cf97ee72.mp4', 1, '1', '2020-11-13 00:58:45', '2020-11-13 00:58:47');
INSERT INTO `tb_article` VALUES (11, 'http://static.yqcode0563.cn/20201031-213501025-703.jpg', '学习学习', 1, '学习学习', 1, '1', '1', 1, '1', '1', '1', 'http://static.yqcode0563.cn/20201031-213501025-703.jpg', 'http://static.yqcode0563.cn/a2f8087fbc27476aa1daf700cf97ee72.mp4', 1, '1', '2020-11-13 00:58:45', '2020-11-13 00:58:47');
INSERT INTO `tb_article` VALUES (12, 'http://static.yqcode0563.cn/20201031-213501025-703.jpg', '学习学习', 1, '学习学习', 1, '1', '1', 1, '1', '1', '1', 'http://static.yqcode0563.cn/20201031-213501025-703.jpg', 'http://static.yqcode0563.cn/a2f8087fbc27476aa1daf700cf97ee72.mp4', 1, '1', '2020-11-13 00:58:45', '2020-11-13 00:58:47');
INSERT INTO `tb_article` VALUES (13, 'http://static.yqcode0563.cn/20201031-213501025-703.jpg', '学习学习', 1, '学习学习', 1, '1', '1', 1, '1', '1', '1', 'http://static.yqcode0563.cn/20201031-213501025-703.jpg', 'http://static.yqcode0563.cn/a2f8087fbc27476aa1daf700cf97ee72.mp4', 1, '1', '2020-11-13 00:58:45', '2020-11-13 00:58:47');
INSERT INTO `tb_article` VALUES (14, 'http://static.yqcode0563.cn/20201031-213501025-703.jpg', '学习学习', 1, '学习学习', 1, '1', '1', 1, '1', '1', '1', 'http://static.yqcode0563.cn/20201031-213501025-703.jpg', 'http://static.yqcode0563.cn/a2f8087fbc27476aa1daf700cf97ee72.mp4', 1, '1', '2020-11-13 00:58:45', '2020-11-13 00:58:47');
INSERT INTO `tb_article` VALUES (15, 'http://static.yqcode0563.cn/20201031-213501025-703.jpg', '学习学习', 1, '学习学习', 1, '1', '1', 1, '1', '1', '1', 'http://static.yqcode0563.cn/20201031-213501025-703.jpg', 'http://static.yqcode0563.cn/a2f8087fbc27476aa1daf700cf97ee72.mp4', 1, '1', '2020-11-13 00:58:45', '2020-11-13 00:58:47');
INSERT INTO `tb_article` VALUES (16, 'http://static.yqcode0563.cn/20201031-213501025-703.jpg', '学习学习', 1, '学习学习', 1, '1', '1', 1, '1', '1', '1', 'http://static.yqcode0563.cn/20201031-213501025-703.jpg', 'http://static.yqcode0563.cn/a2f8087fbc27476aa1daf700cf97ee72.mp4', 1, '1', '2020-11-13 00:58:45', '2020-11-13 00:58:47');
INSERT INTO `tb_article` VALUES (17, 'http://static.yqcode0563.cn/20201031-213501025-703.jpg', '学习学习', 1, '学习学习', 1, '1', '1', 1, '1', '1', '1', 'http://static.yqcode0563.cn/20201031-213501025-703.jpg', 'http://static.yqcode0563.cn/a2f8087fbc27476aa1daf700cf97ee72.mp4', 1, '1', '2020-11-13 00:58:45', '2020-11-13 00:58:47');
INSERT INTO `tb_article` VALUES (18, 'http://static.yqcode0563.cn/20201031-213501025-703.jpg', '学习学习', 1, '学习学习', 1, '1', '1', 1, '1', '1', '1', 'http://static.yqcode0563.cn/20201031-213501025-703.jpg', 'http://static.yqcode0563.cn/a2f8087fbc27476aa1daf700cf97ee72.mp4', 1, '1', '2020-11-13 00:58:45', '2020-11-13 00:58:47');
INSERT INTO `tb_article` VALUES (19, 'http://static.yqcode0563.cn/20201031-213501025-703.jpg', '学习学习', 1, '学习学习', 1, '1', '1', 1, '1', '1', '1', 'http://static.yqcode0563.cn/20201031-213501025-703.jpg', 'http://static.yqcode0563.cn/a2f8087fbc27476aa1daf700cf97ee72.mp4', 1, '1', '2020-11-13 00:58:45', '2020-11-13 00:58:47');

-- ----------------------------
-- Table structure for tb_browsing_history
-- ----------------------------
DROP TABLE IF EXISTS `tb_browsing_history`;
CREATE TABLE `tb_browsing_history`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `article_id` int(11) NULL DEFAULT NULL,
  `user_id` int(11) NULL DEFAULT NULL,
  `browsing_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_browsing_history
-- ----------------------------
INSERT INTO `tb_browsing_history` VALUES (16, 1, 6, '2020-11-13 23:53:01');
INSERT INTO `tb_browsing_history` VALUES (17, 1, 7, '2020-11-14 06:16:06');
INSERT INTO `tb_browsing_history` VALUES (18, 1, 6, '2020-11-14 12:07:16');
INSERT INTO `tb_browsing_history` VALUES (19, 1, 6, '2020-11-14 21:16:34');
INSERT INTO `tb_browsing_history` VALUES (20, 1, 6, '2020-11-14 21:16:47');
INSERT INTO `tb_browsing_history` VALUES (21, 1, 6, '2020-11-14 21:16:52');

-- ----------------------------
-- Table structure for tb_category
-- ----------------------------
DROP TABLE IF EXISTS `tb_category`;
CREATE TABLE `tb_category`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `sort` tinyint(4) NULL DEFAULT NULL,
  `gmt_create` datetime(0) NULL DEFAULT NULL,
  `gmt_modify` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_category
-- ----------------------------
INSERT INTO `tb_category` VALUES (1, '社会政策概论', 1, '2020-11-11 22:52:19', '2020-11-11 22:52:22');
INSERT INTO `tb_category` VALUES (2, '社会行政', 2, '2020-11-11 22:52:19', '2020-11-11 22:52:19');
INSERT INTO `tb_category` VALUES (3, '社会工作概论', 3, '2020-11-11 22:52:19', '2020-11-11 22:52:19');
INSERT INTO `tb_category` VALUES (4, '社区管理', 4, '2020-11-11 22:52:19', '2020-11-11 22:52:19');
INSERT INTO `tb_category` VALUES (5, '政策学习', 5, '2020-11-11 22:52:19', '2020-11-11 22:52:19');

-- ----------------------------
-- Table structure for tb_leave_messages
-- ----------------------------
DROP TABLE IF EXISTS `tb_leave_messages`;
CREATE TABLE `tb_leave_messages`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `article_id` int(11) NULL DEFAULT NULL,
  `user_id` int(11) NULL DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `audio` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `gmt_create` datetime(0) NULL DEFAULT NULL,
  `gmt_modify` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_leave_messages
-- ----------------------------
INSERT INTO `tb_leave_messages` VALUES (6, 1, 6, '啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦', NULL, NULL, '2020-11-14 21:16:46', '2020-11-14 21:16:46');

-- ----------------------------
-- Table structure for tb_map
-- ----------------------------
DROP TABLE IF EXISTS `tb_map`;
CREATE TABLE `tb_map`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `k_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `v_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用来记录一些字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_map
-- ----------------------------
INSERT INTO `tb_map` VALUES (1, 'loginMobile', '15260282340');
INSERT INTO `tb_map` VALUES (2, 'loginMobile', '15260282341');
INSERT INTO `tb_map` VALUES (3, 'loginMobile', '15260282342');

-- ----------------------------
-- Table structure for tb_pay_record
-- ----------------------------
DROP TABLE IF EXISTS `tb_pay_record`;
CREATE TABLE `tb_pay_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ariticle_id` int(11) NULL DEFAULT NULL,
  `user_id` int(11) NULL DEFAULT NULL,
  `money` double(18, 2) NULL DEFAULT NULL,
  `status` tinyint(4) NULL DEFAULT NULL,
  `gmt_create` datetime(0) NULL DEFAULT NULL,
  `gmt_modify` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `openid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `be_user` int(11) NULL DEFAULT NULL,
  `last_login_time` datetime(0) NULL DEFAULT NULL,
  `gmt_create` datetime(0) NULL DEFAULT NULL,
  `gmt_modify` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (6, 'esol diarfa', '15260282340', 'https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJYlsZsq5w9zfSmtD0U5KVygWgk0fKIwlk6DgXdKE8jH8FX39kpjrH9HCBV414wegVicLg7eK7t1Qg/132', 'oq3Y348tloC3Oo-IssS03eLyiH3U', NULL, '2020-11-13 23:51:59', '2020-11-13 23:51:59', '2020-11-13 23:51:59');
INSERT INTO `tb_user` VALUES (7, '奥伯丁', '15260282341', 'https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJJOlibibPFEWOicol1v6JuLWBUIgO5woibSYFZ01nrU760a9tvFWSExI3o2enUITxibQ57kIrlNaVrK2g/132', 'oq3Y349JWEfVhdPAI-jfq-OLmeTw', NULL, '2020-11-14 06:15:31', '2020-11-14 06:15:31', '2020-11-14 06:15:31');

SET FOREIGN_KEY_CHECKS = 1;
