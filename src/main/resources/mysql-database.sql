/*
SQLyog Ultimate v10.51 
MySQL - 5.6.23-log : Database - infos
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`infos` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;

USE `infos`;

/*Table structure for table `blogs` */

DROP TABLE IF EXISTS `blogs`;

CREATE TABLE `blogs` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '记录主键',
  `username` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT '发布人',
  `category1` bigint(11) unsigned NOT NULL COMMENT '一级分类',
  `category2` bigint(11) NOT NULL COMMENT '二级分类',
  `title` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT '标题',
  `content` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '正文文件名',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updatetime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `IDX_CATEGORYS` (`category1`,`category2`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Table structure for table `category` */

DROP TABLE IF EXISTS `category`;

CREATE TABLE `category` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '记录主键',
  `categoryname` varchar(30) COLLATE utf8_unicode_ci NOT NULL COMMENT '分类名称',
  `level` tinyint(2) NOT NULL DEFAULT '1' COMMENT '分类层级',
  `parentcategory` bigint(11) unsigned NOT NULL DEFAULT '0' COMMENT '父分类id',
  `isdel` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updatetime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `updateuser` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Table structure for table `links` */

DROP TABLE IF EXISTS `links`;

CREATE TABLE `links` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '记录主键',
  `linkname` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '名称',
  `link` varchar(150) COLLATE utf8_unicode_ci NOT NULL COMMENT '链接地址',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updatetime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Table structure for table `members` */

DROP TABLE IF EXISTS `members`;

CREATE TABLE `members` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '记录主键',
  `uid` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '集团id',
  `realname` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '姓名',
  `username` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '用户名',
  `rolegroup` tinyint(2) NOT NULL COMMENT '权限组',
  `isdel` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updatetime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Table structure for table `slider` */

DROP TABLE IF EXISTS `slider`;

CREATE TABLE `slider` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '记录主键',
  `idx` int(4) NOT NULL COMMENT '索引号',
  `originname` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT '源文件名',
  `destname` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '目标文件名',
  `isdel` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updatetime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
