package gov.nist.csd.pm.policy.model.access;

/**
 * Constants for operations in the Policy Machine
 */
public class AdminAccessRights {

    // graph
    public static final String CREATE_POLICY_CLASS = "create_policy_class";
    public static final String ASSIGN_OBJECT_ATTRIBUTE = "assign_object_attribute";
    public static final String ASSIGN_OBJECT_ATTRIBUTE_TO = "assign_object_attribute_to";
    public static final String ASSIGN_OBJECT = "assign_object";
    public static final String ASSIGN_OBJECT_TO = "assign_object_to";
    public static final String DELETE_NODE = "delete_node";
    public static final String SET_NODE_PROPERTIES = "set_node_properties";
    public static final String ASSIGN_TO = "assign_to";
    public static final String ASSIGN = "assign";
    public static final String ASSOCIATE = "associate";
    public static final String DISSOCIATE = "dissociate";
    public static final String CREATE_OBJECT = "create_object";
    public static final String CREATE_OBJECT_ATTRIBUTE = "create_object_attribute";
    public static final String CREATE_USER_ATTRIBUTE = "create_user_attribute";
    public static final String CREATE_USER = "create_user";
    public static final String DELETE_OBJECT = "delete_object";
    public static final String DELETE_OBJECT_ATTRIBUTE = "delete_object_attribute";
    public static final String DELETE_USER_ATTRIBUTE = "delete_user_attribute";
    public static final String DELETE_USER = "delete_user";
    public static final String DELETE_POLICY_CLASS = "delete_policy_class";
    public static final String DEASSIGN = "deassign";
    public static final String DEASSIGN_FROM = "deassign_from";
    public static final String CREATE_ASSOCIATION = "create_association";
    public static final String UPDATE_ASSOCIATION = "update_association";
    public static final String DELETE_ASSOCIATION = "delete_association";
    public static final String GET_ASSOCIATIONS = "get_associations";
    public static final String GET_PROHIBITIONS = "get_prohibitions";
    public static final String GET_ACCESS_RIGHTS = "get_access_rights";

    // prohibitions
    public static final String CREATE_PROHIBITION = "create_prohibition";
    public static final String CREATE_PROCESS_PROHIBITION = "create_process_prohibition";
    public static final String DELETE_PROCESS_PROHIBITION = "delete_process_prohibition";
    public static final String GET_PROCESS_PROHIBITIONS = "get_process_prohibitions";
    public static final String ADD_CONTAINER_TO_PROHIBITION = "add_container_to_prohibition";
    public static final String ADD_CONTAINER_COMPLEMENT_TO_PROHIBITION = "add_container_complement_to_prohibition";
    public static final String REMOVE_CONTAINER_FROM_PROHIBITION = "remove_container_to_prohibition";
    public static final String REMOVE_CONTAINER_COMPLEMENT_FROM_PROHIBITION = "remove_container_complement_to_prohibition";
    public static final String DELETE_PROHIBITION = "delete_prohibition";
    public static final String UPDATE_PROHIBITION = "delete_prohibition";

    // obligations
    public static final String CREATE_OBLIGATION = "create_obligation";
    public static final String UPDATE_OBLIGATION = "create_obligation";
    public static final String DELETE_OBLIGATION = "delete_obligation";
    public static final String GET_OBLIGATION = "get_obligation";
    public static final String DELETE_RULE = "delete_rule";
    public static final String SET_RESOURCE_ACCESS_RIGHTS = "set_resource_access_rights";
    public static final String GET_RESOURCE_ACCESS_RIGHTS = "get_resource_access_rights";

    // pal
    public static final String ADD_FUNCTION = "add_function";
    public static final String REMOVE_FUNCTION = "remove_function";
    public static final String GET_FUNCTIONS = "get_functions";
    public static final String ADD_CONSTANT = "add_constant";
    public static final String REMOVE_CONSTANT = "remove_constant";
    public static final String GET_CONSTANTS = "get_constants";
    public static final String GET_CONTEXT = "get_context";

    public static final String ALL_ACCESS_RIGHTS = "*";
    public static final String ALL_ADMIN_ACCESS_RIGHTS = "*a";
    public static final String ALL_RESOURCE_ACCESS_RIGHTS = "*r";

    public static final AccessRightSet ALL_ADMIN_ACCESS_RIGHTS_SET = new AccessRightSet(
            CREATE_POLICY_CLASS,
            ASSIGN_OBJECT_ATTRIBUTE,
            ASSIGN_OBJECT_ATTRIBUTE_TO,
            ASSIGN_OBJECT,
            ASSIGN_OBJECT_TO,
            DELETE_NODE,
            SET_NODE_PROPERTIES,
            ASSIGN_TO,
            ASSIGN,
            ASSOCIATE,
            DISSOCIATE,
            CREATE_OBJECT,
            CREATE_OBJECT_ATTRIBUTE,
            CREATE_USER_ATTRIBUTE,
            CREATE_USER,
            DELETE_OBJECT,
            DELETE_OBJECT_ATTRIBUTE,
            DELETE_USER_ATTRIBUTE,
            DELETE_USER,
            DELETE_POLICY_CLASS,
            DEASSIGN,
            DEASSIGN_FROM,
            CREATE_ASSOCIATION,
            UPDATE_ASSOCIATION,
            DELETE_ASSOCIATION,
            GET_ASSOCIATIONS,
            GET_PROHIBITIONS,
            GET_ACCESS_RIGHTS,
            CREATE_PROHIBITION,
            ADD_CONTAINER_TO_PROHIBITION,
            ADD_CONTAINER_COMPLEMENT_TO_PROHIBITION,
            REMOVE_CONTAINER_FROM_PROHIBITION,
            REMOVE_CONTAINER_COMPLEMENT_FROM_PROHIBITION,
            DELETE_PROHIBITION,
            CREATE_OBLIGATION,
            DELETE_OBLIGATION,
            GET_OBLIGATION,
            DELETE_RULE,
            SET_RESOURCE_ACCESS_RIGHTS,
            GET_RESOURCE_ACCESS_RIGHTS,
            CREATE_PROCESS_PROHIBITION,
            DELETE_PROCESS_PROHIBITION,
            GET_PROCESS_PROHIBITIONS
    );

    public static final AccessRightSet ALL_ACCESS_RIGHTS_SET = new AccessRightSet(ALL_ACCESS_RIGHTS);
}
