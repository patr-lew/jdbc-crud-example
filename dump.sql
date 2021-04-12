-- =====================================================
-- Schema solar_system
-- =====================================================

CREATE SCHEMA IF NOT EXISTS `solar_system` DEFAULT CHARACTER SET utf8 ;

USE `solar_system`;


-- =====================================================
-- Table `solar_system`.`planet`
-- =====================================================
CREATE TABLE IF NOT EXISTS `solar_system`.`planet` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `name` VARCHAR(60) NOT NULL COMMENT 'Name of the planet',
  `biggest_moon` varchar(60) NULL COMMENT 'Name of the biggest moon, if exists',
  `solar_day` FLOAT NULL COMMENT 'Time of planet rotation in Earth days',
  `distance_sun` BIGINT NULL COMMENT 'Distance from Sun in kilometers',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = 'Table with information about planets';


-- =====================================================
-- Populate table `solar_system`.`planet`
-- =====================================================
INSERT INTO `solar_system`.`planet`
(`name`, `biggest_moon`, `solar_day`, `distance_sun`)
VALUES
    ('Mercury',	 null,		58.6,	57910000),
    ('Venus',	 null, 		241.0,	108200000),
    ('Earth',	'the Moon',	1.0,	149600000),
    ('Mars', 	'Phobos',	1.025,	227900000),
    ('Jupiter',	'Ganymede',	0.408, 	778500000),
    ('Saturn', 	'Titan',	0.4375,	1434000000),
    ('Neptune',	'Triton',	0.79,	4495000000),
    ('Uranus', 	'Titania',	0.75,	2871000000);


