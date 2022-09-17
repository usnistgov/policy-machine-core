package gov.nist.csd.pm.policy.author.pal;

import gov.nist.csd.pm.policy.author.pal.compiler.Variable;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.model.access.AdminAccessRights;

import java.util.HashMap;
import java.util.Map;

import static gov.nist.csd.pm.pap.policies.SuperPolicy.*;

public class PALBuiltinConstants {

    public static final Map<String, Value> BUILTIN_VALUES = new HashMap<>();
    static {
        BUILTIN_VALUES.put(SUPER_USER, new Value(SUPER_USER));
        BUILTIN_VALUES.put(SUPER_PC, new Value(SUPER_PC));
        BUILTIN_VALUES.put(SUPER_UA, new Value(SUPER_UA));
        BUILTIN_VALUES.put(SUPER_OBJECT, new Value(SUPER_OBJECT));

        for (String adminAccessRight : AdminAccessRights.ALL_ADMIN_ACCESS_RIGHTS_SET) {
            BUILTIN_VALUES.put(adminAccessRight, new Value(adminAccessRight));
        }
    }

    public static final Map<String, Variable> BUILTIN_VARIABLES = new HashMap<>();
    static {
        BUILTIN_VARIABLES.put(SUPER_USER, new Variable(SUPER_USER, Type.string(), true));
        BUILTIN_VARIABLES.put(SUPER_PC, new Variable(SUPER_PC, Type.string(), true));
        BUILTIN_VARIABLES.put(SUPER_UA, new Variable(SUPER_UA, Type.string(), true));
        BUILTIN_VARIABLES.put(SUPER_OBJECT, new Variable(SUPER_OBJECT, Type.string(), true));

        for (String adminAccessRight : AdminAccessRights.ALL_ADMIN_ACCESS_RIGHTS_SET) {
            BUILTIN_VARIABLES.put(adminAccessRight, new Variable(adminAccessRight, Type.string(), true));
        }
    }

}
