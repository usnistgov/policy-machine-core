CREATE SCHEMA IF NOT EXISTS policydb_core;
USE policydb_core;

DROP TABLE IF EXISTS `assignment`;
DROP TABLE IF EXISTS `association`;
DROP TABLE IF EXISTS `deny_obj_attribute`;
DROP TABLE IF EXISTS `deny`;
DROP TABLE IF EXISTS `node`;
DROP TABLE IF EXISTS `node_type`;
DROP TABLE IF EXISTS `deny_type`;


CREATE TABLE node_type (
                             node_type_id int NOT NULL,
                             name varchar(50) DEFAULT NULL,
                             description varchar(50) DEFAULT NULL,
                             PRIMARY KEY (node_type_id)
);

INSERT INTO `node_type` VALUES (1,'OA','Object Attribute'),(2,'UA','User Attribute'),(3,'U','User'),(4,'O','Object'),(5,'PC','Policy Class'),(6,'OS','Operation Set');

CREATE TABLE `node` (
                        `node_id` int NOT NULL AUTO_INCREMENT,
                        `node_type_id` int NOT NULL,
                        `name` varchar(60) DEFAULT NULL,
                        `node_property` varchar DEFAULT NULL,
                        PRIMARY KEY (`node_id`),
                        UNIQUE KEY `name` (`name`),
                        CONSTRAINT `fk_node_type_id` FOREIGN KEY (`node_type_id`) REFERENCES `node_type` (`node_type_id`)
);

INSERT INTO `node` VALUES (1,5,'super_pc','{"rep": "super_pc_rep", "namespace": "super", "default_oa": "super_pc_default_OA", "default_ua": "super_pc_default_UA"}'),(2,2,'super_ua1','{"namespace": "super"}'),(3,3,'super','{"namespace": "super"}'),(4,2,'super_ua2','{"namespace": "super"}'),(5,1,'super_oa','{"namespace": "super"}'),(6,2,'super_pc_default_UA','{"namespace": "super_pc"}'),(7,1,'super_pc_default_OA','{"namespace": "super_pc"}'),(8,1,'super_pc_rep','{"pc": "super_pc", "namespace": "super_pc"}'),(9,4,'super_o','{"namespace": "super"}');

CREATE TABLE `assignment` (
                              `assignment_id` int NOT NULL AUTO_INCREMENT,
                              `start_node_id` int NOT NULL,
                              `end_node_id` int NOT NULL,
                              PRIMARY KEY (`assignment_id`),
                              CONSTRAINT `fk_endnode` FOREIGN KEY (`end_node_id`) REFERENCES `node` (`node_id`),
                              CONSTRAINT `fk_startnode` FOREIGN KEY (`start_node_id`) REFERENCES `node` (`node_id`)
);

INSERT INTO `assignment` VALUES (1,2,1),(6,3,2),(7,3,4),(2,4,1),(3,5,1),(4,6,1),(5,7,1),(8,8,5);

CREATE TABLE `association` (
                               `association_id` int NOT NULL AUTO_INCREMENT,
                               `start_node_id` int NOT NULL,
                               `end_node_id` int NOT NULL,
                               `operation_set` varchar NOT NULL,
                               PRIMARY KEY (`association_id`),
                               CONSTRAINT `FK_end_node_id` FOREIGN KEY (`end_node_id`) REFERENCES `node` (`node_id`),
                               CONSTRAINT `FK_start_node_id` FOREIGN KEY (`start_node_id`) REFERENCES `node` (`node_id`)
);

INSERT INTO `association` VALUES (1,2,5,'["*"]'),(2,2,6,'["*"]'),(3,2,7,'["*"]'),(4,2,4,'["*"]'),(5,4,2,'["*"]');

CREATE TABLE `deny_type` (
                             `deny_type_id` int NOT NULL,
                             `name` varchar(60) DEFAULT NULL,
                             `abbreviation` varchar(2) NOT NULL,
                             PRIMARY KEY (`deny_type_id`)
);
INSERT INTO `deny_type` VALUES (1,'user','u'),(2,'user_attribute','ua'),(3,'process','p');

CREATE TABLE `deny` (
                        `deny_id` int NOT NULL AUTO_INCREMENT,
                        `deny_name` varchar(60) NOT NULL,
                        `deny_type_id` int NOT NULL,
                        `subject_name` varchar(60) NOT NULL,
                        `user_attribute_id` int DEFAULT NULL,
                        `process_id` varchar(60) DEFAULT NULL,
                        `is_intersection` int NOT NULL,
                        `deny_operations` varchar NOT NULL,
                        PRIMARY KEY (`deny_id`),
                        UNIQUE KEY `deny_name` (`deny_name`),
                        UNIQUE KEY `deny_type_id` (`deny_type_id`,`user_attribute_id`),
                        CONSTRAINT `fk_deny_type_id` FOREIGN KEY (`deny_type_id`) REFERENCES `deny_type` (`deny_type_id`),
                        CONSTRAINT `fk_deny_user_attribute_node_id` FOREIGN KEY (`user_attribute_id`) REFERENCES `node` (`node_id`)
);

CREATE TABLE `deny_obj_attribute` (
                                      `deny_id` int NOT NULL,
                                      `object_attribute_id` int NOT NULL,
                                      `object_complement` int NOT NULL,
                                      PRIMARY KEY (`deny_id`,`object_attribute_id`),
                                      CONSTRAINT `fk_deny_id` FOREIGN KEY (`deny_id`) REFERENCES `deny` (`deny_id`) ON DELETE CASCADE,
                                      CONSTRAINT `fk_deny_obj_attr` FOREIGN KEY (`object_attribute_id`) REFERENCES `node` (`node_id`)
);

