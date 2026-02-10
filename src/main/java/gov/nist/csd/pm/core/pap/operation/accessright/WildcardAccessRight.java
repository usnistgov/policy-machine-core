package gov.nist.csd.pm.core.pap.operation.accessright;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Wildcard pattern access rights to avoid specifying all the access rights in a given domain. These are for convenience
 * only and ndo not need to be used.
 */
public enum WildcardAccessRight {

    WILDCARD("*"),
    ADMIN_WILDCARD("admin:*",
        Arrays.stream(AdminAccessRight.values()).map(AdminAccessRight::toString).collect(Collectors.toSet())),
    RESOURCE_WILDCARD("resource:*"),
    ADMIN_GRAPH_WILDCARD("admin:graph:*",
        AdminAccessRight.ADMIN_GRAPH_NODE_CREATE.toString(),
        AdminAccessRight.ADMIN_GRAPH_NODE_DELETE.toString(),
        AdminAccessRight.ADMIN_GRAPH_NODE_UPDATE.toString(),

        AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_ASCENDANT_CREATE.toString(),
        AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_DESCENDANT_CREATE.toString(),
        AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_ASCENDANT_DELETE.toString(),
        AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_DESCENDANT_DELETE.toString(),

        AdminAccessRight.ADMIN_GRAPH_ASSOCIATION_UA_CREATE.toString(),
        AdminAccessRight.ADMIN_GRAPH_ASSOCIATION_TARGET_CREATE.toString(),
        AdminAccessRight.ADMIN_GRAPH_ASSOCIATION_UA_DELETE.toString(),
        AdminAccessRight.ADMIN_GRAPH_ASSOCIATION_TARGET_DELETE.toString(),

        AdminAccessRight.ADMIN_GRAPH_POLICY_CLASS_LIST.toString(),
        AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_LIST.toString(),
        AdminAccessRight.ADMIN_GRAPH_SUBGRAPH_LIST.toString(),
        AdminAccessRight.ADMIN_GRAPH_ASSOCIATION_LIST.toString()
    ),
    ADMIN_GRAPH_NODE_WILDCARD("admin:graph:node:*",
        AdminAccessRight.ADMIN_GRAPH_NODE_CREATE.toString(),
        AdminAccessRight.ADMIN_GRAPH_NODE_DELETE.toString(),
        AdminAccessRight.ADMIN_GRAPH_NODE_UPDATE.toString(),
        AdminAccessRight.ADMIN_GRAPH_POLICY_CLASS_LIST.toString()
    ),
    ADMIN_GRAPH_ASSIGNMENT_WILDCARD("admin:graph:assignment:*",
        AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_ASCENDANT_CREATE.toString(),
        AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_DESCENDANT_CREATE.toString(),
        AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_ASCENDANT_DELETE.toString(),
        AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_DESCENDANT_DELETE.toString(),
        AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_LIST.toString()
    ),
    ADMIN_GRAPH_ASSOCIATION_WILDCARD("admin:graph:association:*",
        AdminAccessRight.ADMIN_GRAPH_ASSOCIATION_UA_CREATE.toString(),
        AdminAccessRight.ADMIN_GRAPH_ASSOCIATION_TARGET_CREATE.toString(),
        AdminAccessRight.ADMIN_GRAPH_ASSOCIATION_UA_DELETE.toString(),
        AdminAccessRight.ADMIN_GRAPH_ASSOCIATION_TARGET_DELETE.toString(),
        AdminAccessRight.ADMIN_GRAPH_ASSOCIATION_LIST.toString()
    ),
    ADMIN_PROHIBITION_WILDCARD("admin:prohibition:*",
        AdminAccessRight.ADMIN_PROHIBITION_NODE_CREATE.toString(),
        AdminAccessRight.ADMIN_PROHIBITION_PROCESS_CREATE.toString(),
        AdminAccessRight.ADMIN_PROHIBITION_INCLUSION_CREATE.toString(),
        AdminAccessRight.ADMIN_PROHIBITION_EXCLUSION_CREATE.toString(),

        AdminAccessRight.ADMIN_PROHIBITION_NODE_DELETE.toString(),
        AdminAccessRight.ADMIN_PROHIBITION_PROCESS_DELETE.toString(),
        AdminAccessRight.ADMIN_PROHIBITION_INCLUSION_DELETE.toString(),
        AdminAccessRight.ADMIN_PROHIBITION_EXCLUSION_DELETE.toString(),

        AdminAccessRight.ADMIN_PROHIBITION_LIST.toString()
    ),
    ADMIN_OBLIGATION_WILDCARD("admin:obligation:*",
        AdminAccessRight.ADMIN_OBLIGATION_CREATE.toString(),
        AdminAccessRight.ADMIN_OBLIGATION_DELETE.toString(),
        AdminAccessRight.ADMIN_OBLIGATION_LIST.toString()
    ),
    ADMIN_OPERATION_WILDCARD("admin:operation:*",
        AdminAccessRight.ADMIN_OPERATION_CREATE.toString(),
        AdminAccessRight.ADMIN_OPERATION_DELETE.toString(),
        AdminAccessRight.ADMIN_OPERATION_LIST.toString()
    ),
    ADMIN_POLICY_WILDCARD("admin:policy:*",
        AdminAccessRight.ADMIN_POLICY_RESET.toString(),
        AdminAccessRight.ADMIN_POLICY_SERIALIZE.toString(),
        AdminAccessRight.ADMIN_POLICY_DESERIALIZE.toString()
    );

    private final String pattern;
    private final List<String> accessRights;

    WildcardAccessRight(String pattern, String ... accessRights) {
        this.pattern = pattern;
        this.accessRights = List.of(accessRights);
    }

    WildcardAccessRight(String pattern, Collection<String> accessRights) {
        this.pattern = pattern;
        this.accessRights = new ArrayList<>(accessRights);
    }

    public String getPattern() {
        return pattern;
    }

    public List<String> getAccessRights() {
        return accessRights;
    }

    /**
     * @return the pattern string not the enum constant name.
     */
    @Override
    public String toString() {
        return pattern;
    }

    /**
     * Given a set of resource access rights, resolve the access rights represented by this enum constant. If this is
     * WILDCARD return all resource and admin access rights. If this is RESOURCE_WILDCARD, return the resource access
     * rights. Otherwise, the access rights represented by the pattern.
     * @param resourceAccessRights The resource access rights.
     * @return the access rights represented by this WildcardAccessRight.
     */
    public AccessRightSet resolveAccessRights(AccessRightSet resourceAccessRights) {
        if (this == WILDCARD) {
            AccessRightSet accessRightSet = new AccessRightSet(resourceAccessRights);
            accessRightSet.addAll(ADMIN_WILDCARD.getAccessRights());
            return accessRightSet;
        } else if (this == RESOURCE_WILDCARD) {
            return resourceAccessRights;
        }

        return new AccessRightSet(accessRights);
    }

    private static final Map<String, WildcardAccessRight> BY_PATTERN;

    static {
        BY_PATTERN = new HashMap<>();
        for (WildcardAccessRight w : values()) {
            BY_PATTERN.put(w.pattern, w);
        }
    }

    /**
     * Given a string, return the corresponding WildcardAccessRight enum constant or null if it does not exist.
     * @param pattern The pattern to get the enum constant for.
     * @return the corresponding WildcardAccessRight enum constant or null if it does not exist.
     */
    public static WildcardAccessRight fromString(String pattern) {
        return BY_PATTERN.get(pattern);
    }
}
