package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.pap.AdminPolicyNode;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;

import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.allAccessRights;
import static gov.nist.csd.pm.policy.tx.TxRunner.runTx;

public class SuperUserBootstrapper implements PolicyBootstrapper {

    public static final String SUPER_USER = "super";
    public static final String SUPER_PC = "super_policy";
    public static final String SUPER_UA = "super_ua";
    public static final String SUPER_UA1 = "super_ua1";

    @Override
    public void bootstrap(PAP pap) throws PMException {
        // create super policy -- no assigning to PCs after this is called (from PDP)
        runTx(pap, () -> {
            // create policy class
            pap.graph().createPolicyClass(SUPER_PC);

            // create the first superuser attribute which will be used to provide super with privileges on itself
            pap.graph().createUserAttribute(SUPER_UA, SUPER_PC);

            // create the second user attribute which the first will have privileges on
            pap.graph().createUserAttribute(SUPER_UA1, SUPER_PC);

            // create the superuser and assign to both UAs
            pap.graph().createUser(SUPER_USER, SUPER_UA, SUPER_UA1);

            // associate the super_ua and super_ua1 to provide * rights to the super ser on itself
            // if the association already exists this will do nothing
            pap.graph().associate(SUPER_UA1, SUPER_UA, allAccessRights());

            pap.graph().associate(SUPER_UA, AdminPolicyNode.ADMIN_POLICY_TARGET.nodeName(), allAccessRights());
            pap.graph().associate(SUPER_UA, AdminPolicyNode.POLICY_CLASSES_OA.nodeName(), allAccessRights());
            pap.graph().associate(SUPER_UA, AdminPolicyNode.PML_FUNCTIONS_TARGET.nodeName(), allAccessRights());
            pap.graph().associate(SUPER_UA, AdminPolicyNode.PML_CONSTANTS_TARGET.nodeName(), allAccessRights());

            pap.executePML(new UserContext(SUPER_USER), """
                    create obligation "super-obligation" {
                        create rule "grant super privileges on assignments to pc nodes"
                        when any user
                        performs ["assign_to", "create_user_attribute", "create_object_attribute"]
                        do(ctx) {
                            node := getNode(ctx["target"])
                            
                            // only for PC nodes
                            if !equals(node["type"], "PC") {
                                return
                            }

                            // get the attr name from the event context
                            attrName := ""
                            eventName := ctx["eventName"]
                            if equals(eventName, "assign_to") {
                                attrName = ctx["event"]["child"]
                            } else if equals(eventName, "create_user_attribute") {
                                attrName = ctx["event"]["name"]
                            } else if equals(eventName, "create_object_attribute") {
                                attrName = ctx["event"]["name"]
                            }

                            // associate the super user with the attr
                            associate "super_ua" and attrName with [*]       
                        }                      
                    }
                    """);
        });
    }

}
