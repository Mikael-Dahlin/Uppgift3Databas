-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: May 20, 2021 at 07:22 AM
-- Server version: 8.0.21
-- PHP Version: 7.3.21

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `movie_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `actors`
--

DROP TABLE IF EXISTS `actors`;
CREATE TABLE IF NOT EXISTS `actors` (
  `actors_id` int NOT NULL AUTO_INCREMENT,
  `actors_name` varchar(30) NOT NULL,
  `movies_id` int NOT NULL,
  PRIMARY KEY (`actors_id`),
  KEY `movies_id` (`movies_id`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `actors`
--

INSERT INTO `actors` (`actors_id`, `actors_name`, `movies_id`) VALUES
(1, 'brad pitt', 1),
(2, 'leonardo dicaprio', 3),
(3, 'margot robbie', 3),
(4, 'morgan freeman', 1),
(5, 'kevin spacey', 1),
(6, 'brad pitt', 2),
(7, 'christoph waltz', 2),
(8, 'diane kruger', 2),
(9, 'brad pitt', 3);

-- --------------------------------------------------------

--
-- Table structure for table `awards`
--

DROP TABLE IF EXISTS `awards`;
CREATE TABLE IF NOT EXISTS `awards` (
  `awards_id` int NOT NULL AUTO_INCREMENT,
  `awards_award` varchar(40) NOT NULL,
  `actors_id` int DEFAULT NULL,
  `movies_id` int NOT NULL,
  PRIMARY KEY (`awards_id`),
  KEY `movies_id` (`movies_id`),
  KEY `actors_id` (`actors_id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `awards`
--

INSERT INTO `awards` (`awards_id`, `awards_award`, `actors_id`, `movies_id`) VALUES
(1, 'best movie', NULL, 1),
(2, 'best villian', 5, 1),
(3, 'best supporting actor', 7, 2),
(4, 'best supporting actor', 9, 3);

-- --------------------------------------------------------

--
-- Table structure for table `movies`
--

DROP TABLE IF EXISTS `movies`;
CREATE TABLE IF NOT EXISTS `movies` (
  `movies_id` int NOT NULL AUTO_INCREMENT,
  `movies_name` varchar(30) NOT NULL,
  `movies_director` varchar(30) NOT NULL,
  PRIMARY KEY (`movies_id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `movies`
--

INSERT INTO `movies` (`movies_id`, `movies_name`, `movies_director`) VALUES
(1, 'se7en', 'david fincher'),
(2, 'inglorious basterds', 'quentin tarantino'),
(3, 'once upon a time in hollywood', 'quentin tarantino');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
