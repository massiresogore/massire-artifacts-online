# artface-online
create test (shift + cmd + T)
mot de passe
ec505364-73a6-484f-b89a-c186aea065f5
wizard = assistant

Merci pour votre question. L'injection de constructeur est favorisée par rapport à l'injection de champ pour plusieurs raisons :
1. Dépendances explicites : les dépendances requises sont clairement décrites. Cela garantit qu'aucune dépendance n'est négligée lors des tests ou lorsque l'objet est instancié dans des conditions différentes.
2. Dépendances immuables : en permettant aux dépendances d'être déclarées comme finales, l'injection de constructeur améliore la robustesse et la sécurité des threads.
3. Dépendance simplifié : aucune réflexion Java n'est nécessaire pour établir des dépendances.

 <plugin> ceci pour eviter le problème d'intégration de test pour la convertion  du jsonOject en string token
                <!--                Prevent test cases errors during Maven building in CI-->
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <configuration>
                    <reuseForks>false</reuseForks>
                    <forkCount>1</forkCount>
                </configuration>
</plugin> 

requette
CREATE DATABASE IF NOT EXISTS `hogwarts` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `hogwarts`;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `artifact`
--

DROP TABLE IF EXISTS `artifact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `artifact` (
`id` varchar(255) NOT NULL,
`description` varchar(255) DEFAULT NULL,
`image_url` varchar(255) DEFAULT NULL,
`name` varchar(255) DEFAULT NULL,
`owner_id` int DEFAULT NULL,
PRIMARY KEY (`id`),
KEY `FK9wrsj4y4t5uiba7957lcjdfge` (`owner_id`),
CONSTRAINT `FK9wrsj4y4t5uiba7957lcjdfge` FOREIGN KEY (`owner_id`) REFERENCES `wizard` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `artifact`
--

LOCK TABLES `artifact` WRITE;
/*!40000 ALTER TABLE `artifact` DISABLE KEYS */;
INSERT INTO `artifact` VALUES ('1250808601744904191','A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.','ImageUrl','Deluminator (Production DB)',1),('1250808601744904192','An invisibility cloak is used to make the wearer invisible.','ImageUrl','Invisibility Cloak',2),('1250808601744904193','The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.','ImageUrl','Elder Wand',1),('1250808601744904194','A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.','ImageUrl','The Marauder\'s Map',2),('1250808601744904195','A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.','ImageUrl','The Sword Of Gryffindor',3),('1250808601744904196','The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.','ImageUrl','Resurrection Stone',NULL);
/*!40000 ALTER TABLE `artifact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hogwarts_user`
--

DROP TABLE IF EXISTS `hogwarts_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hogwarts_user` (
`id` int NOT NULL,
`enabled` bit(1) NOT NULL,
`password` varchar(255) DEFAULT NULL,
`roles` varchar(255) DEFAULT NULL,
`username` varchar(255) DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hogwarts_user`
--

LOCK TABLES `hogwarts_user` WRITE;
/*!40000 ALTER TABLE `hogwarts_user` DISABLE KEYS */;
INSERT INTO `hogwarts_user` VALUES (1,_binary ' ','$2a$12$8ECo8JZ9aRcdnl.kCFZmGeBgtFl4xhvSTYAhUd58vdPnChIh3UaGW','admin user','massire'),(2,_binary ' ','$2a$12$De5dEwipYckEyDo6el4/cOO6muuIAL/HB/pk33atC8Q2t83JQUTKS','user','eric'),(3,_binary '\0','$2a$12$iNcc4pj7RlPJcSpOQkvLmuQJmeBy/NDo2tAIZZan4gJc/ipz7cXq6','user','tom');
/*!40000 ALTER TABLE `hogwarts_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hogwarts_user_seq`
--

DROP TABLE IF EXISTS `hogwarts_user_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hogwarts_user_seq` (
`next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hogwarts_user_seq`
--

LOCK TABLES `hogwarts_user_seq` WRITE;
/*!40000 ALTER TABLE `hogwarts_user_seq` DISABLE KEYS */;
INSERT INTO `hogwarts_user_seq` VALUES (101);
/*!40000 ALTER TABLE `hogwarts_user_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wizard`
--

DROP TABLE IF EXISTS `wizard`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wizard` (
`id` int NOT NULL,
`name` varchar(255) DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wizard`
--

LOCK TABLES `wizard` WRITE;
/*!40000 ALTER TABLE `wizard` DISABLE KEYS */;
INSERT INTO `wizard` VALUES (1,'Albus Dumbledore'),(2,'Harry Potter'),(3,'Neville Longbottom');
/*!40000 ALTER TABLE `wizard` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wizard_seq`
--

DROP TABLE IF EXISTS `wizard_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wizard_seq` (
`next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wizard_seq`
--

LOCK TABLES `wizard_seq` WRITE;
/*!40000 ALTER TABLE `wizard_seq` DISABLE KEYS */;
INSERT INTO `wizard_seq` VALUES (101);
/*!40000 ALTER TABLE `wizard_seq` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
on a modifier le nom de la base de donnée
alors on ajoute le nom de la tabless# massire-artifacts-online

ok pour eux