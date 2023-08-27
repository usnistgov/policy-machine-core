package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.HashMap;

import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.allAccessRights;
import static gov.nist.csd.pm.policy.tx.TxRunner.runTx;

public class SuperUserBootstrapper implements PolicyBootstrapper {

    public static final String SUPER_USER = "super";
    public static final String SUPER_PC = "super_policy";
    public static final String SUPER_UA = "super_ua";
    public static final String SUPER_UA1 = "super_ua1";

    @Override
    public void bootstrap(PAP pap) throws PMException {
        // create super policy -- no assoigning to PCs after this is called (from PDP)
        runTx(pap, () -> {
            // create policy class
            pap.graph().createPolicyClass(SUPER_PC);

            // create the first superuser attribute which will be used to provide super with privileges on itself
            pap.graph().createUserAttribute(SUPER_UA, SUPER_PC);

            // create the second user attribute which the first will have privileges on
            pap.graph().createUserAttribute(SUPER_UA1, SUPER_PC);

            // create the superuser and assign to both UAs
            pap.graph().createUser(SUPER_USER, SUPER_UA, SUPER_UA1);

            // associate the super_ua and super_ua1 to provide * rights to the super user on itself
            // if the association already exists this will do nothing
            pap.graph().associate(SUPER_UA, SUPER_UA1, allAccessRights());

            pap.graph().associate(SUPER_UA, AdminPolicy.ADMIN_POLICY_TARGET, allAccessRights());
            pap.graph().associate(SUPER_UA, AdminPolicy.POLICY_CLASSES_OA, allAccessRights());
            pap.graph().associate(SUPER_UA, AdminPolicy.PML_FUNCTIONS_TARGET, allAccessRights());
            pap.graph().associate(SUPER_UA, AdminPolicy.PML_CONSTANTS_TARGET, allAccessRights());
        });
    }

}
