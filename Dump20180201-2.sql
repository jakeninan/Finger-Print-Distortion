-- MySQL dump 10.13  Distrib 5.6.24, for Win64 (x86_64)
--
-- Host: localhost    Database: fingerprint
-- ------------------------------------------------------
-- Server version	5.5.28

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
-- Table structure for table `features`
--

DROP TABLE IF EXISTS `features`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `features` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fname` varchar(45) DEFAULT NULL,
  `valuess` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `features`
--

LOCK TABLES `features` WRITE;
/*!40000 ALTER TABLE `features` DISABLE KEYS */;
/*!40000 ALTER TABLE `features` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `referencetable`
--

DROP TABLE IF EXISTS `referencetable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `referencetable` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `orientationmap` longtext NOT NULL,
  `periodmap` longtext NOT NULL,
  `filename` varchar(45) NOT NULL,
  `reference` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `referencetable`
--

LOCK TABLES `referencetable` WRITE;
/*!40000 ALTER TABLE `referencetable` DISABLE KEYS */;
/*!40000 ALTER TABLE `referencetable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `register`
--

DROP TABLE IF EXISTS `register`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `register` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fname` varchar(45) NOT NULL,
  `lname` varchar(45) NOT NULL,
  `uname` varchar(45) NOT NULL,
  `phone` varchar(45) NOT NULL,
  `mail` varchar(45) NOT NULL,
  `pass` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `register`
--

LOCK TABLES `register` WRITE;
/*!40000 ALTER TABLE `register` DISABLE KEYS */;
INSERT INTO `register` VALUES (1,'a','a','a','8714345568','midhu@gmail.com','QWEqwe123'),(2,'a','a','a','8714345459','midhunmohan47@gmail.com','QWEqwe123'),(3,'asd','asd','asd','8714343434','midhun@gmail.com','QWEqwe123'),(4,'QWE','QWE','QWE','8714345454','midhunmohan47@gmail.com','QWEqwe123');
/*!40000 ALTER TABLE `register` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test`
--

DROP TABLE IF EXISTS `test`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `filename` varchar(45) NOT NULL,
  `reference` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test`
--

LOCK TABLES `test` WRITE;
/*!40000 ALTER TABLE `test` DISABLE KEYS */;
INSERT INTO `test` VALUES (1,'101_1.tif','1'),(2,'101_2.tif','1'),(3,'101_3.tif','1'),(4,'101_4.tif','1'),(5,'101_5.tif','1'),(6,'101_6.tif','1'),(7,'101_7.tif','1'),(8,'101_8.tif','1'),(9,'102_1.tif',''),(10,'102_2.tif','2'),(11,'102_3.tif','2'),(12,'102_4.tif','2'),(13,'102_5.tif','2'),(14,'103_1.tif','3'),(15,'103_2.tif','3'),(16,'103_3.tif','3'),(17,'103_4.tif','3'),(18,'103_5.tif','3'),(19,'103_6.tif','3'),(20,'104_1.tif','4'),(21,'104_2.tif','4'),(22,'104_3.tif','4'),(23,'104_4.tif','4'),(24,'104_5.tif','4'),(25,'104_6.tif','4'),(26,'104_7.tif','4'),(27,'104_8.tif','4'),(28,'105_1.tif','5'),(29,'105_2.tif','5'),(30,'105_4.tif','5'),(31,'105_5.tif','5'),(32,'105_6.tif','5'),(33,'106_1.tif','6'),(34,'106_2.tif','6'),(35,'106_3.tif','6'),(36,'106_4.tif','6'),(37,'106_5.tif','6'),(38,'106_6.tif','6'),(39,'106_7.tif','6'),(40,'106_8.tif','6'),(41,'107_1.tif','7'),(42,'107_2.tif','7'),(43,'107_3.tif','7'),(44,'107_5.tif','7'),(45,'107_6.tif','7'),(46,'107_7.tif','7'),(47,'107_8.tif','7'),(48,'108_1.tif','8'),(49,'108_2.tif','8'),(50,'108_3.tif','8'),(51,'108_5.tif','8'),(52,'108_6.tif','8'),(53,'108_7.tif','8'),(54,'110_1.tif','9'),(55,'110_2.tif','9');
/*!40000 ALTER TABLE `test` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-02-01  8:33:39
