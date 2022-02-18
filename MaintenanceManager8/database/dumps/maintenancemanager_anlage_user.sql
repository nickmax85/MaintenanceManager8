-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: maintenancemanager
-- ------------------------------------------------------
-- Server version	5.7.19-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `anlage_user`
--

DROP TABLE IF EXISTS `anlage_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anlage_user` (
  `anlageId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `user` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`anlageId`,`userId`),
  KEY `fk_user_idx` (`userId`),
  KEY `fk_anlage_idx` (`anlageId`),
  CONSTRAINT `fk_anlage` FOREIGN KEY (`anlageId`) REFERENCES `anlage` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_user` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `anlage_user`
--

LOCK TABLES `anlage_user` WRITE;
/*!40000 ALTER TABLE `anlage_user` DISABLE KEYS */;
INSERT INTO `anlage_user` VALUES (2,1,'2018-02-21 14:13:08',NULL),(2,12,'2018-02-21 14:13:08',NULL),(2,14,'2018-02-21 14:13:08',NULL),(3,1,'2014-09-02 10:13:51','thaleradm'),(3,6,'2014-09-02 10:11:15','thaleradm'),(3,9,'2016-08-16 00:02:32','stroblp'),(3,14,'2015-07-28 10:16:23','thaleradm'),(5,1,'2014-09-02 10:15:17','thaleradm'),(5,6,'2014-09-02 10:12:41','thaleradm'),(5,9,'2016-08-16 00:04:57','stroblp'),(5,14,'2015-07-28 10:17:38','thaleradm'),(6,1,'2014-09-02 10:13:45','thaleradm'),(6,6,'2014-09-02 10:11:10','thaleradm'),(6,9,'2016-08-16 00:02:16','stroblp'),(6,14,'2015-07-28 10:16:18','thaleradm'),(7,1,'2014-09-02 10:13:10','thaleradm'),(7,6,'2014-09-02 10:10:37','thaleradm'),(7,9,'2016-08-16 00:00:00','stroblp'),(7,14,'2015-07-28 10:15:35','thaleradm'),(8,1,'2014-09-02 10:13:15','thaleradm'),(8,6,'2014-09-02 10:10:44','thaleradm'),(8,9,'2016-08-16 00:00:59','stroblp'),(8,14,'2015-07-28 10:15:54','thaleradm'),(9,1,'2014-09-02 10:13:24','thaleradm'),(9,6,'2014-09-02 10:10:49','thaleradm'),(9,9,'2016-08-16 00:01:23','stroblp'),(9,14,'2015-07-28 10:16:07','thaleradm'),(10,1,'2014-09-02 10:14:38','thaleradm'),(10,6,'2014-09-02 10:12:06','thaleradm'),(10,9,'2016-08-16 00:04:12','stroblp'),(10,14,'2015-07-28 10:17:10','thaleradm'),(11,1,'2014-09-02 10:14:27','thaleradm'),(11,6,'2014-09-02 10:11:53','thaleradm'),(11,9,'2016-08-16 00:03:56','stroblp'),(11,14,'2015-07-28 10:16:57','thaleradm'),(12,1,'2014-09-02 10:15:25','thaleradm'),(12,6,'2014-09-02 10:12:49','thaleradm'),(12,9,'2016-08-16 00:05:07','stroblp'),(12,14,'2015-07-28 10:17:46','thaleradm'),(13,1,'2014-09-02 10:13:30','thaleradm'),(13,6,'2014-09-02 10:10:54','thaleradm'),(13,9,'2016-08-16 00:02:02','stroblp'),(13,12,'2016-03-09 06:42:28','steinbaa'),(13,14,'2015-07-28 10:16:15','thaleradm'),(13,20,'2016-03-09 06:43:50','steinbaa'),(14,1,'2014-09-02 10:14:52','thaleradm'),(14,6,'2014-09-02 10:12:23','thaleradm'),(14,9,'2016-08-16 00:04:31','stroblp'),(14,14,'2015-07-28 10:17:26','thaleradm'),(15,1,'2014-09-02 10:14:09','thaleradm'),(15,6,'2014-09-02 10:11:42','thaleradm'),(15,9,'2016-08-16 00:03:22','stroblp'),(16,1,'2014-09-02 10:15:33','thaleradm'),(16,6,'2014-09-02 10:12:56','thaleradm'),(16,9,'2016-08-16 00:05:20','stroblp'),(16,10,'2015-04-01 06:39:06','ihviso'),(16,14,'2015-07-28 10:17:54','thaleradm'),(16,19,'2016-03-08 14:06:38','steinbaa'),(17,1,'2014-09-02 10:14:00','thaleradm'),(17,6,'2014-09-02 10:11:33','thaleradm'),(17,9,'2016-08-16 00:02:53','stroblp'),(17,14,'2015-07-28 10:16:45','thaleradm'),(19,1,'2014-09-02 10:15:28','thaleradm'),(19,6,'2014-09-02 10:12:51','thaleradm'),(19,9,'2016-08-16 00:05:12','stroblp'),(19,14,'2015-07-28 10:17:49','thaleradm'),(20,1,'2014-09-02 10:15:22','thaleradm'),(20,6,'2014-09-02 10:12:46','thaleradm'),(20,9,'2016-08-16 00:05:04','stroblp'),(20,14,'2015-07-28 10:17:43','thaleradm'),(21,1,'2014-09-02 10:14:35','thaleradm'),(21,6,'2014-09-02 10:12:03','thaleradm'),(21,14,'2015-07-28 10:17:08','thaleradm'),(22,1,'2014-09-02 10:14:59','thaleradm'),(22,6,'2014-09-02 10:12:30','thaleradm'),(22,9,'2016-08-16 00:04:42','stroblp'),(22,14,'2015-07-28 10:17:32','thaleradm'),(23,1,'2014-09-02 10:14:40','thaleradm'),(23,6,'2014-09-02 10:12:09','thaleradm'),(23,9,'2016-08-16 00:04:17','stroblp'),(23,14,'2015-07-28 10:17:13','thaleradm'),(24,1,'2014-09-02 10:13:54','thaleradm'),(24,6,'2014-09-02 10:11:17','thaleradm'),(24,14,'2015-07-28 10:16:27','thaleradm'),(26,6,'2014-09-02 10:11:40','thaleradm'),(26,9,'2016-08-16 00:03:07','stroblp'),(26,14,'2015-07-28 10:16:51','thaleradm'),(27,1,'2014-09-02 10:13:56','thaleradm'),(27,6,'2014-09-02 10:11:19','thaleradm'),(27,9,'2016-08-16 00:02:42','stroblp'),(27,14,'2015-07-28 10:16:30','thaleradm'),(28,1,'2014-09-02 10:13:48','thaleradm'),(28,6,'2014-09-02 10:11:13','thaleradm'),(28,9,'2016-08-16 00:02:25','stroblp'),(28,14,'2015-07-28 10:16:21','thaleradm'),(30,1,'2014-09-02 10:14:29','thaleradm'),(30,6,'2014-09-02 10:11:57','thaleradm'),(30,9,'2016-08-16 00:03:59','stroblp'),(31,1,'2014-09-02 10:15:08','thaleradm'),(31,6,'2014-09-02 10:12:37','thaleradm'),(31,9,'2016-08-16 00:04:54','stroblp'),(31,14,'2015-07-28 10:17:36','thaleradm'),(32,1,'2014-09-02 10:15:31','thaleradm'),(32,6,'2014-09-02 10:12:54','thaleradm'),(32,9,'2016-08-16 00:05:15','stroblp'),(32,14,'2015-07-28 10:17:51','thaleradm'),(34,1,'2014-09-02 10:14:44','thaleradm'),(34,6,'2014-09-02 10:12:14','thaleradm'),(34,9,'2016-08-16 00:04:25','stroblp'),(34,14,'2015-07-28 10:17:18','thaleradm'),(35,1,'2014-09-02 10:13:16','thaleradm'),(35,6,'2014-09-02 10:10:47','thaleradm'),(35,9,'2016-08-16 00:01:16','stroblp'),(35,14,'2015-07-28 10:16:05','thaleradm'),(39,1,'2014-09-02 10:15:05','thaleradm'),(39,6,'2014-09-02 10:12:34','thaleradm'),(39,9,'2016-08-16 00:04:49','stroblp'),(40,1,'2014-09-02 10:14:24','thaleradm'),(40,6,'2014-09-02 10:11:50','thaleradm'),(40,9,'2016-08-16 00:03:29','stroblp'),(41,1,'2014-09-02 10:14:34','thaleradm'),(41,6,'2014-09-02 10:12:01','thaleradm'),(41,9,'2016-08-16 00:04:06','stroblp'),(41,14,'2015-07-28 10:17:05','thaleradm'),(47,1,'2014-09-02 10:15:35','thaleradm'),(47,6,'2014-09-02 10:13:00','thaleradm'),(47,9,'2016-08-16 00:05:24','stroblp'),(54,1,'2014-09-02 10:13:58','thaleradm'),(54,6,'2014-09-02 10:11:23','thaleradm'),(54,9,'2016-08-16 00:02:46','stroblp'),(54,21,'2016-03-09 06:51:13','steinbaa'),(54,22,'2016-03-09 06:51:06','steinbaa'),(55,1,'2014-09-02 10:14:42','thaleradm'),(55,6,'2014-09-02 10:12:12','thaleradm'),(55,9,'2016-08-16 00:04:21','stroblp'),(55,14,'2015-07-28 10:17:15','thaleradm'),(56,6,'2016-01-14 13:30:06','steinbaa'),(56,9,'2016-08-16 00:01:45','stroblp'),(56,14,'2015-07-28 10:16:13','thaleradm'),(56,15,'2016-01-14 13:34:30','steinbaa'),(56,16,'2016-01-20 05:10:26','steinbaa'),(56,17,'2016-01-20 05:11:26','steinbaa'),(56,18,'2016-01-20 05:12:42','steinbaa'),(57,21,'2016-03-09 06:48:14','steinbaa'),(57,22,'2016-03-09 06:48:07','steinbaa'),(58,9,'2016-08-16 00:05:40','stroblp'),(58,21,'2016-03-09 06:49:38','steinbaa'),(58,22,'2016-03-09 06:49:26','steinbaa'),(59,9,'2016-08-16 00:04:38','stroblp'),(60,9,'2016-08-16 00:00:36','stroblp'),(60,23,'2016-03-09 06:52:44','steinbaa'),(61,9,'2016-08-16 00:03:51','stroblp'),(62,9,'2016-08-16 00:02:13','stroblp'),(63,9,'2016-08-16 00:01:51','stroblp'),(63,21,'2016-03-09 06:48:54','steinbaa'),(63,22,'2016-03-09 06:48:28','steinbaa'),(64,9,'2016-08-16 00:01:56','stroblp'),(65,9,'2016-08-16 00:04:45','stroblp'),(66,9,'2016-08-16 00:03:18','stroblp'),(67,9,'2016-08-16 00:02:09','stroblp'),(68,9,'2016-08-16 00:04:33','stroblp'),(69,9,'2016-08-16 00:03:15','stroblp');
/*!40000 ALTER TABLE `anlage_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-03-02 16:56:11
