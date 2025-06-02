package gov.nist.csd.pm.core.pap.admin;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AdminAccessRights {

    // graph
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

    public static final String DELETE_POLICY_CLASS_FROM = "delete_policy_class_from";
    public static final String DELETE_OBJECT_FROM = "delete_object_from";
    public static final String DELETE_OBJECT_ATTRIBUTE_FROM = "delete_object_attribute_from";
    public static final String DELETE_USER_ATTRIBUTE_FROM = "delete_user_attribute_from";
    public static final String DELETE_USER_FROM = "delete_user_from";

    public static final String ASSIGN = "assign";
    public static final String ASSIGN_TO = "assign_to";
    public static final String DEASSIGN = "deassign";
    public static final String DEASSIGN_FROM = "deassign_from";

    public static final String ASSOCIATE = "associate";
    public static final String ASSOCIATE_TO = "associate_to";

    public static final String DISSOCIATE = "dissociate";
    public static final String DISSOCIATE_FROM = "dissociate_from";

    // prohibitions
    public static final String CREATE_PROHIBITION = "create_prohibition";
    public static final String CREATE_PROCESS_PROHIBITION = "create_process_prohibition";
    public static final String CREATE_PROHIBITION_WITH_COMPLEMENT_CONTAINER = "create_prohibition_with_complement_container";
    public static final String DELETE_PROCESS_PROHIBITION = "delete_process_prohibition";
    public static final String DELETE_PROHIBITION = "delete_prohibition";
    public static final String DELETE_PROHIBITION_WITH_COMPLEMENT_CONTAINER = "delete_prohibition_with_complement_container";

    // obligations
    public static final String CREATE_OBLIGATION = "create_obligation";
    public static final String CREATE_OBLIGATION_WITH_ANY_PATTERN = "create_obligation_with_any_pattern";
    public static final String DELETE_OBLIGATION = "delete_obligation";
    public static final String DELETE_OBLIGATION_WITH_ANY_PATTERN = "delete_obligation_with_any_pattern";

    // operations
    public static final String SET_RESOURCE_OPERATIONS = "set_resource_operations";
    public static final String CREATE_ADMIN_OPERATION = "create_admin_operation";
    public static final String DELETE_ADMIN_OPERATION = "delete_admin_operation";

    // routines
    public static final String CREATE_ADMIN_ROUTINE = "create_admin_routine";
    public static final String DELETE_ADMIN_ROUTINE = "delete_admin_routine";

    // general
    public static final String RESET = "reset";
    public static final String SERIALIZE_POLICY = "serialize_policy";
    public static final String DESERIALIZE_POLICY = "deserialize_policy";

    // query
    public static final String QUERY_ACCESS = "query_access";
    public static final String QUERY_POLICY_CLASSES = "query_policy_classes";
    public static final String QUERY_ASSIGNMENTS = "query_assignments";
    public static final String QUERY_SUBGRAPH = "query_subgraph";
    public static final String QUERY_ASSOCIATIONS = "query_associations";
    public static final String QUERY_PROHIBITIONS = "query_prohibitions";
    public static final String QUERY_PROCESS_PROHIBITIONS = "query_process_prohibitions";
    public static final String QUERY_OBLIGATIONS = "query_obligations";
    public static final String QUERY_RESOURCE_OPERATIONS = "query_resource_operations";
    public static final String QUERY_ADMIN_OPERATIONS = "query_admin_operations";
    public static final String QUERY_ADMIN_ROUTINES = "query_admin_routines";

    public static final String WC_ALL = "*";
    public static final String WC_ADMIN = "*a";
    public static final String WC_RESOURCE = "*r";
    public static final String WC_ADMIN_GRAPH = "*a:graph";
    public static final String WC_ADMIN_PROHIBITION = "*a:prohibition";
    public static final String WC_ADMIN_OBLIGATION = "*a:obligation";
    public static final String WC_ADMIN_OPERATION = "*a:operation";
    public static final String WC_ADMIN_ROUTINE = "*a:routine";
    public static final String WC_QUERY = "*q";
    public static final String WC_QUERY_GRAPH = "*q:graph";
    public static final String WC_QUERY_PROHIBITION = "*q:prohibition";
    public static final String WC_QUERY_OBLIGATION = "*q:obligation";
    public static final String WC_QUERY_OPERATION = "*q:operation";
    public static final String WC_QUERY_ROUTINE = "*q:routine";

    public static final Set<String> ALL_ADMIN_GRAPH_ACCESS_RIGHTS_SET = Set.of(
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
        DELETE_POLICY_CLASS_FROM,
        DELETE_OBJECT_FROM,
        DELETE_OBJECT_ATTRIBUTE_FROM,
        DELETE_USER_ATTRIBUTE_FROM,
        DELETE_USER_FROM,
        ASSIGN,
        ASSIGN_TO,
        DEASSIGN,
        DEASSIGN_FROM,
        ASSOCIATE,
        ASSOCIATE_TO,
        DISSOCIATE,
        DISSOCIATE_FROM
    );

    public static final Set<String> ALL_ADMIN_PROHIBITION_ACCESS_RIGHTS_SET = Set.of(
        CREATE_PROHIBITION,
        CREATE_PROCESS_PROHIBITION,
        CREATE_PROHIBITION_WITH_COMPLEMENT_CONTAINER,
        DELETE_PROCESS_PROHIBITION,
        DELETE_PROHIBITION,
        DELETE_PROHIBITION_WITH_COMPLEMENT_CONTAINER
    );

    public static final Set<String> ALL_ADMIN_OBLIGATION_ACCESS_RIGHTS_SET = Set.of(
        CREATE_OBLIGATION,
        CREATE_OBLIGATION_WITH_ANY_PATTERN,
        DELETE_OBLIGATION,
        DELETE_OBLIGATION_WITH_ANY_PATTERN
    );

    public static final Set<String> ALL_ADMIN_OPERATION_ACCESS_RIGHTS_SET = Set.of(
        SET_RESOURCE_OPERATIONS,
        CREATE_ADMIN_OPERATION,
        DELETE_ADMIN_OPERATION
    );

    public static final Set<String> ALL_ADMIN_ROUTINE_ACCESS_RIGHTS_SET = Set.of(
        CREATE_ADMIN_ROUTINE,
        DELETE_ADMIN_ROUTINE
    );

    public static final Set<String> ALL_GRAPH_QUERY_ACCESS_RIGHTS_SET = Set.of(
        QUERY_POLICY_CLASSES,
        QUERY_ASSIGNMENTS,
        QUERY_SUBGRAPH,
        QUERY_ASSOCIATIONS
    );

    public static final Set<String> ALL_PROHIBITION_QUERY_ACCESS_RIGHTS_SET = Set.of(
        QUERY_PROHIBITIONS,
        QUERY_PROCESS_PROHIBITIONS
    );

    public static final Set<String> ALL_OBLIGATION_QUERY_ACCESS_RIGHTS_SET = Set.of(
        QUERY_OBLIGATIONS
    );

    public static final Set<String> ALL_OPERATION_QUERY_ACCESS_RIGHTS_SET = Set.of(
        QUERY_RESOURCE_OPERATIONS,
        QUERY_ADMIN_OPERATIONS
    );

    public static final Set<String> ALL_ROUTINE_QUERY_ACCESS_RIGHTS_SET = Set.of(
        QUERY_ADMIN_ROUTINES
    );

    private static final Set<String> allQueryAccessRights = new HashSet<>();
    static {
        allQueryAccessRights.addAll(ALL_GRAPH_QUERY_ACCESS_RIGHTS_SET);
        allQueryAccessRights.addAll(ALL_PROHIBITION_QUERY_ACCESS_RIGHTS_SET);
        allQueryAccessRights.addAll(ALL_OBLIGATION_QUERY_ACCESS_RIGHTS_SET);
        allQueryAccessRights.addAll(ALL_OPERATION_QUERY_ACCESS_RIGHTS_SET);
        allQueryAccessRights.addAll(ALL_ROUTINE_QUERY_ACCESS_RIGHTS_SET);
    }
    public static final Set<String> ALL_QUERY_ACCESS_RIGHTS_SET = Collections.unmodifiableSet(allQueryAccessRights);

    private static final Set<String> allAdminAccessRights = new HashSet<>();
    static {
        allAdminAccessRights.addAll(ALL_ADMIN_GRAPH_ACCESS_RIGHTS_SET);
        allAdminAccessRights.addAll(ALL_ADMIN_PROHIBITION_ACCESS_RIGHTS_SET);
        allAdminAccessRights.addAll(ALL_ADMIN_OBLIGATION_ACCESS_RIGHTS_SET);
        allAdminAccessRights.addAll(ALL_ADMIN_OPERATION_ACCESS_RIGHTS_SET);
        allAdminAccessRights.addAll(ALL_ADMIN_ROUTINE_ACCESS_RIGHTS_SET);
        allAdminAccessRights.add(RESET);
        allAdminAccessRights.add(SERIALIZE_POLICY);
        allAdminAccessRights.add(DESERIALIZE_POLICY);
    }
    public static final Set<String> ALL_ADMIN_ACCESS_RIGHTS_SET = Collections.unmodifiableSet(allAdminAccessRights);

    private static final Set<String> allAccessRights = new HashSet<>();
    static {
        allAccessRights.addAll(allAdminAccessRights);
        allAccessRights.addAll(allQueryAccessRights);
    }
    public static final Set<String> ALL_ACCESS_RIGHTS_SET = Collections.unmodifiableSet(allAccessRights);

    public static boolean isAdminAccessRight(String ar) {
        return ALL_ADMIN_ACCESS_RIGHTS_SET.contains(ar);
    }

    private static final Set<String> WILDCARD_ACCESS_RIGHTS = new HashSet<>(List.of(
        WC_ALL,
        WC_ADMIN,
        WC_RESOURCE,
        WC_ADMIN_GRAPH,
        WC_ADMIN_PROHIBITION,
        WC_ADMIN_OBLIGATION,
        WC_ADMIN_OPERATION,
        WC_ADMIN_ROUTINE,
        WC_QUERY,
        WC_QUERY_GRAPH,
        WC_QUERY_PROHIBITION,
        WC_QUERY_OBLIGATION,
        WC_QUERY_OPERATION,
        WC_QUERY_ROUTINE
    ));
    public static boolean isWildcardAccessRight(String ar) {
        return WILDCARD_ACCESS_RIGHTS.contains(ar);
    }

    public static Map<String, Set<String>> WILDCARD_MAP = Map.ofEntries(
        Map.entry(WC_ADMIN, allAdminAccessRights),
        Map.entry(WC_ADMIN_GRAPH, ALL_ADMIN_GRAPH_ACCESS_RIGHTS_SET),
        Map.entry(WC_ADMIN_PROHIBITION, ALL_ADMIN_PROHIBITION_ACCESS_RIGHTS_SET),
        Map.entry(WC_ADMIN_OBLIGATION, ALL_ADMIN_OBLIGATION_ACCESS_RIGHTS_SET),
        Map.entry(WC_ADMIN_OPERATION, ALL_ADMIN_OPERATION_ACCESS_RIGHTS_SET),
        Map.entry(WC_ADMIN_ROUTINE, ALL_ADMIN_ROUTINE_ACCESS_RIGHTS_SET),
        Map.entry(WC_QUERY, ALL_QUERY_ACCESS_RIGHTS_SET),
        Map.entry(WC_QUERY_GRAPH, ALL_GRAPH_QUERY_ACCESS_RIGHTS_SET),
        Map.entry(WC_QUERY_PROHIBITION, ALL_PROHIBITION_QUERY_ACCESS_RIGHTS_SET),
        Map.entry(WC_QUERY_OBLIGATION, ALL_OBLIGATION_QUERY_ACCESS_RIGHTS_SET),
        Map.entry(WC_QUERY_OPERATION, ALL_OPERATION_QUERY_ACCESS_RIGHTS_SET),
        Map.entry(WC_QUERY_ROUTINE, ALL_ROUTINE_QUERY_ACCESS_RIGHTS_SET)
    );

    private AdminAccessRights() {}
}
