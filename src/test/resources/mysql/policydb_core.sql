DROP DATABASE IF EXISTS 'policydb_core';
CREATE DATABASE IF NOT EXISTS 'policydb_core';
USE 'policydb_core';

CREATE TABLE IF NOT EXISTS 'assignment' (
  'assignment_id' int(11) NOT NULL AUTO_INCREMENT,
  'start_node_id' int(11) NOT NULL,
  'end_node_id' int(11) NOT NULL,
  PRIMARY KEY ('assignment_id'),
  KEY 'end_node_id_idx' ('end_node_id'),
  KEY 'fk_start_node_id_idx' ('start_node_id'),
  KEY 'idx_all_columns' ('start_node_id','end_node_id'),
  CONSTRAINT 'fk_endnode' FOREIGN KEY ('end_node_id') REFERENCES 'node' ('node_id'),
  CONSTRAINT 'fk_startnode' FOREIGN KEY ('start_node_id') REFERENCES 'node' ('node_id')
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='This table stores assignment relations';

INSERT INTO 'assignment' ('assignment_id', 'start_node_id', 'end_node_id') VALUES
	(1, 2, 1),
	(2, 4, 1),
	(3, 5, 1),
	(4, 6, 1),
	(5, 7, 1),
	(6, 3, 2),
	(7, 3, 4),
	(8, 8, 5);

CREATE TABLE IF NOT EXISTS 'association' (
  'association_id' int(11) NOT NULL AUTO_INCREMENT,
  'start_node_id' int(11) NOT NULL,
  'end_node_id' int(11) NOT NULL,
  'operation_set' json NOT NULL,
  PRIMARY KEY ('association_id'),
  KEY 'start_node_id' ('start_node_id'),
  KEY 'end_node_id' ('end_node_id'),
  CONSTRAINT 'FK_end_node_id' FOREIGN KEY ('end_node_id') REFERENCES 'node' ('node_id'),
  CONSTRAINT 'FK_start_node_id' FOREIGN KEY ('start_node_id') REFERENCES 'node' ('node_id')
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS 'deny' (
  'deny_id' int(11) NOT NULL AUTO_INCREMENT,
  'deny_name' varchar(60) NOT NULL,
  'deny_type_id' int(11) NOT NULL,
  'subject_name' varchar(60) NOT NULL,
  'user_attribute_id' int(11) DEFAULT NULL,
  'process_id' varchar(60) DEFAULT NULL,
  'is_intersection' int(1) NOT NULL,
  'deny_operations' json NOT NULL,
  PRIMARY KEY ('deny_id'),
  UNIQUE KEY 'deny_name' ('deny_name'),
  UNIQUE KEY 'deny_type_id' ('deny_type_id','user_attribute_id'),
  KEY 'user_attribute_id_idx' ('user_attribute_id'),
  KEY 'deny_user_attribute_id_idx' ('user_attribute_id'),
  KEY 'deny_type_id_idx' ('deny_type_id'),
  KEY 'idx_deny_deny_name' ('deny_name'),
  CONSTRAINT 'fk_deny_type_id' FOREIGN KEY ('deny_type_id') REFERENCES 'deny_type' ('deny_type_id'),
  CONSTRAINT 'fk_deny_user_attribute_node_id' FOREIGN KEY ('user_attribute_id') REFERENCES 'node' ('node_id')
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Deny';

CREATE TABLE IF NOT EXISTS 'deny_obj_attribute' (
  'deny_id' int(11) NOT NULL,
  'object_attribute_id' int(11) NOT NULL,
  'object_complement' int(1) NOT NULL,
  PRIMARY KEY ('deny_id','object_attribute_id'),
  KEY 'fk_deny_obj_attr' ('object_attribute_id'),
  CONSTRAINT 'fk_deny_id' FOREIGN KEY ('deny_id') REFERENCES 'deny' ('deny_id') ON DELETE CASCADE,
  CONSTRAINT 'fk_deny_obj_attr' FOREIGN KEY ('object_attribute_id') REFERENCES 'node' ('node_id')
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS 'deny_type' (
  'deny_type_id' int(11) NOT NULL,
  'name' varchar(60) DEFAULT NULL,
  'abbreviation' varchar(2) NOT NULL,
  PRIMARY KEY ('deny_type_id')
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Deny types';

INSERT INTO 'deny_type' ('deny_type_id', 'name', 'abbreviation') VALUES
	(1, 'user', 'u'),
	(2, 'user_attribute', 'ua'),
	(3, 'process', 'p');

CREATE TABLE IF NOT EXISTS 'node' (
  'node_id' int(11) NOT NULL AUTO_INCREMENT,
  'node_type_id' int(11) NOT NULL,
  'name' varchar(60) DEFAULT NULL,
  'node_property' json DEFAULT NULL,
  PRIMARY KEY ('node_id'),
  UNIQUE KEY 'name' ('name'),
  KEY 'node_type_id_idx' ('node_type_id'),
  CONSTRAINT 'fk_node_type_id' FOREIGN KEY ('node_type_id') REFERENCES 'node_type' ('node_type_id')
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='This table contains all the nodes in the graph';

insert into 'node'('node_id', 'node_type_id', 'name', 'node_property') values
	(1, 5, 'super_pc', '{\"namespace\": \"super\", \"default_oa\": \"super_pc_default_OA\", \"default_ua\": \"super_pc_default_UA\",\"rep\": \"super_pc_rep\"}'),
	(2, 2, 'super_ua1','{\"namespace\": \"super\"}'),
	(3, 3, 'super',    '{\"namespace\": \"super\"}'),
	(4, 2, 'super_ua2', '{\"namespace\": \"super\"}'),
	(5, 1, 'super_oa',  '{\"namespace\": \"super\"}'),
	(6, 2, 'super_pc_default_UA', '{\"namespace\": \"super_pc\"}'),
	(7, 1, 'super_pc_default_OA', '{\"namespace\": \"super_pc\"}'),
	(8, 1, 'super_pc_rep', '{\"namespace\": \"super_pc\", \"pc\": \"super_pc\"}'),
	(9, 4, 'super_o', '{\"namespace\": \"super\"}');

CREATE TABLE IF NOT EXISTS 'node_type' (
  'node_type_id' int(11) NOT NULL,
  'name' varchar(50) DEFAULT NULL,
  'description' varchar(50) DEFAULT NULL,
  PRIMARY KEY ('node_type_id'),
  KEY 'idx_node_type_description' ('description'),
  KEY 'idx_node_type_name' ('name')
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='This table contains node types';

INSERT INTO 'node_type' ('node_type_id', 'name', 'description') VALUES
	(1, 'OA', 'Object Attribute'),
	(2, 'UA', 'User Attribute'),
	(3, 'U', 'User'),
	(4, 'O', 'Object'),
	(5, 'PC', 'Policy Class'),
	(6, 'OS', 'Operation Set');
INSERT INTO 'association' ('association_id', 'start_node_id', 'end_node_id', 'operation_set') VALUES
    (1, 2, 5, '[\"*\"]'),
    (2, 2, 6, '[\"*\"]'),
    (3, 2, 7, '[\"*\"]'),
    (4, 2, 4, '[\"*\"]'),
    (5, 4, 2, '[\"*\"]');
