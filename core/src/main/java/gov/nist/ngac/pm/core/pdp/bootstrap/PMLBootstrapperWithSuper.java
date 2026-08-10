package gov.nist.ngac.pm.core.pdp.bootstrap;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.ngac.pm.core.pap.modification.GraphModification;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.operation.accessright.WildcardAccessRight;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;
import java.util.List;

/**
 * A {@link PolicyBootstrapper} that creates a super user with unrestricted admin privileges and runs the
 * given PML script as that user.
 */
public class PMLBootstrapperWithSuper extends PolicyBootstrapper {

    private final String pml;

    public PMLBootstrapperWithSuper(String pml) {
        this.pml = pml;
    }

    /**
     * Creates the super user and obligations to keep it privileged on new policy class content, then runs
     * the stored PML as that user.
     * @param pap the PAP to bootstrap
     * @throws PMException if configuring the policy fails
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
            graph.associate(superUaId, AdminPolicyNode.PM_ADMIN_BASE_OA.nodeId(), new AccessRightSet(
                WildcardAccessRight.ADMIN_WILDCARD.toString()
            ));

            // this association will grant super privileges on itself
            graph.associate(superUaId, pmAdminId, new AccessRightSet(WildcardAccessRight.ADMIN_WILDCARD.toString()));

            // create an obligation that when any node is created in a PC node or assigned to a PC node,
            // associate the super user with it.
            String obligationPml = """
                    create obligation "grant_super_on_new_ua_assigned_to_pc"
                    when any user
                    performs "create_user_attribute" on (descendants) {
                        pcs := get_policy_class_ids()
                        foreach pcId in pcs {
                            if contains(arr=descendants, element=pcId) {
                                return true
                            }
                        }
                        
                        return false
                    }
                    do(ctx) {
                        associate "@super" to ctx.args.name with ["admin:*"]
                    }
                    
                    create obligation "grant_super_on_new_oa_assigned_to_pc"
                    when any user
                    performs "create_object_attribute" on (descendants) {
                        pcs := get_policy_class_ids()
                        foreach pcId in pcs {
                            if contains(arr=descendants, element=pcId) {
                                return true
                            }
                        }
                        
                        return false
                    }
                    do(ctx) {
                        associate "@super" to ctx.args.name with ["admin:*"]
                    }
                    
                    create obligation "grant_super_on_attr_assigned_to_pc"
                    when any user
                                        
                    performs "assign" on (descendants) {
                        pcs := get_policy_class_ids()
                        foreach pcId in pcs {
                            if contains(arr=descendants, element=pcId) {
                                return true
                            }
                        }
                        
                        return false
                    } 
                    do(ctx) {
                        associate "@super" to name(id=ctx.args.ascendant) with ["admin:*"]
                    }                    
                    """;
            // execute the obligation pml
            tx.executePML(NodeUserContext.of(superUserId), obligationPml);

            // execute the provided pml directly with the PAP with no access checks
            tx.executePML(NodeUserContext.of(superUserId), pml);
        });
    }
}
