package gov.nist.csd.pm.policy.pml;

import gov.nist.csd.pm.policy.pml.compiler.Variable;
import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.model.access.AdminAccessRights;

import java.util.HashMap;
import java.util.Map;

import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.ALL_ADMIN_ACCESS_RIGHTS;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.ALL_RESOURCE_ACCESS_RIGHTS;

public class PMLBuiltinConstants {

    private PMLBuiltinConstants() { }

    private static final Map<String, Value> BUILTIN_VALUES = new HashMap<>();
    static {
        /*for (String adminAccessRight : AdminAccessRights.allAdminAccessRights()) {
            BUILTIN_VALUES.put(adminAccessRight, new Value(adminAccessRight));
        }

        BUILTIN_VALUES.put(ALL_ADMIN_ACCESS_RIGHTS, new Value(ALL_ADMIN_ACCESS_RIGHTS));
        BUILTIN_VALUES.put(ALL_RESOURCE_ACCESS_RIGHTS, new Value(ALL_RESOURCE_ACCESS_RIGHTS));*/
    }

    private static final Map<String, Variable> BUILTIN_VARIABLES = new HashMap<>();
    static {
        /*for (String adminAccessRight : AdminAccessRights.allAdminAccessRights()) {
            BUILTIN_VARIABLES.put(adminAccessRight, new Variable(adminAccessRight, Type.string(), true));
        }

        BUILTIN_VARIABLES.put(ALL_ADMIN_ACCESS_RIGHTS, new Variable(ALL_ADMIN_ACCESS_RIGHTS, Type.string(), true));
        BUILTIN_VARIABLES.put(ALL_RESOURCE_ACCESS_RIGHTS, new Variable(ALL_RESOURCE_ACCESS_RIGHTS, Type.string(), true));*/
    }

    public static Map<String, Value> builtinValues() {
        return new HashMap<>(BUILTIN_VALUES);
    }

    public static Map<String, Variable> builtinVariables() {
        return new HashMap<>(BUILTIN_VARIABLES);
    }
}
