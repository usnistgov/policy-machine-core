package gov.nist.csd.pm.operations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Constants for operations in the Policy Machine
 */
public class Operations {
    public static final String FILE_WRITE                 = "file write";
    public static final String FILE_READ                  = "file read";
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
    public static final String DEASSIGN                   = "deassign";
    public static final String DEASSIGN_FROM              = "deassign from";
    public static final String CREATE_ASSOCIATION         = "create association";
    public static final String UPDATE_ASSOCIATION         = "update association";
    public static final String DELETE_ASSOCIATION         = "delete association";
    public static final String GET_ASSOCIATIONS           = "get associations";
    public static final String ALL_OPERATIONS             = "*";
    public static final String ANY_OPERATIONS             = "any";
    public static final String RESET                      = "reset";
    public static final String GET_PERMISSIONS            = "get permissions";
    public static final String CREATE_PROHIBITION         = "create prohibition";
    public static final String GET_ACCESSIBLE_CHILDREN    = "get accessible children";
    public static final String GET_PROHIBITED_OPS         = "get prohibited ops";
    public static final String GET_ACCESSIBLE_NODES       = "get accessible nodes";
    public static final String PROHIBIT_SUBJECT           = "prohibit subject";
    public static final String PROHIBIT_RESOURCE          = "prohibit resource";

    private static final Set<String> admin = new OperationSet(
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
            DEASSIGN,
            DEASSIGN_FROM,
            CREATE_ASSOCIATION,
            UPDATE_ASSOCIATION,
            DELETE_ASSOCIATION,
            GET_ASSOCIATIONS,
            ALL_OPERATIONS,
            ANY_OPERATIONS,
            GET_PERMISSIONS,
            CREATE_PROHIBITION,
            GET_ACCESSIBLE_CHILDREN,
            GET_PROHIBITED_OPS,
            GET_ACCESSIBLE_NODES,
            PROHIBIT_SUBJECT,
            PROHIBIT_RESOURCE,
            RESET
    );

    private static final Set<String> resource = new OperationSet(
            FILE_WRITE,
            FILE_READ,
            WRITE,
            READ
    );

    public static boolean isAdmin(String op) {
        return admin.contains(op);
    }
}
