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
-- Table structure for table `leerflaeche`
--

DROP TABLE IF EXISTS `leerflaeche`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `leerflaeche` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `panelFormatId` int(11) NOT NULL,
  `timestamp` timestamp NULL DEFAULT NULL,
  `user` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_panelFormat_idx` (`panelFormatId`),
  CONSTRAINT `fk_panelFormat` FOREIGN KEY (`panelFormatId`) REFERENCES `panelformat` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `leerflaeche`
--

LOCK TABLES `leerflaeche` WRITE;
/*!40000 ALTER TABLE `leerflaeche` DISABLE KEYS */;
INSERT INTO `leerflaeche` VALUES (1,'Sozialplatz A',41,'2017-04-25 07:13:21','potzingb'),(3,'Sozialplatz C',43,'2015-12-29 12:20:30','steinbaa'),(4,'Prototypenbau',44,'2013-12-20 13:00:48','thaleradm'),(5,'Instandhaltung',45,'2013-12-20 13:00:48','thaleradm'),(9,'Audit',49,'2013-12-20 13:00:48','thaleradm'),(13,'Schulungsraum',53,'2017-10-18 11:39:18','janischw'),(22,'Sozialplatz D',203,'2015-12-29 12:14:38','steinbaa'),(23,'Sozialplatz B',204,'2017-04-25 07:13:33','potzingb');
/*!40000 ALTER TABLE `leerflaeche` ENABLE KEYS */;
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
