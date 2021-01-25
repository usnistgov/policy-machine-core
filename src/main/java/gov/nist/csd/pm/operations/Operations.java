package gov.nist.csd.pm.operations;

import java.util.Set;

/**
 * Constants for operations in the Policy Machine
 */
public class Operations {
    public static final String WRITE                      = "write";
    public static final String READ                       = "read";
    public static final String CREATE_POLICY_CLASS        = "create policy class";
    public static final String ASSIGN_OBJECT_ATTRIBUTE    = "assign object attribute";
    public static final String ASSIGN_OBJECT_ATTRIBUTE_TO = "assign object attribute to";
    public static final String ASSIGN_OBJECT              = "assign object";
    public static final String ASSIGN_OBJECT_TO           = "assign object to";
    public static final String CREATE_NODE                = "create node";
    public static final String DELETE_NODE                = "delete node";
    public static final String UPDATE_NODE                = "update node";
    public static final String OBJECT_ACCESS              = "object access";
    public static final String ASSIGN_TO                  = "assign to";
    public static final String ASSIGN                     = "assign";
    public static final String ASSOCIATE                  = "associate";
    public static final String DISASSOCIATE               = "disassociate";
    public static final String CREATE_OBJECT              = "create object";
    public static final String CREATE_OBJECT_ATTRIBUTE    = "create object attribute";
    public static final String CREATE_USER_ATTRIBUTE      = "create user attribute";
    public static final String CREATE_USER                = "create user";
    public static final String DELETE_OBJECT              = "delete object";
    public static final String DELETE_OBJECT_ATTRIBUTE    = "delete object attribute";
    public static final String DELETE_USER_ATTRIBUTE      = "delete user attribute";
    public static final String DELETE_USER                = "delete user";
    public static final String DELETE_POLICY_CLASS        = "delete policy class";
    public static final String DEASSIGN                   = "deassign";
    public static final String DEASSIGN_FROM              = "deassign from";
    public static final String CREATE_ASSOCIATION         = "create association";
    public static final String UPDATE_ASSOCIATION         = "update association";
    public static final String DELETE_ASSOCIATION         = "delete association";
    public static final String GET_ASSOCIATIONS           = "get associations";
    public static final String RESET                      = "reset";
    public static final String GET_PERMISSIONS            = "get permissions";
    public static final String CREATE_PROHIBITION         = "create prohibition";
    public static final String UPDATE_PROHIBITION         = "update prohibition";
    public static final String VIEW_PROHIBITION           = "view prohibition";
    public static final String DELETE_PROHIBITION         = "delete prohibition";
    public static final String GET_ACCESSIBLE_CHILDREN    = "get accessible children";
    public static final String GET_PROHIBITED_OPS         = "get prohibited ops";
    public static final String GET_ACCESSIBLE_NODES       = "get accessible nodes";
    public static final String TO_JSON                    = "to json";
    public static final String FROM_JSON                  = "from json";
    public static final String ADD_OBLIGATION             = "add obligation";
    public static final String GET_OBLIGATION             = "get obligation";
    public static final String UPDATE_OBLIGATION          = "update obligation";
    public static final String DELETE_OBLIGATION          = "delete obligation";
    public static final String ENABLE_OBLIGATION          = "enable obligation";

    public static final String ALL_OPS = "*";
    public static final String ALL_ADMIN_OPS = "*a";
    public static final String ALL_RESOURCE_OPS = "*r";

    public static final Set<String> ADMIN_OPS = new OperationSet(
            CREATE_POLICY_CLASS,
            ASSIGN_OBJECT_ATTRIBUTE,
            ASSIGN_OBJECT_ATTRIBUTE_TO,
            ASSIGN_OBJECT,
            ASSIGN_OBJECT_TO,
            CREATE_NODE,
            DELETE_NODE,
            UPDATE_NODE,
            ASSIGN_TO,
            ASSIGN,
            ASSOCIATE,
            DISASSOCIATE,
            CREATE_OBJECT,
            CREATE_OBJECT_ATTRIBUTE,
            CREATE_USER_ATTRIBUTE,
            CREATE_USER,
            DELETE_OBJECT,
            DELETE_OBJECT_ATTRIBUTE,
            DELETE_USER_ATTRIBUTE,
            DELETE_POLICY_CLASS,
            DELETE_USER,
            DEASSIGN,
            DEASSIGN_FROM,
            CREATE_ASSOCIATION,
            UPDATE_ASSOCIATION,
            DELETE_ASSOCIATION,
            GET_ASSOCIATIONS,
            GET_PERMISSIONS,
            CREATE_PROHIBITION,
            GET_ACCESSIBLE_CHILDREN,
            GET_PROHIBITED_OPS,
            GET_ACCESSIBLE_NODES,
            RESET,
            TO_JSON,
            FROM_JSON,
            UPDATE_PROHIBITION,
            DELETE_PROHIBITION,
            VIEW_PROHIBITION,
            ADD_OBLIGATION,
            GET_OBLIGATION,
            UPDATE_OBLIGATION,
            DELETE_OBLIGATION,
            ENABLE_OBLIGATION
    );
}
