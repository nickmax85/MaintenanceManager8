CREATE DATABASE  IF NOT EXISTS `maintenancemanager` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `maintenancemanager`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 10.176.45.4    Database: maintenancemanager
-- ------------------------------------------------------
-- Server version	5.6.15

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
-- Table structure for table `abteilung`
--

DROP TABLE IF EXISTS `abteilung`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `abteilung` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `timestamp` timestamp NULL DEFAULT NULL,
  `user` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anhang`
--

DROP TABLE IF EXISTS `anhang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anhang` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(80) DEFAULT NULL,
  `file` mediumblob NOT NULL,
  `timestamp` timestamp NULL DEFAULT NULL,
  `user` varchar(45) DEFAULT NULL,
  `wartung_id` int(11) DEFAULT NULL,
  `station_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `wartung_idx` (`wartung_id`),
  KEY `station_idx` (`station_id`),
  CONSTRAINT `station` FOREIGN KEY (`station_id`) REFERENCES `station` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `wartung` FOREIGN KEY (`wartung_id`) REFERENCES `wartung` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=232 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anlage`
--

DROP TABLE IF EXISTS `anlage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anlage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `equipment` varchar(45) DEFAULT NULL,
  `auftragNr` varchar(15) DEFAULT NULL,
  `jahresStueck` decimal(6,0) DEFAULT NULL,
  `aktuelleStueck` decimal(6,0) DEFAULT NULL,
  `wartungStueckIntervall` decimal(6,0) DEFAULT NULL,
  `wartungDateIntervall` decimal(6,0) DEFAULT NULL,
  `lastWartungStueck` decimal(6,0) DEFAULT NULL,
  `lastWartungDate` date DEFAULT NULL,
  `wartungStueckWarnung` decimal(3,0) DEFAULT NULL,
  `wartungStueckFehler` decimal(3,0) DEFAULT NULL,
  `wartungDateWarnung` decimal(3,0) DEFAULT NULL,
  `auswertung` bit(1) DEFAULT NULL,
  `status` bit(1) DEFAULT NULL,
  `subMenu` bit(1) DEFAULT NULL,
  `wartungArt` int(11) DEFAULT NULL,
  `wartungsPlanLink` varchar(500) DEFAULT NULL,
  `produkte` varchar(200) DEFAULT NULL,
  `intervallDateUnit` int(11) DEFAULT NULL,
  `warnungDateUnit` int(11) DEFAULT NULL,
  `wartungUeberfaellig` int(11) DEFAULT NULL,
  `mailSent` bit(1) DEFAULT NULL,
  `createDate` date DEFAULT NULL,
  `timestamp` timestamp NULL DEFAULT NULL,
  `user` varchar(45) DEFAULT NULL,
  `panelFormatId` int(11) NOT NULL,
  `abteilungId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_PanelFormat_idx` (`panelFormatId`),
  KEY `fk_Abteilung_idx` (`abteilungId`),
  CONSTRAINT `fk_abteilung` FOREIGN KEY (`abteilungId`) REFERENCES `abteilung` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_panel` FOREIGN KEY (`panelFormatId`) REFERENCES `panelformat` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
-- Table structure for table `calendarwartung`
--

DROP TABLE IF EXISTS `calendarwartung`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `calendarwartung` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `remark` varchar(45) DEFAULT NULL,
  `anlage_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_anlage_idx` (`anlage_id`),
  CONSTRAINT `fk_anlage_cal` FOREIGN KEY (`anlage_id`) REFERENCES `anlage` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `history`
--

DROP TABLE IF EXISTS `history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user` varchar(45) DEFAULT NULL,
  `timestamp` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3282 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
-- Table structure for table `mesanlage`
--

DROP TABLE IF EXISTS `mesanlage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mesanlage` (
  `id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `prodstueck` int(11) DEFAULT NULL,
  `timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `anlageId` int(11) DEFAULT NULL,
  `anlage2Id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_anlage_id_idx` (`anlageId`),
  CONSTRAINT `fk_anlage_id` FOREIGN KEY (`anlageId`) REFERENCES `anlage` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `panelformat`
--

DROP TABLE IF EXISTS `panelformat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `panelformat` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `x` int(11) NOT NULL,
  `y` int(11) NOT NULL,
  `width` int(11) NOT NULL,
  `heigth` int(11) NOT NULL,
  `timestamp` timestamp NULL DEFAULT NULL,
  `user` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=414 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `station`
--

DROP TABLE IF EXISTS `station`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `station` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `equipment` varchar(45) DEFAULT NULL,
  `auftragNr` varchar(15) DEFAULT NULL,
  `wartungStueckIntervall` decimal(6,0) DEFAULT NULL,
  `wartungDateIntervall` decimal(3,0) DEFAULT NULL,
  `lastWartungStueck` decimal(6,0) DEFAULT NULL,
  `lastWartungDate` date DEFAULT NULL,
  `wartungStueckWarnung` decimal(3,0) DEFAULT NULL,
  `wartungStueckFehler` decimal(3,0) DEFAULT NULL,
  `wartungDateWarnung` decimal(3,0) DEFAULT NULL,
  `auswertung` bit(1) DEFAULT NULL,
  `status` bit(1) DEFAULT NULL,
  `wartungArt` int(11) DEFAULT NULL,
  `wartungsPlanLink` varchar(500) DEFAULT NULL,
  `intervallDateUnit` int(11) DEFAULT NULL,
  `warnungDateUnit` int(11) DEFAULT NULL,
  `mailSent` bit(1) DEFAULT NULL,
  `createDate` date DEFAULT NULL,
  `timestamp` timestamp NULL DEFAULT NULL,
  `user` varchar(45) DEFAULT NULL,
  `anlageId` int(11) NOT NULL,
  `panelFormatId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_anl_idx` (`anlageId`),
  KEY `fk_pnl_idx` (`panelFormatId`),
  CONSTRAINT `fk_anl` FOREIGN KEY (`anlageId`) REFERENCES `anlage` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_pnl` FOREIGN KEY (`panelFormatId`) REFERENCES `panelformat` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=289 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
-- Table structure for table `wartung`
--

DROP TABLE IF EXISTS `wartung`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wartung` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `auftragNr` varchar(15) DEFAULT NULL,
  `faellig` date DEFAULT NULL,
  `abgeschlossen` date DEFAULT NULL,
  `prozent` int(11) DEFAULT NULL,
  `mitarbeiter` varchar(50) DEFAULT NULL,
  `info` varchar(500) DEFAULT NULL,
  `wartungNichtMoeglich` bit(1) DEFAULT NULL,
  `status` int(11) DEFAULT '0',
  `anlage_id` int(11) DEFAULT NULL,
  `station_id` int(11) DEFAULT NULL,
  `timestamp` timestamp NULL DEFAULT NULL,
  `user` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_wartung_anlage_idx` (`anlage_id`),
  KEY `fk_wartung_station_idx` (`station_id`),
  CONSTRAINT `fk_wartung_anlage` FOREIGN KEY (`anlage_id`) REFERENCES `anlage` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_wartung_station` FOREIGN KEY (`station_id`) REFERENCES `station` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1225 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-11-10 14:52:39
