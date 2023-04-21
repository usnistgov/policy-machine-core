package gov.nist.csd.pm.policy.pml;

import gov.nist.csd.pm.policy.pml.compiler.Variable;
import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.model.access.AdminAccessRights;

import java.util.HashMap;
import java.util.Map;

import static gov.nist.csd.pm.pap.SuperPolicy.*;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.ALL_ADMIN_ACCESS_RIGHTS;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.ALL_RESOURCE_ACCESS_RIGHTS;

public class PALBuiltinConstants {

    private PALBuiltinConstants() { }

    private static final Map<String, Value> BUILTIN_VALUES = new HashMap<>();
    static {
        BUILTIN_VALUES.put(SUPER_USER, new Value(SUPER_USER));
        BUILTIN_VALUES.put(SUPER_PC, new Value(SUPER_PC));
        BUILTIN_VALUES.put(SUPER_UA, new Value(SUPER_UA));
        BUILTIN_VALUES.put(SUPER_PC_REP, new Value(SUPER_PC_REP));

        for (String adminAccessRight : AdminAccessRights.allAdminAccessRights()) {
            BUILTIN_VALUES.put(adminAccessRight, new Value(adminAccessRight));
        }

        BUILTIN_VALUES.put(ALL_ADMIN_ACCESS_RIGHTS, new Value(ALL_ADMIN_ACCESS_RIGHTS));
        BUILTIN_VALUES.put(ALL_RESOURCE_ACCESS_RIGHTS, new Value(ALL_RESOURCE_ACCESS_RIGHTS));
    }

    private static final Map<String, Variable> BUILTIN_VARIABLES = new HashMap<>();
    static {
        BUILTIN_VARIABLES.put(SUPER_USER, new Variable(SUPER_USER, Type.string(), true));
        BUILTIN_VARIABLES.put(SUPER_PC, new Variable(SUPER_PC, Type.string(), true));
        BUILTIN_VARIABLES.put(SUPER_UA, new Variable(SUPER_UA, Type.string(), true));
        BUILTIN_VARIABLES.put(SUPER_PC_REP, new Variable(SUPER_PC_REP, Type.string(), true));

        for (String adminAccessRight : AdminAccessRights.allAdminAccessRights()) {
            BUILTIN_VARIABLES.put(adminAccessRight, new Variable(adminAccessRight, Type.string(), true));
        }

        BUILTIN_VARIABLES.put(ALL_ADMIN_ACCESS_RIGHTS, new Variable(ALL_ADMIN_ACCESS_RIGHTS, Type.string(), true));
        BUILTIN_VARIABLES.put(ALL_RESOURCE_ACCESS_RIGHTS, new Variable(ALL_RESOURCE_ACCESS_RIGHTS, Type.string(), true));
    }

    public static Map<String, Value> builtinValues() {
        return new HashMap<>(BUILTIN_VALUES);
    }

    public static Map<String, Variable> builtinVariables() {
        return new HashMap<>(BUILTIN_VARIABLES);
    }
}
