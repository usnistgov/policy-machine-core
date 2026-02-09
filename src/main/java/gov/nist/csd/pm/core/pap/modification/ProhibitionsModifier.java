package gov.nist.csd.pm.core.pap.modification;

import static gov.nist.csd.pm.core.pap.operation.accessright.AccessRightValidator.validateAccessRights;

import gov.nist.csd.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.exception.ProhibitionExistsException;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.store.PolicyStore;
import java.util.Collection;
import java.util.Set;

public class ProhibitionsModifier extends Modifier implements ProhibitionsModification {

    public ProhibitionsModifier(PolicyStore store) {
        super(store);
    }

    @Override
    public void createNodeProhibition(String name,
                                      long nodeId,
                                      AccessRightSet accessRightSet,
                                      Set<Long> inclusionSet,
                                      Set<Long> exclusionSet,
                                      boolean isConjunctive) throws PMException {
        checkCreateInput(name, nodeId, accessRightSet, inclusionSet, exclusionSet);

        policyStore.prohibitions().createNodeProhibition(name, nodeId, accessRightSet, inclusionSet, exclusionSet, isConjunctive);
    }

    @Override
    public void createProcessProhibition(String name,
                                         long userId,
                                         String process,
                                         AccessRightSet accessRightSet,
                                         Set<Long> inclusionSet,
                                         Set<Long> exclusionSet,
                                         boolean isConjunctive) throws PMException {
        checkCreateInput(name, userId, accessRightSet, inclusionSet, exclusionSet);

        policyStore.prohibitions().createProcessProhibition(name, userId, process, accessRightSet, inclusionSet, exclusionSet, isConjunctive);
    }

    @Override
    public void deleteProhibition(String name) throws PMException {
        if(!checkDeleteInput(name)) {
            return;
        }

        policyStore.prohibitions().deleteProhibition(name);
    }

    /**
     * Validate create prohibition inputs.
     * @param name the name of the prohibition.
     * @param nodeId the id of the subject node.
     * @param accessRightSet the access right set.
     * @param inclusionSet the set of inclusion attributes.
     * @param exclusionSet the set of exclusion attributes.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    protected void checkCreateInput(String name, long nodeId, AccessRightSet accessRightSet,
                                    Set<Long> inclusionSet, Set<Long> exclusionSet) throws PMException {
        if (policyStore.prohibitions().prohibitionExists(name)) {
            throw new ProhibitionExistsException(name);
        }

        // check the prohibition parameters are valid
        validateAccessRights(policyStore.operations().getResourceAccessRights(), accessRightSet);
        checkProhibitionSubjectExists(nodeId);
        checkProhibitionContainersExist(inclusionSet, exclusionSet);
    }

    /**
     * Check if the prohibition exists. If it doesn't, return false to indicate to the caller that execution should not
     * proceed.
     *
     * @param name The name of the prohibition.
     * @return True if the execution should proceed, false otherwise.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    protected boolean checkDeleteInput(String name) throws PMException {
	    return policyStore.prohibitions().prohibitionExists(name);
    }

    protected void checkProhibitionSubjectExists(long nodeId)
            throws PMException {
            if (!policyStore.graph().nodeExists(nodeId)) {
                throw new NodeDoesNotExistException(nodeId);
            }
    }

    protected void checkProhibitionContainersExist(Set<Long> inclusionSet, Set<Long> exclusionSet) throws PMException {
        for (long inc : inclusionSet) {
            if (!policyStore.graph().nodeExists(inc)) {
                throw new NodeDoesNotExistException(inc);
            }
        }

        for (long exc : exclusionSet) {
            if (!policyStore.graph().nodeExists(exc)) {
                throw new NodeDoesNotExistException(exc);
            }
        }
    }
}
