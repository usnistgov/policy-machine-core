package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.pap.store.PolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;

import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.ALL_ACCESS_RIGHTS_SET;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.noprops;
import static gov.nist.csd.pm.policy.tx.TxRunner.runTx;

public class SuperPolicy {

    public static final String SUPER_PREFIX = "super";

    public static final String SUPER_USER = "super";
    public static final String SUPER_PC = "super_pc";
    public static final String SUPER_UA = "super_ua";
    public static final String SUPER_OBJECT = "super_object";

    public static void applySuperPolicy(Graph graph, String pc, String uaName, String oaName, String rep) throws PMException {
        PolicyStore store = graph.store();
        runTx(store, () -> {
            if (store.graph().nodeExists(SUPER_USER)) {
                graph.createUser(SUPER_USER, noprops(), uaName);
            } else {
                graph.assign(SUPER_USER, uaName);
            }

            if (store.graph().nodeExists(SUPER_UA)) {
                graph.createUserAttribute(SUPER_UA, noprops(), pc);
            } else {
                graph.assign(SUPER_UA, pc);
            }

            if (store.graph().nodeExists(SUPER_OBJECT)) {
                graph.createUserAttribute(SUPER_OBJECT, noprops(), oaName);
            } else {
                graph.assign(SUPER_OBJECT, oaName);
            }

            //associate super ua with base ua and oa and rep oa
            graph.associate(SUPER_UA, uaName, ALL_ACCESS_RIGHTS_SET);
            graph.associate(SUPER_UA, oaName, ALL_ACCESS_RIGHTS_SET);
            graph.associate(SUPER_UA, rep, ALL_ACCESS_RIGHTS_SET);
        });
    }
}