DROP SCHEMA IF EXISTS `pm_core`;
CREATE SCHEMA `pm_core`;
USE `pm_core`;

CREATE TABLE IF NOT EXISTS `resource_access_rights` (
    `id` int(1) NOT NULL AUTO_INCREMENT,
    `access_rights` json NOT NULL,
    PRIMARY KEY (`id`)
    );

CREATE TABLE IF NOT EXISTS `node_type` (
    `id` int(11) NOT NULL,
    `name` varchar(50) DEFAULT NULL,
    `description` varchar(50) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_node_type_name` (`name`)
    );

CREATE TABLE IF NOT EXISTS `node` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `node_type_id` int(11) NOT NULL,
    `name` varchar(50) DEFAULT NULL,
    `properties` json DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `node_name` (`name`),
    KEY `node_type_id_idx` (`node_type_id`),
    CONSTRAINT `fk_node_type_id` FOREIGN KEY (`node_type_id`) REFERENCES `node_type` (`id`)
    );

CREATE TABLE IF NOT EXISTS `assignment` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `start_node_id` int(11) NOT NULL,
    `end_node_id` int(11) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `assign_start_node_id` (`start_node_id`),
    KEY `assign_end_node_id` (`end_node_id`),
    UNIQUE `assign_start_and_end_ids` (`start_node_id`,`end_node_id`),
    CONSTRAINT `fk_endnode` FOREIGN KEY (`end_node_id`) REFERENCES `node` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_startnode` FOREIGN KEY (`start_node_id`) REFERENCES `node` (`id`) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS `association` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `start_node_id` int(11) NOT NULL,
    `end_node_id` int(11) NOT NULL,
    `operation_set` json NOT NULL,
    PRIMARY KEY (`id`),
    KEY `assoc_start_node_id` (`start_node_id`),
    KEY `assoc_end_node_id` (`end_node_id`),
    UNIQUE KEY `assoc_start_and_end_ids` (`start_node_id`,`end_node_id`),
    CONSTRAINT `FK_end_node_id` FOREIGN KEY (`end_node_id`) REFERENCES `node` (`id`) ON DELETE CASCADE,
    CONSTRAINT `FK_start_node_id` FOREIGN KEY (`start_node_id`) REFERENCES `node` (`id`) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS `prohibition_type` (
    `id` int(11) NOT NULL,
    `name` varchar(20),
    PRIMARY KEY (`id`),
    UNIQUE KEY `prohibition_type_name` (`name`)
    );

CREATE TABLE IF NOT EXISTS `prohibition` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `label` varchar(50) NOT NULL,
    `node_id` int(11),
    `process_id` varchar(50),
    `subject_type` int(11) NOT NULL,
    `is_intersection` int(1) NOT NULL,
    `access_rights` json NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `prohibition_label` (`label`),
    CONSTRAINT `fk_prohibition_node_id` FOREIGN KEY (`node_id`) REFERENCES `node` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_prohibition_type_id` FOREIGN KEY (`subject_type`) REFERENCES `prohibition_type` (`id`)
    );

CREATE TABLE IF NOT EXISTS `prohibition_container` (
    `prohibition_id` int(11) NOT NULL,
    `container_id` int(11) NOT NULL,
    `is_complement` int(1) NOT NULL,
    PRIMARY KEY (`prohibition_id`,`container_id`),
    CONSTRAINT `fk_prohibition_id` FOREIGN KEY (`prohibition_id`) REFERENCES `prohibition` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_container_id` FOREIGN KEY (`container_id`) REFERENCES `node` (`id`)
    );

CREATE TABLE IF NOT EXISTS `obligation` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `label` varchar(50) NOT NULL,
    `author` json NOT NULL,
    `rules` BLOB NOT NULL,
    PRIMARY KEY(`id`),
    UNIQUE KEY `obligation_label` (`label`)
    );

CREATE TABLE IF NOT EXISTS `pml_function` (
    `name` varchar(50) NOT NULL,
    `bytes` BLOB NOT NULL,
    PRIMARY KEY (`name`)
);

CREATE TABLE IF NOT EXISTS `pml_constant` (
    `name` varchar(50) NOT NULL,
    `value` BLOB NOT NULL,
    PRIMARY KEY (`name`)
);

INSERT INTO `prohibition_type` (id, name) VALUES
                                              (1, 'USER'),
                                              (2, 'USER_ATTRIBUTE'),
                                              (3, 'PROCESS');

INSERT INTO `node_type` (`id`, `name`, `description`) VALUES
                                                                    (1, 'OA', 'Object Attribute'),
                                                                    (2, 'UA', 'User Attribute'),
                                                                    (3, 'U', 'User'),
                                                                    (4, 'O', 'Object'),
                                                                    (5, 'PC', 'Policy Class');


/* Function get_node_id */
CREATE DEFINER=`root`@`localhost` FUNCTION `get_node_id`(attr_in varchar(50)) RETURNS int
BEGIN
DECLARE attr_id int default 0;
SELECT id
INTO attr_id
FROM NODE
WHERE upper(name)= upper(attr_in);
RETURN attr_id;
END
/* Function assignment_allowed */
CREATE DEFINER=`root`@`localhost` FUNCTION `assignment_allowed`(child_name varchar(50), parent_name varchar(50)) RETURNS tinyint(1)
BEGIN
-- If can't find any of the attributes in the system, then return false
-- If child is already a (direct/indirect) parent of the 'parent', then return false (cyclic relation not allowed)
-- If child is already a direct child of the 'parent', then return false
-- If child is only an in-direct child of the 'parent', then return true
-- If child is not a child, nor a parent of the 'parent' node, return true - assignment allowed

declare count, child_id, parent_id int;

select get_node_id(child_name) into child_id;
select get_node_id(parent_name) into parent_id;
if (child_id = 0 or parent_id = 0)
then return false;
end if;

with recursive cte (start_node_id, end_node_id) as
                   (select a.start_node_id, a.end_node_id 
                    from assignment a
                    where end_node_id = child_id
                    union all
                    select b.start_node_id, b.end_node_id
                    from assignment b
                    inner join cte on b.end_node_id = cte.start_node_id
                    where b.end_node_id = parent_id)
select count(start_node_id) INTO count
from cte;
return if(count > 0, false, true);
END
