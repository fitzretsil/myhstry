SET NAMES utf8 ;


--
-- Table structure for table `person`
--

ALTER TABLE `person` ADD COLUMN `paf_parents_id` VARCHAR(10) NULL AFTER `father_id`;
ALTER TABLE `person` ADD COLUMN `paf_id` VARCHAR(10) NULL AFTER `id`;
ALTER TABLE `marriage` ADD COLUMN `paf_id` VARCHAR(10) NULL AFTER `id`;