SET NAMES utf8 ;

ALTER TABLE `event` ADD COLUMN `fl` TINYINT NULL DEFAULT 0 AFTER `year`;