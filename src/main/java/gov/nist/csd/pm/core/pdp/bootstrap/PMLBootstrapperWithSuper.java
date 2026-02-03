package gov.nist.csd.pm.core.pdp.bootstrap;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.accessrights.AccessRightSet;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminAccessRights;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.modification.GraphModification;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.List;

public class PMLBootstrapperWithSuper extends PolicyBootstrapper {

    private final String pml;

    public PMLBootstrapperWithSuper(String pml) {
        this.pml = pml;
    }

    /**
     * Apply the stored PML to the given PAP. This will create a super user called "super" which will have privileges
     * to do everything in the policy machine. This method will also create obligations to be stored in the policy store
     * that creates an association when any user creates or assigns a node to a PC node. This will ensure the super user
     * retains access to the node and its ascendants.
     * @param pap the PAP to execute the bootstrap PML.
     * @throws PMException if there is an exception configuring the policy
     */
    @Override
    public void bootstrap(PAP pap) throws PMException {
        pap.runTx(tx -> {
            GraphModification graph = tx.modify().graph();

            // the main super UA node
            long superUaId = graph.createUserAttribute("@super", List.of(AdminPolicyNode.PM_ADMIN_PC.nodeId()));

            // extra UA to allow super to have * on itself
            long pmAdminId = graph.createUserAttribute("@pm_admin_users", List.of(AdminPolicyNode.PM_ADMIN_PC.nodeId()));

            // super user
            long superUserId = graph.createUser("super", List.of(superUaId, pmAdminId));

            // grant the super user all privileges on operations that require access to the admin policy nodes
            graph.associate(superUaId, AdminPolicyNode.PM_ADMIN_BASE_OA.nodeId(), new AccessRightSet(AdminAccessRights.WC_ALL));

            // this association will grant super privileges on itself
            graph.associate(superUaId, pmAdminId, new AccessRightSet(AdminAccessRights.WC_ALL));

            // create an obligation that when any node is created in a PC node or assigned to a PC node,
            // associate the super user with it.
            String obligationPml = """
                    create obligation "grant_super_on_new_ua_assigned_to_pc"
                    when any user
                    performs "create_user_attribute" on (descendants) {
                        pcs := getPolicyClassIds()
                        foreach pcId in pcs {
                            if contains(descendants, pcId) {
                                return true
                            }
                        }
                        
                        return false
                    }
                    do(ctx) {
                        associate "@super" and ctx.args.name with ["*"]
                    }
                    
                    create obligation "grant_super_on_new_oa_assigned_to_pc"
                    when any user
                    performs "create_object_attribute" on (descendants) {
                        pcs := getPolicyClassIds()
                        foreach pcId in pcs {
                            if contains(descendants, pcId) {
                                return true
                            }
                        }
                        
                        return false
                    }
                    do(ctx) {
                        associate "@super" and ctx.args.name with ["*"]
                    }
                    
                    create obligation "grant_super_on_attr_assigned_to_pc"
                    when any user
                                        
                    performs "assign" on (descendants) {
                        pcs := getPolicyClassIds()
                        foreach pcId in pcs {
                            if contains(descendants, pcId) {
                                return true
                            }
                        }
                        
                        return false
                    } 
                    do(ctx) {
                        associate "@super" and name(ctx.args.ascendant) with ["*"]
                    }                    
                    """;
            // execute the obligation pml
            tx.executePML(new UserContext(superUserId), obligationPml);

            // execute the provided pml directly with the PAP with no access checks
            tx.executePML(new UserContext(superUserId), pml);
        });
    }
}
