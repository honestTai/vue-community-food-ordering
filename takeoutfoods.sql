-- Public initialization: schema only, no original users or business records.

-- Create and select your database before import.

SET NAMES utf8mb4;

SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE IF NOT EXISTS `address`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '地址',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '姓名',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '电话号码',
  `user_id` int NOT NULL COMMENT '用户表关联',
  `is_default` int NULL DEFAULT NULL COMMENT '0不是1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `cart`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '购物车主键',
  `user_id` int NOT NULL COMMENT '所属用户',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `class`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '分类表',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `parent_id` int NULL DEFAULT 0 COMMENT '父级',
  `pic_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片',
  `merchant_id` int NOT NULL COMMENT '所属商家',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `evaluation`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '评价主键',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '评价内容',
  `time` datetime NOT NULL COMMENT '时间',
  `user_id` int NOT NULL COMMENT '评价人',
  `reply_id` int NULL DEFAULT NULL COMMENT '回复id',
  `goods_id` int NOT NULL COMMENT '评价的商品',
  `reply_content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '回复内容',
  `reply_time` datetime NULL DEFAULT NULL COMMENT '回复时间',
  `reply_status` int NULL DEFAULT 0 COMMENT '0没有回复1回复',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `goods`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `introduce` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '介绍，富文本',
  `images` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '4张图片',
  `merchant_id` int NULL DEFAULT NULL COMMENT '所属商家',
  `class_id` int NULL DEFAULT NULL COMMENT '所属分类',
  `status` int NULL DEFAULT 0 COMMENT '0上架1下架',
  `price` int NULL DEFAULT NULL COMMENT '价格',
  `stock` int NULL DEFAULT NULL COMMENT '库存',
  `sort_introduce` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '简介',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `merchant`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '商家主键',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商家名称',
  `time` datetime NOT NULL COMMENT '创建时间',
  `file` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `me_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家编码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `orders`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `goods_id` int NOT NULL COMMENT '商品主键',
  `address_id` int NOT NULL COMMENT '地址主键',
  `time` datetime NOT NULL COMMENT '下单时间',
  `type` int NOT NULL DEFAULT 0 COMMENT '0配送1到店',
  `status` int NOT NULL DEFAULT 0 COMMENT '0待收货1待消费2待评价3已完成4取消5已发货',
  `get_time` datetime NULL DEFAULT NULL COMMENT '送达时间/消费时间',
  `user_id` int NOT NULL COMMENT '下单用户',
  `order_string` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单编码',
  `payment_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '支付金额',
  `num` int NULL DEFAULT NULL COMMENT '数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 71 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `takeuser`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '骑手表主键',
  `order_id` int NOT NULL COMMENT '订单表主键',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '骑手名称',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '骑手电话',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

CREATE TABLE IF NOT EXISTS `user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户表主键',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户表姓名',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '电话号码',
  `sex` int NOT NULL DEFAULT 0 COMMENT '性别',
  `time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '注册时间',
  `type` int NOT NULL DEFAULT 0 COMMENT '0顾客1商家2管理员，3禁止登录',
  `number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录账号',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录密码',
  `merchant_id` int NULL DEFAULT NULL COMMENT '商家id',
  `headimg_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '头像',
  `me_status` int NULL DEFAULT NULL COMMENT '0商家管理员1商家普通用户',
  `file` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证明文件',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `usercarts`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '详情主键',
  `goods_id` int NULL DEFAULT NULL COMMENT '商品主键',
  `num` int NULL DEFAULT NULL COMMENT '商品数量',
  `cart_id` int NULL DEFAULT NULL COMMENT '对应的购物车',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS=1;
