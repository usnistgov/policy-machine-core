package gov.nist.csd.pm.policy.model.access;

public class AdminAccessRights {

    public static final String CREATE_POLICY_CLASS = "create_policy_class";
    public static final String CREATE_OBJECT = "create_object";
    public static final String CREATE_OBJECT_ATTRIBUTE = "create_object_attribute";
    public static final String CREATE_USER_ATTRIBUTE = "create_user_attribute";
    public static final String CREATE_USER = "create_user";
    public static final String SET_NODE_PROPERTIES = "set_node_properties";
    public static final String DELETE_POLICY_CLASS = "delete_policy_class";
    public static final String DELETE_OBJECT = "delete_object";
    public static final String DELETE_OBJECT_ATTRIBUTE = "delete_object_attribute";
    public static final String DELETE_USER_ATTRIBUTE = "delete_user_attribute";
    public static final String DELETE_USER = "delete_user";
    public static final String ASSIGN_OBJECT_ATTRIBUTE = "assign_object_attribute";
    public static final String ASSIGN_TO_OBJECT_ATTRIBUTE = "assign_to_object_attribute";
    public static final String ASSIGN_OBJECT = "assign_object";
    public static final String ASSIGN_USER_ATTRIBUTE = "assign_user_attribute";
    public static final String ASSIGN_TO_USER_ATTRIBUTE = "assign_to_user_attribute";
    public static final String ASSIGN_TO_POLICY_CLASS = "assign_to_policy_class";
    public static final String ASSIGN_USER = "assign_user";

    public static final String DEASSIGN_USER_ATTRIBUTE = "deassign_user_attribute";
    public static final String DEASSIGN_OBJECT_ATTRIBUTE = "deassign_object_attribute";
    public static final String DEASSIGN_USER = "deassign_user";
    public static final String DEASSIGN_OBJECT = "deassign_object";
    public static final String DEASSIGN_FROM_USER_ATTRIBUTE = "deassign_from_user_attribute";
    public static final String DEASSIGN_FROM_OBJECT_ATTRIBUTE = "deassign_from_object_attribute";
    public static final String DEASSIGN_FROM_POLICY_CLASS = "deassign_from_policy_class";

    public static final String ASSOCIATE_USER_ATTRIBUTE = "associate_user_attribute";
    public static final String ASSOCIATE_OBJECT_ATTRIBUTE = "associate_object_attribute";
    public static final String DISSOCIATE_USER_ATTRIBUTE = "associate_user_attribute";
    public static final String DISSOCIATE_OBJECT_ATTRIBUTE = "associate_object_attribute";
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

    // obligations
    public static final String CREATE_OBLIGATION = "create_obligation";
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

    private static final AccessRightSet ALL_ADMIN_ACCESS_RIGHTS_SET = new AccessRightSet(
            CREATE_POLICY_CLASS,
            CREATE_OBJECT,
            CREATE_OBJECT_ATTRIBUTE,
            CREATE_USER_ATTRIBUTE,
            CREATE_USER,
            SET_NODE_PROPERTIES,
            DELETE_POLICY_CLASS,
            DELETE_OBJECT,
            DELETE_OBJECT_ATTRIBUTE,
            DELETE_USER_ATTRIBUTE,
            DELETE_USER,
            ASSIGN_OBJECT_ATTRIBUTE,
            ASSIGN_TO_OBJECT_ATTRIBUTE,
            ASSIGN_OBJECT,
            ASSIGN_USER_ATTRIBUTE,
            ASSIGN_TO_USER_ATTRIBUTE,
            ASSIGN_USER,
            ASSIGN_TO_POLICY_CLASS,

            DEASSIGN_USER_ATTRIBUTE,
            DEASSIGN_OBJECT_ATTRIBUTE,
            DEASSIGN_USER,
            DEASSIGN_OBJECT,
            DEASSIGN_FROM_USER_ATTRIBUTE,
            DEASSIGN_FROM_OBJECT_ATTRIBUTE,
            DEASSIGN_FROM_POLICY_CLASS,

            ASSOCIATE_USER_ATTRIBUTE,
            ASSOCIATE_OBJECT_ATTRIBUTE,
            DISSOCIATE_USER_ATTRIBUTE,
            DISSOCIATE_OBJECT_ATTRIBUTE,
            GET_ASSOCIATIONS,
            GET_PROHIBITIONS,
            GET_ACCESS_RIGHTS,

            // prohibitions
            CREATE_PROHIBITION,
            CREATE_PROCESS_PROHIBITION,
            DELETE_PROCESS_PROHIBITION,
            GET_PROCESS_PROHIBITIONS,
            ADD_CONTAINER_TO_PROHIBITION,
            ADD_CONTAINER_COMPLEMENT_TO_PROHIBITION,
            REMOVE_CONTAINER_FROM_PROHIBITION,
            REMOVE_CONTAINER_COMPLEMENT_FROM_PROHIBITION,
            DELETE_PROHIBITION,

            // obligations
            CREATE_OBLIGATION,
            DELETE_OBLIGATION,
            GET_OBLIGATION,
            DELETE_RULE,
            SET_RESOURCE_ACCESS_RIGHTS,
            GET_RESOURCE_ACCESS_RIGHTS,

            // pal
            ADD_FUNCTION,
            REMOVE_FUNCTION,
            GET_FUNCTIONS,
            ADD_CONSTANT,
            REMOVE_CONSTANT,
            GET_CONSTANTS,
            GET_CONTEXT
    );

    private static final AccessRightSet ALL_ACCESS_RIGHTS_SET = new AccessRightSet(ALL_ACCESS_RIGHTS);

    private AdminAccessRights() {}

    public static AccessRightSet allAdminAccessRights() {
        return new AccessRightSet(ALL_ADMIN_ACCESS_RIGHTS_SET);
    }

    public static AccessRightSet allAccessRights() {
        return new AccessRightSet(ALL_ACCESS_RIGHTS_SET);
    }

    public static boolean isAdminAccessRight(String ar) {
        return ALL_ADMIN_ACCESS_RIGHTS_SET.contains(ar);
    }

}
