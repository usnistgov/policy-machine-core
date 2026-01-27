package gov.nist.csd.pm.core.pdp.bootstrap;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminAccessRights;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.modification.GraphModification;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.List;

public class PMLBootstrapperWithSuper extends PolicyBootstrapper {

    private final boolean deleteSuperAfterBootstrap;
    private final String pml;

    public PMLBootstrapperWithSuper(boolean deleteSuperAfterBootstrap, String pml) {
        this.deleteSuperAfterBootstrap = deleteSuperAfterBootstrap;
        this.pml = pml;
    }

    @Override
    public void bootstrap(PAP pap) throws PMException {
        pap.runTx(tx -> {
            GraphModification graph = tx.modify().graph();
            long superUaId = graph.createUserAttribute("@super", List.of(AdminPolicyNode.PM_ADMIN_PC.nodeId()));
            long pmAdminId = graph.createUserAttribute("@pm_admin_users", List.of(AdminPolicyNode.PM_ADMIN_PC.nodeId()));

            long superUserId = graph.createUser("super", List.of(superUaId, pmAdminId));

            // execute the pml
            tx.executePML(new UserContext(superUserId), pml);

            // if either the super or @super nodes were used in the PML, these calls will throw an exception because
            // they are being used in the policy
            // this also extends to any obligations that were created in the PML which will have the super user as the
            // defined author
            if (deleteSuperAfterBootstrap) {
                graph.deleteNode(superUserId);
                graph.deleteNode(superUaId);
                graph.deleteNode(pmAdminId);
            } else {
                // if the super is not deleted, associate it with the PM_ADMIN_BASE_OA with *
                // this will grant the super user all privileges on operations that require access to the admin policy nodes
                graph.associate(superUaId, AdminPolicyNode.PM_ADMIN_BASE_OA.nodeId(), new AccessRightSet(AdminAccessRights.WC_ALL));

                // this association will grant super privileges on itself
                graph.associate(superUaId, pmAdminId, new AccessRightSet(AdminAccessRights.WC_ALL));

                // create an obligation that when any node is created in a PC node, associate the super user with it.
                String obligationPml = """
                    create obligation "grant_super_on_new_ua_assigned_to_pc"
                    when any user
                    performs "create_user_attribute" on (descendants) {
                        pcs := getPolicyClassIds()
                        foreach pcId in pcs {
                            if !contains(descendants, pcId) {
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
                            if !contains(descendants, pcId) {
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
                            if !contains(descendants, pcId) {
                                return true
                            }
                        }
                        
                        return false
                    } 
                    do(ctx) {
                        associate "@super" and name(ctx.args.ascendant) with ["*"]
                    }                    
                    """;

                tx.executePML(new UserContext(superUserId), obligationPml);
            }
        });
    }
}
