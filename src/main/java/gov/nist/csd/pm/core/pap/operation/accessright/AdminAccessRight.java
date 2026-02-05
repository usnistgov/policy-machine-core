package gov.nist.csd.pm.core.pap.operation.accessright;

import java.util.HashMap;
import java.util.Map;

/**
 * An enum for all allowed admin access rights
 */
public enum AdminAccessRight {

    // graph
    ADMIN_GRAPH_NODE_CREATE("admin:graph:node:create"),
    ADMIN_GRAPH_NODE_DELETE("admin:graph:node:delete"),
    ADMIN_GRAPH_NODE_UPDATE("admin:graph:node:update"),

    ADMIN_GRAPH_ASSIGNMENT_ASCENDANT_CREATE("admin:graph:assignment:ascendant:create"),
    ADMIN_GRAPH_ASSIGNMENT_ASCENDANT_DELETE("admin:graph:assignment:ascendant:delete"),
    ADMIN_GRAPH_ASSIGNMENT_DESCENDANT_CREATE("admin:graph:assignment:descendant:create"),
    ADMIN_GRAPH_ASSIGNMENT_DESCENDANT_DELETE("admin:graph:assignment:descendant:delete"),

    ADMIN_GRAPH_ASSOCIATION_UA_CREATE("admin:graph:association:ua:create"),
    ADMIN_GRAPH_ASSOCIATION_TARGET_CREATE("admin:graph:association:target:create"),
    ADMIN_GRAPH_ASSOCIATION_UA_DELETE("admin:graph:association:ua:delete"),
    ADMIN_GRAPH_ASSOCIATION_TARGET_DELETE("admin:graph:association:target:delete"),

    ADMIN_GRAPH_POLICY_CLASS_LIST("admin:graph:node:pc:list"),
    ADMIN_GRAPH_ASSIGNMENT_LIST("admin:graph:assignment:list"),
    ADMIN_GRAPH_SUBGRAPH_LIST("admin:graph:subgraph:list"),
    ADMIN_GRAPH_ASSOCIATION_LIST("admin:graph:association:list"),

    // prohibition
    ADMIN_PROHIBITION_SUBJECT_CREATE("admin:prohibition:subject:create"),
    ADMIN_PROHIBITION_INCLUSION_CREATE("admin:prohibition:inclusion:create"),
    ADMIN_PROHIBITION_EXCLUSION_CREATE("admin:prohibition:exclusion:create"),

    ADMIN_PROHIBITION_SUBJECT_DELETE("admin:prohibition:subject:delete"),
    ADMIN_PROHIBITION_INCLUSION_DELETE("admin:prohibition:inclusion:delete"),
    ADMIN_PROHIBITION_EXCLUSION_DELETE("admin:prohibition:exclusion:delete"),

    ADMIN_PROHIBITION_LIST("admin:prohibition:list"),

    // obligation
    ADMIN_OBLIGATION_CREATE("admin:obligation:create"),
    ADMIN_OBLIGATION_DELETE("admin:obligation:delete"),
    ADMIN_OBLIGATION_LIST("admin:obligation:list"),

    // operation
    ADMIN_OPERATION_CREATE("admin:operation:create"),
    ADMIN_OPERATION_DELETE("admin:operation:delete"),
    ADMIN_OPERATION_LIST("admin:operation:list"),

    // general
    ADMIN_POLICY_RESET("admin:policy:reset"),
    ADMIN_POLICY_SERIALIZE("admin:policy:serialize"),
    ADMIN_POLICY_DESERIALIZE("admin:policy:deserialize"),
    ADMIN_POLICY_RESOURCE_ACCESS_RIGHTS_UPDATE("admin:policy:resource_access_rights:update"),

    ADMIN_ACCESS_QUERY("admin:access:query");

    private final String value;
    AdminAccessRight(String value) {
        this.value = value;
    }

    /**
     * @return the actual value of the access right, not the enum constant name
     */
    @Override
    public String toString() {
        return value;
    }

    private static final Map<String, AdminAccessRight> BY_VALUE;

    static {
        BY_VALUE = new HashMap<>();
        for (AdminAccessRight ar : values()) {
            BY_VALUE.put(ar.value, ar);
        }
    }

    /**
     * Given a string, return the corresponding AdminAccessRight enum constant or null if it does not exist.
     * @param value The value to get the enum constant for.
     * @return the corresponding AdminAccessRight enum constant or null if it does not exist.
     */
    public static AdminAccessRight fromString(String value) {
        return BY_VALUE.get(value);
    }
}
