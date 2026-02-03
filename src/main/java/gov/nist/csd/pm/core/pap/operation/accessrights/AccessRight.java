package gov.nist.csd.pm.core.pap.operation.accessrights;

public enum AccessRight {

    // wild card
    A_ALL("a:*"),
    R_ALL("r:*"),

    // graph
    A_GRAPH_NODE_PC_CREATE("a:graph:node:pc:create"),
    A_GRAPH_NODE_OA_CREATE("a:graph:node:oa:create"),
    A_GRAPH_NODE_O_CREATE("a:graph:node:o:create"),
    A_GRAPH_NODE_U_CREATE("a:graph:node:u:create"),
    A_GRAPH_NODE_UA_CREATE("a:graph:node:ua:create"),

    A_GRAPH_NODE_PC_DELETE("a:graph:node:pc:delete"),
    A_GRAPH_NODE_OA_DELETE("a:graph:node:oa:delete"),
    A_GRAPH_NODE_O_DELETE("a:graph:node:o:delete"),
    A_GRAPH_NODE_U_DELETE("a:graph:node:u:delete"),
    A_GRAPH_NODE_UA_DELETE("a:graph:node:ua:delete"),

    A_GRAPH_NODE_PROPERTIES_UPDATE("a:graph:node:properties:update"),

    A_GRAPH_ASSIGNMENT_CREATE_ASCENDANT("a:graph:assignment:create:ascendant"),
    A_GRAPH_ASSIGNMENT_CREATE_DESCENDANT("a:graph:assignment:create:descendant"),
    A_GRAPH_ASSIGNMENT_DELETE_ASCENDANT("a:graph:assignment:delete:ascendant"),
    A_GRAPH_ASSIGNMENT_DELETE_DESCENDANT("a:graph:assignment:delete:descendant"),

    A_GRAPH_ASSOCIATION_CREATE_UA("a:graph:association:ua:create"),
    A_GRAPH_ASSOCIATION_CREATE_TARGET("a:graph:association:target:create"),
    A_GRAPH_ASSOCIATION_DELETE_FROM("a:graph:association:ua:delete"),
    A_GRAPH_ASSOCIATION_DELETE_TO("a:graph:association:target:delete"),

    A_GRAPH_POLICY_CLASS_LIST("a:graph:policy_class:list"),
    A_GRAPH_ASSIGNMENT_LIST("a:graph:assignment:list"),
    A_GRAPH_SUBGRAPH_READ("a:graph:subgraph:read"),
    A_GRAPH_ASSOCIATION_LIST("a:graph:association:list"),

    // prohibition
    A_PROHIBITION_CREATE("a:prohibition:create"),
    A_PROHIBITION_PROCESS_CREATE("a:prohibition:process:create"),
    A_PROHIBITION_COMPLEMENT_CONTAINER_CREATE("a:prohibition:complement_container:create"),

    A_PROHIBITION_DELETE("a:prohibition:delete"),
    A_PROHIBITION_PROCESS_DELETE("a:prohibition:process:delete"),
    A_PROHIBITION_COMPLEMENT_CONTAINER_DELETE("a:prohibition:complement_container:delete"),

    A_PROHIBITION_LIST("a:prohibition:list"),

    // obligation
    A_OBLIGATION_CREATE("a:obligation:create"),
    A_OBLIGATION_DELETE("a:obligation:delete"),
    A_OBLIGATION_LIST("a:obligation:list"),

    // operation
    A_OPERATION_CREATE("a:operation:create"),
    A_OPERATION_DELETE("a:operation:delete"),
    A_OPERATION_LIST("a:operation:list"),

    // general
    A_POLICY_RESET("a:policy:reset"),
    A_POLICY_SERIALIZE("a:policy:serialize"),
    A_POLICY_DESERIALIZE("a:policy:deserialize");

    private final String value;
    AccessRight(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
