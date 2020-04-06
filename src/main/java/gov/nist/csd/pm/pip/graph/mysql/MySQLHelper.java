package gov.nist.csd.pm.pip.graph.mysql;

public class MySQLHelper {

    public static final String SELECT_NODE_TYPE_ID_FROM_NODE_TYPE = "SELECT node_type_id from node_type where name =? ";
    public static final String SELECT_NODE_TYPE_NAME_FROM_NODE_TYPE = "SELECT name from node_type where node_type_id =?";


    public static final String INSERT_NODE = "INSERT INTO node(node_type_id, name, node_property) VALUES(?,?,?)";
    public static final String UPDATE_NODE = "UPDATE node SET name = ?, node_property = ? WHERE node_id = ?";
    public static final String DELETE_NODE = "DELETE from node where name=?";
    public static final String SELECT_NODE_ID_NAME_FROM_NODE = "SELECT node_id, name from node where name=?";
    public static final String SELECT_ALL_FROM_NODE = "SELECT node_id, name, node_type_id, node_property from node";

    public static final String SELECT_START_NODE_ID = "SELECT start_node_id from assignment where end_node_id=";
    public static final String SELECT_END_NODE_ID = "SELECT end_node_id from assignment where start_node_id=";
    public static final String SELECT_ASSIGNMENT_ID = "Select assignment_id from assignment where start_node_id=? AND end_node_id = ?";
    public static final String INSERT_ASSIGNMENT = "INSERT into assignment( start_node_id, end_node_id) VALUES (?, ?)";
    public static final String DELETE_ASSIGNMENT = "DELETE from assignment where start_node_id=? AND end_node_id = ?";

    public static final String SELECT_ASSOCIATION_ID = "Select association_id from association where start_node_id=? AND end_node_id = ?";
    public static final String INSERT_ASSOCIATION = "INSERT into association( start_node_id, end_node_id, operation_set) VALUES (?, ?, ?)";
    public static final String UPDATE_ASSOCIATION = "UPDATE association SET operation_set =? WHERE start_node_id= ? AND end_node_id = ?";
    public static final String DELETE_ASSOCIATION = "DELETE from association where start_node_id=? AND end_node_id = ?";
    public static final String SELECT_END_NODE_ID_OPERATION = "SELECT end_node_id, operation_set from association where start_node_id=";
    public static final String SELECT_START_NODE_ID_OPERATION = "SELECT start_node_id, operation_set from association where end_node_id=";
}
