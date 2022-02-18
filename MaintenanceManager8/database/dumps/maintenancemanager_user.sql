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
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `mail` varchar(45) DEFAULT NULL,
  `timestamp` timestamp NULL DEFAULT NULL,
  `user` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Markus Thaler','markus.thaler@magna.com','2015-04-01 07:03:44','thaleradm'),(6,'Arnold Steinbauer','arnold.steinbauer@magna.com','2015-04-01 07:03:39','thaleradm'),(9,'Strobl Philipp','philipp.strobl@magna.com','2016-08-15 23:46:28','stroblp'),(10,'Martin Reisenhofer','martin.reisenhofer@magna.com','2015-04-01 06:38:27','ihviso'),(12,'Christoph Loder','christoph.loder@magna.com','2015-06-12 10:30:06','steinbaa'),(13,'Werner Janisch','werner.janisch@magna.com','2016-07-01 07:41:31','janischw'),(14,'Bernhard Potzinger','bernhard.potzinger@magna.com','2015-07-28 10:15:29','thaleradm'),(15,'Klaus Höllerbauer','klaus.hoellerbauer@magna.com','2016-01-14 13:34:03','steinbaa'),(16,'Roland Prexl','roland.prexl@magna.com','2016-01-20 05:10:18','steinbaa'),(17,'Matthias Turrer','matthias.turrer@magna.com','2016-01-20 05:11:13','steinbaa'),(18,'Rene Weitzer','rene.weitzer@magna.com','2016-01-20 05:12:24','steinbaa'),(19,'Johann Schreiner','Johann.Schreiner@magna.com','2016-03-08 14:06:26','steinbaa'),(20,'Walfried Kresina','walfried.kresina@magna.com','2016-03-09 06:43:11','steinbaa'),(21,'Wolfgang Taschner','Wolfgang.Taschner@magna.com','2016-03-09 06:47:15','steinbaa'),(22,'Erwin Schnalzer','Erwin.Schnalzer@magna.com','2016-03-09 06:47:49','steinbaa'),(23,'Roman Rücker','Roman.Ruecker@magna.com','2016-03-09 06:52:14','steinbaa');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
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
