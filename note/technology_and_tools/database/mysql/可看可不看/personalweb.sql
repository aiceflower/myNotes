-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.7.10 - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  9.3.0.4984
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 导出 personalweb 的数据库结构
DROP DATABASE IF EXISTS `personalweb`;
CREATE DATABASE IF NOT EXISTS `personalweb` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `personalweb`;


-- 导出  表 personalweb.tb_music 结构
DROP TABLE IF EXISTS `tb_music`;
CREATE TABLE IF NOT EXISTS `tb_music` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `music_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `img_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `description` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;

-- 正在导出表  personalweb.tb_music 的数据：~14 rows (大约)
DELETE FROM `tb_music`;
/*!40000 ALTER TABLE `tb_music` DISABLE KEYS */;
INSERT INTO `tb_music` (`id`, `music_name`, `img_name`, `description`) VALUES
	(1, '陈奕迅 - 爱情转移', '1', NULL),
	(2, '此情可待', '2', NULL),
	(3, '此情可待', '3', NULL),
	(4, '那英 - 默', '4', NULL),
	(5, '佟大为、邓超、黄晓明 - 光阴的故事', '5', NULL),
	(6, '许巍 - 故乡', '6', NULL),
	(7, '张杰 - 剑心', '7', NULL),
	(8, '张信哲 - 爱你没错', '8', NULL),
	(9, '庄心妍 - 爱囚', '9', NULL),
	(10, 'Donna Lewis - I Could Be The One', '10', NULL),
	(11, 'G.E.M.邓紫棋 - 你把我灌醉', '11', NULL),
	(12, 'G.E.M.邓紫棋 - 泡沫', '12', NULL),
	(13, 'G.E.M.邓紫棋 - 喜欢你', '13', NULL),
	(14, 'M2M - Pretty Boy', '14', NULL),
	(15, 'Nickelback - If Everyone Cared', '15', NULL);
/*!40000 ALTER TABLE `tb_music` ENABLE KEYS */;


-- 导出  表 personalweb.tb_note 结构
DROP TABLE IF EXISTS `tb_note`;
CREATE TABLE IF NOT EXISTS `tb_note` (
  `note_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '笔记ID',
  `notebook_id` int(11) DEFAULT NULL COMMENT '笔记本ID',
  `user_id` int(11) DEFAULT NULL COMMENT '用户ID',
  `note_status` int(11) DEFAULT NULL COMMENT '笔记状态ID:备用',
  `note_name` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '笔记标题',
  `note_body` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT '笔记内容',
  `note_createtime` timestamp NULL DEFAULT NULL COMMENT '笔记创建时间',
  `note_updatetime` timestamp NULL DEFAULT NULL COMMENT '笔记最近修改时间',
  PRIMARY KEY (`note_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

-- 正在导出表  personalweb.tb_note 的数据：~1 rows (大约)
DELETE FROM `tb_note`;
/*!40000 ALTER TABLE `tb_note` DISABLE KEYS */;
INSERT INTO `tb_note` (`note_id`, `notebook_id`, `user_id`, `note_status`, `note_name`, `note_body`, `note_createtime`, `note_updatetime`) VALUES
	(1, 1, 1, 0, '第一天', '这是个美好的一天', '2016-04-18 21:21:19', '2016-04-18 21:21:30');
/*!40000 ALTER TABLE `tb_note` ENABLE KEYS */;


-- 导出  表 personalweb.tb_notebook 结构
DROP TABLE IF EXISTS `tb_notebook`;
CREATE TABLE IF NOT EXISTS `tb_notebook` (
  `notebook_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '笔记本ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `notebook_type` int(11) DEFAULT '0' COMMENT '笔记本类型ID',
  `notebook_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '笔记本名',
  `notebook_desc` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT '笔记本说明',
  `notebook_createtime` timestamp NULL DEFAULT NULL,
  `notebook_updatetime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`notebook_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- 正在导出表  personalweb.tb_notebook 的数据：~3 rows (大约)
DELETE FROM `tb_notebook`;
/*!40000 ALTER TABLE `tb_notebook` DISABLE KEYS */;
INSERT INTO `tb_notebook` (`notebook_id`, `user_id`, `notebook_type`, `notebook_name`, `notebook_desc`, `notebook_createtime`, `notebook_updatetime`) VALUES
	(1, 1, 0, 'java', NULL, '2016-04-18 21:20:27', '2016-04-18 21:20:27'),
	(2, 1, 0, 'php', NULL, '2016-04-18 21:21:41', '2016-04-18 21:21:41'),
	(3, 1, 1, 'c++', NULL, '2016-04-18 21:21:48', '2016-04-18 21:21:48');
/*!40000 ALTER TABLE `tb_notebook` ENABLE KEYS */;


-- 导出  表 personalweb.tb_user 结构
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE IF NOT EXISTS `tb_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '用户名',
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '密码',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

-- 正在导出表  personalweb.tb_user 的数据：~1 rows (大约)
DELETE FROM `tb_user`;
/*!40000 ALTER TABLE `tb_user` DISABLE KEYS */;
INSERT INTO `tb_user` (`id`, `userName`, `password`, `email`) VALUES
	(1, 'admin', 'admin', '892604768@qq.com');
/*!40000 ALTER TABLE `tb_user` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
