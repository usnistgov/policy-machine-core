package gov.nist.csd.pm.pip.mysql;

public class MySQLHelper {

    public static final String SELECT_NODE_TYPE_ID_FROM_NODE_TYPE = "SELECT node_type_id from policydb_core.node_type where name =? ";
    public static final String SELECT_NODE_TYPE_FROM_NODE_TYPE = "SELECT node_type_id, description from policydb_core.node_type where name =? ";
    public static final String SELECT_ALL_NODE_TYPE = "SELECT * from policydb_core.node_type";

    public static final String SELECT_NODE_TYPE_NAME_FROM_NODE_TYPE = "SELECT name from policydb_core.node_type where node_type_id =?";


    public static final String INSERT_NODE = "INSERT INTO policydb_core.node(node_type_id, name, node_property) VALUES(?,?,?)";
    public static final String UPDATE_NODE = "UPDATE policydb_core.node SET name = ?, node_property = ? WHERE node_id = ?";
    public static final String DELETE_NODE = "DELETE from policydb_core.node where name=?";
    public static final String SELECT_NODE_ID_NAME_FROM_NODE = "select count(*) AS total from policydb_core.node where name = ?";
    public static final String SELECT_ALL_FROM_NODE = "SELECT node_id, name, node_type_id, node_property from policydb_core.node";
    public static final String SELECT_ALL_FROM_NAME = "SELECT node_id, name, node_type_id, node_property from policydb_core.node where name = ?";
    public static final String DELETE_DENYS = "DELETE from policydb_core.deny";
    public static final String DELETE_ASSOCIATIONS = "DELETE from policydb_core.association";
    public static final String DELETE_ASSIGNMENTS = " DELETE from policydb_core.assignment ";
    public static final String DELETE_NODES = "DELETE from policydb_core.node;";

    public static final String SELECT_START_NODE_ID = "SELECT start_node_id from policydb_core.assignment where end_node_id=";
    public static final String SELECT_END_NODE_ID = "SELECT end_node_id from policydb_core.assignment where start_node_id=";
    public static final String SELECT_ASSIGNMENT_ID = "Select assignment_id from policydb_core.assignment where start_node_id=? AND end_node_id = ?";
    public static final String INSERT_ASSIGNMENT = "INSERT into policydb_core.assignment( start_node_id, end_node_id) VALUES (?, ?)";
    public static final String DELETE_ASSIGNMENT = "DELETE from policydb_core.assignment where start_node_id=? AND end_node_id = ?";

    public static final String SELECT_ASSOCIATION_ID = "Select association_id from policydb_core.association where start_node_id=? AND end_node_id = ?";
    public static final String INSERT_ASSOCIATION = "INSERT into policydb_core.association( start_node_id, end_node_id, operation_set) VALUES (?, ?, ?)";
    public static final String UPDATE_ASSOCIATION = "UPDATE policydb_core.association SET operation_set =? WHERE start_node_id= ? AND end_node_id = ?";
    public static final String DELETE_ASSOCIATION = "DELETE from policydb_core.association where start_node_id=? AND end_node_id = ?";
    public static final String SELECT_END_NODE_ID_OPERATION = "SELECT end_node_id, operation_set from policydb_core.association where start_node_id=";
    public static final String SELECT_START_NODE_ID_OPERATION = "SELECT start_node_id, operation_set from policydb_core.association where end_node_id=";

    public static final String INSERT_PROHIBITION = "INSERT INTO policydb_core.deny(deny_name, deny_type_id, subject_name, user_attribute_id, process_id, is_intersection, deny_operations) VALUES(?,?,?,?,?,?,?)";
    public static final String INSERT_CONTAINERS = "INSERT INTO policydb_core.deny_obj_attribute(deny_id, object_attribute_id, object_complement) VALUES(?,?,?)";
    public static final String DELETE_PROHIBITION_CONTAINER = "DELETE from policydb_core.deny_obj_attribute where deny_id = ?";
    public static final String DELETE_PROHIBITION = "DELETE from policydb_core.deny where deny_name = ?";
    public static final String SELECT_PROHIBITION_FROM_NAME = "SELECT deny_id, deny_name, subject_name, is_intersection, deny_operations from policydb_core.deny where deny_name =? ";
    public static final String SELECT_ALL_PROHIBITION = "SELECT deny_id, deny_name, subject_name, is_intersection, deny_operations from policydb_core.deny";
    public static final String SELECT_EXISTS_ID_NODE_ID = "SELECT node_id from policydb_core.node where name =?";
    public static final String SELECT_EXISTS_NAME_NODE_ID = "SELECT name from policydb_core.node where node_id=?";
    public static final String SELECT_CONTAINER_DENY_ID = "SELECT object_attribute_id, object_complement from policydb_core.deny_obj_attribute where deny_id=? ";
    public static final String SELECT_CONTAINER_DENY_ID_SIMPLE = "SELECT object_attribute_id, object_complement from policydb_core.deny_obj_attribute where deny_id=";
    public static final String SELECT_ALL_CONTAINERS = "SELECT deny_id, object_attribute_id, object_complement from policydb_core.policydb_core.deny_obj_attribute";
    public static final String UPDATE_PROHIBITION = "UPDATE policydb_core.deny SET deny_name=?, subject_name=?, user_attribute_id=?, process_id=?, is_intersection =?, deny_operations=?, deny_type_id = ? WHERE deny_name=?";

}
